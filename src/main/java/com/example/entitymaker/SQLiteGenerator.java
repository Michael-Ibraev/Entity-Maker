package com.example.entitymaker;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SQLiteGenerator  implements Initializable {
    String script = "";
    String alterScript = "";
    String linkScript = "";
    @FXML
    TextArea textArea;
    @FXML
    TextField jdbcField;
    @FXML
    Button submitBtn;
    public static Map<String, String[][]> tables = new HashMap<String, String[][]>();
    public static Map<String, String[]> links = new HashMap<String, String[]>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        jdbcField.setText("jdbc:sqlite:/home/mibraev/tt.db");
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

        for(int i = 0; i < tables.size(); i++){
            script += "CREATE TABLE IF NOT EXISTS `" +  tablesKeys[i] +"`(\n";
            String[][] value = tables.get(tablesKeys[i]);
            for(int k = 0; k < value.length; k++){
                for(int j = 0; j < 5; j++ ){
                    if(j == 0){
                        script += "`"+ value[k][j] + "` ";
                    } else if (j == 1) {
                        if(value[k][j].equals("INT")){
                            script += "INTEGER";
                        } else {
                            script += value[k][j];
                        }
                    } else if (value[k][j].equals("PK")) {
                        script += " PRIMARY KEY";
                    } else if (value[k][j].equals("NN")){
                        script += " NOT NULL";
                    } else if (value[k][j].equals("AI")){
                        script += " AUTOINCREMENT";
                    }
                }
                if(k == value.length-1){
                    script += ");\n\n";
                } else {
                    script += ",\n";
                }
            }
        }

        for(int i = 0; i < links.size(); i++){
            String[] value = links.get(linkKeys[i]);
            String targetColumn = "";
            String targetColumnType = "";
            String[][] targetTable = tables.get(value[1]);

            for(int k = 0; k < targetTable.length; k ++){
                if(targetTable[k][2].equals("PK")){
                    targetColumn = targetTable[k][0];
                    targetColumnType = targetTable[k][1];

                }
            }
            if(value[2].equals("1:M")){
                linkScript += "ALTER TABLE `" + value[0] + "` ADD COLUMN " + value[1] + "_" + targetColumn + " " +
                        targetColumnType + " NOT NULL;\n\n" +

                        "ALTER TABLE `" + value[0] + "` ADD CONSTRAINT " + "fk_" + value[0] + "_" + value[1] +
                        "\nFOREIGN KEY (" + value[1] + "_" + targetColumn + ") REFERENCES `" + value[1] + "` (" +
                        targetColumn + ");\n\n";
            } else if(value[2].equals("1:1")){
                linkScript += "ALTER TABLE `" + value[0] + "` ADD COLUMN `" + value[1] + "_" + targetColumn + "` " +
                        targetColumnType + " UNIQUE NOT NULL;\n\n" +

                        "ALTER TABLE `" + value[0] + "` ADD CONSTRAINT " + "fk_" + value[0] + "_" + value[1] +
                        "\nFOREIGN KEY (" + value[1] + "_" + targetColumn + ") REFERENCES `" + value[1] + "` (" +
                        targetColumn + ");\n\n";
            }
        }
        textArea.setText(script + alterScript + linkScript);
    }

    public void pushScript() throws SQLException {
        String[] script = textArea.getText().split(";");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcField.getText());
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText("Connection Error");
            alert.setContentText(e.getMessage()+"\nCheck the JDBC String");
            alert.show();
        }
        Statement statement = connection.createStatement();
        for(int i = 0; i < script.length - 1; i++){
            script[i] += ";";
            if(script[i].contains("ALTER TABLE") && script[i].contains("UNIQUE")){
                script[i] = script[i].replace(" UNIQUE", "");
                script[i] = script[i].replace("INT", "INTEGER");
                statement.execute(script[i]);
                String tableName[] = script[i].split(" ");
                String query = "select sql from sqlite_master where name = "+tableName[2];
                query = query.replace('`','\'');
                Statement alterStatement = connection.createStatement();
                ResultSet resultSet = alterStatement.executeQuery(query);
                String res = resultSet.getString("sql");
                String uniqueScript = res.substring(0, res.length()-9)+"UNIQUE " + res.substring(res.length()-9);
                uniqueScript = uniqueScript.replace("`", "\'") + ";";
                alterStatement.execute("PRAGMA foreign_keys = 0;");
                alterStatement.execute("DROP TABLE [" + tableName[2].replace("`", "") + "];");
                alterStatement.execute(uniqueScript);
                script[i] = "\nPRAGMA foreign_keys = 1;";
            }
            if(script[i].contains("ALTER TABLE") && script[i].contains("FOREIGN")){
                String[] fkMyScript = script[i].split(" ");
                String fk0 = "PRAGMA foreign_keys = 0;";
                String fk1 = "PRAGMA foreign_keys = 1;";
                String dropTable = "DROP TABLE [" + fkMyScript[2].replace("`", "") + "];";

                String query = "select sql from sqlite_master where name = "+fkMyScript[2];
                query = query.replace("`", "\'");
                Statement alterStatement = connection.createStatement();
                ResultSet resultSet = alterStatement.executeQuery(query);
                String res = resultSet.getString("sql");

                String fkScript = res.substring(0, res.length()-1) + ", FOREIGN KEY("
                        + fkMyScript[7].replace("(", "").replace(")", "") + ") " +
                        " REFERENCES " + fkMyScript[9].replace("`","") + "(" +
                        fkMyScript[10].replace("(", "").replace(");", "") + "));";

                alterStatement.execute(fk0);
                alterStatement.execute(dropTable);
                alterStatement.execute(fkScript);
                script[i] = fk1;
            }
            try {
                statement.execute(script[i]);
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("SQL Syntax Error");
                alert.setHeaderText("SQL Syntax Error");
                alert.setContentText(e.getMessage());
                alert.show();
            }
        }
        connection.close();
    }
}