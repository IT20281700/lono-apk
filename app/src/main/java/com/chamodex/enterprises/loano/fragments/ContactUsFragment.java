package com.chamodex.enterprises.loano.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chamodex.enterprises.loano.R;
import com.google.firebase.auth.FirebaseAuth;

public class ContactUsFragment extends Fragment {
    FirebaseAuth auth;
    public ContactUsFragment(FirebaseAuth auth) {
        // Required empty public constructor
        this.auth = auth;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        // content here
        TextView testUserMail = view.findViewById(R.id.user_details);
        testUserMail.setText(auth.getCurrentUser().getEmail());
        // content end
        return view;
    }
}