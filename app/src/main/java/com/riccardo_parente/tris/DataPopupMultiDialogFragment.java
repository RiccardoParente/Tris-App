package com.riccardo_parente.tris;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DataPopupMultiDialogFragment extends DialogFragment
{
    
    DialogListener listener;
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.data_insert_popup_multi, null))
                .setPositiveButton(R.string.gioca, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        String nome1 = ((EditText) getDialog().getWindow().findViewById(R.id.nome1)).getText().toString();
                        String nome2 = ((EditText) getDialog().getWindow().findViewById(R.id.nome2)).getText().toString();
                        listener.onDialogPositiveClick(nome1, nome2);
                    }
                })
                .setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        DataPopupMultiDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
    
    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        try
        {
            listener = (DialogListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException();
        }
    }
}
