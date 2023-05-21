package com.example.entitymaker;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TableConstructor implements Initializable{
    public TableView<Table> tableView;
    public TableColumn<Table, String> title_column;
    public TableColumn<Table, String> type_column;
    public TableColumn<Table, Boolean> pk_column;
    public TableColumn<Table, Boolean> nn_column;
    public TableColumn<Table, Boolean> ai_column;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        title_column.setCellValueFactory(new PropertyValueFactory<>("Title"));
        type_column.setCellValueFactory(new PropertyValueFactory<>("Type"));
        pk_column.setCellValueFactory(new PropertyValueFactory<>("Pk"));
        nn_column.setCellValueFactory(new PropertyValueFactory<>("Nn"));
        ai_column.setCellValueFactory(new PropertyValueFactory<>("Ai"));

        tableView.setEditable(true);
        title_column.setCellFactory(TextFieldTableCell.forTableColumn());

        ObservableList<Table> tables = FXCollections.observableArrayList();

        String [][] buffer = TableEditor.columnMap.get(TableEditor.tableId);
        if(buffer != null) {
            for (int i = 0; i < buffer.length; i++) {
                tableView.getItems().add(new Table(buffer[i][0], buffer[i][1], toBoolean(buffer[i][2]), toBoolean(buffer[i][3]), toBoolean(buffer[i][4])));
            }
        }
    }


    public void addRow(ActionEvent actionEvent) {
        tableView.getItems().add(new Table());
    }

    public void deleteRow(ActionEvent actionEvent) {
        ObservableList<Table> allColumns, singleColumn;
        allColumns = tableView.getItems();
        singleColumn = tableView.getSelectionModel().getSelectedItems();
        singleColumn.forEach(allColumns::remove);
    }

    public void onTitleEdit(TableColumn.CellEditEvent<Table, String> tableStringCellEditEvent) {
        Table table = tableView.getSelectionModel().getSelectedItem();
        table.setTitle(tableStringCellEditEvent.getNewValue());
    }

    public void save(){
        Table table =  new Table();

        List <List<String>> arrList = new ArrayList<>();

        for (int i = 0; i<tableView.getItems().size(); i++){
            table = tableView.getItems().get(i);
            arrList.add(new ArrayList<>());
            arrList.get(i).add(table.getTitle());
            arrList.get(i).add(table.getType().getValue().toString());
            arrList.get(i).add(getPkValue(table.getPk()));
            arrList.get(i).add(getNnValue(table.getNn()));
            arrList.get(i).add(getAiValue(table.getAi()));

        }
        String[][] arr = new String[arrList.size()][5];

        for(int i = 0; i < arrList.size(); i++){
            for(int j = 0; j < arrList.get(i).size(); j++){
               // System.out.println(arrList.get(i).get(j));
                arr[i][j] = arrList.get(i).get(j);
            }
          //  System.out.println("-----------------------");
        }
        TableEditor.columnMap.put(TableEditor.tableId, arr);
    }

    public String getPkValue(CheckBox checkBox){
        String state;
        if(checkBox.isSelected()){
            state = "PK";
        } else {
            state = "";
        }
        return state;
    }
    public String getNnValue(CheckBox checkBox){
        String state;
        if(checkBox.isSelected()){
            state = "NN";
        } else {
            state = "";
        }
        return state;
    }
    public String getAiValue(CheckBox checkBox){
        String state;
        if(checkBox.isSelected()){
            state = "AI";
        } else {
            state = "";
        }
        return state;
    }
    public Boolean toBoolean(String key){
        if(key.equals("PK") || key.equals("NN") || key.equals("AI")){
            return true;
        }
        else {
            return false;
        }
    }
}
