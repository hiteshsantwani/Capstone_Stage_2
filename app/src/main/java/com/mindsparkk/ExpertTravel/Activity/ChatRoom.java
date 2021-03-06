package com.mindsparkk.ExpertTravel.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mindsparkk.ExpertTravel.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hitesh on 29-01-2017.
 */

public class ChatRoom extends AppCompatActivity{

    private Button btnSendMsg;
    private EditText inputMsg;
    private TextView chatConversation;

    private String userName, roomName;
    private DatabaseReference root ;
    private String tempKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        btnSendMsg = (Button) findViewById(R.id.btn_send);
        inputMsg = (EditText) findViewById(R.id.msg_input);
        chatConversation = (TextView) findViewById(R.id.textView);

        userName = getIntent().getExtras().get("userName").toString();
        roomName = getIntent().getExtras().get("roomName").toString();
        setTitle(" Room - "+ roomName);

        root = FirebaseDatabase.getInstance().getReference().child(roomName);

        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> map = new HashMap<String, Object>();
                tempKey = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(tempKey);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("name", userName);
                map2.put("msg", inputMsg.getText().toString());

                message_root.updateChildren(map2);
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private String chatMsg, chatUserName;

    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){

            chatMsg = (String) ((DataSnapshot)i.next()).getValue();
            chatUserName = (String) ((DataSnapshot)i.next()).getValue();

            chatConversation.append(chatUserName +" : "+ chatMsg +" \n");
        }


    }
}

