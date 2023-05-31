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
    String[] linkKeys;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] nameKeys = TableEditor.nameMap.keySet().toArray(new String[0]);

        for(int i = 0; i < TableEditor.nameMap.size(); i++){
            tables.put(TableEditor.nameMap.get(nameKeys[i]), TableEditor.columnMap.get(nameKeys[i]));
        }

        tablesKeys = tables.keySet().toArray(new String[0]);
        linkKeys = TableEditor.linkMap.keySet().toArray(new String[0]);



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
                    "public class " + fileName + " {\n" +
                    "\tpublic " + fileName + "() {\n\t}");
            String[][] value = tables.get(tablesKeys[i]);
            for(int k = 0; k < value.length; k++) {
                String columnName = "";
                String columnType = "";
                boolean pk = false;
                for (int j = 0; j < 5; j++) {
                    if (j == 0) {
                        columnName = value[k][j];
                    } else if (j == 1) {
                        if (value[k][j].equals("INT")) {
                            columnType = "int";
                        } else if (value[k][j].contains("VARCHAR") || value[k][j].contains("TEXT")) {
                            columnType = "String";
                        } else if (value[k][j].contains("REAL") || value[k][j].contains("DECIMAL")) {
                            columnType = "float";
                        }
                    } else if (j == 2) {
                        if (value[k][j].equals("PK")) {
                            pk = true;
                        } else {
                            pk = false;
                        }
                    }
                }
                if (pk) {
                    fileWriter.write("\n\t@Id\n" +
                            "\t@GeneratedValue(strategy = GenerationType.IDENTITY)\n");
                }
                fileWriter.write("\n\t@Column(name = \"" + columnName + "\")\n" +
                        "\tprivate " + columnType + " " + columnName + ";\n\n");

                fileWriter.write("\tpublic " + columnType + " get" + columnName + "() {\n" +
                            "\t\t return " + columnName + ";\n\t}\n");
                fileWriter.write("\tpublic void set" + columnName + "(" + columnType + " " +
                        columnName.toLowerCase() + ") {\n" +
                        "\t\tthis." + columnName + " = " + columnName.toLowerCase() + ";\n\t}");
            }
            for (int j = 0; j < links.size(); j++) {
                String[] linkValue = links.get(linkKeys[j]);
                String[][] targetTable = tables.get(linkValue[1]);
                String targetColumn = "";
                String sourceColumn = "";
                if (linkValue[0].equals(fileName) && linkValue[2].equals("1:1")) {
                    for (int n = 0; n < targetTable.length; n++) {
                        if (targetTable[n][2].equals("PK")) {
                            targetColumn = targetTable[n][0];
                        }
                    }
                    fileWriter.write("\n\t@OneToOne\n" +
                            "\t@JoinColumn(name = \"" + linkValue[1] + "_" + targetColumn +
                            "\", referencedColumnName = \"" + targetColumn + "\")\n" +
                            "\tprivate " + linkValue[1] + " " + linkValue[1].toLowerCase() + ";\n");
                }
                if(linkValue[1].equals(fileName) && linkValue[2].equals("1:1")){
                    fileWriter.write("\n\t@OneToOne(mappedBy = \"" + linkValue[1] + "\")\n" +
                            "\tprivate " + linkValue[0] + " " + linkValue[0].toLowerCase() + ";\n");
                }

                if (linkValue[0].equals(fileName) && linkValue[2].equals("1:M")) {
                    for (int n = 0; n < targetTable.length; n++) {
                        if (targetTable[n][2].equals("PK")) {
                            targetColumn = targetTable[n][0];
                        }
                    }
                    fileWriter.write("\n\t@ManyToOne\n" +
                            "\t@JoinColumn(name = \"" + linkValue[1] + "_" + targetColumn + "\")\n" +
                            "\tprivate " + linkValue[1] + " " + linkValue[1].toLowerCase() + ";\n");
                }
                if(linkValue[1].equals(fileName) && linkValue[2].equals("1:M")){
                    fileWriter.write("\n\t@OneToMany(mappedBy = \"" + linkValue[1] + "\")\n" +
                            "\tprivate List<" + linkValue[0] + "> " + linkValue[0].toLowerCase()
                            + " = new ArrayList<>();\n");
                }
            }
            fileWriter.write("\n}");
            fileWriter.close();
        }
    }
}
