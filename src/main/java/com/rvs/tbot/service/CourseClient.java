package com.rvs.tbot.service;

import com.rvs.tbot.model.yandex.Courses;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CourseClient {
    private static final String URL = "https://yandex.ru";
    private final static long MINUTES_FOR_UPDATE = 8;
    private static final Courses last = new Courses();

    static {
        last.setLast(LocalDateTime.MIN);
    }

    public String getCourse(String valute) {
            if (LocalDateTime.now().minusMinutes(MINUTES_FOR_UPDATE).isAfter(last.getLast())) {
                try {
                    Document doc = getDocument();
                    assert doc != null;
                    Elements elements = doc.getElementsByClass("stocks__item-value");
                    last.setUsdCb(elements.get(0).text());
                    last.setEurCb(elements.get(1).text());
                    last.setUsdM(elements.get(2).text());
                    last.setEurM(elements.get(3).text());
                    last.setOil(elements.get(4).text());
                    last.setLast(LocalDateTime.now());
                    System.out.println("ЗАПРОС В ЯНДЕКС ...");
                } catch (Exception e) {
                    System.out.println("ЗАПРОС В ЯНДЕКС ... отклонено.");
                    e.printStackTrace();
                }
            }

            switch (valute) {
                case "$":
                    return "MOEX: " + last.getUsdM() + ", ЦБ: " + last.getUsdCb();
                case "E":
                    return "MOEX: " + last.getEurM() + ", ЦБ: " + last.getEurCb();
                case "N":
                    return last.getOil();
            }
            return "Хрень какая-то!!!";
    }

    private Document getDocument() {
        try {
             return Jsoup
                    .connect(URL)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/607.1.15 (KHTML, like Gecko) Version/11.1.1 Safari/6705.1.15")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}