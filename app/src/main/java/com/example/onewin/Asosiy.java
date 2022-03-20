package com.example.onewin;
import static android.util.Log.d;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Asosiy extends AppCompatActivity {

    private SqlData MyDb;
    private FirebaseFirestore db;
    private ShapeableImageView navimge;
    private final int PICK_IMAGE_REQUEST = 71;
    Intent intent;
    private TextView txtnavid, txtnavuser, txtnavtel, txtnavnetkash, txtdaraja,
            txtnetsom,txtnetdollar,txtnetrubl,txtneteuro;
    ImageView tstart, tend;
    Handler handler;
    DrawerLayout drawerLayout;
    String UID, TIL, REJIM, KALIT, PAROL, daraja;
    String id, uid, til, rejim, kalit, parol, parol1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();

        String languageToLoad = til;
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.asosiy);

        View viewhisob = findViewById(R.id.viewhisob);
        View viewfoydalan = findViewById(R.id.viewfoydalan);
        View viewsozlama = findViewById(R.id.viewsozlama);
        View viewotkazma = findViewById(R.id.viewotkazma);
        View viewmalumot = findViewById(R.id.viewmalumot);
        View viewchiq = findViewById(R.id.viewchiq);

        MyDb = new SqlData(this);
        db = FirebaseFirestore.getInstance();
        txtnavid = findViewById(R.id.txtnavid);
        txtnavuser = findViewById(R.id.txtnavuser);
        txtnavtel = findViewById(R.id.txtnavtel);
        txtnavnetkash = findViewById(R.id.txtnavnetkash);
        txtdaraja = findViewById(R.id.txtdaraja);
        navimge = findViewById(R.id.imgsozdeuser);
         txtnetsom = findViewById(R.id.txtnetsom);
         txtnetdollar = findViewById(R.id.txtnetdollar);
         txtnetrubl = findViewById(R.id.txtnetrubl);
         txtneteuro = findViewById(R.id.txtneteuro);

        oqishsh();
        readphoto();
        navimge.setOnClickListener(view -> new AlertDialog.Builder(Asosiy.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.diqqat)).setMessage(getString(R.string.rasmozgartir))
                .setPositiveButton(getString(R.string._ha), (dialog, which) -> chooseImage()).setNegativeButton(getString(R.string._yoq), null).show());
        viewsozlama.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FragmentSozlama fragmentSozlama = new FragmentSozlama();
            fragmentTransaction.replace(R.id.fragmentlayout,fragmentSozlama);
            fragmentTransaction.commit();
            drawerLayout.closeDrawer(GravityCompat.START);
        });
        viewhisob.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FragmentAsosiy fragmentAsosiy = new FragmentAsosiy();
            fragmentTransaction.replace(R.id.fragmentlayout,fragmentAsosiy);
            fragmentTransaction.commit();
            drawerLayout.closeDrawer(GravityCompat.START);
        });
        viewfoydalan.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FragmentFoydalanuvchi fragmentFoydalanuvchi = new FragmentFoydalanuvchi();
            fragmentTransaction.replace(R.id.fragmentlayout,fragmentFoydalanuvchi);
            fragmentTransaction.commit();
            drawerLayout.closeDrawer(GravityCompat.START);
        });
        viewotkazma.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FragmentOtkazma fragmentOtkazma = new FragmentOtkazma();
            fragmentTransaction.replace(R.id.fragmentlayout,fragmentOtkazma);
            fragmentTransaction.commit();
            drawerLayout.closeDrawer(GravityCompat.START);
        });
        viewmalumot.setOnClickListener(view -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FragmentMalumot fragmentMalumot = new FragmentMalumot();
            fragmentTransaction.replace(R.id.fragmentlayout,fragmentMalumot);
            fragmentTransaction.commit();
            drawerLayout.closeDrawer(GravityCompat.START);
        });
        txtnavnetkash.setOnClickListener(view -> new AlertDialog.Builder(this)
                .setIcon(R.drawable.logonetcash)
                .setTitle(getString(R.string.netCash))
                .setMessage("   \n" + txtnavnetkash.getText().toString())
                .show());
        viewchiq.setOnClickListener(view -> logout());


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentAsosiy fragmentAsosiy = new FragmentAsosiy();
        fragmentTransaction.replace(R.id.fragmentlayout,fragmentAsosiy);
        fragmentTransaction.commit();
        tstart = findViewById(R.id.navstart);
        tend = findViewById(R.id.navend);
        drawerLayout = findViewById(R.id.mylayout);
        tstart.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        tend.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.END));

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                navimge.setImageBitmap(bitmap);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference();
                if (filePath != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(Asosiy.this);
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
                                Toast.makeText(Asosiy.this, "Uploaded", Toast.LENGTH_LONG).show();
                            }).addOnFailureListener(e -> {
                        d("TAG", e.toString());
                        progressDialog.dismiss();
                        Toast.makeText(Asosiy.this, "Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
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
    private void oqishsh() {
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
             handler = new Handler();
            handler.postDelayed(() -> {
                oqishsh();
                readphoto();
            }, 8000);
        }
    }
    private void logout(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.diqqat)).setMessage(getString(R.string.eslatma))
                .setPositiveButton(getString(R.string._ha), (dialog, which) -> {
                    Cursor res = MyDb.oqish();
                    StringBuilder stringBuffer = new StringBuilder();
                    StringBuilder stringBuffer1 = new StringBuilder();
                    StringBuilder stringBuffer2 = new StringBuilder();
                    StringBuilder stringBuffer3 = new StringBuilder();
                    StringBuilder stringBuffer4 = new StringBuilder();
                    StringBuilder stringBuffer5 = new StringBuilder();
                    if (res != null && res.getCount() > 0) {
                        while (res.moveToNext()) {
                            stringBuffer.append(res.getString(0));
                            stringBuffer1.append(res.getString(1));
                            stringBuffer2.append(res.getString(2));
                            stringBuffer3.append(res.getString(3));
                            stringBuffer4.append(res.getString(4));
                            stringBuffer5.append(res.getString(5));
                        }
                        id = stringBuffer.toString();
                        uid = stringBuffer1.toString();
                        til = stringBuffer2.toString();
                        rejim = stringBuffer3.toString();
                        kalit = stringBuffer4.toString();
                        parol = stringBuffer5.toString();
                        parol1 = stringBuffer5.toString();
                    }
                    til = "en";
                    kalit = "0";
                    rejim = "0";
                    MyDb.ozgartir(id, uid, til, rejim, kalit, parol);
                    intent = new Intent(Asosiy.this, Login.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton(getString(R.string._yoq), null)
                .show();
    }

    @SuppressLint("SetTextI18n")
    public void readphoto() {
        DocumentReference documentReference = db.document("user/" + UID);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String urell = documentSnapshot.getString("PHOTO");
                String ism = documentSnapshot.getString("ISMI");
                String tel = documentSnapshot.getString("TEL");
                String id = documentSnapshot.getString("USERID");
                String netkash = documentSnapshot.getString("NETCASH");
                daraja = documentSnapshot.getString("DARAJA");
                txtnavuser.setText(ism);
                txtnavtel.setText(tel);
                txtnavid.setText(getString(R.string.id)+" " + id);
                txtnavnetkash.setText(getString(R.string.netCash)+": \n" + netkash);
                txtdaraja.setText(daraja);

                if (Objects.equals(urell, "0")) {
                    navimge.setImageResource(R.drawable.usericon);
                } else {
                    Glide.with(getApplicationContext()).load(urell).into(navimge);
                }

            }
        });
        DocumentReference documentReference1 = db.document("Admin/netcash");
        documentReference1.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String dollar = documentSnapshot.getString("dollar");
                String som = documentSnapshot.getString("som");
                String euro = documentSnapshot.getString("euro");
                String rubl = documentSnapshot.getString("rubl");

                txtneteuro.setText("1 - NetCash "+euro+" euro");
                txtnetsom.setText("1 - NetCash "+som+" so`m");
                txtnetdollar.setText("1 - NetCash "+dollar+" dollars");
                txtnetrubl.setText("1 - NetCash "+rubl+" eubl");
            }
        });

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MyDb = new SqlData(this);
        Cursor res = MyDb.oqish();
        StringBuilder stringBuffer = new StringBuilder();
        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                stringBuffer.append(res.getString(2));
            }
            til = stringBuffer.toString();
        }
    }
    Context mBase;
}
