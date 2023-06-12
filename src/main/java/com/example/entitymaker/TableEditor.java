package com.example.entitymaker;

import java.util.HashMap;
import java.util.Map;

public class TableEditor {
    // Объект для хранения данных о названиях таблиц
    public static Map<String, String> nameMap = new HashMap<String, String>();
    // Объект для хранения данных о столбцах таблиц и их атрибутах
    public static Map<String, String[][]> columnMap = new HashMap<String, String[][]>();
    // Объект для хранения данных о связях между таблицами
    public static Map<String, String[]> linkMap = new HashMap<String, String[]>();
    public static String tableId;
    // Переменная, хранящая данные о типе связи (1:1 || 1:N)
    public static boolean linkColour = true;
}
