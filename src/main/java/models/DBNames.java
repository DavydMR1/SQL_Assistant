package models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DBNames {
    //Список со всеми таблицами
    private List<String> tables = new ArrayList<>();
    private List<String> views = new ArrayList<>();
    private List<String> indexes = new ArrayList<>();
    private List<String> sequences = new ArrayList<>();

    //Поиск по имени во всех списках
    public boolean contains(String DBName) {
        if (tables.contains(DBName)) {
            return true;
        }
        if (views.contains(DBName)) {
            return true;
        }
        if (indexes.contains(DBName)) {
            return true;
        }
        if (sequences.contains(DBName)) {
            return true;
        }
        return false;
    }
}
