package com.example.onewin;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class FragmentAsosiy extends Fragment {
    private SqlData MyDb;
    private FirebaseFirestore db;
    private DocumentReference documentReference;

    String UID, TIL, REJIM, KALIT, PAROL, wievpromocodefirebase = "PROMOSOM";
    String[] values;
    Handler handler;
    private TextView txthom1, txthom2, txthom3, txthom4, txthom5, txthompromo,
            txthomfoiz, txthomfoiz1, txthomfoiz2, txthomfoiz3,txthomregdep,
            texthomnewdepo,texthomnewdepo2,texthomnewdepo3;
    private ProgressBar progresshisob;
    private SwipeRefreshLayout swiperefleshhisob;
    private LottieAnimationView animhombtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hisob,container,false);
    }

    @SuppressLint({"SetTextI18n", "UseRequireInsteadOfGet"})
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        MyDb = new SqlData(getContext());
        db = FirebaseFirestore.getInstance();

        txthomfoiz = view.findViewById(R.id.txthomfoiz);
        txthomfoiz1 = view.findViewById(R.id.txthomfoiz1);
        txthomfoiz2 = view.findViewById(R.id.txthomfoiz2);
        txthomfoiz3 = view.findViewById(R.id.txthomfoiz3);
        txthomregdep = view.findViewById(R.id.txthomregdep);
        texthomnewdepo = view.findViewById(R.id.texthomnewdepo);
        texthomnewdepo2 = view.findViewById(R.id.texthomnewdepo2);
        texthomnewdepo3 = view.findViewById(R.id.texthomnewdepo3);
        ImageButton imageButton = view.findViewById(R.id.imageButton);
        animhombtn = view.findViewById(R.id.animhombtn);
        swiperefleshhisob = view.findViewById(R.id.swiperefleshhisob);

        View viewbugun = view.findViewById(R.id.viewbugun);
        View viewkecha = view.findViewById(R.id.viewkecha);
        View viewhafta = view.findViewById(R.id.viewhafta);
        View viewoy = view.findViewById(R.id.viewoy);
        View viewjami = view.findViewById(R.id.viewjami);
        View viewhompromocod = view.findViewById(R.id.viewhompromocod);
        View viewhomallpromocode = view.findViewById(R.id.viewhomallpromocode);

        txthom1 = view.findViewById(R.id.txthom1);
        txthom2 = view.findViewById(R.id.txthom2);
        txthom3 = view.findViewById(R.id.txthom3);
        txthom4 = view.findViewById(R.id.txthom4);
        txthom5 = view.findViewById(R.id.txthom5);
        txthompromo = view.findViewById(R.id.txthompromo);
        progresshisob = view.findViewById(R.id.progresshisob);
        progresshisob.setVisibility(View.VISIBLE);
        readdatasql();

        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.btnanim);
        imageButton.startAnimation(shake);
        handler = new Handler();
        handler.postDelayed(() -> animhombtn.setVisibility(View.GONE), 4000);
        imageButton.setOnClickListener(v -> {

            @SuppressLint("InflateParams")
            View view1 = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
            @SuppressLint("UseRequireInsteadOfGet")
            BottomSheetDialog dialog = new BottomSheetDialog(Objects.requireNonNull(getContext()));
            dialog.setContentView(view1);
            ImageView rasm1 = dialog.findViewById(R.id.rasm1),
                    rasm2 = dialog.findViewById(R.id.rasm2),
                    rasm3 = dialog.findViewById(R.id.rasm3),
                    rasm4 = dialog.findViewById(R.id.rasm4);
            TextView txtbottomxabar = dialog.findViewById(R.id.txtbottomxabar),
                    txtbottomyangilik = dialog.findViewById(R.id.txtbottomyangilik);
            progresshisob.setVisibility(View.VISIBLE);
            documentReference = db.document("Admin/netcash");
            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    if (txtbottomxabar != null) {
                        txtbottomxabar.setText(documentSnapshot.getString("XABARLAR"));
                    }
                    if (txtbottomyangilik != null) {
                        txtbottomyangilik.setText(documentSnapshot.getString("YANAGILIKLAR"));
                    }
                    progresshisob.setVisibility(View.GONE);
                }
            });
            documentReference = db.document("Admin/photo");
            documentReference.get().addOnSuccessListener(documentSnapshot1 -> {
                if (documentSnapshot1.exists()) {
                    String urell = documentSnapshot1.getString("photo1");
                    String urell1 = documentSnapshot1.getString("photo2");
                    String urell2 = documentSnapshot1.getString("photo3");
                    String urell3 = documentSnapshot1.getString("photo4");

                    if (!Objects.equals(urell, "TextView")) {
                        if (rasm1 != null) {
                            Glide.with(Objects.requireNonNull(getActivity())).load(urell).into(rasm1);
                        }
                        if (rasm2 != null) {
                            Glide.with(Objects.requireNonNull(getActivity())).load(urell1).into(rasm2);
                        }
                        if (rasm3 != null) {
                            Glide.with(Objects.requireNonNull(getActivity())).load(urell2).into(rasm3);
                        }
                        if (rasm4 != null) {
                            Glide.with(Objects.requireNonNull(getActivity())).load(urell3).into(rasm4);
                        }
                        progresshisob.setVisibility(View.GONE);
                    }
                }
            }).addOnFailureListener(e -> {
            });

            dialog.show();
        });
        viewhomallpromocode.setOnClickListener(view12 -> {
            documentReference = db.document("Admin/admin");
            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    wievpromocodefirebase = documentSnapshot.getString("POMOCODE");
                    if (wievpromocodefirebase != null) {
                        values = wievpromocodefirebase.split("," + "\n");
                    }
                    new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                            .setIcon(R.drawable.promocodelogo)
                            .setTitle(getString(R.string.umumiy_promocodlar))
                            .setMessage("\n   " + wievpromocodefirebase)
                            .show();
                }
            });
        });
        viewbugun.setOnClickListener(view13 -> new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setIcon(R.drawable.logonetcash)
                .setTitle(getString(R.string.netCash) + "\n" + getString(R.string.netCash))
                .setMessage("\n   " + txthom1.getText().toString())
                .show());
        viewhompromocod.setOnClickListener(view14 -> new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setIcon(R.drawable.promocodelogo)
                .setTitle(getString(R.string.netCash) + "\n" + getString(R.string.promocod))
                .setMessage("\n   " + txthompromo.getText().toString())
                .show());
        viewkecha.setOnClickListener(view15 -> new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setIcon(R.drawable.logonetcash)
                .setTitle(getString(R.string.netCash) + "\n" + getString(R.string.maosh2))
                .setMessage("\n   " + txthom2.getText().toString())
                .show());
        viewhafta.setOnClickListener(view16 -> new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setIcon(R.drawable.logonetcash)
                .setTitle(getString(R.string.netCash) + "\n" + getString(R.string.maosh3))
                .setMessage("\n   " + txthom3.getText().toString())
                .show());
        viewoy.setOnClickListener(view17 -> new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setIcon(R.drawable.logonetcash)
                .setTitle(getString(R.string.netCash) + "\n" + getString(R.string.maosh4))
                .setMessage("\n   " + txthom4.getText().toString())
                .show());
        viewjami.setOnClickListener(view18 -> new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setIcon(R.drawable.logonetcash)
                .setTitle(getString(R.string.netCash) + "\n" + getString(R.string.maosh5))
                .setMessage("\n   " + txthom5.getText().toString())
                .show());
        readfirebase();
        swiperefleshhisob.setOnRefreshListener(() -> {
            readfirebase();
            swiperefleshhisob.setRefreshing(false);
        });
    }
    @SuppressLint("SetTextI18n")
    private void readfirebase(){
        progresshisob.setVisibility(View.VISIBLE);
        documentReference = db.document("user/" + UID);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String bugun = documentSnapshot.getString("BUGUNPUL");
                String kecha = documentSnapshot.getString("KECHAPUL");
                String hafta = documentSnapshot.getString("HAFTAPUL");
                String oy = documentSnapshot.getString("OYPUL");
                String jami = documentSnapshot.getString("JAMIPUL");
                String promoc = documentSnapshot.getString("PROMOCOD");
                txthom1.setText("NetCash: " + bugun);
                txthom2.setText("NetCash: " + kecha);
                txthom3.setText("NetCash: " + hafta);
                txthom4.setText("NetCash: " + oy);
                txthom5.setText("NetCash: " + jami);
                txthompromo.setText(promoc);
                progresshisob.setVisibility(View.GONE);
            }
        });
        documentReference = db.document("Admin/admin");
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String foiz = documentSnapshot.getString("foiz"),
                        foiz1 = documentSnapshot.getString("foiz1"),
                        foiz2 = documentSnapshot.getString("foiz2"),
                        foiz3 = documentSnapshot.getString("foiz3"),
                        foiztext1 = documentSnapshot.getString("text1"),
                        foiz2text = documentSnapshot.getString("text2"),
                        foiz3text = documentSnapshot.getString("text3"),
                        foiz4text = documentSnapshot.getString("text4");
                txthomfoiz.setText(foiz);
                txthomfoiz1.setText(foiz1);
                txthomfoiz2.setText(foiz2);
                txthomfoiz3.setText(foiz3);

                txthomregdep.setText(foiztext1);
                texthomnewdepo.setText(foiz2text);
                texthomnewdepo2.setText(foiz3text);
                texthomnewdepo3.setText(foiz4text);

                progresshisob.setVisibility(View.GONE);
            }
        });
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
