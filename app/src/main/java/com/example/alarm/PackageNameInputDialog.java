package com.example.alarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class PackageNameInputDialog extends AppCompatDialogFragment {
    private EditText packageNameInput;
    private PackageNameInputDialogListener listner;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.package_desc_add_dialog, null);

        builder.setView(view)
                .setTitle("New Package Name")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String packageName = packageNameInput.getText().toString();
                        listner.applyInputText(packageName);

                    }
                });
        packageNameInput = view.findViewById(R.id.packageDescInput);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listner = (PackageNameInputDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement PackageNameInputDialogListener");
        }
    }

    public interface PackageNameInputDialogListener {
        void applyInputText(String packageName);
    }

}
