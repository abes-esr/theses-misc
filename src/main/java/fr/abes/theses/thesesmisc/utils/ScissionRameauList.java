package fr.abes.theses.thesesmisc.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScissionRameauList {
    private static final List<ScissionRameauEntry> scissionRameauList = new ArrayList<>();
    private static final List<Integer> scissionRameauTefIdList = new ArrayList<>();

    public static void loadScissionRameauList() throws IOException {
        Reader in;
        try {
            in = new FileReader("src/main/resources/autorites_rameau_td_scission_liste.csv");
        } catch (Exception e) {
            in = new FileReader("autorites_rameau_td_scission_liste.csv");
        }
        final Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter('\t').withFirstRecordAsHeader().parse(in);
        for (final CSVRecord record : records) {
            scissionRameauList.add(new ScissionRameauEntry(record.get("PPN A REMPLACER"), record.get("PPN 1 DE REMPLACEMENT"), record.get("LIBELLE PPN 1 DE  REMPLACEMENT"), record.get("PPN 2 DE REMPLACEMENT"), record.get("LIBELLE PPN 2 DE  REMPLACEMENT")));
        }
    }

    public static void loadScissionRameauTefIdList() throws IOException {
        Reader in;
        try {
            in = new FileReader("src/main/resources/autorites_rameau_td_scission_liste_tef_id.csv");
        } catch (Exception e) {
            in = new FileReader("autorites_rameau_td_scission_liste_tef_id.csv");
        }
        final Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter('\t').withFirstRecordAsHeader().parse(in);
        for (final CSVRecord record : records) {
            scissionRameauTefIdList.add(Integer.valueOf(record.get("TEF ID")));
        }
    }

    public static List<ScissionRameauEntry> getEntries() {
        return scissionRameauList;
    }

    public static List<Integer> getTefIds() {
        return scissionRameauTefIdList;
    }
}