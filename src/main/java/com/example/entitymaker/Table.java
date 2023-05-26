package com.example.entitymaker;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

public class Table {
    private SimpleStringProperty Title;
    private ComboBox Type;
    private CheckBox pk;
    private CheckBox nn;
    private CheckBox ai;

    public Table(){
        Title = new SimpleStringProperty();
        Type = new ComboBox();
        ObservableList<String> types = FXCollections.observableArrayList("---","INT", "VARCHAR()", "DECIMAL()", "TEXT", "REAL");
        Type.setItems(types);
        Type.getSelectionModel().selectFirst();
        Type.setEditable(true);
        this.pk = new CheckBox();
        this.nn = new CheckBox();
        this.ai = new CheckBox();
    }
    public Table(String title, String type, Boolean pk, Boolean nn, Boolean ai) {
        Title = new SimpleStringProperty();
        Type = new ComboBox();
        ObservableList<String> types = FXCollections.observableArrayList("---","INT", "VARCHAR()", "DECIMAL()", "TEXT", "REAL");
        Type.setItems(types);
        Type.getSelectionModel().selectFirst();
        Type.setEditable(true);
        this.pk = new CheckBox();
        this.nn = new CheckBox();
        this.ai = new CheckBox();
        this.Title.set(title);
        this.Type.getSelectionModel().select(type);
        if(pk){
            this.pk.setSelected(true);
        }
        if(nn){
            this.nn.setSelected(true);
        }
        if(ai){
            this.ai.setSelected(true);
        }
    }

    public String getTitle() {
        return Title.get();
    }

    public void setTitle(String title) {
        Title = new SimpleStringProperty(title);
    }

    public ComboBox getType() {
        return Type;
    }

    public void setType(ComboBox type) {
        this.Type = type;
    }

    public CheckBox getPk() {
        return pk;
    }

    public void setPk(CheckBox pk) {
        this.pk = pk;
    }

    public CheckBox getNn() {
        return nn;
    }

    public void setNn(CheckBox nn) {
        this.nn = nn;
    }

    public CheckBox getAi() {
        return ai;
    }

    public void setAi(CheckBox ai) {
        this.ai = ai;
    }
}
