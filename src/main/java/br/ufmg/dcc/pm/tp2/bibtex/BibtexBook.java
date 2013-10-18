package br.ufmg.dcc.pm.tp2.bibtex;

public class BibtexBook extends BibtexFormat {
    private String publisher;


    public static  String TYPE = "book";

    public BibtexBook() {

    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    // BibtexFormat interface
    public String getType()
    {
        return TYPE;
    }

    public String getRequiredFieldsText() {
        StringBuilder text = new StringBuilder();
        text.append(super.getRequiredFieldsText());
        text.append("publisher = {").append(this.getPublisher()).append("},\n");
        return text.toString();
    }
}
