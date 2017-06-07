package net.sharksystem.sharknet.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import net.sharkfw.system.L;
import net.sharksystem.sharknet.R;

import java.io.IOException;

/**
 * Created by j4rvis on 3/18/17.
 */

public class NewProfileFragment extends Fragment implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 1777;
    NewProfileFragmentButtonListener mListener;
    private ImageView mProfileImage;
    private EditText mProfileName;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (NewProfileFragmentButtonListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement NewProfileAddressFragmentButtonListener.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_new_account_name_fragment, container, false);

        // Buttons
        view.findViewById(R.id.next_fragment).setOnClickListener(this);
        view.findViewById(R.id.previous_fragment).setOnClickListener(this);

        mProfileImage = (ImageView) view.findViewById(R.id.profile_image);
        mProfileName = (EditText) view.findViewById(R.id.profile_name);

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
            }
        });

        return view;
    }

    public interface NewProfileFragmentButtonListener {
        void onNextFragment();

        void onBackToStartupFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        L.d("onActivityResult called", this);

        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                ((MainActivity) getActivity()).mContactImage = bitmap;
                mProfileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_fragment:
                if (mProfileName.getText().toString().isEmpty()) {
                    Snackbar.make(v, R.string.main_new_profile_empty_name, Snackbar.LENGTH_SHORT).show();
                } else {
                    ((MainActivity) getActivity()).mContactName = mProfileName.getText().toString();
                    mListener.onNextFragment();
                }
                break;
            case R.id.previous_fragment:
                mListener.onBackToStartupFragment();
                break;
            default:
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        L.d("Stop called", this);
    }
}
