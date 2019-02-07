package com.example.wangbeimin.Nodepad.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wangbeimin.Nodepad.R;
import com.example.wangbeimin.Nodepad.utils.DeletedNote;
import com.example.wangbeimin.Nodepad.utils.DeletedNoteAdapter;
import com.example.wangbeimin.Nodepad.utils.DividerItemDecoration;
import com.example.wangbeimin.Nodepad.utils.Note;
import com.example.wangbeimin.Nodepad.utils.NoteAdapter;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class DeleteActivity extends AppCompatActivity {

    private List<DeletedNote> noteList = new ArrayList<>();
    final Calendar calendar = Calendar.getInstance();
    private String htmlData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        LitePal.initialize(this);
        SQLiteDatabase db = LitePal.getDatabase();
        ininView();
        addData();
        ininDataBase();
        ininRecycleView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ininView();
        addData();
        ininDataBase();
        ininRecycleView();
    }

    public void addData(){
        htmlData = getIntent().getStringExtra("MtoD");
        if (htmlData!=null){
            DeletedNote note = new DeletedNote();
            note.setMessage(htmlData);
            note.setYear(calendar.get(Calendar.YEAR));
            note.setMonth(calendar.get(Calendar.MONTH)+1);
            note.setDay(calendar.get(Calendar.DAY_OF_MONTH));
            note.setHour( calendar.get(Calendar.HOUR_OF_DAY));
            note.setMinute(calendar.get(Calendar.MINUTE));
            note.setSecond(calendar.get(Calendar.SECOND));
            note.save();
        }
    }

    public void ininDataBase(){
        SharedPreferences pre=this.getSharedPreferences("dShare",MODE_PRIVATE);
        boolean isDeleteFirstRun=pre.getBoolean("isDeleteFirstRun", true);
        SharedPreferences.Editor editor=pre.edit();
        if(isDeleteFirstRun){
            SQLiteDatabase db = LitePal.getDatabase();
            editor.putBoolean("isDeleteFirstRun", false);
            editor.commit();
        }else{
            noteList = LitePal.findAll(DeletedNote.class);
        }
    }

    public  void ininRecycleView(){

        final RecyclerView recyclerView = findViewById(R.id.recycle_view_delete);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Collections.reverse(noteList);
        //Toast.makeText(this,""+noteList.size(),Toast.LENGTH_SHORT).show();
        final DeletedNoteAdapter adapter = new DeletedNoteAdapter(noteList);
        recyclerView.addItemDecoration(new DividerItemDecoration(DeleteActivity.this,DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(adapter);

        adapter.setRecyclerViewOnItemClickListener(new NoteAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent = new Intent(DeleteActivity.this, EditActivity.class);
                String newHtmlData = noteList.get(position).getMessage();
                intent.putExtra("message", newHtmlData);
                startActivity(intent);
            }
        });

        adapter.setOnItemLongClickListener(new NoteAdapter.RecyclerViewOnItemLongClickListener(){
            @Override
            public boolean onItemLongClickListener(View view,final int position) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(DeleteActivity.this);
                dialog.setTitle("警告");
                dialog.setMessage("是否要恢复或者永久删除这条记录  是为恢复           否为永久删除");
                dialog.setCancelable(true);
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       Intent intent = new Intent(DeleteActivity.this,MainActivity.class);
                        intent.putExtra("DeleteMessage", htmlData);
                        startActivity(intent);
                        String newHtmlData = noteList.get(position).getMessage();
                        LitePal.deleteAll(DeletedNote.class,"message == ?",newHtmlData);
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newHtmlData = noteList.get(position).getMessage();
                        LitePal.deleteAll(DeletedNote.class,"message == ?",newHtmlData);
                        ininDataBase();
                        ininRecycleView();
                    }
                });
                dialog.create().show();
                return true;
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public  void ininView(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
        Button deleteAll=findViewById(R.id.delete_all);
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(DeleteActivity.this);
                dialog.setTitle("警告");
                dialog.setMessage("是否要永久删除这些记录");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LitePal.deleteAll(DeletedNote.class);
                        noteList.clear();
                        ininDataBase();
                        ininRecycleView();
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
    }
}
