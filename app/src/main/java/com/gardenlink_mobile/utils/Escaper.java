package com.gardenlink_mobile.utils;

public final class Escaper {
    public static String escapeHTMLTagsExceptIMG(String str_html){
        String beforeEscape = str_html.replaceAll("< *[iI][mM][gG]", "_iimmgg");
        String escaped = android.text.Html.fromHtml(beforeEscape).toString();
        String postEscape = escaped.replaceAll("_iimmgg", "<img");
        return postEscape;
    }

    public static String escapeHTLMTags(String str_html){
        return android.text.Html.fromHtml(str_html).toString();
    }

    public static String escapePhotoURL(String url){
        try {
            return url.substring(2, url.length() - 2);
        }
        catch (Exception e) {return null;}
    }
}
