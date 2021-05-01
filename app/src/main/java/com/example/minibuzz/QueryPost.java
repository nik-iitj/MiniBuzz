package com.example.minibuzz;


public class QueryPost {

    private String User_ID , Query, Date_Time ;



    public QueryPost(){


    }



    public QueryPost (String User_ID, String Query, String Date_Time){

        this.Date_Time=Date_Time;
        this.User_ID=User_ID;
        this.Query=Query;

    }






    public String getDate_Time() {

        return Date_Time;
    }

    public void setDate_Time(String date_Time) {

        Date_Time = date_Time;
    }

    public String getQuery() {

        return Query;
    }

    public void setQuery(String query) {

        Query = query;
    }
    public void setUser_Id(String user_ID) {

        User_ID = user_ID;
    }

    public String getUser_Id() {
        return User_ID;
    }



}



