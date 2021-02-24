package com.a3.lottery.app;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.a3.lottery.util.DateTransformer;

public class Demo1 {

    public static void main(String[] args) throws MalformedURLException, IOException, ParseException {
        Document document = Jsoup.parse(new URL("https://store.steampowered.com/stats/"), 10000);

        // System.out.println(document.html());

        // 获取title的内容
        // Element title = document.getElementsByTag("title").first();
        // System.out.println(title.text());
        // Elements elementsContainingText = document.getElementsContainingText("Updated:");
        Elements elementsByClass = document.getElementsByClass("pageheader");
        // String text = elementsByClass.first().text();
        // System.out.println(text);
        Elements spanEle = elementsByClass.first().getElementsByTag("span");
        String timeText = spanEle.first().text();
        if (StringUtils.isBlank(timeText)) {
            return;
        }
        timeText = timeText.replaceAll("Updated: ", "");
        System.out.println(timeText);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, yyyy @ hh:mmaa", Locale.ENGLISH);
        // String ss = "17 February, 2020 @ 8:26am";
        Date parseDate = sdf.parse(timeText);
        System.out.println(parseDate);
        System.out.println(getNowTimeFromSteamTime(parseDate));

        // 获取点击数
        Element hitEle = document.getElementsByClass("statsTopHi").first();
        System.out.println(hitEle.text());
    }

    // Updated: 17 February, 2020 @ 8:26am
    public static void main2(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, yyyy @ hh:mmaa", Locale.ENGLISH);
        String ss = "17 February, 2020 @ 8:26am";
        Date parse = sdf.parse(ss);
        System.out.println(parse);
        System.out.println(getNowTimeFromSteamTime(parse));
    }

    public static String getNowTimeFromSteamTime(Date date) {
        String fmt = "yyyy-MM-dd HH:mm";
        TimeZone srcTimeZone = TimeZone.getTimeZone("GMT-8");
        TimeZone destTimeZone = TimeZone.getTimeZone("GMT+8");
        DateFormat formatter = new SimpleDateFormat(fmt);
        return dateTransformBetweenTimeZone(date, formatter, srcTimeZone, destTimeZone);
    }

    public static String dateTransformBetweenTimeZone(Date sourceDate, DateFormat formatter, TimeZone sourceTimeZone,
            TimeZone targetTimeZone) {
        Long targetTime = sourceDate.getTime() - sourceTimeZone.getRawOffset() + targetTimeZone.getRawOffset();
        return DateTransformer.getTime(new Date(targetTime), formatter);
    }

}
