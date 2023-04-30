package com.example.entitymaker;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class Table {
    private SimpleStringProperty Title;
    private SimpleStringProperty Type;
    private SimpleBooleanProperty pk;
    private SimpleBooleanProperty nn;
    private SimpleBooleanProperty ai;

    public Table(String title, String type, Boolean pk, Boolean nn, Boolean ai) {
        Title = new SimpleStringProperty(title);
        Type = new SimpleStringProperty(type);
        this.pk = new SimpleBooleanProperty(pk);
        this.nn = new SimpleBooleanProperty(nn);
        this.ai = new SimpleBooleanProperty(ai);
    }

    public String getTitle() {
        return Title.get();
    }

    public void setTitle(String title) {
        Title = new SimpleStringProperty(title);
    }

    public String getType() {
        return Type.get();
    }

    public void setType(String type) {
        Type = new SimpleStringProperty(type);
    }

    public boolean isPk() {
        return pk.get();
    }

    public void setPk(Boolean pk) {
        this.pk = new SimpleBooleanProperty(pk);
    }

    public boolean isNn() {
        return nn.get();
    }

    public void setNn(Boolean nn) {
        this.nn = new SimpleBooleanProperty(nn);
    }

    public boolean isAi() {
        return ai.get();
    }

    public void setAi(Boolean ai) {
        this.ai = new SimpleBooleanProperty(ai);
    }
}
