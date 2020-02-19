package com.gardenlink_mobile.utils;

public final class Stripper {
    public final String stripHTMLtagsExceptIMG(String str_html){
        String beforetStrip = str_html.replaceAll("< *[iI][mM][gG]", "_iimmgg");
        String stripped = android.text.Html.fromHtml(beforetStrip).toString();
        String postStrip = stripped.replaceAll("_iimmgg", "<img");
        return postStrip;
    }
}
