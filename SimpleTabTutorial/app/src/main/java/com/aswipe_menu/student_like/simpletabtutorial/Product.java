package com.aswipe_menu.student_like.simpletabtutorial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Product {
    private List<String> consumedProducts = new ArrayList<String>(Arrays.asList(
            "Bier 0.5",
            "Bier 0.33",
            "Wein 0.25",
            "Ofen 1",
            "Ziga 1",
            "Shot 0.125"));

    private String title, thumbnailUrl;
    private double year;
    private int rating;
    private ArrayList<String> genre;

    public Product() {
    }

    // variables to which you can access from outside
    public Product(String name, String thumbnailUrl, List<String> consumedProducts, double year, int rating,
                   ArrayList<String> genre) {
        this.consumedProducts = consumedProducts;

        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.year = year;
        this.rating = rating;
        this.genre = genre;
    }

    public void addProductList(String string2add){
        consumedProducts.add(consumedProducts.size()+1, string2add);
    }

    public List<String> getProductList(){
        return consumedProducts;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public double getYear() {
        return year;
    }

    public void setYear(double year) {
        this.year = year;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
    }

}