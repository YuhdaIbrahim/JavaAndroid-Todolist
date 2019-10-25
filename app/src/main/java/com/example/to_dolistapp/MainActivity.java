package com.example.to_dolistapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  final List<String> list = new ArrayList<>();
  int[] backgroundColors;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);

    final ListView listView = findViewById((R.id.ListView));
    final TextAdapter adapter = new TextAdapter();

    int maxItems = 100;
    backgroundColors = new int[maxItems];


    for(int i=0;i < maxItems;i++){
      if (i % 2 ==0){
        backgroundColors[i]=Color.CYAN;
      }else{
        backgroundColors[i] = Color.YELLOW;
      }
    }

    readInfo();
    adapter.setData(list, backgroundColors);
    listView.setAdapter(adapter);


    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
            .setTitle("Delete this Task?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                list.remove(position);
                adapter.setData(list,backgroundColors);
              }
            })
            .setNegativeButton("No", null)
            .create();
        dialog.show();
      }
    });




    final Button newTaskButton = findViewById(R.id.newTask);

    newTaskButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final EditText taskInput = new EditText(MainActivity.this);
        taskInput.setSingleLine();
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
            .setTitle("Add a new task")
            .setMessage("what is your new task?")
            .setView(taskInput)
            .setPositiveButton("Add Task", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                list.add(taskInput.getText().toString());
                adapter.setData(list,backgroundColors);
                saveInfo();
              }
            })
            .setNegativeButton("Cancel",null  )
            .create();
        dialog.show();
      }
    });

    final Button deleteAllTask = findViewById(R.id.clearTask);
    deleteAllTask.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
            .setTitle("Delete all task?")
            .setMessage("Really? u want delete all task?")
            .setPositiveButton("Delete all", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                list.clear();
                adapter.setData(list,backgroundColors);
                saveInfo();
              }
            })
            .setNegativeButton("Cancel", null)
            .create();
        dialog.show();



      }
    });

  }

  @Override
  protected void onPause(){
    super.onPause();
    saveInfo();
  }




  private void saveInfo(){
    try{
      File file = new File(this.getFilesDir(), "Save");

      FileOutputStream fout = new FileOutputStream(file);
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fout));

      for (int i = 0; i < list.size(); i++){
        bw.write(list.get(i));
        bw.newLine();
      }

      bw.close();
      fout.close();

    } catch (Exception e){
      e.printStackTrace();
    }
  }


  private  void readInfo(){
    File file = new File(this.getFilesDir(), "Save");
    if(!file.exists()){
      return;
    }


    try{
      FileInputStream is = new FileInputStream(file);
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      String line = reader.readLine();
      while (line != null){
        list.add(line);
        line = reader.readLine();
      }


    }catch (Exception e){
      e.printStackTrace();
    }
  }

  class TextAdapter extends BaseAdapter {

    List<String> list = new ArrayList<>();

    int[] backgroundColors;

    void setData(List<String> mList, int[] mBackgroundColors){
      list.clear();
      list.addAll(mList);
      backgroundColors = new int[list.size()];
      for(int i=0;i < list.size();i++){
        backgroundColors[i] = mBackgroundColors[i];
      }
      notifyDataSetChanged();
    }

    @Override
    public int getCount(){
      return list.size();
    }

    @Override
    public Object getItem(int position){
      return null;
    }

    @Override
    public long getItemId(int position){
      return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
      if(convertView==null){
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item,parent,false);
      }

      TextView textView = convertView.findViewById(R.id.Task);

      textView.setBackgroundColor(backgroundColors[position]);
      textView.setText(list.get(position));
      return convertView;
    }
  }
}
