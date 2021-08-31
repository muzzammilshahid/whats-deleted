package com.deskconn.dmr.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "whatsapp_data", indices = @Index(value = {"name"}, unique = true))
public class WhatsappData implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "messages")
    private String messages;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo
    private Boolean isAdMOb = false;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getAdMOb() {
        return isAdMOb;
    }

    public void setAdMOb(Boolean adMOb) {
        isAdMOb = adMOb;
    }

}
