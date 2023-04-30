package com.example.entitymaker;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.util.ResourceBundle;

public class TableConstructor implements Initializable{
    public TableView<Table> tableView;
    public TableColumn<Table, String> title_column;
    public TableColumn<Table, String> type_column;
    public TableColumn<Table, Boolean> pk_column;
    public TableColumn<Table, Boolean> nn_column;
    public TableColumn<Table, Boolean> ai_column;
    public TextField title_field;
    public ComboBox type_comboBox;
    public CheckBox pk_check;
    public CheckBox ai_check;
    public CheckBox nn_check;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        title_column.setCellValueFactory(new PropertyValueFactory<>("Title"));
        type_column.setCellValueFactory(new PropertyValueFactory<>("Type"));
        pk_column.setCellValueFactory(new PropertyValueFactory<>("Pk"));
        nn_column.setCellValueFactory(new PropertyValueFactory<>("Nn"));
        ai_column.setCellValueFactory(new PropertyValueFactory<>("Ai"));

        tableView.setEditable(true);
        title_column.setCellFactory(TextFieldTableCell.forTableColumn());
    }


    public void addRow(ActionEvent actionEvent) {
        Table table = new Table(title_field.getText(), type_comboBox.toString(),
                pk_check.isSelected(), nn_check.isSelected(), ai_check.isSelected());
        tableView.getItems().add(table);
    }

    public void deleteRow(ActionEvent actionEvent) {
        ObservableList<Table> allColumns, singleColumn;
        allColumns = tableView.getItems();
        singleColumn = tableView.getSelectionModel().getSelectedItems();
        singleColumn.forEach(allColumns::remove);
    }

    public void onEditChanged(TableColumn.CellEditEvent<Table, String> tableStringCellEditEvent) {
        Table table = tableView.getSelectionModel().getSelectedItem();
        table.setTitle(tableStringCellEditEvent.getNewValue());
    }
}
