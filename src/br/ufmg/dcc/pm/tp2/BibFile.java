package br.ufmg.dcc.pm.tp2;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class BibFile {
    private List<BibtexFormat> bibs = new ArrayList<BibtexFormat>();

    public enum SearchCriteria {
        ALL, AUTHOR, YEAR, VEHICLE, TITLE, REFERENCE;

        public static SearchCriteria fromInteger(Integer i) {
            switch (i) {
                case 1:
                    return AUTHOR;
                case 2:
                    return YEAR;
                case 3:
                    return VEHICLE;
                case 4:
                    return TITLE;
                case 5:
                    return REFERENCE;
                default:
                    return ALL;
            }
        }
    }

    public BibFile() {

    }

    public void createBibtex(BibtexFormat bib) {
        bibs.add(bib);
    }

    public BibtexFormat retrieveBibtex(SearchCriteria criteria, String key) throws Exception {
        String actualText = "";
        for (BibtexFormat bib : bibs) {
            switch (criteria) {
                case AUTHOR:
                    actualText = bib.getAuthors().getAuthorsText();
                    break;
                case YEAR:
                    actualText = String.valueOf(bib.getYear());
                    break;
                case VEHICLE:
                    if (bib.getType().compareTo(BibtexArticle.TYPE) == 0) {
                        actualText = ((BibtexArticle) bib).getJournal();
                    } else {
                        if (bib.getType().compareTo(BibtexInproceedings.TYPE) == 0) {
                            actualText = ((BibtexInproceedings) bib).getBooktitle();
                        }
                    }
                    break;
                case TITLE:
                    actualText = bib.getTitle();
                    break;
                case REFERENCE:
                    actualText = bib.getReference();
                    break;
                default:
                    break;
            }
            if (key.length() == 0) {
                if (actualText.length() == 0) {
                    return bib;
                }
            } else {
                if (actualText.contains(key)) {
                    return bib;
                }
            }
        }
        throw new Exception("Bibtex nao encontrado");
    }

    public void updateBibtex(BibtexFormat bib) throws Exception {
        if (bibs.contains(bib)) {
            bibs.remove(bib);
            bibs.add(bib);
        } else {
            throw new Exception("Bibtex a ser atualizado nao esta na lista.");
        }
    }

    public void deleteBibtex(BibtexFormat bib) {
        bibs.remove(bib);
    }

    public String toText() {
        StringBuilder stringBuilder = new StringBuilder();
        for (BibtexFormat bib : bibs) {
            stringBuilder.append(bib.getHeader());
            stringBuilder.append(bib.getRequiredFieldsText());
            stringBuilder.append("}\n");
        }

        return stringBuilder.toString();
    }

    public List<BibtexFormat> getBibs() {
        return bibs;
    }
}
