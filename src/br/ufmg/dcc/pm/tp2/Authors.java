package br.ufmg.dcc.pm.tp2;

import java.util.ArrayList;
import java.util.List;

public class Authors {
    private List<String> authors;

    public Authors() {
        authors = new ArrayList<String>();
    }

    public void addAuthor(String author) {
        authors.add(author);
    }

    public void removeAuthor(String author) {
        authors.remove(author);
    }

    public String getAuthorsText() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String author: authors) {
            if(stringBuilder.length() > 0) {
                stringBuilder.append(" and ");
            }
            stringBuilder.append(author);
        }
        return stringBuilder.toString();
    }
}
