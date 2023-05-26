package com.chamodex.enterprises.loano.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chamodex.enterprises.loano.LoginActivity;
import com.chamodex.enterprises.loano.MainActivity;
import com.chamodex.enterprises.loano.R;
import com.chamodex.enterprises.loano.utils.OnFragmentInteractionListener;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {
    FirebaseAuth auth;
    CardView cVMyProfile, cVMyInstallment, cVContactUs, cVAbout, cVLogout;
    private OnFragmentInteractionListener fragmentInteractionListener;
    public HomeFragment(FirebaseAuth auth) {
        // Required empty public constructor
        this.auth = auth;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // content here
        initializeVariables(view);
        navigate(view);
        // content end
        return view;
    }

    private void navigate(View view) {
        cVMyProfile.setOnClickListener(v -> {
            fragmentInteractionListener.changeFragment(R.id.profile_detail);
        });
        cVMyInstallment.setOnClickListener(v -> {
            fragmentInteractionListener.changeFragment(R.id.installment);
        });
        cVContactUs.setOnClickListener(v -> {
            fragmentInteractionListener.changeFragment(R.id.support);
        });
        cVAbout.setOnClickListener(v -> {});
        cVLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent login = new Intent(getActivity(), LoginActivity.class);
            startActivity(login);
            getActivity().finish();
        });
    }

    private void initializeVariables(View view) {
        cVMyProfile = view.findViewById(R.id.cViewMyProfile);
        cVMyInstallment = view.findViewById(R.id.cViewMyInstallments);
        cVContactUs = view.findViewById(R.id.cViewContactUs);
        cVAbout = view.findViewById(R.id.cViewAbout);
        cVLogout = view.findViewById(R.id.cViewLogout);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            fragmentInteractionListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}