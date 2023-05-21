package com.example.entitymaker;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.*;

public class SQLGenerator implements Initializable {
    String script = "";
    public static Map<String, String[][]> tables = new HashMap<String, String[][]>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] nameKeys = TableEditor.nameMap.keySet().toArray(new String[0]);

        for(int i = 0; i < TableEditor.nameMap.size(); i++){
            tables.put(TableEditor.nameMap.get(nameKeys[i]), TableEditor.columnMap.get(nameKeys[i]));
        }

        String[] tablesKeys = tables.keySet().toArray(new String[0]);

        /*
        * CREATE TABLE IF NOT EXISTS `mydb`.`table1` (
  `idtable1` INT NOT NULL,
  `table1col` INT NOT NULL AUTO_INCREMENT,
  `table1col1` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idtable1`))
ENGINE = InnoDB;
        * */

        //   0-title 1-type 2-pk 3-nn 4-ai

        for(int i = 0; i < tables.size(); i++){
            script += "CREATE TABLE IF NOT EXISTS `" +  tablesKeys[i] +"`(\n";
            String[][] value = tables.get(tablesKeys[i]);
            for(int k = 0; k < value.length; k++){
                for(int j = 0; j < 5; j++ ){
                    if(j == 0){
                        script += "`"+value[k][j].toString() + "` ";
                    } else if (j == 1) {
                        script += value[k][j];
                    } else if (value[k][j].equals("NN")){
                        script += " NOT NULL";
                    } else if (value[k][j].equals("AI")){
                        script += " AUTO_INCREMENT";
                    }
                }
                if(k == value.length-1){
                    script += ");\n\n";
                } else {
                    script += ",\n";
                }
            }
        }
        System.out.println(script);
    }
}
