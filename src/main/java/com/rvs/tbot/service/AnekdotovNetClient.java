package com.rvs.tbot.service;

import com.rvs.tbot.model.yandex.Courses;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

@Component
public class AnekdotovNetClient {
    private static final String URL1 = "http://anekdotov.net/anekdot/";
    private static final String URL2 = "http://anekdotov.net/anekdot/week/";
    private static final String URL3 = "http://anekdotov.net/anekdot/lastdays.html";

    public String getAnekdot() {
        String url;
        int maxIndex;
        int num = new Random().nextInt(35);
        if (num < 15) {
            url = URL1;
            maxIndex = 15;
        } else if (num < 25){
            url = URL2;
            maxIndex = 10;
        } else {
            url = URL3;
            maxIndex = 10;
        }
        String anekdot = "Не удалось обработать запрос на " + url;
        try {
            Document doc = getDocument(url);
            assert doc != null;
            Elements elements = doc.getElementsByClass("anekdot");
            num = new Random().nextInt(maxIndex);
            anekdot = elements.get(num).text();
        } catch (Exception e) {
            System.out.println("ЗАПРОС В http://anekdotov.net/anekdot/ ... отклонено.");
            e.printStackTrace();
        }

        return anekdot;
    }

    private Document getDocument(String url) {
        try {
             return Jsoup
                    .connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/607.1.15 (KHTML, like Gecko) Version/11.1.1 Safari/6705.1.15")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}