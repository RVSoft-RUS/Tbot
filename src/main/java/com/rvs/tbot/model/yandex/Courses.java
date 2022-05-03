package com.rvs.tbot.model.yandex;

import java.time.LocalDateTime;

public class Courses {
    private String usdCb;
    private String eurCb;
    private String usdM;
    private String eurM;
    private String oil;
    private LocalDateTime last;

    public String getUsdCb() {
        return usdCb;
    }

    public void setUsdCb(String usdCb) {
        this.usdCb = usdCb;
    }

    public String getEurCb() {
        return eurCb;
    }

    public void setEurCb(String eurCb) {
        this.eurCb = eurCb;
    }

    public String getUsdM() {
        return usdM;
    }

    public void setUsdM(String usdM) {
        this.usdM = usdM;
    }

    public String getEurM() {
        return eurM;
    }

    public void setEurM(String eurM) {
        this.eurM = eurM;
    }

    public String getOil() {
        return oil;
    }

    public void setOil(String oil) {
        this.oil = oil;
    }

    public LocalDateTime getLast() {
        return last;
    }

    public void setLast(LocalDateTime last) {
        this.last = last;
    }
}
