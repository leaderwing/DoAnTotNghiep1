package com.hust.chat;

import com.parse.ParseUser;
import com.utils.Const;
import com.model.ProfileUser;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by QUY2016 on 4/27/2016.
 */
@ParseClassName("Chat_data")
public class Conversation extends ParseObject {
    private String msg;

    /** The status. */
    private int status = Const.STATUS_SENT;

    /** The date. */
    private Date date;

    /** The sender. */
    private String sender;
    private ProfileUser profileUser;

    public Conversation()
    {

    }
    public String getMsg() {
        return getString("message");
    }

    /**
     * Sets the msg.
     *
     * @param msg the new msg
     */
    public void setMsg(String msg) {
        put("message", msg);
    }


    public Date getDate() {
        return getDate("createdAt");
    }

    /**
     * Sets the date.
     *
     * @param date the new date
     */
    public void setDate(Date date) {
        put("createdAt" , date);
    }

    /**
     * Gets the sender.
     *
     * @return the sender
     */
    public ParseUser getSender() {
        return getParseUser("sender");
    }

    /**
     * Sets the sender.
     *
     * @param receiver the new sender
     */
    public void setReceiver(ParseUser receiver) {
        put("receiver",receiver);
    }

    public ParseUser getReceiver() {
        return getParseUser("receiver");
    }

    /**
     * Sets the sender.
     *
     * @param sender the new sender
     */
    public void setSender(ParseUser sender) {
        put("sender",sender);
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status the new status
     */
    public void setStatus(int status) {
        this.status = status;
    }

}