package hagai.edu.sqlite.dialogs;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import hagai.edu.sqlite.R;
import hagai.edu.sqlite.TodosDBHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTodoFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    EditText etMission;
    Spinner spImportance;
    Button btnDone;


    public AddTodoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_add_todo, container, false);
        etMission = v.findViewById(R.id.etMission);
        spImportance = v.findViewById(R.id.spImportance);
        btnDone = v.findViewById(R.id.btDone);
        btnDone.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        String mission = etMission.getText().toString();
        String importance = spImportance.getSelectedItem().toString();

        //spinner -> selected
        TodosDBHelper helper = new TodosDBHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        db.execSQL("INSERT INTO Todos (mission , importance)" +
                " VALUES (" + mission + " ," + importance + "');");
        dismiss();
    }
}
