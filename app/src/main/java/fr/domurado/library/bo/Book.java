package fr.domurado.library.bo;

public class Book {
    private String isbn;
    private String title;
    private int price;
    private String cover;

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", cover='" + cover + '\'' +
                '}';
    }
}
