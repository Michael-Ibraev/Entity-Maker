package com.example.entitymaker;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class MySQLGeneratorTest extends TestCase {

    // Объект для хранения данных о названиях таблиц
    public static Map<String, String> nameMap = new HashMap<String, String>();
    // Объект для хранения данных о столбцах таблиц и их атрибутах
    public static Map<String, String[][]> columnMap = new HashMap<String, String[][]>();
    // Объект для хранения данных о связях между таблицами
    public static Map<String, String[]> linkMap = new HashMap<String, String[]>();

//    @Test
//    public void testMySQLGenerateScript() {
//        nameMap.put("1", "Table");
//        nameMap.put("2", "Table1");
//
//        columnMap.put("1", new String[][]{new String[]{"col1", "INT", "PK", "NN", ""},
//                new String[]{"col2", "VARCHAR(12)", "", "NN", ""}});
//
//        columnMap.put("2", new String[][]{new String[]{"col1", "INT", "PK", "NN", ""},
//                new String[]{"col2", "DECIMAL(10,2)", "", "NN", ""}});
//
//        linkMap.put("1", new String[]{"1" , "2", "1:1"});
//
//        MySQLGenerator mySQLGenerator = new MySQLGenerator();
//        assertEquals("    CREATE TABLE IF NOT EXISTS `Table`(\n" +
//                "        `col1` INT PRIMARY KEY NOT NULL,\n" +
//                "        `col2` VARCHAR(12) NOT NULL);\n" +
//                "\n" +
//                "        CREATE TABLE IF NOT EXISTS `Table2`(\n" +
//                "        `col1` INT PRIMARY KEY NOT NULL,\n" +
//                "        `col2` DECIMAL(10,2) NOT NULL);\n" +
//                "\n" +
//                "        ALTER TABLE `Table` ADD COLUMN Table2_col1 INT UNIQUE NOT NULL;\n" +
//                "\n" +
//                "        ALTER TABLE `Table` ADD CONSTRAINT fk_Table_Table2\n" +
//                "        FOREIGN KEY (Table2_col1) REFERENCES `Table2` (col1);",
//                mySQLGenerator.generateScript(columnMap, linkMap, nameMap));
//    }
//
//    @Test
//    public void testSQLiteGenerateScript() {
//        nameMap.put("1", "Table");
//        nameMap.put("2", "Table1");
//
//        columnMap.put("1", new String[][]{new String[]{"col1", "INT", "PK", "NN", ""},
//                new String[]{"col2", "TEXT", "", "NN", ""}});
//
//        columnMap.put("2", new String[][]{new String[]{"col1", "INT", "PK", "NN", ""},
//                new String[]{"col2", "REAL", "", "NN", ""}});
//
//        linkMap.put("1", new String[]{"1" , "2", "1:M"});
//
//        SQLiteGenerator sqLiteGenerator = new SQLiteGenerator();
//        assertEquals("    CREATE TABLE IF NOT EXISTS `Table`(\n" +
//                        "        `col1` INT PRIMARY KEY NOT NULL,\n" +
//                        "        `col2` VARCHAR(12) NOT NULL);\n" +
//                        "\n" +
//                        "        CREATE TABLE IF NOT EXISTS `Table2`(\n" +
//                        "        `col1` INT PRIMARY KEY NOT NULL,\n" +
//                        "        `col2` DECIMAL(10,2) NOT NULL);\n" +
//                        "\n" +
//                        "        ALTER TABLE `Table` ADD COLUMN Table2_col1 INT NOT NULL;\n" +
//                        "\n" +
//                        "        ALTER TABLE `Table` ADD CONSTRAINT fk_Table_Table2\n" +
//                        "        FOREIGN KEY (Table2_col1) REFERENCES `Table2` (col1);",
//                sqLiteGenerator.generateScript(columnMap, linkMap, nameMap));
//    }
}