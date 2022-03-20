package com.example.onewin;

import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class FragmentOtkazma extends Fragment {

    private SqlData MyDb;
    private FirebaseFirestore db;
    private DocumentReference documentReference;
    String  UID, TIL, REJIM, KALIT, PAROL;

    String somm, dollarr, rubll, euroo, a, pulnetcash, otkazma, vaqtlar;
    double cashsom, cashdollar, casheuro, cashrubl, aa;
    private TextView txtotkapul, txtotkanarx, txtotkaeslat;
    private Button btnotkavalyuta;
    private Button btnotkachiqar;
    private EditText ediotkanetkash, ediotkakarta;
    private ProgressBar progressBarotka;
    private SwipeRefreshLayout refleshotkaz;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_otkazma,container,false);
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n", "UseRequireInsteadOfGet"})
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {

        MyDb = new SqlData(getContext());
        db = FirebaseFirestore.getInstance();

        txtotkapul = view.findViewById(R.id.txtotkapul);
        txtotkanarx = view.findViewById(R.id.txtotkanarx);
        txtotkaeslat = view.findViewById(R.id.txtotkaeslat);
        btnotkavalyuta = view.findViewById(R.id.btnotkavalyuta);
        btnotkachiqar = view.findViewById(R.id.btnotkachiqar);
        Button btnotkazsotibol = view.findViewById(R.id.btnotkazsotibol);
        ediotkanetkash = view.findViewById(R.id.ediotkanetkash);
        ediotkakarta = view.findViewById(R.id.ediotkakarta);
        LottieAnimationView lottiotkatarix = view.findViewById(R.id.lottiotkatarix);
        progressBarotka = view.findViewById(R.id.progressBarotka);
        refleshotkaz = view.findViewById(R.id.refleshotkaz);
        txtotkanarx.setText("-");
        readdatasql();

        ediotkanetkash.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (!Character.toString(source.charAt(i)).matches("[a-zA-Z0-9-_.]+")) {
                    return "";
                }
            }
            return null;
        };
        InputFilter filter1 = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (!Character.toString(source.charAt(i)).matches("[a-zA-Z0-9-_]+")) {
                    return "";
                }
            }
            return null;
        };
        ediotkanetkash.setFilters(new InputFilter[]{filter});
        ediotkakarta.setFilters(new InputFilter[]{filter1});
        documentReference = db.document("Admin/netcash");
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            somm = documentSnapshot.getString("som");
            rubll = documentSnapshot.getString("rubl");
            dollarr = documentSnapshot.getString("dollar");
            euroo = documentSnapshot.getString("euro");
            cashsom = Double.parseDouble(somm);
            cashdollar = Double.parseDouble(dollarr);
            casheuro = Double.parseDouble(euroo);
            cashrubl = Double.parseDouble(rubll);
            documentReference = db.document("user/" + UID);
            documentReference.get().addOnSuccessListener(documentSnapshot12 -> {
                pulnetcash = documentSnapshot12.getString("NETCASH");
                txtotkapul.setText(pulnetcash);
                documentReference = db.document("otkazma/" + UID);
                documentReference.get().addOnSuccessListener(documentSnapshot121 -> {
                    otkazma = documentSnapshot121.getString("NET_KASH");
                    vaqtlar = documentSnapshot121.getString("VAQT");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy:MM:dd", Locale.getDefault());
                    String vaqtreal = sdf1.format(new Date());
                    if (Objects.equals(vaqtlar, vaqtreal)) {
                        txtotkaeslat.setVisibility(View.VISIBLE);
                        btnotkavalyuta.setVisibility(View.INVISIBLE);
                        btnotkachiqar.setVisibility(View.INVISIBLE);
                        ediotkakarta.setVisibility(View.INVISIBLE);
                        ediotkanetkash.setVisibility(View.INVISIBLE);
                        txtotkanarx.setVisibility(View.INVISIBLE);
                    }
                    progressBarotka.setVisibility(View.GONE);
                });
            });
        });
        lottiotkatarix.setOnClickListener(view120 -> {
            Intent intent = new Intent(getActivity(), OtkazmaTarix.class);
            intent.putExtra("name", UID);
            startActivity(intent);
        });
        ediotkanetkash.addTextChangedListener(new TextWatcher() {
            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (Objects.equals(btnotkavalyuta.getText().toString(), "")) {
                    txtotkanarx.setText("-");
                } else {
                    if (Objects.equals(btnotkavalyuta.getText().toString(), getString(R.string.netcash_uzs))) {
                        if (Objects.equals(ediotkanetkash.getText().toString(), "")) {
                            txtotkanarx.setText("-");
                        } else {
                            if (Objects.equals(ediotkanetkash.getText().toString(), ".") ||
                                    Objects.equals(ediotkanetkash.getText().toString(), ",")) {
                                txtotkanarx.setText(".");
                                ediotkanetkash.setText(null);
                                ediotkanetkash.setError(getString(R.string.xatolik));
                            } else {
                                a = ediotkanetkash.getText().toString();
                                aa = Double.parseDouble(a);
                                if (aa > cashsom || aa == cashsom || aa < cashsom) {
                                    aa *= cashsom;
                                    txtotkanarx.setText(aa + " UZS");
                                } else {
                                    ediotkanetkash.setError(getString(R.string.xatolik));
                                }
                            }
                        }
                    } else {
                        if (Objects.equals(btnotkavalyuta.getText().toString(), getString(R.string.netcash_usd))) {
                            if (Objects.equals(ediotkanetkash.getText().toString(), "")) {
                                txtotkanarx.setText("-");
                            } else {
                                if (Objects.equals(ediotkanetkash.getText().toString(), ".") ||
                                        Objects.equals(ediotkanetkash.getText().toString(), ",")) {
                                    txtotkanarx.setText(".");
                                    ediotkanetkash.setText(null);
                                    ediotkanetkash.setError(getString(R.string.xatolik));
                                } else {
                                    a = ediotkanetkash.getText().toString();
                                    aa = Double.parseDouble(a);
                                    if (aa > cashdollar || aa == cashdollar || aa < cashdollar) {
                                        aa *= cashdollar;
                                        txtotkanarx.setText(aa + " USD");
                                    } else {
                                        ediotkanetkash.setError(getString(R.string.xatolik));
                                    }
                                }
                            }
                        } else {
                            if (Objects.equals(btnotkavalyuta.getText().toString(), getString(R.string.netcash_eur))) {
                                if (Objects.equals(ediotkanetkash.getText().toString(), "")) {
                                    txtotkanarx.setText("-");
                                } else {
                                    if (Objects.equals(ediotkanetkash.getText().toString(), ".") ||
                                            Objects.equals(ediotkanetkash.getText().toString(), ",")) {
                                        txtotkanarx.setText(".");
                                        ediotkanetkash.setText(null);
                                        ediotkanetkash.setError(getString(R.string.xatolik));
                                    } else {
                                        a = ediotkanetkash.getText().toString();
                                        aa = Double.parseDouble(a);
                                        if (aa > casheuro || aa == casheuro || aa < casheuro) {
                                            aa *= casheuro;
                                            txtotkanarx.setText(aa + " EUR");
                                        } else {
                                            ediotkanetkash.setError(getString(R.string.xatolik));
                                        }
                                    }
                                }
                            } else {
                                if (Objects.equals(btnotkavalyuta.getText().toString(), getString(R.string.netcash_rubl))) {
                                    if (Objects.equals(ediotkanetkash.getText().toString(), " ")) {
                                        txtotkanarx.setText("-");
                                    } else {
                                        if (Objects.equals(ediotkanetkash.getText().toString(), ".")) {
                                            txtotkanarx.setText(".");
                                            ediotkanetkash.setText(null);
                                            ediotkanetkash.setError(getString(R.string.xatolik));
                                        } else {
                                            a = ediotkanetkash.getText().toString();
                                            aa = Double.parseDouble(a);
                                            if (aa > cashrubl || aa == cashrubl || aa < cashrubl) {
                                                aa *= cashrubl;
                                                txtotkanarx.setText(aa + " RUB");
                                            } else {
                                                ediotkanetkash.setError(getString(R.string.xatolik));
                                            }
                                        }
                                    }
                                } else {
                                    ediotkanetkash.setError(getString(R.string.xatolik));
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        btnotkachiqar.setOnClickListener(view121 -> {
            if (ediotkanetkash.getText().toString().isEmpty()) {
                ediotkanetkash.setError(getString(R.string.bosh));
                if (ediotkakarta.getText().toString().isEmpty()) {
                    ediotkakarta.setError(getString(R.string.bosh));
                }
            } else {
                if (ediotkakarta.getText().toString().isEmpty()) {
                    ediotkakarta.setError(getString(R.string.bosh));
                    if (ediotkanetkash.getText().toString().isEmpty()) {
                        ediotkanetkash.setError(getString(R.string.bosh));
                    }
                } else {
                    String a = ediotkanetkash.getText().toString();
                    double aa = Double.parseDouble(a);
                    if (aa < 1) {
                        ediotkanetkash.setError(getString(R.string.xatolik));
                    } else {
                        if (ediotkakarta.getText().toString().length() <= 15) {
                            ediotkakarta.setError(getString(R.string.xatolik));
                        } else {
                            progressBarotka.setVisibility(View.VISIBLE);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd     HH:mm", Locale.getDefault());
                            String datajami = sdf.format(new Date());
                            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy:MM:dd", Locale.getDefault());
                            String vaqtchesk = sdf1.format(new Date());
                            String c = ediotkanetkash.getText().toString();
                            String c1 = txtotkapul.getText().toString();
                            String c2 = txtotkanarx.getText().toString();
                            double son = Double.parseDouble(c);
                            double pul = Double.parseDouble(c1);
                            String otkaazma = otkazma + "," + c + " NetCash (" + c2 + ") " + datajami;
                            if (son == pul || son < pul) {
                                double i = pul - son;
                                String ii = valueOf(i);
                                Map<String, Object> netcas = new HashMap<>();
                                netcas.put("NETCASH", ii);
                                Map<String, Object> mappul = new HashMap<>();
                                mappul.put("NET_KASH", otkaazma);
                                mappul.put("OTKAZMA", "1");
                                mappul.put("KARTA", ediotkakarta.getText().toString());
                                mappul.put("VAQT", vaqtchesk);
                                db.collection("otkazma")
                                        .document(UID)
                                        .update(mappul).addOnSuccessListener(unused -> db.collection("user")
                                        .document(UID)
                                        .update(netcas)
                                        .addOnSuccessListener(unused1 -> {
                                            documentReference = db.document("user/" + UID);
                                            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                                                progressBarotka.setVisibility(View.VISIBLE);
                                                pulnetcash = documentSnapshot.getString("NETCASH");
                                                txtotkapul.setText(pulnetcash);
                                                documentReference = db.document("otkazma/" + UID);
                                                documentReference.get().addOnSuccessListener(documentSnapshot14 -> {
                                                    otkazma = documentSnapshot14.getString("NET_KASH");
                                                    vaqtlar = documentSnapshot14.getString("VAQT");
                                                    SimpleDateFormat sdf11 = new SimpleDateFormat("yyyy:MM:dd", Locale.getDefault());
                                                    String vaqtreal = sdf11.format(new Date());
                                                    if (Objects.equals(vaqtlar, vaqtreal)) {
                                                        new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                                                                .setIcon(android.R.drawable.ic_popup_reminder)
                                                                .setTitle(getString(R.string.o_tkazmalar))
                                                                .setMessage("\n   " + getString(R.string.eslatma2))
                                                                .show();
                                                        txtotkaeslat.setVisibility(View.VISIBLE);
                                                        btnotkavalyuta.setVisibility(View.INVISIBLE);
                                                        btnotkachiqar.setVisibility(View.INVISIBLE);
                                                        ediotkakarta.setVisibility(View.INVISIBLE);
                                                        ediotkanetkash.setVisibility(View.INVISIBLE);
                                                        txtotkanarx.setVisibility(View.INVISIBLE);
                                                    } else {
                                                        txtotkaeslat.setVisibility(View.INVISIBLE);
                                                        btnotkavalyuta.setVisibility(View.VISIBLE);
                                                        btnotkachiqar.setVisibility(View.VISIBLE);
                                                        ediotkakarta.setVisibility(View.VISIBLE);
                                                        ediotkanetkash.setVisibility(View.VISIBLE);
                                                        txtotkanarx.setVisibility(View.VISIBLE);
                                                    }
                                                    progressBarotka.setVisibility(View.GONE);
                                                });
                                            });
                                            progressBarotka.setVisibility(View.GONE);
                                        })).addOnFailureListener(e -> {
                                    ediotkanetkash.setError(getString(R.string.xatolik));
                                    ediotkakarta.setError(getString(R.string.xatolik));
                                    progressBarotka.setVisibility(View.GONE);
                                });
                            } else {
                                ediotkanetkash.setError(getString(R.string.mablag_kam));
                                progressBarotka.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }
        });
        btnotkazsotibol.setOnClickListener(view122 -> {
            Uri uri = Uri.parse("http://t.me/NETCASHBUYBOT");
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
            likeIng.setPackage("com.telegram.android");
            try {
                startActivity(likeIng);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://t.me/NETCASHBUYBOT")));
            }
        });
        btnotkavalyuta.setOnClickListener(view123 -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), view123);
            popupMenu.inflate(R.menu.valyuta);
            popupMenu
                    .setOnMenuItemClickListener(menuItem -> {
                        switch (menuItem.getItemId()) {
                            case R.id.dollarpul:
                                if (ediotkanetkash.getText().toString().isEmpty()) {
                                    ediotkanetkash.setError(getString(R.string.xatolik));
                                } else {
                                    btnotkavalyuta.setText(getString(R.string.netcash_usd));
                                    ediotkanetkash.setText(ediotkanetkash.getText().toString());
                                }
                                return true;
                            case R.id.europul:
                                if (ediotkanetkash.getText().toString().isEmpty()) {
                                    ediotkanetkash.setError(getString(R.string.xatolik));
                                } else {
                                    btnotkavalyuta.setText(getString(R.string.netcash_eur));
                                    ediotkanetkash.setText(ediotkanetkash.getText().toString());
                                }
                                return true;
                            case R.id.rublpul:
                                if (ediotkanetkash.getText().toString().isEmpty()) {
                                    ediotkanetkash.setError(getString(R.string.xatolik));
                                } else {
                                    btnotkavalyuta.setText(getString(R.string.netcash_rubl));
                                    ediotkanetkash.setText(ediotkanetkash.getText().toString());
                                }
                                return true;
                            case R.id.sompul:
                                if (ediotkanetkash.getText().toString().isEmpty()) {
                                    ediotkanetkash.setError(getString(R.string.xatolik));
                                } else {
                                    btnotkavalyuta.setText(getString(R.string.netcash_uzs));
                                    ediotkanetkash.setText(ediotkanetkash.getText().toString());
                                }

                                return true;
                        }
                        return false;
                    });
            popupMenu.show();
        });
        refleshotkaz.setOnRefreshListener(() -> {
            progressBarotka.setVisibility(View.VISIBLE);
            documentReference = db.document("Admin/netcash");
            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                somm = documentSnapshot.getString("som");
                rubll = documentSnapshot.getString("rubl");
                dollarr = documentSnapshot.getString("dollar");
                euroo = documentSnapshot.getString("euro");
                cashsom = Double.parseDouble(somm);
                cashdollar = Double.parseDouble(dollarr);
                casheuro = Double.parseDouble(euroo);
                cashrubl = Double.parseDouble(rubll);
                documentReference = db.document("user/" + UID);
                documentReference.get().addOnSuccessListener(documentSnapshot13 -> {
                    pulnetcash = documentSnapshot13.getString("NETCASH");
                    txtotkapul.setText(pulnetcash);
                    progressBarotka.setVisibility(View.GONE);
                });
            });
            if (Objects.equals(btnotkavalyuta.getText().toString(), "")) {
                txtotkanarx.setText("-");
            } else {
                if (Objects.equals(btnotkavalyuta.getText().toString(), getString(R.string.netcash_uzs))) {
                    if (Objects.equals(ediotkanetkash.getText().toString(), "")) {
                        txtotkanarx.setText("-");
                    } else {
                        if (Objects.equals(ediotkanetkash.getText().toString(), ".") ||
                                Objects.equals(ediotkanetkash.getText().toString(), ",")) {
                            txtotkanarx.setText(".");
                            ediotkanetkash.setText(null);
                            ediotkanetkash.setError(getString(R.string.xatolik));
                        } else {
                            a = ediotkanetkash.getText().toString();
                            aa = Double.parseDouble(a);
                            if (aa > cashsom || aa == cashsom || aa < cashsom) {
                                aa *= cashsom;
                                txtotkanarx.setText(aa + " UZS");
                            } else {
                                ediotkanetkash.setError(getString(R.string.xatolik));
                            }
                        }
                    }
                } else {
                    if (Objects.equals(btnotkavalyuta.getText().toString(), getString(R.string.netcash_usd))) {
                        if (Objects.equals(ediotkanetkash.getText().toString(), "")) {
                            txtotkanarx.setText("-");
                        } else {
                            if (Objects.equals(ediotkanetkash.getText().toString(), ".") ||
                                    Objects.equals(ediotkanetkash.getText().toString(), ",")) {
                                txtotkanarx.setText(".");
                                ediotkanetkash.setText(null);
                                ediotkanetkash.setError(getString(R.string.xatolik));
                            } else {
                                a = ediotkanetkash.getText().toString();
                                aa = Double.parseDouble(a);
                                if (aa > cashdollar || aa == cashdollar || aa < cashdollar) {
                                    aa *= cashdollar;
                                    txtotkanarx.setText(aa + " USD");
                                } else {
                                    ediotkanetkash.setError(getString(R.string.xatolik));
                                }
                            }
                        }
                    } else {
                        if (Objects.equals(btnotkavalyuta.getText().toString(), getString(R.string.netcash_eur))) {
                            if (Objects.equals(ediotkanetkash.getText().toString(), "")) {
                                txtotkanarx.setText("-");
                            } else {
                                if (Objects.equals(ediotkanetkash.getText().toString(), ".") ||
                                        Objects.equals(ediotkanetkash.getText().toString(), ",")) {
                                    txtotkanarx.setText(".");
                                    ediotkanetkash.setText(null);
                                    ediotkanetkash.setError(getString(R.string.xatolik));
                                } else {
                                    a = ediotkanetkash.getText().toString();
                                    aa = Double.parseDouble(a);
                                    if (aa > casheuro || aa == casheuro || aa < casheuro) {
                                        aa *= casheuro;
                                        txtotkanarx.setText(aa + " EUR");
                                    } else {
                                        ediotkanetkash.setError(getString(R.string.xatolik));
                                    }
                                }
                            }
                        } else {
                            if (Objects.equals(btnotkavalyuta.getText().toString(), getString(R.string.netcash_rubl))) {
                                if (Objects.equals(ediotkanetkash.getText().toString(), " ")) {
                                    txtotkanarx.setText("-");
                                } else {
                                    if (Objects.equals(ediotkanetkash.getText().toString(), ".")) {
                                        txtotkanarx.setText(".");
                                        ediotkanetkash.setText(null);
                                        ediotkanetkash.setError(getString(R.string.xatolik));
                                    } else {
                                        a = ediotkanetkash.getText().toString();
                                        aa = Double.parseDouble(a);
                                        if (aa > cashrubl || aa == cashrubl || aa < cashrubl) {
                                            aa *= cashrubl;
                                            txtotkanarx.setText(aa + " RUB");
                                        } else {
                                            ediotkanetkash.setError(getString(R.string.xatolik));
                                        }
                                    }
                                }
                            } else {
                                ediotkanetkash.setError(getString(R.string.xatolik));
                            }
                        }
                    }
                }
            }
            refleshotkaz.setRefreshing(false);
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
