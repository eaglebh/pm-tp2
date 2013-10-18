package br.ufmg.dcc.pm.tp2.bibtex;

public class BibtexInproceedings extends BibtexHasPages {
    private String booktitle;

    public static String TYPE = "inproceedings";

    public BibtexInproceedings() {

    }

    public String getBooktitle() {
        return booktitle;
    }

    public void setBooktitle(String booktitle) {
        this.booktitle = booktitle;
    }

    // BibtexFormat interface
    public String getType()
    {
        return "inproceedings";
    }

    public String getRequiredFieldsText() {
        StringBuilder text = new StringBuilder();
        text.append(super.getRequiredFieldsText());
        text.append("booktitle = {").append(this.booktitle).append("},\n");
        return text.toString();
    }
}
