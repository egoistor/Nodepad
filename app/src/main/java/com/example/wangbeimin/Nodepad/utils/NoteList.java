package com.example.wangbeimin.Nodepad.utils;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class NoteList{

    static private List<Note> mainNoteList = new ArrayList<>();
    static private List<Note> deleteNoteList = new ArrayList<>();

    public void setMainNoteList(List<Note> list){
        this.mainNoteList = list;
    }

    public List getMainNoteList(){
        return mainNoteList;
    }

    public void setDeleteNoteList(List<Note> list){
        this.deleteNoteList = list;
    }

    public List getDeleteNoteLis(){
        return deleteNoteList;
    }
}
