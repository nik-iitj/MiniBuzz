package com.example.minibuzz;

import java.util.Date;

public class comments extends commentId {
    private  String message;
    private String user_id;



    private String Date_Time;
    private Date Timestamp;

    public comments(){

    }



    public comments(String message, String user_id, Date timestamp, String Date_Time) {
        this.message = message;
        this.user_id = user_id;
        Timestamp = timestamp;
        this.Date_Time = Date_Time;
    }
    public String getDate_Time() {
        return Date_Time;
    }

    public void setDate_Time(String date_Time) {
        Date_Time = date_Time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(Date timestamp) {
        Timestamp = timestamp;
    }
}
