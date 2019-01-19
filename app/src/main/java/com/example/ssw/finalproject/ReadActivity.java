package com.example.ssw.finalproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

public class ReadActivity extends AppCompatActivity {

    TextView title;
    TextView radioId;
    TextView uid;
    TextView userId;

    RecyclerView recyclerView;
    private Button uploadComment;
    private EditText writeComment;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private List<CommentDTO> commentDTOs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        final RVAdapter rvAdapter = new RVAdapter();
        recyclerView.setAdapter(rvAdapter);
        writeComment = (EditText)findViewById(R.id.write_comment);
        uploadComment = (Button)findViewById(R.id.upload_comment);
        final Intent intent = getIntent();
        final String uidKey = intent.getStringExtra("uidKey");



        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //파이어베이스에서 "write" 밑에있는 "comment" 데이터들을 모두 읽어들임
        database.getReference("write").child(uidKey).child("comment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentDTOs.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CommentDTO commentDTO = snapshot.getValue(CommentDTO.class);
                    commentDTOs.add(commentDTO);

                }
                rvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        title = (TextView)findViewById(R.id.read_title);
        radioId = (TextView)findViewById(R.id.read_radioId);


        //라디오버튼 id로 구분하여 어떤선택을 했는지 구분
        title.setText(intent.getStringExtra("title"));
        if(intent.getIntExtra("radioId",1) == 2131230859)
        {
            radioId.setText("경험을 듣고싶어요");
        }
        else if(intent.getIntExtra("radioId",1) == 2131230858)
        {
            radioId.setText("쓴소리를 듣고싶어요");
        }
        else if(intent.getIntExtra("radioId",1) == 2131230857)
        {
            radioId.setText("위로가 듣고싶어요");
        }
        else
        {
            radioId.setText("선택안함");
        }


        //댓글등록버튼을 누를때 댓글데이터를 파이어베이스에 저장
        uploadComment.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                CommentDTO commentDTO = new CommentDTO();
                commentDTO.writeComment = writeComment.getText().toString();
                commentDTO.userId = auth.getCurrentUser().getEmail();
                database.getReference("write").child(uidKey).child("comment").push().setValue(commentDTO);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    class RVAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_board, parent, false);

            return new CCustomViewHolder(view);
        }

        //댓글을 눌렀을때 이벤트
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ((CCustomViewHolder)holder).textView.setText(commentDTOs.get(position).writeComment);
            //

            //
            ((CCustomViewHolder)holder).textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Toast.makeText(ReadActivity.this, commentDTOs.get(position).userId +"님이 쓰신 답변을 채택하셨습니다!!", Toast.LENGTH_SHORT).show();


                }
            });
        }

        @Override
        public int getItemCount() {
            return commentDTOs.size();
        }

        class CCustomViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public CCustomViewHolder(View view) {
                super(view);
                textView =(TextView)view.findViewById(R.id.comment_textView);

            }
        }
    }


}
