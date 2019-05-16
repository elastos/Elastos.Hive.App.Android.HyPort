package org.elastos.app.hivedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.elastos.carrier.ConnectionStatus;
import org.elastos.carrier.FriendInfo;

import java.util.List;


public class MyFriendsActivity extends AppCompatActivity {

    private SimpleCarrier simpleCarrier;
    private ListView myfriend_list;
    private FriendsAdapter friendsAdapter;
    private String filePath;
    private int type;
    List<FriendInfo> friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Friends");

        initView();

        initDate();

    }

    private void initOnClick() {
        myfriend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (friends.get(position).getConnectionStatus() == ConnectionStatus.Connected) {

                    simpleCarrier.sendFile(filePath , friends.get(position).getUserId());

                } else {
                    Toast.makeText(MyFriendsActivity.this, "Your Friend is offline!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initDate() {
        simpleCarrier = SimpleCarrier.getInstance();
        friends = simpleCarrier.getFriends();
        friendsAdapter = new FriendsAdapter(MyFriendsActivity.this, friends);
        myfriend_list.setAdapter(friendsAdapter);

        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);
        if (type == 1) {
            initOnClick();
            filePath = intent.getStringExtra("filePath");
        }
    }

    private void initView() {
        myfriend_list = findViewById(R.id.myfriend_list);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
