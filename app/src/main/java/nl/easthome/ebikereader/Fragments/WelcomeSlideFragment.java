package nl.easthome.ebikereader.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.easthome.ebikereader.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeSlideFragment extends Fragment {


    public WelcomeSlideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome_slide, container, false);
    }

}
