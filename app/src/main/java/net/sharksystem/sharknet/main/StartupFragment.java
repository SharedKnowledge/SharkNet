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

public class StartupFragment extends Fragment implements View.OnClickListener {

    private View mPreviousAccount;

    public interface StartupFragmentButtonListener{
        void onCreateNewProfileSelected();
        void onCreateDummyDataSelected();
        void onUsePreviousProfileSelected();
    }

    StartupFragmentButtonListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mListener = (StartupFragmentButtonListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                + " must implement StartupFragmentButtonListener.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_startup_fragment, container, false);

//        view.findViewById(R.id.button_create_dummy_data).setOnClickListener(this);
        mPreviousAccount = view.findViewById(R.id.button_use_previous_profile);
        mPreviousAccount.setOnClickListener(this);
        mPreviousAccount.setVisibility(View.GONE);
        view.findViewById(R.id.button_create_new_profile).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.button_create_dummy_data:
//                mListener.onCreateDummyDataSelected();
//                break;
            case R.id.button_use_previous_profile:
                mListener.onUsePreviousProfileSelected();
                break;
            case R.id.button_create_new_profile:
                mListener.onCreateNewProfileSelected();
                break;
            default:
                break;
        }
    }
    public void showOldUSerButton(){
        mPreviousAccount.setVisibility(View.VISIBLE);
    }
}
