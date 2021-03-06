package com.example.wangbeimin.Nodepad.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wangbeimin.Nodepad.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener{

    private RecyclerViewOnItemClickListener onItemClickListener;
    private RecyclerViewOnItemLongClickListener onItemLongClickListener;


    private List<Note> myNoteList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private View view;
        TextView NoteName;
        TextView TimeShow;

        public ViewHolder(View view){
            super(view);
            this.view=view;
            NoteName = view.findViewById(R.id.note_message);
            TimeShow = view.findViewById(R.id.time_shows);
        }
    }

    public NoteAdapter(List<Note> noteList){
        myNoteList = noteList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return holder;
    }

    @Override
    public void onClick(View v) { if (onItemClickListener != null) {
        onItemClickListener.onItemClickListener(v, (Integer) v.getTag());
        }
    }

    @Override public boolean onLongClick(View v) {
        return onItemLongClickListener != null && onItemLongClickListener.onItemLongClickListener(v, (Integer) v.getTag());
    }

    public void setRecyclerViewOnItemClickListener(RecyclerViewOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

    }
    public void setOnItemLongClickListener(RecyclerViewOnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface RecyclerViewOnItemClickListener {
        void onItemClickListener(View view, int position);
    }

    public interface RecyclerViewOnItemLongClickListener {
        boolean onItemLongClickListener(View view, int position);
    }
    @Override

    public void onBindViewHolder(final ViewHolder holder, int position) {

        Note Note = myNoteList.get(position);
        holder.NoteName.setText(isChinese(Note.getMessage()));
        holder.TimeShow.setText(Note.getYear()+"/"+Note.getMonth()+"/"+Note.getDay());
        holder.view.setTag(position);
    }

    public boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FA5;
    }

    public String isChinese(String str) {
        String chinses="";
        for (char c : str.toCharArray()) {
            if (isChinese(c)) {
                chinses+=c;
                if (chinses.length()>10){
                    break;
                }
            }
        }
        if (chinses==""){
            chinses="无汉语内容，你写什么笔记";
        }
        return chinses;
    }

    @Override
    public int getItemCount() {
        return myNoteList.size();
    }
}