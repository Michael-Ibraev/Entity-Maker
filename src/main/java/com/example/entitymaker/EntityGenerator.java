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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        directoryChooser.setInitialDirectory(new File("/home/mibraev/entityClasses"));
        textField.setText(directoryChooser.getInitialDirectory().toString()+"/");

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


    }
    @FXML
    public void setBrowseBtn(){
        selectedDirectory = directoryChooser.showDialog(new Stage());
        textField.setText(selectedDirectory.getAbsolutePath()+"/");
    }

    public void entityGenerate(ActionEvent actionEvent) throws IOException {
        fileName = "qwerty";
        File file = new File(textField.getText() + fileName + ".java");
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write("package models;\n\n" +
                "import javax.persistence.*;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;");
        fileWriter.close();
    }
}
