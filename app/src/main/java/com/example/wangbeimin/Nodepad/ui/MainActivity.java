package com.example.wangbeimin.Nodepad.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wangbeimin.Nodepad.R;
import com.example.wangbeimin.Nodepad.utils.DividerItemDecoration;
import com.example.wangbeimin.Nodepad.utils.Note;
import com.example.wangbeimin.Nodepad.utils.NoteAdapter;
import com.example.wangbeimin.Nodepad.utils.NoteList;
import com.example.wangbeimin.Nodepad.utils.NoteListCallBack;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Note> noteList = new ArrayList<>();
    private List<Note> searchList = new ArrayList<>();
    private Button edit;
    private Button delete;
    final Calendar calendar = Calendar.getInstance();
    private String htmlData1;
    private String htmlData2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LitePal.initialize(this);
        SQLiteDatabase db = LitePal.getDatabase();
        ininView();
        addData();
        ininDataBase();
        ininRecycleView(noteList);
        search();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ininView();
        addData();
        ininDataBase();
        ininRecycleView(noteList);
        search();
    }

    public void search(){
        final EditText searchMessage = findViewById(R.id.search_message);
        Button beginSearch = findViewById(R.id.begin_search);
        beginSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key=searchMessage.getText().toString();
                for (Note note:noteList){
                    if (note.getMessage().contains(key)){
                        searchList.add(note);
                    }
                }
                ininRecycleView(searchList);
            }
        });
    }

    public void addData(){
        htmlData1 = getIntent().getStringExtra("message");
        htmlData2 = getIntent().getStringExtra("DeleteMessage");
        if (htmlData1!=null){
            Note note = new Note();
            note.setMessage(htmlData1);
            note.setYear(calendar.get(Calendar.YEAR));
            note.setMonth(calendar.get(Calendar.MONTH)+1);
            note.setDay(calendar.get(Calendar.DAY_OF_MONTH));
            note.setHour( calendar.get(Calendar.HOUR_OF_DAY));
            note.setMinute(calendar.get(Calendar.MINUTE));
            note.setSecond(calendar.get(Calendar.SECOND));
            note.save();
        }
        if (htmlData2!=null){
            Note note = new Note();
            note.setMessage(htmlData2);
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
        SharedPreferences sharedPreferences=this.getSharedPreferences("share",MODE_PRIVATE);
        boolean isFirstRun=sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if(isFirstRun){
            SQLiteDatabase db = LitePal.getDatabase();
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        }else{
            noteList = LitePal.findAll(Note.class);
        }
    }

    public  void ininRecycleView(final List<Note> noteList){

        final RecyclerView recyclerView = findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Collections.reverse(noteList);
        final NoteAdapter adapter = new NoteAdapter(noteList);
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(adapter);

        adapter.setRecyclerViewOnItemClickListener(new NoteAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                String newHtmlData = noteList.get(position).getMessage();
                intent.putExtra("message", newHtmlData);
                startActivity(intent);
            }
        });

        adapter.setOnItemLongClickListener(new NoteAdapter.RecyclerViewOnItemLongClickListener(){
            @Override
            public boolean onItemLongClickListener(View view,final int position) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("警告");
                dialog.setMessage("是否要删除这条记录");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newHtmlData = noteList.get(position).getMessage();
                        Intent intent = new Intent(MainActivity.this, DeleteActivity.class);
                        intent.putExtra("MtoD", newHtmlData);
                        LitePal.deleteAll(Note.class,"message == ?",newHtmlData);
                        startActivity(intent);
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
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

        edit = findViewById(R.id.goEdit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, EditActivity.class );
                startActivity(intent1);
            }
        });

        delete = findViewById(R.id.goDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, DeleteActivity.class);
                startActivity(intent2);
            }
        });
    }
}
