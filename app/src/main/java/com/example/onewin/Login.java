package com.example.onewin;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;
import java.util.Objects;

public class Login extends AppCompatActivity {

    private EditText edilogpochta, edilogparol;
    private ProgressBar progressBarlog;
    FirebaseFirestore db;
    private FirebaseAuth auth;
    private SqlData MyDb;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        String languageToLoad = "en";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.login);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        MyDb = new SqlData(this);

        edilogpochta = findViewById(R.id.edilogpochta);
        edilogparol = findViewById(R.id.edilogparol);
        TextView txtlogforgatclick = findViewById(R.id.txtlogforgatclick);
        Button btnparroy = findViewById(R.id.btnparroy);
        Button btnlogkir = findViewById(R.id.btnlogkir);
        progressBarlog = findViewById(R.id.progressBarlog);

        txtlogforgatclick.setOnClickListener(view -> {
            Uri uri = Uri.parse("https://t.me/MANAGER_1XBETBOT");
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
            likeIng.setPackage("com.telegram.android");
            try {
                startActivity(likeIng);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://t.me/MANAGER_1XBETBOT")));
            }
        });
        btnparroy.setOnClickListener(view -> {
            intent = new Intent(Login.this, Regstartion.class);
            startActivity(intent);
        });
        btnlogkir.setOnClickListener(view -> {
            progressBarlog.setVisibility(View.VISIBLE);
            String email = edilogpochta.getText().toString();
            final String password = edilogparol.getText().toString();

            if (TextUtils.isEmpty(email)) {
                edilogpochta.setError(getString(R.string.bosh));
                return;
            }

            if (TextUtils.isEmpty(password)) {
                edilogparol.setError(getString(R.string.bosh));
                return;
            }
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Login.this, task -> {
                        if (!task.isSuccessful()) {
                            if (password.length() < 6) {
                                progressBarlog.setVisibility(View.GONE);
                                edilogparol.setError(getString(R.string.kam));
                            } else {
                                progressBarlog.setVisibility(View.GONE);
                                edilogparol.setError(getString(R.string.xatolik));
                                edilogpochta.setError(getString(R.string.xatolik));
                                Toast.makeText(Login.this, getString(R.string.xatolik), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            if (password.length() < 6) {
                                edilogparol.setError(getString(R.string.kam));
                            } else {
                                String userid1 = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                                //textView2.setText(userid1);
                                String til = "en";
                                String rejim = "rejim";
                                String kalit = "1";
                                String parol = edilogparol.getText().toString();
                                Boolean result = MyDb.kiritish(userid1, til, rejim, kalit, parol);
                                progressBarlog.setVisibility(View.GONE);
                                if (result == true) {
                                    intent = new Intent(Login.this, Asosiy.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), getString(R.string.xatolik), Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });
        });
    }
}
