package com.example.ssw.finalproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView emailTextView;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private RecyclerView recyclerView;
    private List<WriteDTO> writeDTOs = new ArrayList<>();
    private List<String> uidLists = new ArrayList<>();


    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    boolean isDrawerOpend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        recyclerView = (RecyclerView)findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(linearLayoutManager);
        final RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        // JSON형태에서 "write"의 데이터들이 변경되는것을 감지하는 리스너 등록
        database.getReference().child("write").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                writeDTOs.clear();
                uidLists.clear();

                //파이어베이스에 저장되있는 "write"자료들을 모두 읽어들임
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    WriteDTO writeDTO = snapshot.getValue(WriteDTO.class);
                    String uidKey = snapshot.getKey();
                    writeDTOs.add(writeDTO);
                    uidLists.add(uidKey);
                }
        // 데이터변경을 리사이클러뷰 어댑터에게 알림
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        drawer = (DrawerLayout)findViewById(R.id.main_drawer);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.drawer_open, R.string.drawer_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toggle.syncState();
        NavigationView navigationView = (NavigationView)findViewById(R.id.main_drawer_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                int id=item.getItemId();


        // 로그아웃
                if(id==R.id.nav_logout){
                    auth.signOut();
                    finish();
                    Intent intent = new Intent(Main2Activity.this, LoginActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        View view = navigationView.getHeaderView(0);
        nameTextView = (TextView)view.findViewById(R.id.header_name_textView);
        emailTextView = (TextView)view.findViewById(R.id.header_name_email);

        nameTextView.setText(auth.getCurrentUser().getDisplayName());
        emailTextView.setText(auth.getCurrentUser().getEmail());
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board,parent,false);
            
            return new CustomViewHolder(view);
        }


        //각 아이템들을 클릭하였을때 위에서 파이어베이스에서 읽어온 데이터들 ReadActivity로 전달
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((CustomViewHolder)holder).textView.setText(writeDTOs.get(position).title);
            final String title = writeDTOs.get(position).title.toString();
            final int radioId = writeDTOs.get(position).radioId;
            final String uid = writeDTOs.get(position).uid.toString();
            final String userId = writeDTOs.get(position).userId.toString();
            final String uidKey = uidLists.get(position).toString();
            ((CustomViewHolder)holder).textView.setOnClickListener(new View.OnClickListener(){


                @Override
                public void onClick(View v) {
                    Toast.makeText(Main2Activity.this, title + "이 클릭되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Main2Activity.this, ReadActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("radioId", radioId);
                    intent.putExtra("uid", uid);
                    intent.putExtra("userId",userId);
                    intent.putExtra("uidKey",uidKey);
                    startActivity(intent);



                }
            });
        }

        @Override
        public int getItemCount() {
            return writeDTOs.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            private TextView textView;

            public CustomViewHolder(View view) {
                super(view);
                textView = (TextView) view.findViewById(R.id.item_textView);
            }
        }
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write, menu);
        return true;
    }

//액션바의 버튼클릭 구분
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return false;
        } else{
            Intent intent = new Intent(this, WritePostActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
