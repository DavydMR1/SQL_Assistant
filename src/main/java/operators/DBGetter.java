package operators;

import models.DBNames;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBGetter {
    //Вся эта конструкция собирает список из бд и распределяет их по типам

    DBNames names;
    Connection connection;

    public DBNames getCatalog(Connection connection) {
        this.connection = connection;
        names = new DBNames();
        requestTables();
        requestViews();
        requestIndexes();
        requestSequences();
        return names;
    }

    private void requestTables() {
        try {
            ResultSet resultSet = connection.getMetaData().getTables(
                    connection.getCatalog(), null, "%",
                    new String[]{"TABLE"});
            while (resultSet.next()) {
                names.getTables().add(resultSet.getString(3));
                System.out.println(resultSet.getString(3));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void requestViews() {
        try {
            ResultSet resultSet = connection.getMetaData().getTables(
                    connection.getCatalog(), null, "%",
                    new String[]{"VIEW"});
            while (resultSet.next()) {
                names.getViews().add(resultSet.getString(3));
                System.out.println(resultSet.getString(3));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void requestIndexes() {
        try {
            ResultSet resultSet = connection.getMetaData().getTables(
                    connection.getCatalog(), null, "%",
                    new String[]{"INDEX"});
            while (resultSet.next()) {
                names.getIndexes().add(resultSet.getString(3));
                System.out.println(resultSet.getString(3));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void requestSequences() {
        try {
            ResultSet resultSet = connection.getMetaData().getTables(
                    connection.getCatalog(), null, "%",
                    new String[]{"SEQUENCE"});
            while (resultSet.next()) {
                names.getSequences().add(resultSet.getString(3));
                System.out.println(resultSet.getString(3));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
