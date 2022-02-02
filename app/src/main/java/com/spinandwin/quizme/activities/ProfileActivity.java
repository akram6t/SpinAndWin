package com.spinandwin.quizme.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.spinandwin.quizme.R;
import com.spinandwin.quizme.databinding.ActivityProfileBinding;
import com.spinandwin.quizme.databinding.LoadingDialogLayoutBinding;
import com.spinandwin.quizme.models.UserModels;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private DatabaseReference database;
    private FirebaseAuth auth;
    private ActivityProfileBinding binding;
    private AlertDialog loadingdialog;
    private final int IMAGE_PICK_CODE = 454;
    private Uri selectedImage;
    private FirebaseStorage storage;
    UserModels userModels;
    String profileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        alertdialogcreate();

        database = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        fetchfirebasedata();

        updateprofile();

        binding.profilebackarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        binding.btnInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String insUrl = "https://www.instagram.com/akram6t/";
                Intent goinstagram = new Intent(Intent.ACTION_VIEW);
                goinstagram.setData(Uri.parse(insUrl));
                startActivity(goinstagram);
            }
        });


    }

    private void fetchfirebasedata(){
        database.child("user").child(auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                userModels = dataSnapshot.getValue(UserModels.class);
                binding.pname.setText(userModels.getName());
                binding.pEmail.setText(userModels.getEmail());
                Glide.with(ProfileActivity.this)
                        .load(userModels.getProfile())
                        .placeholder(R.drawable.profile)
                        .into(binding.pprofile);
                loadingdialog.dismiss();
            }
        });
    }

    private void alertdialogcreate(){
        LoadingDialogLayoutBinding loadlayout = LoadingDialogLayoutBinding.inflate(getLayoutInflater());
        loadingdialog = new AlertDialog.Builder(ProfileActivity.this).setView(loadlayout.getRoot())
                .setCancelable(false).create();
        loadingdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loadingdialog.show();
    }

    private void updateprofile(){
        binding.pprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                chackpermissions(intent);
            }
        });

        binding.btnpupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImage!=null || !binding.pname.getText().toString().trim().isEmpty()){
                    loadingdialog.show();
                    if (selectedImage!=null){
                        StorageReference reference = storage.getReference().child("profile").child(auth.getUid()+".jpeg");

                        Bitmap bmp = null;
                        try {
                            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // size reduce selected profile image
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        assert bmp != null;
                        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                        byte [] imageData = baos.toByteArray();

                        reference.putBytes(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        profileUri = uri.toString();
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("profile", profileUri);
                                        map.put("name", binding.pname.getText().toString());
                                        database.child("user").child(auth.getUid()).updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                selectedImage = null;
                                                loadingdialog.dismiss();
                                                Toast.makeText(ProfileActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                                                Glide.with(ProfileActivity.this).load(profileUri)
                                                        .placeholder(R.drawable.profile).into(binding.pprofile);
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }else {
                        if (!userModels.getName().equals(binding.pname.getText().toString())){
                            Map<String, Object> map = new HashMap<>();
                            map.put("name", binding.pname.getText().toString());
                            database.child("user").child(auth.getUid()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ProfileActivity.this, "successfully Updated", Toast.LENGTH_SHORT).show();
                                    loadingdialog.dismiss();
                                }
                            });
                        }else {
                            Toast.makeText(ProfileActivity.this, "Already Updated", Toast.LENGTH_SHORT).show();
                            loadingdialog.dismiss();
                        }
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMAGE_PICK_CODE && resultCode==RESULT_OK){
            assert data != null;
            selectedImage = data.getData();
            binding.pprofile.setImageURI(selectedImage);
        }
    }

    private void chackpermissions(Intent intent){
        Dexter.withContext(ProfileActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        startActivityForResult(intent, IMAGE_PICK_CODE);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        finish();
                        Toast.makeText(ProfileActivity.this, "Allow Permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void logout(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        auth.signOut();
        mGoogleSignInClient.signOut();
        Toast.makeText(ProfileActivity.this, "Successfully Logout", Toast.LENGTH_LONG).show();
        startActivity(new Intent(ProfileActivity.this, AuthActivity.class));
        finishAffinity();

    }

}