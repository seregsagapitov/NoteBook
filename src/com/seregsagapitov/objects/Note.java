package com.seregsagapitov.objects;

import javafx.beans.property.SimpleStringProperty;


public class Note {


    private SimpleStringProperty noteText = new SimpleStringProperty("");
    private SimpleStringProperty currentMoment = new SimpleStringProperty("");
    private SimpleStringProperty title = new SimpleStringProperty("");
    private Integer id;
    //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM. yyyy г HH:mm:ss");

    public Note(SimpleStringProperty noteText) {

    }

    public Note(Integer id) {
        this.id = id;
    }


    public Note(String noteText, String currentMoment, Integer id) {
        this.noteText = new SimpleStringProperty(noteText);
        this.currentMoment = new SimpleStringProperty(currentMoment);
        this.id = id;

        if (noteText.length() >= 20) {
            if (noteText.substring(0, 20).contains("\n")) {
                this.title = new SimpleStringProperty((noteText.split("\n")[0]) + "..." + "\n" + String.format("%60s", currentMoment));
            } else {
                this.title = new SimpleStringProperty(noteText.substring(0, 20) + "..." + "\n" + String.format("%60s", currentMoment));
            }
        }
        if (noteText.split(" \n")[0].length() < 20) {
            if (noteText.contains("\n")) {

                this.title = new SimpleStringProperty(noteText.split("\n")[0] + "..." + "\n" + String.format("%60s", currentMoment));
            } else {
                this.title = new SimpleStringProperty(noteText + "..." + "\n" + String.format("%60s", currentMoment));
            }

        }
    }

    public String getNoteText() {
        return noteText.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setNoteText(String noteText) {
        this.noteText.set(noteText);
    }

    public String getTitle() {
        return title.get();
    }


    public String getCurrentMoment() {
        return currentMoment.get();
    }


    public void setCurrentMoment(String currentMoment) {
        this.currentMoment.set(currentMoment);
    }

    public SimpleStringProperty noteTextProperty() {
        return noteText;
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteText=" + noteText +
                ", currentMoment=" + currentMoment +
                ", title=" + title +
                ", id=" + id +
                '}';
    }

    public Integer getId() {
        return id;
    }


}
