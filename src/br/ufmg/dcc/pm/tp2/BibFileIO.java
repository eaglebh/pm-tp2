package br.ufmg.dcc.pm.tp2;

import java.io.*;
import java.util.Scanner;

public class BibFileIO {
    private String fileName;
    private BibFile bibFile;

    private BibtexFormat readBibtex(String bibStr) throws Exception {
        return parseTypeAndReference(bibStr);
    }

    private static BibtexFormat parseTypeAndReference(String bibStr) throws Exception {
        String type, ref;
        type = bibStr.substring(0,bibStr.indexOf('{'));
        ref = bibStr.substring(bibStr.indexOf('{') + 1, bibStr.indexOf(','));
        BibtexFormat bib;

        if (BibtexArticle.TYPE.compareTo(type) == 0) {
            bib = new BibtexArticle();
            ((BibtexArticle) bib).setJournal(Util.parseField("journal", bibStr));
            ((BibtexArticle) bib).setVolume(Util.parseIntField("volume", bibStr));
            ((BibtexArticle) bib).setNumber(Util.parseIntField("number", bibStr));
            ((BibtexArticle) bib).setPages(PagesReader.parsePages(bibStr));
        } else {

            if (BibtexBook.TYPE.compareTo(type) == 0) {
                bib = new BibtexBook();
                ((BibtexBook) bib).setPublisher(Util.parseField("publisher", bibStr));
            } else {

                if (BibtexInproceedings.TYPE.compareTo(type) == 0) {
                    bib = new BibtexInproceedings();
                    ((BibtexInproceedings) bib).setBooktitle(Util.parseField("booktitle", bibStr));
                    ((BibtexInproceedings) bib).setPages(PagesReader.parsePages(bibStr));
                } else {
                    throw new Exception("Tipo não reconhecido de BibTex");
                }
            }
        }
        bib.setAuthors(AuthorReader.parseAuthors(bibStr));
        bib.setTitle(Util.parseField("title", bibStr));
        bib.setYear(Util.parseIntField("year", bibStr));
        bib.setReference(ref);

        return bib;
    }

    public BibFileIO(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        bibFile = new BibFile();
        String line;

        Scanner scanner = new Scanner(new File(fileName));
        scanner.useDelimiter("@");

        while (scanner.hasNext()) {
            StringBuilder bibStr = new StringBuilder();
            line = scanner.next();
            bibStr.append(line);
            try {
                BibtexFormat bib = readBibtex(bibStr.toString());
                bibFile.createBibtex(bib);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        scanner.close();
    }

    public BibFile getBibFile() {
        return bibFile;
    }

    public void persist() throws IOException {
        OutputStream ofs = new FileOutputStream(fileName);

        for (BibtexFormat bib : bibFile.getBibs()) {
            if (bib.getReference().length() > 0) {
                ofs.write(bib.getHeader().getBytes());
                ofs.write(bib.getRequiredFieldsText().getBytes());
                ofs.write("}\n".getBytes());
            }
        }

        ofs.close();
    }
}
