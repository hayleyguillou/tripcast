package com.comp4020.tripcast;

/**
 * Created by hayleyguillou on 15-03-15.
 */
public class Duration {
    public Duration(String text, long value) {
        this.text = text;
        this.value = value;
    }

    private String text;
    private long value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

}