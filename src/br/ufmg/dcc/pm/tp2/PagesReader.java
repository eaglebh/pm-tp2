package br.ufmg.dcc.pm.tp2;

public class PagesReader {
    static Pages parsePages(String text) throws Exception {
        String values = Util.parseField("pages",text);
        return parsePagesFromValue(values);
    }

    static Pages parsePagesFromValue(String values) throws Exception {
        Pages pages = new Pages();

        String[] commaSplit = values.split(",");
        for (String strFromComma : commaSplit) {
            if (strFromComma.contains("-")) {
                String[] hiphenSplit = strFromComma.split("-");
                for (Integer pageNumber = Integer.parseInt(hiphenSplit[0]); pageNumber <= Integer.parseInt(hiphenSplit[1]); pageNumber++) {
                    pages.addPage(pageNumber);
                }
            } else {
                pages.addPage(Integer.parseInt(strFromComma));
            }
        }

        return pages;
    }
}
