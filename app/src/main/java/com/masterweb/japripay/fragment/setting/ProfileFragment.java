package com.masterweb.japripay.fragment.setting;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdView;
import com.masterweb.japripay.R;
import com.masterweb.japripay.classes.AdMob;
import com.masterweb.japripay.classes.request.post.Members;


public class ProfileFragment extends Fragment {
    Members members;
    EditText name,phone,address;
    Button submit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        members = new Members(getContext(),getActivity());
        name = root.findViewById(R.id.name);
        phone = root.findViewById(R.id.phone);
        address = root.findViewById(R.id.address);
        submit = root.findViewById(R.id.submit);
        members.Profile(phone,name,address);
        submit.setOnClickListener(V->{
            members.UpdateProfile(phone,name,address);
        });
        AdView mAdView = root.findViewById(R.id.adView);
        AdMob adMob = new AdMob(getContext());
        adMob.load(mAdView);
        return root;
    }
}