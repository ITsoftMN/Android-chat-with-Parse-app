package com.example.tsend_ayush.google.model;

import com.example.tsend_ayush.google.UserListActivity;

import java.util.Date;

/**
 * Created by Tsend-Ayush on 11/15/2015.
 */
public class Conversation
{

    /** The Constant STATUS_SENDING. */
    public static final int STATUS_SENDING = 0;

    /** The Constant STATUS_SENT. */
    public static final int STATUS_SENT = 1;

    /** The Constant STATUS_FAILED. */
    public static final int STATUS_FAILED = 2;
    /** The msg. */
    private String msg;

    /** The status. */
    private int status = STATUS_SENT;

    /** The date. */
    private Date date;

    /** The sender. */
    private String sender;
    /** The receiver. */
    private String receiver;


    /**
     * Instantiates a new conversation.
     *
     * @param msg
     *            the msg
     * @param date
     *            the date
     * @param sender
     *            the sender
     */
    public Conversation(String msg, Date date, String sender)
    {
        this.msg = msg;
        this.date = date;
        this.sender = sender;
    }

    /**
     * Instantiates a new conversation.
     */
    public Conversation()
    {
    }

    /**
     * Gets the msg.
     *
     * @return the msg
     */
    public String getMsg()
    {
        return msg;
    }

    /**
     * Sets the msg.
     *
     * @param msg
     *            the new msg
     */
    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    /**
     * Checks if is sent.
     *
     * @return true, if is sent
     */
    public boolean isSent()
    {
        return UserListActivity.user.getUsername().equals(sender);
    }

    /**
     * Gets the date.
     *
     * @return the date
     */
    public Date getDate()
    {
        return date;
    }

    /**
     * Sets the date.
     *
     * @param date
     *            the new date
     */
    public void setDate(Date date)
    {
        this.date = date;
    }

    /**
     * Gets the sender.
     *
     * @return the sender
     */
    public String getSender()
    {
        return sender;
    }

    /**
     * Sets the sender.
     *
     * @param sender
     *            the new sender
     */

    public void setSender(String sender)
    {
        this.sender = sender;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *            the new status
     */
    public void setStatus(int status)
    {
        this.status = status;
    }
    /**
     * Gets the status.
     *
     * @return the receiver
     */
    public String getReceiver()
    {
        return receiver;
    }

    /**
     * Sets the status.
     *
     * @param receiver
     */
    public void setReceiver(String receiver)
    {
        this.receiver = receiver;
    }


}