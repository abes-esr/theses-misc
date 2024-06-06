package fr.abes.theses.thesesmisc.utils;

public class Utils {

    public static String getUrlSolr(String databaseUser) {
        switch (databaseUser){

            case "STAR":
                return "http://denim.v102.abes.fr:8080/solr1";
            case "SUJETS":
                return "http://denim.v102.abes.fr:8080/solrSujets";
            case "PORTAIL":
                return "http://denim.v102.abes.fr:8080/solr2";

            default:
                throw new IllegalStateException("Unexpected value: " + databaseUser);
        }
    }
}
