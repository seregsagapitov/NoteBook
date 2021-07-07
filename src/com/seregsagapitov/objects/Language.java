package com.seregsagapitov.objects;

import java.util.Locale;

public class Language {
    private String code;
    private String name;
    private Locale locale;
    private int index;

    public Language(String code, String name, Locale locale, int index) {
        this.code = code;
        this.name = name;
        this.locale = locale;
        this.index = index;
    }


    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Locale getLocale() {
        return locale;
    }

    public int getIndex() {
        return index;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return name;
    }
}
