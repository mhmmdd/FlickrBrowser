package com.example.mhmmdd.flickrbrowser;

/**
 * Created by muhammed on 4.12.2017.
 */

public class Photo {
    private String title;
    private String author;
    private String authorId;
    private String link;
    private String tags;
    private String image;


    public Photo(String title, String author, String authorId, String link, String tags, String image) {
        this.title = title;
        this.author = author;
        this.authorId = authorId;
        this.link = link;
        this.tags = tags;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", authorId='" + authorId + '\'' +
                ", link='" + link + '\'' +
                ", tags='" + tags + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
