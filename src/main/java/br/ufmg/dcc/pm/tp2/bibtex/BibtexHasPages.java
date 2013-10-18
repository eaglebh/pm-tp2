package br.ufmg.dcc.pm.tp2.bibtex;

public abstract class BibtexHasPages extends BibtexFormat {
    private Pages pages;

    public BibtexHasPages() {

    }

    public String getRequiredFieldsText() {
        StringBuilder text = new StringBuilder();
        text.append(super.getRequiredFieldsText());
        text.append("pages = {").append(Pages.getPagesText(this.getPages())).append("},\n");
        return text.toString();
    }

    public Pages getPages() {
        return pages;
    }

    public void setPages(Pages pages) {
        this.pages = pages;
    }
}
