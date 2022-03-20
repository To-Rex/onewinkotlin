package com.example.onewin;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FragmentSozlama extends Fragment {

    private SqlData MyDb;
    private FirebaseFirestore db;
    Intent intent;
    String UID, TIL, REJIM, KALIT, PAROL;
    String id, uid, til, rejim, kalit, parol, parol1, email, photo, name;

    private EditText edislidejoparol, edislideyaparol, edislideyanaparol;
    private Button btnslidtil;
    private ImageView imgslidebayroq;
    private TextView txtslideusername;
    private ShapeableImageView imgsozdeuser;
    private ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sozlama,container,false);
    }
    @SuppressLint({"NonConstantResourceId", "UseRequireInsteadOfGet"})
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        MyDb = new SqlData(getContext());
        db = FirebaseFirestore.getInstance();

        edislidejoparol = view.findViewById(R.id.edislidejoparol);
        edislideyaparol = view.findViewById(R.id.edislideyaparol);
        edislideyanaparol = view.findViewById(R.id.edislideyanaparol);
        Button btnslideozgar = view.findViewById(R.id.btnslideozgar);
        btnslidtil = view.findViewById(R.id.btnslidtil);
        ImageView imgslidelogout = view.findViewById(R.id.imgslidelogout);
        progressBar = view.findViewById(R.id.progressBar);
        imgslidebayroq = view.findViewById(R.id.imgslidebayroq);
        txtslideusername = view.findViewById(R.id.txtslideusername);
        imgsozdeuser = view.findViewById(R.id.imgsozdeuser);
        progressBar.setVisibility(View.VISIBLE);

        readdatasql();

        Cursor res0 = MyDb.oqish();
        StringBuilder stringBuffer0 = new StringBuilder();
        StringBuilder stringBuffer01 = new StringBuilder();
        StringBuilder stringBuffer02 = new StringBuilder();
        StringBuilder stringBuffer03 = new StringBuilder();
        StringBuilder stringBuffer04 = new StringBuilder();
        StringBuilder stringBuffer05 = new StringBuilder();
        if (res0 != null && res0.getCount() > 0) {
            while (res0.moveToNext()) {
                stringBuffer0.append(res0.getString(0));
                stringBuffer01.append(res0.getString(1));
                stringBuffer02.append(res0.getString(2));
                stringBuffer03.append(res0.getString(3));
                stringBuffer04.append(res0.getString(4));
                stringBuffer05.append(res0.getString(5));
            }
            id = stringBuffer0.toString();
            uid = stringBuffer01.toString();
            til = stringBuffer02.toString();
            rejim = stringBuffer03.toString();
            kalit = stringBuffer04.toString();
            parol = stringBuffer05.toString();
            parol1 = stringBuffer05.toString();
            progressBar.setVisibility(View.GONE);
            if (Objects.equals(til, "uz")) {
                imgslidebayroq.setImageResource(R.drawable.uzbay);
                btnslidtil.setText(getString(R.string.o_zbekcha));
            } else {
                if (Objects.equals(til, "ru")) {
                    imgslidebayroq.setImageResource(R.drawable.rusbay);
                    btnslidtil.setText(getString(R.string.russian));
                } else {
                    if (Objects.equals(til, "en")) {
                        imgslidebayroq.setImageResource(R.drawable.engbay);
                        btnslidtil.setText(getString(R.string.english));
                    }
                }
            }
            DocumentReference documentReference = db.document("user/" + UID);
            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    email = documentSnapshot.getString("POCHTA");
                    photo = documentSnapshot.getString("PHOTO");
                    name = documentSnapshot.getString("ISMI");
                    txtslideusername.setText(name);
                    Glide.with(Objects.requireNonNull(getActivity())).load(photo).into(imgsozdeuser);
                    progressBar.setVisibility(View.GONE);
                }
            });
            imgslidelogout.setOnClickListener(view117 -> {
                PopupMenu popupMenu = new PopupMenu(getContext(), view117);
                popupMenu.inflate(R.menu.logout);
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.chiqish:
                            new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle(getString(R.string.eslatma)).setMessage(getString(R.string.eslatma))
                                    .setPositiveButton(getString(R.string._ha), (dialog, which) -> {
                                        til = "en";
                                        kalit = "0";
                                        MyDb.ozgartir(id, uid, til, rejim, kalit, parol);
                                        intent = new Intent(getContext(), Login.class);
                                        startActivity(intent);
                                        Objects.requireNonNull(getActivity()).finish();
                                    })
                                    .setNegativeButton(getString(R.string._yoq), null)
                                    .show();
                            return true;
                        case R.id.ochirishchiq:
                            new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle(getString(R.string.eslatma)).setMessage(getString(R.string.eslatma3))
                                    .setPositiveButton(getString(R.string._ha), (dialog, which) -> {
                                        progressBar.setVisibility(View.VISIBLE);
                                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        AuthCredential authCredential = EmailAuthProvider.getCredential(email, parol1);
                                        if (firebaseUser != null) {
                                            firebaseUser.reauthenticate(authCredential)
                                                    .addOnCompleteListener(task -> firebaseUser.delete().addOnCompleteListener(task1 -> {
                                                        if (task1.isSuccessful()) {
                                                            db.collection("user")
                                                                    .document(UID)
                                                                    .delete().addOnSuccessListener(unused -> db.collection("otkazma")
                                                                    .document(UID)
                                                                    .delete().addOnSuccessListener(unused12 -> {
                                                                        progressBar.setVisibility(View.GONE);
                                                                        til = "ru";
                                                                        kalit = "0";
                                                                        uid = "0";
                                                                        parol = "0";
                                                                        MyDb.ozgartir(id, uid, til, rejim, kalit, parol);
                                                                        intent = new Intent(getContext(), Login.class);
                                                                        startActivity(intent);
                                                                        Objects.requireNonNull(getActivity()).finish();
                                                                    })).addOnFailureListener(e -> {
                                                                Toast.makeText(getContext(), getString(R.string.xatolik) + "=", Toast.LENGTH_SHORT).show();
                                                                progressBar.setVisibility(View.GONE);
                                                            });
                                                        }
                                                    }));
                                        } else {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getContext(), getString(R.string.xatolik), Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton(getString(R.string._yoq), null)
                                    .show();
                            return true;
                    }
                    return false;
                });
                popupMenu.show();
            });
            btnslidtil.setOnClickListener(view118 -> {
                PopupMenu popupMenu = new PopupMenu(getContext(), view118);
                popupMenu.inflate(R.menu.tilii);
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.english:
                            til = "en";
                            imgslidebayroq.setImageResource(R.drawable.engbay);
                            btnslidtil.setText(getString(R.string.english));
                            Boolean result2 = MyDb.ozgartir(id, uid, til, rejim, kalit, parol);
                            if (result2 == true) {
                                intent = new Intent(getActivity(), Asosiy.class);
                                startActivity(intent);
                                Objects.requireNonNull(getActivity()).finish();
                            } else {
                                Toast.makeText(getContext(), getString(R.string.xatolik), Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        case R.id.russian:
                            til = "ru";
                            imgslidebayroq.setImageResource(R.drawable.rusbay);
                            btnslidtil.setText(getString(R.string.russian));
                            Boolean result1 = MyDb.ozgartir(id, uid, til, rejim, kalit, parol);
                            if (result1 == true) {
                                intent = new Intent(getActivity(), Asosiy.class);
                                startActivity(intent);
                                Objects.requireNonNull(getActivity()).finish();
                            } else {
                                Toast.makeText(getContext(), getString(R.string.xatolik), Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        case R.id.ozbekcha:
                            imgslidebayroq.setImageResource(R.drawable.uzbay);
                            til = "uz";
                            btnslidtil.setText(getString(R.string.o_zbekcha));
                            Boolean result = MyDb.ozgartir(id, uid, til, rejim, kalit, parol);
                            if (result == true) {
                                intent = new Intent(getActivity(), Asosiy.class);
                                startActivity(intent);
                                Objects.requireNonNull(getActivity()).finish();
                            } else {
                                Toast.makeText(getContext(), getString(R.string.xatolik), Toast.LENGTH_SHORT).show();
                            }
                            return true;
                    }
                    return false;
                });
                popupMenu.show();
            });
            btnslideozgar.setOnClickListener(view119 -> {
                if (edislidejoparol.getText().toString().isEmpty()) {
                    edislidejoparol.setError(getString(R.string.bosh));
                    if (edislideyaparol.getText().toString().isEmpty()) {
                        edislideyaparol.setError(getString(R.string.bosh));
                    }
                    if (edislideyanaparol.getText().toString().isEmpty()) {
                        edislideyanaparol.setError(getString(R.string.bosh));
                    }
                } else {
                    if (edislideyaparol.getText().toString().isEmpty()) {
                        edislideyaparol.setError(getString(R.string.bosh));
                        if (edislidejoparol.getText().toString().isEmpty()) {
                            edislidejoparol.setError(getString(R.string.bosh));
                        }
                        if (edislideyanaparol.getText().toString().isEmpty()) {
                            edislideyanaparol.setError(getString(R.string.bosh));
                        }
                    } else {
                        if (edislideyanaparol.getText().toString().isEmpty()) {
                            edislideyanaparol.setError(getString(R.string.bosh));
                            if (edislidejoparol.getText().toString().isEmpty()) {
                                edislidejoparol.setError(getString(R.string.bosh));
                            }
                            if (edislideyaparol.getText().toString().isEmpty()) {
                                edislideyaparol.setError(getString(R.string.bosh));
                            }
                        } else {
                            if (Objects.equals(parol, edislidejoparol.getText().toString())) {
                                if (Objects.equals(edislideyanaparol.getText().toString(), edislideyaparol.getText().toString())) {
                                    new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setTitle(getString(R.string.eslatma)).setMessage(getString(R.string.eslatma1))
                                            .setPositiveButton(getString(R.string._ha), (dialog, which) -> {
                                                parol = edislideyanaparol.getText().toString();
                                                MyDb.ozgartir(id, uid, til, rejim, kalit, parol);
                                                Toast.makeText(getActivity(), "Souccess", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.VISIBLE);
                                                AuthCredential credential = EmailAuthProvider.getCredential(email, parol1);
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                if (user != null) {
                                                    user.reauthenticate(credential)
                                                            .addOnCompleteListener(task -> {
                                                                if (task.isSuccessful()) {
                                                                    user.updatePassword(parol).addOnCompleteListener(task12 -> {
                                                                        if (task12.isSuccessful()) {
                                                                            Map<String, Object> updatecash = new HashMap<>();
                                                                            updatecash.put("PAROL", parol);
                                                                            db.collection("user")
                                                                                    .document(UID)
                                                                                    .update(updatecash);
                                                                            edislidejoparol.setText(null);
                                                                            edislideyaparol.setText(null);
                                                                            edislideyanaparol.setText(null);
                                                                            progressBar.setVisibility(View.GONE);
                                                                            Toast.makeText(getContext(), "soucses", Toast.LENGTH_SHORT).show();
                                                                        } else {
                                                                            progressBar.setVisibility(View.GONE);
                                                                            Toast.makeText(getContext(), getString(R.string.xatolik), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                } else {
                                                                    progressBar.setVisibility(View.GONE);
                                                                    Toast.makeText(getContext(), getString(R.string.xatolik), Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            })
                                            .setNegativeButton(getString(R.string._yoq), null)
                                            .show();
                                } else {
                                    edislideyanaparol.setError(getString(R.string.xatolik));
                                }
                            } else {
                                edislidejoparol.setError(getString(R.string.xatolik));
                            }
                        }
                    }
                }
            });
        }
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
