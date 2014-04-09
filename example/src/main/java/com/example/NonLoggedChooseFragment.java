package com.example;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yoman on 06.11.2013.
 */
public class NonLoggedChooseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_choose_login, container, false);

        rootView.findViewById(R.id.chooseActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseLoginAction();
            }
        });

        rootView.findViewById(R.id.facebook_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginButtonClicked();
            }
        });

        return rootView;
    }


    private void onLoginButtonClicked() {
        final NonLoggedActivity activity = (NonLoggedActivity) getActivity();
        activity.onLoginButtonClicked();
    }




    private void chooseLoginAction() {
        CharSequence loginOptions[] = new CharSequence[] {getString(R.string.login_text), getString(R.string.register_text)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose login option");
        builder.setItems(loginOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        loginAction();
                        break;
                    case 1:
                        registerAction();
                        break;
                }
            }


        });
        builder.show();
    }

    private void registerAction() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, new NonLoggedRegisterFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void loginAction() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, new NonLoggedLoginFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}