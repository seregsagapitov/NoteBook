package com.seregsagapitov.interfaces;

import com.seregsagapitov.objects.Note;

import java.util.ArrayList;

public interface NoteImpl {

    // Добавить запись
    void addNote(Note note);

    // Внести изменения в запись (подтвердить изменения записи)
    void update(Note note);

    //Удалить запись
    void delete (ArrayList<Note> notes);

    //Переместить запись
    void replace(ArrayList<Note> notes, String newTable);
}
