package com.example.user.myevents;


import android.content.Intent;
import android.os.Bundle;


import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment


        View view = inflater.inflate(R.layout.profile_view, parent, false);

        Button btn = (Button) view.findViewById(R.id.Log_out_button);
        btn.setOnClickListener(this);
        Button btn2 = (Button) view.findViewById(R.id.Delete_button);
        btn2.setOnClickListener(this);
        ImageButton btn3 = (ImageButton) view.findViewById(R.id.add_friend);
        btn3.setOnClickListener(this);

        final TextView nom = (TextView) view.findViewById(R.id.user_profile_name);
        nom.setText(mAuth.getCurrentUser().getDisplayName());
        final TextView mail = (TextView) view.findViewById(R.id.user_profile_short_bio);
        mail.setText(mAuth.getCurrentUser().getEmail());
        if (!mAuth.getCurrentUser().getProviders().get(0).equals("password")){
            ImageButton profilePicture = (ImageButton) view.findViewById(R.id.user_profile_photo);
            String photoUrl = mAuth.getCurrentUser().getPhotoUrl().toString();
            Picasso.with(getContext()).load(photoUrl).fit().centerCrop().into(profilePicture);
        }


        return view;
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()) {

                case R.id.Log_out_button:
                    mAuth.signOut();
                    Toast.makeText(getActivity(), "Log Out !",
                            Toast.LENGTH_LONG).show();
                    getActivity().finish();
                    break;

                case R.id.Delete_button:
                    mAuth.getCurrentUser().delete();
                    mAuth.signOut();
                    Toast.makeText(getActivity(), "Account Deleted !",
                            Toast.LENGTH_LONG).show();
                    getActivity().finish();
                    break;

                case R.id.add_friend:
                    Toast.makeText(getActivity(), "Friends !",
                            Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
    }
}

