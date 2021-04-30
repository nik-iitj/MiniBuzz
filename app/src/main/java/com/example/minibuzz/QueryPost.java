package com.example.minibuzz;


import java.util.ArrayList;

public class QueryPost {

    private String User_Id , Query ;

   // public ArrayList Images ;

    public QueryPost() {}

    /*
    public QueryPost(String user_Id, String query, ArrayList images) {
        User_Id = user_Id;
        Query = query;
     //   Images = images;
    }
    */



    public QueryPost(String user_Id, String query) {
        User_Id = user_Id;
        Query = query;
        //   Images = images;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }

    public String getQuery() {
        return Query;
    }

    public void setQuery(String query) {
        Query = query;
    }

    /*
    public ArrayList getImages() {
        return Images;
    }

    public void setImages(ArrayList images) {
        Images = images;
    }

    */


}
