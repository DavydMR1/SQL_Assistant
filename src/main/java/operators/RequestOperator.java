package operators;

import lombok.Setter;
import models.DBNames;
import models.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
public class RequestOperator {
    //Сборщик данных от пользователя в бд и обратно
    private SQL_Operator sql_operator;

    //Подключается к бд
    //[НЕ ПРИНИМАЕТ ПАРОЛИ Т.К. НА ДАННЫЙ МОМЕНТ ПОДДЕРЖИВАЕТСЯ ТОЛЬКО SQLITE]
    //(нужно подключить другие драйверы)
    public void createConnection(String userName, String password, String url, String driver) {
//        String protocol = "jdbc:sqlite:" + url + ";" + "user=" + userName + ";"
//                + "password=" + password + ";";
        String protocol = "jdbc:" + driver + ":" + url;
        sql_operator.tryConnection(protocol);
    }

    //Проверяет подключение через sql_operator
    public boolean checkConnection() {
        return sql_operator.testConnection();
    }

    //Запрашивает полный список бд у sql_operator
    public DBNames requestDBNames() {
        return sql_operator.requestDBList();
    }

    //Запрашивает конкретную бд у sql_operator
    public Table getDB(String title) {
        return sql_operator.getDB("SELECT * FROM " + title);
    }

    //Загоняет новую таблицу в бд
    //[КОММЕНТАРИИ ВНУТРИ - ШПОРА, НЕ УДАЛЯТЬ]
    //(нужно добавить изменеие типов колонок и размера таблиццы или вернуть к варианту из старой версии)
    public void updateDB(Table data) {
        //INSERT INTO table_name(primary_key)
        //SELECT primary_key_value
        //WHERE NOT EXISTS (SELECT 1 FROM table_name WHERE primary_key = primary_key_value);
        //
        //UPDATE table_name
        //SET any_non_primary_key = 'AAA'
        //WHERE primary_key = primary_key_value
        for (List<Object> tr : data.getTableData()) {
            StringBuilder statement = new StringBuilder();
            for (int i = 1; i < data.getRowTitles().size(); i++) {
                statement.append("INSERT INTO ").append(data.getTitle()).append("(").append(data.getPrimaryKeyName())
                        .append(")\nSELECT ").append(tr.get(0)).append("\nWHERE NOT EXISTS (SELECT 1 FROM ")
                        .append(data.getTitle()).append(" WHERE ").append(data.getPrimaryKeyName()).append(" = ")
                        .append(tr.get(0)).append(");\n\nUPDATE ").append(data.getTitle()).append("\nSET ")
                        .append(data.getRowTitles().get(i)).append(" = '").append(tr.get(i)).append("'\nWHERE ")
                        .append(data.getPrimaryKeyName()).append(" = ").append(tr.get(0)).append(";\n\n");
            }
            System.out.println(statement);
            sql_operator.insertStatement(statement.toString());
        }
    }

    //Всё, что ниже - генератор для sql запросов с несколькими колонками из старой версии
    //(нужно сильно переделать генератор запросов из updateDB)
    private String stringGenerator(List<Object> data, List<String> dataTypes) {
        List<List<String>> dataList = new ArrayList<>();
        List<String> rowList = new ArrayList<>();
        for (Object o : data) {
            rowList.add(String.valueOf(o));
        }
        dataList.add(dateAssembler(rowList, dataTypes));
        return builder(dataList);
    }

    private String builder(List<List<String>> data) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.get(0).size(); i++) {
            for (int j = 0; j < data.size(); j++) {
                if (j > 0) {
                    builder.append(" ");
                }
                builder.append(data.get(j).get(i));
            }
            if (i < data.get(0).size() - 1) {
                builder.append(",\n");
            }
            else {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    private List<String> dateAssembler(List<String> data, List<String> types) {
        for (int i = 0; i < types.size(); i++) {
            if (Objects.equals(types.get(i), "DATE")) {
                if (!String.valueOf(data.get(i).charAt(0)).equals("'")) {
                    data.set(i, "'" + data.get(i));
                }
                if (!String.valueOf(data.get(i).charAt(data.get(i).length() - 1)).equals("'")) {
                    data.set(i, data.get(i) + "'");
                }
            }
        }
        return data;
    }
}
