package com.example.entitymaker;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.*;

public class SQLGenerator implements Initializable {
    String script = "";
    String alterScript = "";
    String linkScript = "";
    public static Map<String, String[][]> tables = new HashMap<String, String[][]>();
    public static Map<String, String[]> links = new HashMap<String, String[]>();
    //key: linkId; value:[sourceId, targetId, type]

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] nameKeys = TableEditor.nameMap.keySet().toArray(new String[0]);
        String[] linkKeys = TableEditor.linkMap.keySet().toArray(new String[0]);

        for(int i = 0; i < TableEditor.nameMap.size(); i++){
            tables.put(TableEditor.nameMap.get(nameKeys[i]), TableEditor.columnMap.get(nameKeys[i]));
        }

        for(int i = 0; i < TableEditor.linkMap.size(); i++){
            String[] buffer = TableEditor.linkMap.get(linkKeys[i]);
            String source = TableEditor.nameMap.get(buffer[0]);
            String target = TableEditor.nameMap.get(buffer[1]);;
            String type = buffer[2];
            links.put(linkKeys[i], new String[]{source, target, type});
        }

        String[] tablesKeys = tables.keySet().toArray(new String[0]);

        /*
         CREATE TABLE IF NOT EXISTS `mydb`.`table1` (
             `idtable1` INT NOT NULL,
             `table1col` INT NOT NULL AUTO_INCREMENT,
             `table1col1` VARCHAR(45) NOT NULL,
              PRIMARY KEY (`idtable1`))
         ENGINE = InnoDB;
        */

        //   0-title 1-type 2-pk 3-nn 4-ai

        for(int i = 0; i < tables.size(); i++){
            script += "CREATE TABLE IF NOT EXISTS `" +  tablesKeys[i] +"`(\n";
            String[][] value = tables.get(tablesKeys[i]);
            for(int k = 0; k < value.length; k++){
                for(int j = 0; j < 5; j++ ){
                    if(j == 0){
                        script += "`"+ value[k][j] + "` ";
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

        for(int i = 0; i < tables.size(); i++){
            String[][] value = tables.get(tablesKeys[i]);
            for(int k = 0; k < value.length; k++){
                for(int j = 0; j < 5; j++ ){
                    if(value[k][j].equals("PK")){
                        alterScript += "ALTER TABLE `" +  tablesKeys[i] +"` ADD PRIMARY KEY ("+value[k][0]+");\n";
                    }
                }
            }
        }

        for(int i = 0; i < links.size(); i++){
            String[] value = links.get(linkKeys[i]);
            String sourceColumn = "";
            String targetColumn = "";
            String targetColumnType = "";
            String[][] sourceTable = tables.get(value[0]);
            String[][] targetTable = tables.get(value[1]);

            for(int k = 0; k < sourceTable.length; k ++){
                if(sourceTable[k][2].equals("PK")){
                    sourceColumn = sourceTable[k][0];
                }
            }

            for(int k = 0; k < targetTable.length; k ++){
                if(targetTable[k][2].equals("PK")){
                    targetColumn = targetTable[k][0];
                    targetColumnType = targetTable[k][1];
                }
            }

            linkScript += "ALTER TABLE " + value[0] + " ADD COLUMN " + value[1] + "_" + targetColumn + " " +
                    targetColumnType + " NOT NULL;\n" +

                    "ALTER TABLE " + value[0] + " ADD CONSTRAINT " + "fk_" + value[0] + "_" + value[1] +
                    " FOREIGN KEY (" + value[1] + "_" + targetColumn + ") REFERENCES " + value[1] + " (" +
                    targetColumn + ");";
        }

        System.out.println(script);
        System.out.println(alterScript);
        System.out.println(linkScript);

    }
}