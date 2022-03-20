package com.example.onewin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OtkazmaTarix extends AppCompatActivity {
    private ListView listtar;
    private SwipeRefreshLayout swiperefleshtarix;
    String nikname;
    private FirebaseFirestore db;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.otkazmatarix);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        listtar = findViewById(R.id.listtar);
        swiperefleshtarix = findViewById(R.id.swiperefleshtarix);
        db = FirebaseFirestore.getInstance();

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            nikname = (String) b.get("name");
        }
        oqi1();
        swiperefleshtarix.setOnRefreshListener(() -> {
            oqi1();
            swiperefleshtarix.setRefreshing(false);
        });
    }
    public void oqi1() {
        DocumentReference documentReference = db.document("otkazma/" + nikname);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String history = documentSnapshot.getString("NET_KASH");
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    String[] values = new String[0];
                    if (history != null) {
                        values = history.split(",");
                    }
                    List<String> list = new ArrayList<>(Arrays.asList(values));
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(OtkazmaTarix.this,
                            android.R.layout.simple_list_item_1, list);
                    listtar.setAdapter(adapter);
                }, 2000);
            }
        });
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
