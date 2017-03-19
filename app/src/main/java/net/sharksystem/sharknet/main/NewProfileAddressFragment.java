package net.sharksystem.sharknet.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.sharknet.R;

/**
 * Created by j4rvis on 3/18/17.
 */

public class NewProfileAddressFragment extends Fragment implements View.OnClickListener {

    public interface NewProfileAddressFragmentButtonListener {
        void onCreateProfile();
        void onBackToNewProfileFragment();
    }

    NewProfileAddressFragmentButtonListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mListener = (NewProfileAddressFragmentButtonListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                + " must implement NewProfileAddressFragmentButtonListener.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_new_profile_address_fragment, container, false);

        view.findViewById(R.id.next_fragment).setOnClickListener(this);
        view.findViewById(R.id.previous_fragment).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next_fragment:
                mListener.onCreateProfile();
                break;
            case R.id.previous_fragment:
                mListener.onBackToNewProfileFragment();
                break;
            default:
                break;
        }
    }
}
