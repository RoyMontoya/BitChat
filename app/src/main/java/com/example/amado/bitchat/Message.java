package com.example.amado.bitchat;

import java.util.Date;

/**
 * Created by Amado on 02/06/2015.
 */
public class Message {

    private String mSender;
    private String mText;
    private Date mDate;

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    Message(String text, String sender){
        mText = text;
        mSender = sender;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getSender() {
        return mSender;
    }

    public void setSender(String sender) {
        mSender = sender;
    }
}
