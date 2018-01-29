package com.firebasechat.chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebasechat.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.immigration.appdata.Constant;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import kotlin.jvm.JvmStatic;

public class Chat extends AppCompatActivity {
    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    ImageButton btn_back,btn_file;
    TextView txt_title, txt_message;
    Firebase reference1, reference2;
    boolean isCheck=true;

    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    private String img_url="";
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout) findViewById(R.id.layout1);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_file = (ImageButton) findViewById(R.id.btn_file);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_message = (TextView) findViewById(R.id.txt_message);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Firebase.setAndroidContext(this);

        reference1 = new Firebase(Constant.url_Messages + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase(Constant.url_Messages + UserDetails.chatWith + "_" + UserDetails.username);



        txt_title.setText(UserDetails.chatWith.substring(0,1).toUpperCase());
        txt_message.setText(UserDetails.chatWith.substring(0,1).toUpperCase()+UserDetails.chatWith.substring(1).toLowerCase());


        messageArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (messageArea.getText().toString().length()>1) {
                    if (messageArea.getText().toString() == messageArea.getText().toString()) {
                        isCheck = false;
                    } isCheck=true;
                 }else{

                }

            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SimpleDateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
                String formattedDate = dateFormat.format(new Date()).toString();
                System.out.println(formattedDate);
                UserDetails.date=formattedDate;
                String messageText = messageArea.getText().toString();



                if(isCheck) {
                    if (!messageText.equals("")) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("user", UserDetails.username);
                        map.put("date", UserDetails.date);
                        map.put("message", messageText);
                        map.put("image", "");
                        map.put("isCheck", "1");

                        reference1.push().setValue(map);
                        reference2.push().setValue(map);
                    }
                }else
                {

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("user", UserDetails.username);
                    map.put("date", UserDetails.date);
                    map.put("message", "");
                    map.put("image",img_url);
                    map.put("isCheck", "2");

                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                }



                Constant.hideSoftKeyboad(Chat.this,v);
                messageArea.setText("");

            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                String userDate = map.get("date").toString();
                String userisCheck = map.get("isCheck").toString();
                String userUrlimage = map.get("image").toString();

                if (userName.equals(UserDetails.username)) {
                   // addMessageBox("You:-\n" + message, 1);
                    addMessageBox(message, 1,userDate,userUrlimage,userisCheck);
                } else {
                    //addMessageBox(UserDetails.chatWith + ":-\n" + message, 2);
                    addMessageBox(message, 2,userDate,userUrlimage,userisCheck);

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                isCheck=false;
                uploadImage();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            img_url="https:"+taskSnapshot.getDownloadUrl().getEncodedSchemeSpecificPart();
                            Toast.makeText(Chat.this, "Uploaded", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Chat.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }



    public void addMessageBox(String message, int type,String date,String imageUrl,String userCheck) {

       // layout.removeAllViews();
        LayoutInflater layoutInflater;
        View view = null;
        if (type == 1) {
            layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_message_sent, layout, false);
            TextView text_message_body = view.findViewById(R.id.text_message_body);
            TextView text_message_time = view.findViewById(R.id.text_message_time);
            ImageView img_body = view.findViewById(R.id.img_body);

            if (userCheck.equals("1")) {
                text_message_body.setVisibility(View.VISIBLE);
                img_body.setVisibility(View.GONE);
                text_message_body.setText(message);
            }else {
                text_message_body.setVisibility(View.GONE);
                img_body.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(imageUrl)
                        .into(img_body);
            }

            text_message_time.setText(date);
            layout.addView(view);

        } else {
            MediaPlayer player = MediaPlayer.create(Chat.this, Settings.System.DEFAULT_NOTIFICATION_URI);
            player.start();

            layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_message_received, layout, false);
            TextView text_message_body = view.findViewById(R.id.text_message_body);
            TextView text_message_time = view.findViewById(R.id.text_message_time);
            ImageView img_body = view.findViewById(R.id.img_body);

            if (userCheck.equals("1")) {
                text_message_body.setVisibility(View.VISIBLE);
                img_body.setVisibility(View.GONE);
                text_message_body.setText(message);
            }else {
                text_message_body.setVisibility(View.GONE);
                img_body.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(imageUrl)
                        .into(img_body);
            }

            text_message_time.setText(date);
            layout.addView(view);
        }

        scrollView.fullScroll(View.FOCUS_UP);
    }
}