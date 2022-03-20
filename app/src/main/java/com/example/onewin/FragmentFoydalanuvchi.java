package com.example.onewin;

import static android.app.Activity.RESULT_OK;
import static android.util.Log.d;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FragmentFoydalanuvchi extends Fragment {
    private SqlData MyDb;
    private FirebaseFirestore db;
    private final int PICK_IMAGE_REQUEST = 71;
    String UID, TIL, REJIM, KALIT, PAROL;
    private EditText edigalism, edigalfamilya, edigaltel;
    private TextView txtgalemail, txtgalusername;
    private Button btngaltahrir, btngalsaqla;
    private ProgressBar progresgall;
    private DatePicker dapickergal;
    private ShapeableImageView imggaldeuser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_foydalanuvchi,container,false);
    }
    @SuppressLint("UseRequireInsteadOfGet")
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        MyDb = new SqlData(getContext());
        db = FirebaseFirestore.getInstance();
        edigalism = view.findViewById(R.id.edigalism);
        edigalfamilya = view.findViewById(R.id.edigalfamilya);
        edigaltel = view.findViewById(R.id.edigaltel);
        txtgalemail = view.findViewById(R.id.txtgalemail);
        txtgalusername = view.findViewById(R.id.txtgalusername);
        ImageButton btngaltelegram = view.findViewById(R.id.btngaltelegram);
        btngaltahrir = view.findViewById(R.id.btngaltahrir);
        btngalsaqla = view.findViewById(R.id.btngalsaqla);
        progresgall = view.findViewById(R.id.progresgall);
        dapickergal = view.findViewById(R.id.dapickergal);
        imggaldeuser = view.findViewById(R.id.imgsozdeuser);

        progresgall.setVisibility(View.VISIBLE);
        edigalism.setEnabled(false);
        edigalfamilya.setEnabled(false);
        edigaltel.setEnabled(false);
        readdatasql();

        DocumentReference documentReference = db.document("user/" + UID);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String email = documentSnapshot.getString("POCHTA"),
                        ism = documentSnapshot.getString("ISMI"),
                        familya = documentSnapshot.getString("FAMILYA"),
                        photo = documentSnapshot.getString("PHOTO"),
                        telefon = documentSnapshot.getString("TEL");
                txtgalemail.setText(email);
                edigalism.setText(ism);
                txtgalusername.setText(ism);
                edigalfamilya.setText(familya);
                edigaltel.setText(telefon);
                Glide.with(Objects.requireNonNull(getActivity())).load(photo).into(imggaldeuser);
                progresgall.setVisibility(View.GONE);
            }
        }).addOnFailureListener(e -> progresgall.setVisibility(View.GONE));
        btngaltelegram.setOnClickListener(view19 -> {
            Uri uri = Uri.parse("https://t.me/Partner_1winbot");
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
            likeIng.setPackage("com.telegram.android");
            try {
                startActivity(likeIng);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://t.me/Partner_1winbot")));
            }
        });
        btngaltahrir.setOnClickListener(view112 -> new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.malumotlar)).setMessage(getString(R.string.h_malumotlar))
                .setPositiveButton(getString(R.string._ha), (dialog, which) -> {
                    btngalsaqla.setVisibility(View.VISIBLE);
                    btngaltahrir.setVisibility(View.INVISIBLE);
                    edigalism.setEnabled(true);
                    edigalfamilya.setEnabled(true);
                    edigaltel.setEnabled(true);
                })
                .setNegativeButton(getString(R.string._yoq), null)
                .show());
        imggaldeuser.setOnClickListener(v -> chooseImage());
        btngalsaqla.setOnClickListener(view113 -> {
            String ism = edigalism.getText().toString().trim();
            String famil = edigalfamilya.getText().toString().trim();
            String teel = edigaltel.getText().toString().trim();
            String data = dapickergal.getDayOfMonth() + "/" +
                    (dapickergal.getMonth() + 1) + "/" + dapickergal.getYear();
            if (ism.isEmpty()) {
                edigalism.setError(getString(R.string.bosh));
                if (famil.isEmpty()) {
                    edigalfamilya.setError(getString(R.string.bosh));
                }
                if (teel.isEmpty()) {
                    edigaltel.setError(getString(R.string.bosh));
                }
            } else {
                if (famil.isEmpty()) {
                    edigalfamilya.setError(getString(R.string.bosh));
                    if (ism.isEmpty()) {
                        edigalism.setError(getString(R.string.bosh));
                    }
                    if (teel.isEmpty()) {
                        edigaltel.setError(getString(R.string.bosh));
                    }
                } else {
                    if (teel.isEmpty()) {
                        if (ism.isEmpty()) {
                            edigalism.setError(getString(R.string.bosh));
                        }
                        if (famil.isEmpty()) {
                            edigalfamilya.setError(getString(R.string.bosh));
                        }
                        edigaltel.setError(getString(R.string.bosh));
                    } else {
                        progresgall.setVisibility(View.VISIBLE);
                        Map<String, Object> updatecash = new HashMap<>();
                        updatecash.put("ISMI", ism);
                        updatecash.put("FAMILYA", famil);
                        updatecash.put("TEL", teel);
                        updatecash.put("YOSHI", data);

                        db.collection("user")
                                .document(UID)
                                .update(updatecash).addOnSuccessListener(unused -> {
                            btngalsaqla.setVisibility(View.INVISIBLE);
                            btngaltahrir.setVisibility(View.VISIBLE);
                            edigalism.setEnabled(false);
                            edigalfamilya.setEnabled(false);
                            edigaltel.setEnabled(false);
                            progresgall.setVisibility(View.GONE);
                        }).addOnFailureListener(e -> Toast.makeText(getContext(), getString(R.string.xatolik), Toast.LENGTH_SHORT).show());
                    }
                }
            }

        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                @SuppressLint("UseRequireInsteadOfGet") Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getContext()).getContentResolver(), filePath);
                imggaldeuser.setImageBitmap(bitmap);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference();
                if (filePath != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();
                    //txturluserphoto.setText(UUID.randomUUID().toString());
                    StorageReference ref = storageReference.child(UID + "/" + UID);
                    ref.putFile(filePath)
                            .addOnSuccessListener(taskSnapshot -> {
                                Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                                downloadUrl.addOnCompleteListener(task -> {
                                    String downloadURL = "https://" + task.getResult().getEncodedAuthority()
                                            + task.getResult().getEncodedPath()
                                            + "?alt=media&token="
                                            + task.getResult().getQueryParameters("token").get(0);
                                    Map<String, Object> userphoto = new HashMap<>();
                                    userphoto.put("PHOTO", downloadURL);
                                    db.collection("user")
                                            .document(UID)
                                            .update(userphoto);
                                });
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_LONG).show();
                            }).addOnFailureListener(e -> {
                        d("TAG", e.toString());
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
                    })
                            .addOnProgressListener(snapshot -> {

                                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");

                            });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void readdatasql(){
        Cursor res = MyDb.oqish();
        StringBuilder stringBuffer = new StringBuilder();
        StringBuilder stringBuffer1 = new StringBuilder();
        StringBuilder stringBuffer2 = new StringBuilder();
        StringBuilder stringBuffer3 = new StringBuilder();
        StringBuilder stringBuffer4 = new StringBuilder();
        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                stringBuffer.append(res.getString(1));
                stringBuffer1.append(res.getString(2));
                stringBuffer2.append(res.getString(3));
                stringBuffer3.append(res.getString(4));
                stringBuffer4.append(res.getString(4));
            }
            UID = stringBuffer.toString();
            TIL = stringBuffer1.toString();
            REJIM = stringBuffer2.toString();
            KALIT = stringBuffer3.toString();
            PAROL = stringBuffer4.toString();
        }
    }
}
