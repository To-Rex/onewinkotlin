package com.example.onewin;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;

public class FragmentMalumot extends Fragment {

    ImageView imgmaltelegram;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_malumot,container,false);
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        imgmaltelegram = view.findViewById(R.id.imgmaltelegram);

        imgmaltelegram.setOnClickListener(view1 -> {
            Uri uri = Uri.parse("http://t.me/Partner_1winbot");
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
            likeIng.setPackage("com.telegram.android");
            try {
                startActivity(likeIng);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://t.me/Partner_1winbot")));
            }
        });

    }
}
