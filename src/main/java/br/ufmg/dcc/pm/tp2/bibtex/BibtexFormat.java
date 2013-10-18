package br.ufmg.dcc.pm.tp2.bibtex;

public abstract class BibtexFormat {
    private String reference;
    private Authors authors;
    private String title;
    private long year;


    public BibtexFormat() {
        reference = "";
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Authors getAuthors() {
        return authors;
    }

    public void setAuthors(Authors authors) {
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public abstract String getType();

    public String getHeader() {
        StringBuilder header = new StringBuilder();
        header.append('@').append(getType()).append("{").append(reference).append(",\n");
        return header.toString();
    }

    public String getRequiredFieldsText() {
        StringBuilder text = new StringBuilder();
        text.append("author = {").append(this.authors.getAuthorsText()).append("},\n");
        text.append("title = {").append(this.title).append("},\n");
        text.append("year = {").append(this.year).append("},\n");
        return text.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BibtexFormat that = (BibtexFormat) o;

        return reference != null && reference.equals(that.reference);

    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

    @Override
    public String toString() {
        return reference;
    }
}
