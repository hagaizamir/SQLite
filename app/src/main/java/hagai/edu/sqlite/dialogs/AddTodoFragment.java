package hagai.edu.sqlite.dialogs;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import hagai.edu.sqlite.R;
import hagai.edu.sqlite.models.Todo;
import hagai.edu.sqlite.sqlite.DAO;

/*
*A simple{

@link Fragment} subclass.
        */
public class AddTodoFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    //constants:
    public static final int ACTION_UPDATE = 0;
    public static final int ACTION_INSERT = 1;
    public static final String ARG_TODO = "todo";
    public static final String ARG_ACTION = "action";
    public static final String INTENT_ACTION_UPDATE = "updatedTodo";
    public static final String INTENT_ACTION_INSERT = "insertedTodo";
    public static final String INTENT_ACTION_DELETE = "deleteTodo";

    //private fields:
    private int action;
    private Todo model;

    //Views
    EditText etMission;
    Spinner spImportance; //entries = @array / importance
    Button btnDone;

    public static AddTodoFragment newInstance(int action, Todo model) {
        Bundle args = new Bundle();
        args.putInt(ARG_ACTION, action);
        args.putParcelable(ARG_TODO, model);

        AddTodoFragment fragment = new AddTodoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_todo, container, false);

        etMission = v.findViewById(R.id.etMission);
        spImportance = v.findViewById(R.id.spImportance);
        btnDone = v.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);

        action = getArguments().getInt(ARG_ACTION);
        model = getArguments().getParcelable(ARG_TODO);
        if (action == ACTION_UPDATE)
            updateUI();
        return v;
    }

    private void updateUI() {
        etMission.setText(model.getMission());
        int idx = importanceIndex(model);
        spImportance.setSelection(idx);
    }

    private int importanceIndex(Todo model) {
        //Arrays.sort(importance);
        //Arrays.binarySearch(importance, model.getImportance());
        String[] importance = getResources().getStringArray(R.array.importance);
        int idx = -1;
        for (int i = 0; i < importance.length; i++) {
            if (model.getImportance().equals(importance[i])) {
                idx = i;
                break;
            }
        }
        return idx;
    }

    @Override
    public void onClick(View view) {
        String mission = etMission.getText().toString();
        String importance = spImportance.getSelectedItem().toString();

        // A IS B
        // LSP

        //spinner -> selected
        DAO dao = DAO.getInstance(getContext());

        long todoID = 0;
        String intentAction = "";
        switch (action) {
            case ACTION_INSERT:
                todoID = dao.addTodo(mission, importance);
                intentAction = INTENT_ACTION_INSERT;
                break;
            case ACTION_UPDATE:
                todoID = model.getId();
                dao.updateTodo(todoID, mission, importance);
                intentAction = INTENT_ACTION_UPDATE;
                break;
        }

        //Notify the listeners:
        Todo todo = new Todo((int) todoID, mission, importance);
        Intent intent = new Intent(intentAction);
        intent.putExtra("todo", todo);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

        dismiss();
    }
}
