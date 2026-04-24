package fr.abes.theses.thesesmisc.utils;

public class ScissionRameauEntry {
    private final String oldPpn;
    private final String newPpn1;
    private final String newLabel1;
    private final String newPpn2;
    private final String newLabel2;

    public ScissionRameauEntry(String oldPpn, String newPpn1, String newLabel1, String newPpn2, String newLabel2) {
        this.oldPpn = oldPpn;
        this.newPpn1 = newPpn1;
        this.newLabel1 = newLabel1;
        this.newPpn2 = newPpn2;
        this.newLabel2 = newLabel2;
    }

    // Getters
    public String getOldPpn() {
        return oldPpn;
    }

    public String getNewPpn1() {
        return newPpn1;
    }

    public String getNewLabel1() {
        return newLabel1;
    }

    public String getNewPpn2() {
        return newPpn2;
    }

    public String getNewLabel2() {
        return newLabel2;
    }
}
