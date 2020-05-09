package com.example.demomusicplayerver101;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.demomusicplayerver101.adapter.SongAdapter;
import com.example.demomusicplayerver101.model.Song;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    ImageButton downloadImageButton, deleteImageButton;

    ListView listView;
    ArrayList<Song> listSong;
    SongAdapter songAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        downloadImageButton = findViewById(R.id.downloadImageButton);
        deleteImageButton = findViewById(R.id.deleteImageButton);

        String jsonString = readFromJsonFile();

        String json = jsonString;
        Type targetClassType = new TypeToken<ArrayList<Song>>(){}.getType();
        listSong = new Gson().fromJson(json, targetClassType);
        songAdapter = new SongAdapter(MainActivity.this, R.layout.item_song, listSong);
        listView.setAdapter(songAdapter);


//        downloadImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // if song chua co trong external hay internal --> nút down visible
//                // if ton tai --> nut down invisible
//
//            }
//        });
    }

    private String readFromJsonFile() {
        //String result = "";

        File file = new File(getFilesDir().getAbsolutePath() + "/songData.txt");
        int length = (int) file.length();

        byte[] bytes = new byte[length];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String contents = new String(bytes);
        return contents;
    }
}
