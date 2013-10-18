package br.ufmg.dcc.pm.tp2.util;

import br.ufmg.dcc.pm.tp2.bibtex.Authors;

import java.util.Scanner;

public class AuthorReader {
    public static Authors parseAuthors(String text) throws Exception {
        String values = Util.parseField("author", text);

        return parseAuthorsFromValue(values);
    }

    public static Authors parseAuthorsFromValue(String values) {
        Authors authors = new Authors();
        Scanner iss = new Scanner(values);
        String word;
        StringBuilder actualAuthor = new StringBuilder();
        while (iss.hasNext()) {
            word = iss.next();
            if (word.compareTo("and") == 0) {
                authors.addAuthor(actualAuthor.toString().trim());
                actualAuthor = new StringBuilder();
            } else {
                if(actualAuthor.length() > 0) {
                    actualAuthor.append(" ");
                }
                actualAuthor.append(word);
            }
        }
        authors.addAuthor(actualAuthor.toString().trim());

        return authors;
    }
}
