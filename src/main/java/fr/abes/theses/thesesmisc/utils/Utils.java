package fr.abes.theses.thesesmisc.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Utils {

    public static String getUrlSolr(String databaseUrl, String databaseUser) {
        String radical = "";
        if (databaseUrl.contains("-p-")) {
            radical = "http://denim.v102.abes.fr:8080";
        } else if (databaseUrl.contains("-t-")) {
            radical = "http://denim-test.v202.abes.fr:8080";
        } else {
            if (!databaseUrl.contains("-d-")) {
                throw new IllegalStateException("Unexpected value: " + databaseUrl);
            }

            radical = "http://denim-dev.v212.abes.fr:8080";
        }

        switch (databaseUser) {
            case "STAR":
                return radical + "/solr1";
            case "SUJETS":
                return radical + "/solrSujets";
            case "PORTAIL":
                return radical + "/solr2";
            default:
                throw new IllegalStateException("Unexpected value: " + databaseUser);
        }
    }

    public static String getXslSolr(String databaseUser) {
        switch (databaseUser) {
            case "STAR":
                return "src/main/resources/xls/tef2solr.xsl";
            case "SUJETS":
                return "src/main/resources/xls/sujets2solr.xsl";
            case "PORTAIL":
                return "";
            default:
                throw new IllegalStateException("Unexpected value: " + databaseUser);
        }
    }
    public static List<Integer> findAllSolr(String request, String urlSolr, PageRequest pageable) throws IOException {
        String query = urlSolr + "/select/?q=" + request + "&fl=id&sort=" + ((Sort.Order)pageable.getSort().get().findFirst().get()).getProperty() + " " + ((Sort.Order)pageable.getSort().get().findFirst().get()).getDirection().toString().toLowerCase() + "&rows=" + pageable.getPageSize() + "&start=" + pageable.getPageNumber() * pageable.getPageSize();
        query = query.replace(" ", "%20");
        StringBuilder response = getResponse(query);
        return getList(response);
    }

    private static StringBuilder getResponse(String query) throws IOException {
        URL url = new URL(query);
        HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
        urlc.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
        StringBuilder response = new StringBuilder();

        String inputLine;
        while((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();
        return response;
    }

    private static List<Integer> getList(StringBuilder response) {
        String regex = "<doc><str name=\"id\">(\\d+)<\\/str><\\/doc>";
        Pattern pattern = Pattern.compile("<doc><str name=\"id\">(\\d+)<\\/str><\\/doc>", 40);
        Matcher matcher = pattern.matcher(response);
        List<Integer> idList = new ArrayList();

        while(matcher.find()) {
            for(int i = 1; i <= matcher.groupCount(); ++i) {
                idList.add(Integer.valueOf(Integer.parseInt(matcher.group(i))));
            }
        }

        return idList;
    }

    public static boolean indexerDansSolr(int iddoc, String tef, String cheminXsl, String urlSolr) throws TransformerException, IOException {
        boolean res;
        try {
            String docSolr = transfoXSL(tef, iddoc, cheminXsl);
            envoieSurSolr(docSolr, urlSolr);
            res = true;
        } catch (Exception e) {
            log.info("Erreur dans indexerDansSolr :" + e.getMessage());
            throw e;
        }
        return res;
    }

    public static boolean isInSolr(int idDoc, String urlSolr) throws IOException {
        HttpURLConnection urlc;
        URL url = new URL(urlSolr + "/select/?q=id:" + idDoc);
        urlc = (HttpURLConnection) url.openConnection();
        urlc.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString().contains("<result name=\"response\" numFound=\"1\" start=\"0\">");
    }

    public static boolean supprimerDeSolr(int idDoc, String urlSolr) throws IOException {
        boolean res = false;
        final StringWriter sw = new StringWriter();
        try
        {
            postData(new StringReader("<delete><id>" + idDoc + "</id></delete>"), sw, urlSolr);
            if (sw.toString().indexOf("<int name=\"status\">0</int>") < 0) {
                log.error("unexpected response from solr...");
            }
            postData(new StringReader("<commit/>"), sw, urlSolr)       ;
            res = true;
        }
        catch (Exception e) {
            log.error("Erreur dans supprimerDeSolr :"+e.getMessage());
            throw e;
        }
        return res;
    }

    private static String transfoXSL(String tef, int idDoc, String cheminXslt) throws TransformerException {
        InputStream stream = new ByteArrayInputStream(tef.getBytes(StandardCharsets.UTF_8));
        TransformerFactory tFactory = new net.sf.saxon.TransformerFactoryImpl();
        Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(cheminXslt));
        transformer.setParameter("idDeLaBase", Optional.of(idDoc));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        transformer.transform(new javax.xml.transform.stream.StreamSource(stream), new javax.xml.transform.stream.StreamResult(out));
        return out.toString();
    }

    private static void envoieSurSolr(String docSolr, String urlSolr) throws IOException {
        final StringWriter sw = new StringWriter();
        postData(new StringReader(docSolr), sw, urlSolr);
        if (sw.toString().indexOf("<int name=\"status\">0</int>") < 0) {
            log.error("unexpected response from solr...");
        }
        postData(new StringReader("<commit/>"), sw, urlSolr);
    }

    private static void postData(Reader data, Writer output, String url) throws IOException {
        URL solrUrl = new URL(url);
        HttpURLConnection urlc = null;
        try {
            urlc = (HttpURLConnection) solrUrl.openConnection();
            try {
                urlc.setRequestMethod("POST");
            } catch (ProtocolException e) {
                throw new ProtocolException("Shouldn't happen: HttpURLConnection doesn't support POST??");
            }
            urlc.setDoOutput(true);
            urlc.setDoInput(true);
            urlc.setUseCaches(false);
            urlc.setAllowUserInteraction(false);
            urlc.setRequestProperty("Content-type", "text/xml; charset=UTF-8");

            try (OutputStream out = urlc.getOutputStream()) {
                Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
                pipe(data, writer);
                writer.close();
            } catch (IOException e) {
                throw new IOException("IOException while posting data", e);
            }

            try (InputStream in = urlc.getInputStream()) {
                Reader reader = new InputStreamReader(in);
                pipe(reader, output);
                reader.close();
            } catch (IOException e) {
                throw new IOException("IOException while reading response", e);
            }

        } catch (IOException e) {
            try {
                assert urlc != null;
                log.error("Solr returned an error: " + urlc.getResponseMessage());
                throw new IOException("Erreur lors du post sur solr : "
                        + urlc.getResponseMessage(), e);
            } catch (IOException f) {
                log.error("Connection error (is Solr running at " + solrUrl + " ?): " + e);
                throw new IOException("Erreur de connexion à solr", e);
            }
        } finally {
            if (urlc != null) {
                urlc.disconnect();
            }
        }
    }

    private static void pipe(Reader reader, Writer writer) throws IOException {
        char[] buf = new char[1024];
        int read = 0;
        while ((read = reader.read(buf)) >= 0) {
            writer.write(buf, 0, read);
        }
        writer.flush();

    }
}
