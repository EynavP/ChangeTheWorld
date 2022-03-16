package com.example.changetheworld.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireStoreDB implements DataBaseInterface{

    private static FireStoreDB single_instance = null;
    public FirebaseFirestore db;
    private FireStoreDB() {
        db = FirebaseFirestore.getInstance();
    }

    public static FireStoreDB getInstance(){
        if (single_instance == null){
            single_instance = new FireStoreDB();
        }
        return single_instance;
    }
    @Override
    public void VerifyAndSavePrivateClient(Context context, PrivateClient user){
        db.collection("PrivateClient")
                .document(user.getUser_name())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            Toast.makeText(context, "UserName already exists" , Toast.LENGTH_LONG).show();
                        }else {
                            SavePrivateClient(context, user);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Fail to connect to databases " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void SavePrivateClient(Context context, PrivateClient user) {

            final String KEY_FULL_NAME = "full_name";
            final String KEY_PHONE = "phone";
            final String KEY_MAIL = "mail";
            final String KEY_CURRENCY = "currency";
            final String KEY_USER_NAME = "user_name";
            final String KEY_PASSWORD = "password";
            final String KEY_PERSONAL_PHOTO = "personal_photo";
            final String KEY_PASSPORT_PHOTO = "passport_photo";

            Map<String, Object> data = new HashMap<>();
            data.put(KEY_FULL_NAME, user.getFull_name());
            data.put(KEY_PHONE, user.getPhone_number());
            data.put(KEY_CURRENCY, user.getLocal_currency());
            data.put(KEY_USER_NAME, user.getUser_name());
            data.put(KEY_PASSWORD, user.getPassword());
            data.put(KEY_MAIL, user.getMail_address());

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference personalImageRef = storageRef.child("images/"+user.getUser_name()+"/"+KEY_PERSONAL_PHOTO+".jpg");
            StorageReference passportImageRef = storageRef.child("images/"+user.getUser_name()+"/"+KEY_PASSPORT_PHOTO+".jpg");
            UploadTask personal_photo_task =  personalImageRef.putBytes(user.getPhoto());
            personal_photo_task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                }
            });


            UploadTask passport_task = passportImageRef.putBytes(user.getPassport());
            passport_task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                }
            });




        db.collection("PrivateClient")
                    .document(user.getUser_name())
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused)
                        {
                            Toast.makeText(context, "Client created successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Fail create new client : " + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
        }


    @Override
    public void VerifyAndSaveBusiness(Context context, BusinessClient businessClient) {
        db.collection("BusinessClient")
                .document(businessClient.getUser_name())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            Toast.makeText(context, "Business UserName already exists" , Toast.LENGTH_LONG).show();
                        }else {
                            SaveBusinessClient(context, businessClient);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Fail to connect to databases " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void VerifyAndPrivateClientLogin(Context context, String user_name, String password, Intent intent) {
        db.collection("PrivateClient")
                .document(user_name)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            if (password.equals(documentSnapshot.getString("password")) && user_name.equals(documentSnapshot.getString("user_name"))){
                                intent.putExtra("userName",user_name);
                                context.startActivity(intent);
                            }
                        }else {
                            Toast.makeText(context, "UserName or password are incorrect", Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Fail to connect to databases " + e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void VerifyAndBusinessLogin(Context context, String user_name, String password, Intent intent) {
        db.collection("BusinessClient")
                .document(user_name)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            BusinessClient client = new BusinessClient(documentSnapshot);
                            if (client.getPassword().equals(password) && client.getUser_name().equals(user_name)){
                                // TODO: Move to homepage with data
                                context.startActivity(intent);
                            }
                        }else {
                            Toast.makeText(context, "UserName or password are incorrect", Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Fail to connect to databases " + e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void LoadProfilePhoto(ImageView profilPhoto, String user_name) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String personal_photo_path = "images/" + user_name + "/" + "personal_photo.jpg";
        StorageReference PersonalpathReference = storageRef.child(personal_photo_path);
        Task<byte[]> personal_photo = PersonalpathReference.getBytes(2000000000);
        personal_photo.addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap photo = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                profilPhoto.setImageBitmap(photo);
            }
        });
}

    private void SaveBusinessClient(Context context,BusinessClient user) {

        final String KEY_BUSINESS_NAME = "business_name";
        final String KEY_MAIL = "mail";
        final String KEY_STATE = "state";
        final String KEY_PHONE = "phone";
        final String KEY_USER_NAME = "user_name";
        final String KEY_PASSWORD = "password";
        final String KEY_OWNER_NAME = "owner_name";
        final String KEY_BUSINESS_APPROVAL = "business_approval";
        final String KEY_OWNER_ID = "owner_id";


        Map<String, Object> data = new HashMap<>();
        data.put(KEY_BUSINESS_NAME, user.getBusiness_name());
        data.put(KEY_MAIL, user.getMail_address());
        data.put(KEY_STATE, user.getState());
        data.put(KEY_PHONE, user.getPhone());
        data.put(KEY_USER_NAME, user.getUser_name());
        data.put(KEY_PASSWORD, user.getPassword());
        data.put(KEY_OWNER_NAME, user.getBusiness_owner_name());
        data.put(KEY_BUSINESS_APPROVAL, user.getBusiness_approval_document());
        data.put(KEY_OWNER_ID, user.getGetBusiness_owner_id());


        db.collection("BusinessClient")
                .document(user.getUser_name())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void unused)
                    {
                        Toast.makeText(context, "Business created successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Fail create new business : " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        }

    }

