package drawrite.booknet;

//Resource : https://codinginflow.com/tutorials/android/custom-dialog-interface
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class SearchDialog extends AppCompatDialogFragment {
    private EditText editTextBookName;
    private SearchDialogListener listener;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_search_dialog,null);

        builder.setView(view)
                .setTitle("Search Book")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String bookName = editTextBookName.getText().toString();
                        listener.applyTexts(bookName);

                    }
                });
        editTextBookName = view.findViewById(R.id.edit_search);

        return builder.create();
        
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (SearchDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    " must implement SearchDialogListener"
            );
        }
    }



    public interface SearchDialogListener{  //listener implemented in the BookDetailActivityTabbed.
        void  applyTexts(String bookName);
    }


}
