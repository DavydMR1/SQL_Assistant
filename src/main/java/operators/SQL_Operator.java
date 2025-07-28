package operators;

import models.DBNames;
import models.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQL_Operator {
    //Только бля соединения с бд
    private Connection connection;

    //Попытка подключения
    public void tryConnection(String protocol) {
        try {
            connection = DriverManager.getConnection(protocol);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Проерка наличия подключения
    //(Надо улучшить систему проверки)
    public boolean testConnection() {
        return connection != null;
    }

    //Делает полный список бд через DBGetter
    public DBNames requestDBList() {
        DBGetter dbGetter = new DBGetter();
        return dbGetter.getCatalog(connection);
    }

    //Вытаскивает нужные для Table компоненты и на месте собирает
    public Table getDB(String statement) {
        try {
            Table table = new Table();
            ResultSet resultSet = connection.createStatement().executeQuery(statement);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet primKeys = databaseMetaData.getPrimaryKeys(null, null,
                    resultSetMetaData.getTableName(1));

            table.setTitle(resultSetMetaData.getTableName(1));

            for (int i = 1; i <= columnCount; i++) {
                table.getDataTypes().add(resultSetMetaData.getColumnTypeName(i));
                table.getRowTitles().add(resultSetMetaData.getColumnName(i));
                System.out.println(resultSetMetaData.getCatalogName(i) + " "
                        + resultSetMetaData.getColumnName(i) + " "
                        + resultSetMetaData.getColumnTypeName(i) + " "
                        + resultSetMetaData.getTableName(i));
            }

            while (primKeys.next()) {
                System.out.println(primKeys.getString("COLUMN_NAME"));
                table.setPrimaryKeyName(primKeys.getString("COLUMN_NAME"));
            }

            while (resultSet.next()) {
                List<Object> objects = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    objects.add(resultSet.getString(i));
                }
                table.getTableData().add(objects);
            }
            return table;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Просто приёмник запросов
    //[ЗАПРОСЫ ДЕЛАТЬ В RequestOperator]
    public void insertStatement(String statement) {
        try {
            connection.prepareStatement(statement).execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
