package models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Table {
    //Всё, что нужно для бд
    private String title = "";
    private String primaryKeyName = "";
    private List<String> dataTypes = new ArrayList<>();
    private List<String> rowTitles = new ArrayList<>();
    private List<List<Object>> tableData = new ArrayList<>();

    //Добавляет колонку со всеми составляющими
    public void addColumn(int place) {
        dataTypes.add(place, "TEXT");
        rowTitles.add(place, "Row" + (place + 1));
        for (List<Object> tableDatum : tableData) {
            tableDatum.add(place, null);
        }
    }

    //Убирает колонку со всеми составляющими
    public void removeColumn(int column) {
        dataTypes.remove(column);
        rowTitles.remove(column);
        for (List<Object> tableDatum : tableData) {
            tableDatum.remove(column);
        }
    }
}
