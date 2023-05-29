package com.example.entitymaker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class EntityGenerator implements Initializable {
    @FXML TextField textField;
    @FXML Button browseBtn;
    String fileName;
    DirectoryChooser directoryChooser = new DirectoryChooser();
    File selectedDirectory;

    public static Map<String, String[][]> tables = new HashMap<String, String[][]>();
    public static Map<String, String[]> links = new HashMap<String, String[]>();
    String[] tablesKeys;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        directoryChooser.setInitialDirectory(new File("/home/mibraev/entityClasses"));
        textField.setText(directoryChooser.getInitialDirectory().toString()+"/");

        String[] nameKeys = TableEditor.nameMap.keySet().toArray(new String[0]);
        String[] linkKeys = TableEditor.linkMap.keySet().toArray(new String[0]);

        for(int i = 0; i < TableEditor.nameMap.size(); i++){
            tables.put(TableEditor.nameMap.get(nameKeys[i]), TableEditor.columnMap.get(nameKeys[i]));
        }

        tablesKeys = tables.keySet().toArray(new String[0]);


        for(int i = 0; i < TableEditor.linkMap.size(); i++){
            String[] buffer = TableEditor.linkMap.get(linkKeys[i]);
            String source = TableEditor.nameMap.get(buffer[0]);
            String target = TableEditor.nameMap.get(buffer[1]);;
            String type = buffer[2];
            links.put(linkKeys[i], new String[]{source, target, type});
        }


    }
    @FXML
    public void setBrowseBtn(){
        selectedDirectory = directoryChooser.showDialog(new Stage());
        textField.setText(selectedDirectory.getAbsolutePath()+"/");
    }

    public void entityGenerate(ActionEvent actionEvent) throws IOException {
//        for(int i = 0; i < tables.size(); i++){
//            script += "CREATE TABLE IF NOT EXISTS `" +  tablesKeys[i] +"`(\n";
//            String[][] value = tables.get(tablesKeys[i]);
//            for(int k = 0; k < value.length; k++){
//                for(int j = 0; j < 5; j++ ){
//                    if(j == 0){
//                        script += "`"+ value[k][j] + "` ";
//                    } else if (j == 1) {
//                        script += value[k][j];
//                    } else if (value[k][j].equals("PK")) {
//                        script += " PRIMARY KEY";
//                    } else if (value[k][j].equals("NN")){
//                        script += " NOT NULL";
//                    } else if (value[k][j].equals("AI")){
//                        script += " AUTO_INCREMENT";
//                    }
//                }
//                if(k == value.length-1){
//                    script += ");\n\n";
//                } else {
//                    script += ",\n";
//                }
//            }
//        }
        for(int i = 0; i < tables.size(); i++) {
            fileName = tablesKeys[i];
            File file = new File(textField.getText() + fileName + ".java");
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("package models;\n\n" +
                    "import javax.persistence.*;\n" +
                    "import java.util.*;\n\n" +
                    "@Entity\n" +
                    "@Table(name = " + "\"" + fileName + "\")\n" +
                    "public class " + fileName + " {\n");
            String[][] value = tables.get(tablesKeys[i]);
            for(int k = 0; k < value.length; k++){
                String columnName = "";
                String columnType = "";
                boolean pk = false;
                for(int j = 0; j < 5; j++){
                    if(j == 0){
                        columnName = value[k][j];
                    } else if(j == 1){
                        if(value[k][j].equals("INT")){
                            columnType = "int";
                        } else if(value[k][j].contains("VARCHAR") || value[k][j].contains("TEXT")){
                            columnType = "String";
                        } else if (value[k][j].contains("REAL") || value[k][j].contains("DECIMAL")){
                            columnType = "float";
                        }
                    } else if (j == 2) {
                        if(value[k][j].equals("PK")){
                            pk = true;
                        } else {
                            pk = false;
                        }
                    }
                }
                if(pk){
                    fileWriter.write("\t@Id\n");
                }
                fileWriter.write("\t@Column(name = \"" + columnName + "\")\n" +
                        "\tprivate " + columnType + " " + columnName + ";\n\n");
            }
            fileWriter.write("\n}");
            fileWriter.close();
        }
    }
}
