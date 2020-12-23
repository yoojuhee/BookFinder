package kr.or.womanup.nambu.yjh.bookfinder;

import java.io.Serializable;

public class Book implements Serializable {
    String titile; //doucmnets정의
    String contents;
    String url;
    String isbn;
    String date;
    String authors;
    String publisher;
    String translators;
    int price;
    int sale_price;
    String thumbnail;
    String status;

    public Book(String titile, String contents, String url, String isbn, String date,
                String authors, String publisher, String translators, int price,
                int sale_price, String thumbnail, String status) {
        this.titile = titile;
        this.contents = contents;
        this.url = url;
        this.isbn = isbn;
        this.date = date;
        this.authors = authors;
        this.publisher = publisher;
        this.translators = translators;
        this.price = price;
        this.sale_price = sale_price;
        this.thumbnail = thumbnail;
        this.status = status;
    }

}
