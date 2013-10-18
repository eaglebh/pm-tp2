package br.ufmg.dcc.pm.tp2.bibtex;

public class BibtexArticle extends BibtexHasPages {
    private String journal;
    private int volume;
    private int number;

    public static String TYPE = "article";

    public BibtexArticle() {
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    // BibtexFormat interface
    public String getType()
    {
        return TYPE;
    }

    public String getRequiredFieldsText() {
        StringBuilder text = new StringBuilder();
        text.append(super.getRequiredFieldsText());
        text.append("journal = {").append(this.journal).append("},\n");
        text.append("volume = {").append(this.volume).append("},\n");
        text.append("number = {").append(this.number).append("},\n");
        return text.toString();
    }
}
