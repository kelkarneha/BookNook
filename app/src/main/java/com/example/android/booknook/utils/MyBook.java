package com.example.android.booknook.utils;

import java.io.Serializable;

/**
 * Created by Administrator on 12/21/2016.
 */

public class MyBook implements Serializable{
    private static final long serialVersionUID = 1L;
    private String book_title;
    private String book_author;
    private String book_rank;
    private String image_link;
    private String description;
    private String isbn_13;
    private int _id;

    public MyBook(int id, String rank, String title, String author, String image_link, String description, String isbn_13){
        this._id = id;
        this.book_rank = rank;
        this.book_title = title;
        this.book_author = author;
        this.image_link = image_link;
        this.description = description;
        this.isbn_13 = isbn_13;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getBook_title() {
        return book_title;
    }

    public String getBook_author() {
        return book_author;
    }

    public String getBook_rank() {
        return book_rank;
    }

    public int get_id() {
        return _id;
    }

    public String getImage_link() {
        return image_link;
    }

    public String getDescription() {
        return description;
    }

    public String getIsbn_13() {
        return isbn_13;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "MyBook{" +
                "book_title='" + book_title + '\'' +
                ", book_author='" + book_author + '\'' +
                ", book_rank='" + book_rank + '\'' +
                ", image_link='" + image_link + '\'' +
                ", description='" + description + '\'' +
                ", isbn_13='" + isbn_13 + '\'' +
                ", _id=" + _id +
                '}';
    }
}
