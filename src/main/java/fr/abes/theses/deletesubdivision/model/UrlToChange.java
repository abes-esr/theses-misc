package fr.abes.theses.deletesubdivision.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UrlToChange {
    private String IDstar;
    private String ancienne_url;
    private String nouvelle_url;
}
