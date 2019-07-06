package com.harpreet.mydictonary;

public class history {

    private String en_word;
    private String en_def;

    public history(String en_word,String en_def)
    {
        this.en_word=en_word;
        this.en_def=en_def;
    }

    public String get_en_word()
    {
        return en_word;
    }

    public String get_def()
    {
        return en_def;
    }

}