package br.ufmg.dcc.pm.tp2.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    Util() {

    }

    static int parseInt(String text) {
        return Integer.parseInt(text);
    }

    static String stripBrackets(String searchString) {
        StringBuilder sb = new StringBuilder(searchString);
        sb.deleteCharAt(searchString.indexOf('{'));
        sb.deleteCharAt(searchString.indexOf('}'));
        return sb.toString();
    }

    static String parseString(String text) {
        return stripBrackets(text);
    }

    public static String parseField(String fieldName, String text) throws Exception {
        String regex = "(" + fieldName + "\\s+\\=\\s[\\{\"]*)([^\\}\"]*)([\\}\"]*,)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(2);
        } else {
            throw new Exception("Nao encontrou campo " + fieldName);
        }
    }

    public static int parseIntField(String fieldName, String text) throws Exception {
        return parseInt(parseField(fieldName, text));
    }
}
