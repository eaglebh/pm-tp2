package br.ufmg.dcc.pm.tp2;

import java.util.ArrayList;
import java.util.List;

public class Pages {
    private List<Integer> pageNumbers;

    public Pages() {
        pageNumbers = new ArrayList<Integer>();
    }

    public void addPage(int page) throws Exception {
        if(page <= 0) {
            throw new Exception("Pagina invalida");
        }
        pageNumbers.add(page);
    }

    public void removePage(int page) {
        pageNumbers.remove(page);
    }

    public static String getPagesText(Pages pages) {
        StringBuilder pagesText = new StringBuilder();
        List<Integer> pageNumbers = pages.getPageNumbers();
        boolean first = true;
        int last = 0;
        boolean endSequence = false;
        for (Integer it : pageNumbers) {
            if (first) {
                last = it;
                pagesText.append(it);
                first = false;
            } else {
                if ((last + 1) == it) { // sequencia identificada
                    last = it;
                    endSequence = true;
                } else { // quebra de sequencia
                    if (endSequence) {
                        endSequence = false;
                        pagesText.append("-").append(last).append(',').append(it).append(',');
                    } else {
                        pagesText.append(it).append(',');
                    }
                }
            }
        }
        if (endSequence) {
            endSequence = false;
            pagesText.append("-").append(last).append(',');
        }
        return pagesText.toString();
    }

    public List<Integer> getPageNumbers() {
        return new ArrayList<Integer>(pageNumbers);
    }
}
