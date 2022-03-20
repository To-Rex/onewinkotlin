package com.example.onewin;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class Regstartion extends AppCompatActivity {
    DocumentReference documentReference;
    FirebaseFirestore db;
    private FirebaseAuth auth;
    private EditText ediroypochta, ediroyparol, ediroyism, ediroyfamil, ediroytel, ediroylink;
    private DatePicker ediroydatapicker;
    private ProgressBar progressBar2;
    String userid,email,password,ism,famil,teel;
    String[] values;
    private SqlData MyDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String languageToLoad = "en";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.regstartion);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        MyDb = new SqlData(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ediroypochta = findViewById(R.id.ediroypochta);
        ediroyparol = findViewById(R.id.ediroyparol);
        Button btnroyhisob = findViewById(R.id.btnroyhisob);
        ediroylink = findViewById(R.id.ediroylink);
        ediroyism = findViewById(R.id.ediroyism);
        ediroyfamil = findViewById(R.id.ediroyfamil);
        ediroydatapicker = findViewById(R.id.ediroydatapicker);
        ediroytel = findViewById(R.id.ediroytel);
        progressBar2 = findViewById(R.id.progressBar2);
        setTitle(getString(R.string.royhatdan_o_tish));

        btnroyhisob.setOnClickListener(view -> {
            email = ediroypochta.getText().toString().trim();
            password = ediroyparol.getText().toString().trim();
            ism = ediroyism.getText().toString().trim();
            famil = ediroyfamil.getText().toString().trim();
            teel = ediroytel.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                ediroypochta.setError(getString(R.string.bosh));
                return;
            }
            if (TextUtils.isEmpty(password)) {
                ediroyparol.setError(getString(R.string.bosh));
                return;
            }
            if (password.length() < 6) {
                ediroyparol.setError(getString(R.string.bosh));
                return;
            }
            if (ism.length() < 3) {
                ediroyism.setError(getString(R.string.bosh));
                return;
            }
            if (famil.length() < 4) {
                ediroyfamil.setError(getString(R.string.kam));
                return;
            }
            if (teel.length() < 7) {
                ediroytel.setError(getString(R.string.kam));
                return;
            }
            if (Objects.equals(ediroylink.getText().toString(),"")){
                fun();
            }else{
                db.collection("user")
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String a = document.getString("USERID") + "," + document.getId();
                                    values = a.split(",");
                                    if (Objects.equals(ediroylink.getText().toString(), values[0])) {
                                        progressBar2.setVisibility(View.VISIBLE);
                                        auth.createUserWithEmailAndPassword(email, password)
                                                .addOnCompleteListener(Regstartion.this, task1 -> {
                                                    //Toast.makeText(RoyhatdanOtish.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                                    if (!task1.isSuccessful()) {
                                                        ediroypochta.setError(getString(R.string.foydalanuvchibor));
                                                        Toast.makeText(Regstartion.this, getString(R.string.foydalanuvchibor), Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        //sendVerificationEmail();
                                                        userid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                                                        int min = 10000;
                                                        int max = 99999;
                                                        int diff = max - min;
                                                        Random random = new Random();
                                                        int i = random.nextInt(diff + 1);
                                                        i += min;
                                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd_HH:mm:ss", Locale.getDefault());
                                                        String time = sdf.format(new Date());
                                                        String son = String.valueOf(i),
                                                                ism1 = ediroyism.getText().toString(),
                                                                familya = ediroyfamil.getText().toString(),
                                                                parool = ediroyparol.getText().toString(),
                                                                tel = ediroytel.getText().toString(),
                                                                link = "0",
                                                                linkuser = ediroylink.getText().toString();
                                                        if (Objects.equals(ediroylink.getText().toString(),"")){
                                                            linkuser = "0";
                                                        }
                                                        String data = ediroydatapicker.getDayOfMonth() + "/" + (ediroydatapicker.getMonth() + 1) + "/" + ediroydatapicker.getYear();
                                                        String netCash = "0";
                                                        String bugunpul = "0";
                                                        String kechapul = "0";
                                                        String haftapul = "0";
                                                        String oypul = "0";
                                                        String jamipul = "0";
                                                        Map<String, Object> user1 = new HashMap<>();
                                                        user1.put("ISMI", ism1);
                                                        user1.put("FAMILYA", familya);
                                                        user1.put("PAROL", parool);
                                                        user1.put("POCHTA", email);
                                                        user1.put("TEL", tel);
                                                        user1.put("YOSHI", data);
                                                        user1.put("LINK", link);
                                                        user1.put("LINKUSER", linkuser);
                                                        user1.put("NETCASH", netCash);
                                                        user1.put("BUGUNPUL", bugunpul);
                                                        user1.put("KECHAPUL", kechapul);
                                                        user1.put("HAFTAPUL", haftapul);
                                                        user1.put("OYPUL", oypul);
                                                        user1.put("JAMIPUL", jamipul);
                                                        user1.put("DARAJA", "0");
                                                        user1.put("PROMOCOD", "null");
                                                        user1.put("USERID", son);
                                                        user1.put("UID", userid);
                                                        user1.put("PHOTO", "0");
                                                        user1.put("SONGIFAOLLIK", time);
                                                        user1.put("KIRGANVAQT", time);
                                                        String til = "en";
                                                        String rejim = "rejim";
                                                        String kalit = "1";
                                                        MyDb.kiritish(userid, til, rejim, kalit, parool);
                                                        db.collection("user").document(userid).set(user1)
                                                                .addOnSuccessListener(unused -> {
                                                                }).addOnFailureListener(e -> progressBar2.setVisibility(View.GONE));
                                                        documentReference = db.document("user/" + values[1]);
                                                        documentReference.get().addOnSuccessListener(documentSnapshot -> {
                                                            if (documentSnapshot.exists()) {
                                                                String llink = documentSnapshot.getString("LINK");
                                                                int aa = 0;
                                                                if (llink != null) {
                                                                    aa = Integer.parseInt(llink);
                                                                }
                                                                aa += 1;
                                                                Map<String, Object> updatelink = new HashMap<>();
                                                                updatelink.put("LINK", String.valueOf(aa));
                                                                db.collection("user")
                                                                        .document(values[1])
                                                                        .update(updatelink);
                                                                Toast.makeText(Regstartion.this, llink, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                        String username = ism1 + " - " + familya;
                                                        Map<String, Object> mappul = new HashMap<>();
                                                        mappul.put("KARTA", "0");
                                                        mappul.put("NET_KASH", "O'tkazmalar");
                                                        mappul.put("TOLANMADI", "0");
                                                        mappul.put("OTKAZMA", "0");
                                                        mappul.put("VAQT", "0");
                                                        mappul.put("USER", username);
                                                        db.collection("otkazma").document(userid).set(mappul)
                                                                .addOnSuccessListener(unused -> progressBar2.setVisibility(View.GONE)).addOnFailureListener(e -> progressBar2.setVisibility(View.GONE));
                                                        progressBar2.setVisibility(View.GONE);
                                                        startActivity(new Intent(Regstartion.this, Asosiy.class));
                                                        finish();
                                                    }
                                                }).addOnFailureListener(e -> progressBar2.setVisibility(View.GONE));
                                        break;
                                    }else {
                                        ediroylink.setError(getString(R.string.xatolik));
                                    }
                                    //Toast.makeText(Register.this, document.getString("USERID"), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
    private void fun(){
        email = ediroypochta.getText().toString().trim();
        password = ediroyparol.getText().toString().trim();
        ism = ediroyism.getText().toString().trim();
        famil = ediroyfamil.getText().toString().trim();
        teel = ediroytel.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            ediroypochta.setError(getString(R.string.bosh));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ediroyparol.setError(getString(R.string.bosh));
            return;
        }
        if (password.length() < 6) {
            ediroyparol.setError(getString(R.string.kam));
            return;
        }
        if (ism.length() < 3) {
            ediroyism.setError(getString(R.string.kam));
            return;
        }
        if (famil.length() < 4) {
            ediroyfamil.setError(getString(R.string.kam));
            return;
        }
        if (teel.length() < 7) {
            ediroytel.setError(getString(R.string.kam));
            return;
        }
        progressBar2.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Regstartion.this, task -> {
                    if (!task.isSuccessful()) {
                        ediroypochta.setError(getString(R.string.foydalanuvchibor));
                        Toast.makeText(Regstartion.this, getString(R.string.foydalanuvchibor), Toast.LENGTH_SHORT).show();
                    } else {
                        //sendVerificationEmail();
                        userid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                        int min = 10000;
                        int max = 99999;
                        int diff = max - min;
                        Random random = new Random();
                        int i = random.nextInt(diff + 1);
                        i += min;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd_HH:mm:ss", Locale.getDefault());
                        String time = sdf.format(new Date());
                        String son = String.valueOf(i);
                        String ism1 = ediroyism.getText().toString();
                        String familya = ediroyfamil.getText().toString();
                        String parool = ediroyparol.getText().toString();
                        String tel = ediroytel.getText().toString();
                        String link = ediroylink.getText().toString();
                        if (Objects.equals(ediroylink.getText().toString(),"")){
                            link = "0";
                        }
                        String data = ediroydatapicker.getDayOfMonth() + "/" + (ediroydatapicker.getMonth() + 1) + "/" + ediroydatapicker.getYear();
                        String netCash = "0";
                        String bugunpul = "0";
                        String kechapul = "0";
                        String haftapul = "0";
                        String oypul = "0";
                        String jamipul = "0";
                        Map<String, Object> user1 = new HashMap<>();
                        user1.put("ISMI", ism1);
                        user1.put("FAMILYA", familya);
                        user1.put("PAROL", parool);
                        user1.put("POCHTA", email);
                        user1.put("TEL", tel);
                        user1.put("YOSHI", data);
                        user1.put("LINK", link);
                        user1.put("NETCASH", netCash);
                        user1.put("BUGUNPUL", bugunpul);
                        user1.put("KECHAPUL", kechapul);
                        user1.put("HAFTAPUL", haftapul);
                        user1.put("OYPUL", oypul);
                        user1.put("JAMIPUL", jamipul);
                        user1.put("DARAJA", "0");
                        user1.put("PROMOCOD", "null");
                        user1.put("USERID", son);
                        user1.put("UID", userid);
                        user1.put("PHOTO", "0");
                        user1.put("SONGIFAOLLIK", time);
                        user1.put("KIRGANVAQT", time);
                        String til = "en";
                        String rejim = "rejim";
                        String kalit = "1";
                        MyDb.kiritish(userid, til, rejim, kalit, parool);
                        db.collection("user").document(userid).set(user1)
                                .addOnSuccessListener(unused -> {
                                }).addOnFailureListener(e -> progressBar2.setVisibility(View.GONE));
                        String username = ism1 + " - " + familya;
                        Map<String, Object> mappul = new HashMap<>();
                        mappul.put("KARTA", "0");
                        mappul.put("NET_KASH", "O'tkazmalar");
                        mappul.put("TOLANMADI", "0");
                        mappul.put("OTKAZMA", "0");
                        mappul.put("VAQT", "0");
                        mappul.put("USER", username);
                        db.collection("otkazma").document(userid).set(mappul)
                                .addOnSuccessListener(unused -> progressBar2.setVisibility(View.GONE)).addOnFailureListener(e -> progressBar2.setVisibility(View.GONE));
                        progressBar2.setVisibility(View.GONE);
                        startActivity(new Intent(Regstartion.this, Asosiy.class));
                        finish();
                    }
                }).addOnFailureListener(e -> progressBar2.setVisibility(View.GONE));
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
