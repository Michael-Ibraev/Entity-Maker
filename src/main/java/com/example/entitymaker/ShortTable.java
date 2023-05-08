package com.example.entitymaker;

import javafx.beans.property.SimpleStringProperty;

public class ShortTable {
    public SimpleStringProperty Title;
    public SimpleStringProperty Pk;

    public ShortTable(String title, String pk) {
        Title = new SimpleStringProperty(title);
        Pk = new SimpleStringProperty(pk);
    }
    public String getTitle() {
        return Title.get();
    }
    public void setTitle(String title) {
        Title = new SimpleStringProperty(title);
    }
    public String getPk() {
        return Pk.get();
    }
    public void setPk(String pk) {
        Pk = new SimpleStringProperty(pk);
    }
}