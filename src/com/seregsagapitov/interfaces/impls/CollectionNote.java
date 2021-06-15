package com.seregsagapitov.interfaces.impls;

import com.seregsagapitov.DB.ConnectDB;
import com.seregsagapitov.interfaces.NoteImpl;
import com.seregsagapitov.objects.Note;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.ArrayList;

import static com.seregsagapitov.DB.ConnectDB.*;

// класс реализовывает интерфейс с помощью коллекции
public class CollectionNote implements NoteImpl {

    public static ObservableList<Note> noteList = FXCollections.observableArrayList();

    @Override
    public void addNote(Note note) {
        try {
            addData(note);
            //updateData(note);
            showData(noteList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        //noteList.add(note);
    }

    // метод будет пригоден в случае с БД
    @Override
    public void update(Note note) {

        try {
            updateData(note);
            showData(noteList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Note note) {
        try {
            deleteData(note);
            showData(noteList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // noteList.remove(note);
    }

    @Override
    public void replace(Note note, String newTable) {

        try {
            replaceFrom(note, newTable);
            showData(noteList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Note> getNoteList() {
        return noteList;
    }

//    public void print() {
//        int number = 0;
//        System.out.println();
//        for (Note note : noteList) {
//            number++;
//            System.out.println(note.getId() + note.getNoteText() + " " + note.getCurrentMoment().toString());
//        }
//    }

    public void fillTestData() throws SQLException {
        showData(noteList);


//        noteList.add(new Note("Первая строка", new Date()));
//        noteList.add(new Note("Вторая строка", new Date()));
//        noteList.add(new Note("Третья строка", new Date()));
//        noteList.add(new Note("Четвертая строка", new Date()));
//        noteList.add(new Note("Пятая строка", new Date()));
//        noteList.add(new Note("Шестая строка", new Date()));
//        noteList.add(new Note("Седьмая строка", new Date()));
//        noteList.add(new Note("Восьмая строка", new Date()));
//        noteList.add(new Note("Девятая строка", new Date()));
    }


}


