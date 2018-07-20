package com.vedas.weightloss.Models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Rise on 28/06/2018.
 */
@DatabaseTable(tableName = "Remainder")
public class RemainderObject {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @DatabaseField(generatedId = true,columnName = "reminderid")
    int id;

    @DatabaseField(columnName = "status")
    private String status;

    @DatabaseField(columnName = "notes")
    private String notes;

    @DatabaseField(columnName = "days")
    private String days;

    @DatabaseField(columnName = "timestamp")
    private String timestamp;

    @DatabaseField(columnName = "currenttime")
    private String currenttime;

    @DatabaseField(columnName = "remaindertype")
    private String remaindertype;



    public boolean isFromOnline() {
        return isFromOnline;
    }

    public void setFromOnline(boolean fromOnline) {
        isFromOnline = fromOnline;
    }

    @DatabaseField(dataType= DataType.BOOLEAN,columnName = "ischecked")
    private  boolean isFromOnline;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @DatabaseField(columnName = "user_id", canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private User user;

    public RemainderObject() {
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(String currenttime) {
        this.currenttime = currenttime;
    }

    public String getRemaindertype() {
        return remaindertype;
    }

    public void setRemaindertype(String remaindertype) {
        this.remaindertype = remaindertype;
    }




   /* public String status;
    public String notes;
    public String days;
    public String timestamp;
    public String currenttime;
    public String remaindertype;*/


   /* public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(String currenttime) {
        this.currenttime = currenttime;
    }

    public String getRemaindertype() {
        return remaindertype;
    }

    public void setRemaindertype(String remaindertype) {
        this.remaindertype = remaindertype;
    }

    public String getRemainderstatus() {
        return status;
    }

    public void setRemainderstatus(String remainderstatus) {
        this.status = remainderstatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
*/


}
