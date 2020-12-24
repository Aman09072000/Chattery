package com.google.firebase.and.chatter;

public class Article {

    private String title;
    private String author;
    private String section;
    private String date;
    private String webUrl;
    private final static String NO_AUTHOR_INFORMATION_FOUND = "No Info of Author Found";

    //Constructor
    public Article(
            String localTitle,
            String localAuthor,
            String localSection,
            String localDate,
            String localWebUrl
    ) {
        title = localTitle;
        author = localAuthor;
        section = localSection;
        date = localDate;
        webUrl = localWebUrl;
    }

    //Getters
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        if (author == null) {
            return NO_AUTHOR_INFORMATION_FOUND;
        }
        return author;
    }

    public String getSection() {
        return section;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return webUrl;
    }
}
