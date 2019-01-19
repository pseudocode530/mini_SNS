package com.example.ssw.finalproject;



import android.content.Context;
import android.os.Bundle;

import android.support.v7.app.ActionBar;


import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class WritePostActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private EditText title;
    private RadioGroup rg;
    private RadioButton radioButton;
    private ListView listview;
    int radioId = -1;
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_write_post);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        title = (EditText)findViewById(R.id.title);
        rg = (RadioGroup)findViewById(R.id.radioGroup);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_main2,null);

        ActionBar actionBar = getSupportActionBar();

//액션바에 뒤로가기버튼 사용
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }



    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_submit, menu);

        return true;

    }



    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();


// 액션바의 글쓰기 버튼을 눌렀을때 파이어베이스에 저장
        if( itemId == R.id.menu_submit ){

            radioId = rg.getCheckedRadioButtonId();
            /** radioButton = (RadioButton)findViewById(radioId);*/


            WriteDTO writeDTO = new WriteDTO();
            writeDTO.title = title.getText().toString();
            writeDTO.radioId = radioId;
            writeDTO.uid = auth.getCurrentUser().getUid();
            writeDTO.userId = auth.getCurrentUser().getEmail();
            database.getReference().child("write").push().setValue(writeDTO);

            Toast.makeText(this, "게시글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
            finish();

        }

        return super.onOptionsItemSelected(item);

    }

}


