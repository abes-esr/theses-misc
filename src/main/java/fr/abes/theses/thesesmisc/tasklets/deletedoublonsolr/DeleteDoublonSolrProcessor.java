package fr.abes.theses.thesesmisc.tasklets.deletedoublonsolr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


@Slf4j
@Component
public class DeleteDoublonSolrProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(String id) throws IOException {
        return id;
    }
}
