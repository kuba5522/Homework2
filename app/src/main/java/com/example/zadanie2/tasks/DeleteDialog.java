package com.example.zadanie2.tasks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import com.example.zadanie2.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnDeleteDialogInteractionListener} interface
 * to handle interaction events.
 */
public class DeleteDialog extends DialogFragment {

    private OnDeleteDialogInteractionListener mListener;

    public DeleteDialog() {
    }
    public static DeleteDialog newInstance(){
        return new DeleteDialog();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach ( context );
        if (context instanceof OnDeleteDialogInteractionListener) {
            mListener = ( OnDeleteDialogInteractionListener ) context;
        } else {
            throw new RuntimeException ( context.toString ()
                    + " must implement OnDeleteDialogInteractionListener" );
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder ( getActivity () );

        builder.setMessage ( getString ( R.string.delete_question ) );   //??????

        builder.setPositiveButton ( getString ( R.string.dialog_confirm ), new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogPositiveClick ( DeleteDialog.this);
            }
        } );
        builder.setNegativeButton ( getString ( R.string.dialog_cancel ), new DialogInterface.OnClickListener () {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogNegativeClick ( DeleteDialog.this);
            }
        } );
    return builder.create ();
    }


    @Override
    public void onDetach() {
        super.onDetach ();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDeleteDialogInteractionListener {
        // TODO: Update argument type and name
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);

    }
}
