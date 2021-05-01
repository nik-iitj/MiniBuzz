package com.example.minibuzz;


public class QueryPost {

    private String User_Id , Query, Date_Time ;



    public QueryPost(){}

    public QueryPost (String User_id, String Query, String Date_Time){

        this.Date_Time=Date_Time;
        this.User_Id=User_id;
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

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }
}
