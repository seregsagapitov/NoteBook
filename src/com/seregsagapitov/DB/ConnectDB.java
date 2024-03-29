package com.seregsagapitov.DB;

import com.seregsagapitov.controllers.Controller;
import com.seregsagapitov.objects.Note;
import javafx.collections.ObservableList;
import javafx.scene.control.PasswordField;

import java.sql.*;
import java.util.ArrayList;

public class ConnectDB {
    public static Connection connection;
    private static Statement stmt;
    private static PreparedStatement ps;

    // F:\NoteText\src\com\seregsagapitov\DB
    //создание подключения
    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:NOTETEXT_DB.db");
            stmt = connection.createStatement();

            stmt.execute("CREATE TABLE IF NOT EXISTS NOTES (\n" +
                    "    ID       INTEGER        PRIMARY KEY AUTOINCREMENT\n" +
                    "     UNIQUE\n" +
                    "    NOT NULL,\n" +
                    "    NOTETEXT VARCHAR (8192) NOT NULL,\n" +
                    "    DATE     VARCHAR (256)  NOT NULL\n" +
                    ");");
            stmt.execute("CREATE TABLE IF NOT EXISTS PASSWORD(\n" +
                    "ID_PASS INTEGER       PRIMARY KEY AUTOINCREMENT \n" +
                    "     UNIQUE\n" +
                    "    NOT NULL,\n" +
                    "    PASS    VARCHAR (256) NOT NULL\n" +
                    ");");
            stmt.execute("CREATE TABLE IF NOT EXISTS RECYCLED (\n" +
                    "    ID       INTEGER        PRIMARY KEY AUTOINCREMENT\n" +
                    "     UNIQUE\n" +
                    "    NOT NULL,\n" +
                    "    NOTETEXT VARCHAR (8192) NOT NULL,\n" +
                    "    DATE     VARCHAR (256)  NOT NULL\n" +
                    ");");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // вывод данных из БД
    public static void printTable() throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + Controller.currentTable + " ;");
        while (rs.next()) {
            System.out.println(rs.getInt("ID") + " " + rs.getString("NOTETEXT") +
                    " " + rs.getString("DATE"));
        }
    }

    // вывод данных в TableView
    public static void showData(ObservableList<Note> noteList) throws SQLException {
        connect();
        noteList.clear();
        String query = "SELECT * FROM " + Controller.currentTable + " ;";
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {

            noteList.add(new Note(rs.getString("NOTETEXT"), rs.getString("DATE"), rs.getInt("ID")));
        }
        //printTable();
        disconnect();
    }

    // Заполнение данных в TreeMap по базам данных и их названиям
    public static void feelDataDB() throws SQLException {
        connect();
        //Controller.dataTable.clear();
        String query = "SELECT * FROM NAMES WHERE NAME_BASE like 'base%'";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            Controller.dataTable.put(rs.getString("NAME_BASE"), rs.getString("TITLE_BASE"));
            Controller.count = rs.getInt("ID_NAMES");
        }
        disconnect();
//        System.out.println(Controller.dataTable);
//        System.out.println(Controller.count);

    }

    // Запрос пароля из Таблицы PASSWORD
    public static String selectPassword() throws SQLException {
        String pass = "";
        connect();
        String query = "SELECT * FROM PASSWORD WHERE ID_PASS = 1";
        ResultSet rs = stmt.executeQuery(query);
        pass = rs.getString("PASS");
        disconnect();
        return pass;

    }

    // Установка пароля в таблицу PASSWORD
    public static void setPassword(PasswordField passwordField) throws SQLException {
        connect();
        ps = connection.prepareStatement("UPDATE PASSWORD SET PASS = ? WHERE ID_PASS = 1;");
        ps.setString(1, passwordField.getText());
        ps.executeUpdate();
        ps.close();
        disconnect();

    }


    // Обновление БД
    public static void updateData(Note note) throws SQLException {
        connect();
        ps = connection.prepareStatement("UPDATE " + Controller.currentTable + " SET NOTETEXT = ?, DATE = ? WHERE ID = ?;");
        ps.setString(1, note.getNoteText());
        ps.setString(2, note.getCurrentMoment());
        ps.setInt(3, note.getId());
        ps.executeUpdate();
        ps.close();
        disconnect();
    }

    // Очистка корзины
    public static void deleteFromRecycled() {
        connect();
        String sql1 = "DROP TABLE " + (Controller.currentTable) + ";";

        try {
            stmt = connection.createStatement();
            stmt.execute(sql1);
            // stmt.execute(sql2);
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        disconnect();
    }

    // Удаление данных из БД (Из Текущей таблицы)
    public static void deleteData(ArrayList<Note> notes) throws SQLException {

        replaceFrom(notes, "RECYCLED");
    }

    // Удаление данных из БД (строки из NAMES)
    public static void deleteItems(String deleteItem) throws SQLException {
        connect();
        //String nameFolderItem = Controller.currentTable;
        String sql = "DROP TABLE " + (deleteItem);
        stmt = connection.createStatement();
        stmt.execute(sql);
        stmt.close();
        ps = connection.prepareStatement("DELETE FROM NAMES WHERE NAME_BASE = ?");
        ps.setString(1, deleteItem);
        System.out.println(deleteItem + " удаляемая строка");
        ps.executeUpdate();
        ps.close();
        disconnect();
    }

    // закрытие соединения с БД
    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // удалениe таблицы из БД
    public static void dropTable() {
        try {
            connect();
            String nameTable = Controller.dataTable.get(Controller.currentTable);
            String nameFolderItem = Controller.currentTable;
            String sql = "DROP TABLE " + (nameFolderItem);
            stmt = connection.createStatement();
            stmt.execute(sql);
            System.out.println("Перед удалением папки " + nameTable);
            ps = connection.prepareStatement("DELETE FROM NAMES WHERE TITLE_BASE = ?");
            ps.setString(1, nameFolderItem);
            System.out.println(nameFolderItem + " папка удалена");
            ps.executeUpdate();
            ps.close();
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void replaceFrom(ArrayList<Note> notes, String newTable) throws SQLException {
        connect();
        connection.setAutoCommit(false);
        for (Note selectNotes : notes) {
            int idNote = selectNotes.getId();
            String sqlReplace1 = "INSERT INTO " + newTable + "  (NOTETEXT, DATE) SELECT NOTETEXT, DATE FROM " + Controller.currentTable + " WHERE ID=?;";
            String sqlReplace2 = "DELETE FROM " + Controller.currentTable + " WHERE ID = ?";

            ps = connection.prepareStatement(sqlReplace1);
            ps.setInt(1, idNote);
            ps.executeUpdate();
            ps = connection.prepareStatement(sqlReplace2);
            ps.setInt(1, idNote);
            ps.executeUpdate();
        }
        connection.commit();
        connection.setAutoCommit(true);
        System.out.println("Перемещаемые строки " + notes.toArray());
        ps.close();

        disconnect();
    }

    // Добавление данных в БД
    public static void addData(Note note) throws SQLException {
        // connection.setAutoCommit(false);
        connect();
        ps = connection.prepareStatement("INSERT INTO " + Controller.currentTable + "( NOTETEXT, DATE) VALUES (?,?);");
        ps.setString(1, note.getNoteText());
        ps.setString(2, note.getCurrentMoment());
        ps.execute();
        ps.close();
        disconnect();
        // connection.setAutoCommit(true);
    }
}
