package com.example.medhet;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";

    private EditText addTaskText;
    private Button taskSaveButton;
    private ToDoDBHelper db;

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addTaskText = getView().findViewById(R.id.NewTaskText);
        taskSaveButton = getView().findViewById(R.id.addTaskbtn2);

        db = new ToDoDBHelper(getActivity());

        boolean isUpdate = false;
        final Bundle bundle = getArguments();

        if(bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            addTaskText.setText(task);

            if(task.length() > 0) {
                addTaskText.setTextColor(Color.WHITE);
            }
        }

        addTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")) {
                    taskSaveButton.setEnabled(false);
                    taskSaveButton.setTextColor(Color.GRAY);
                }
                else
                {
                    taskSaveButton.setEnabled(true);
                    taskSaveButton.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        boolean finalIsUpdate = isUpdate;
        taskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = addTaskText.getText().toString();

                if(finalIsUpdate){
                    db.updateTask(bundle.getInt("_id"), text);
                }
                else
                {
                    ToDoModel task = new ToDoModel();
                    task.setId(db.getLastID() + 1);
                    task.setTask(text);
                    task.setStatus(0);
                    db.createNewTask(task);
                }

                dismiss();
            }
        });

    }

    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();

        if(activity instanceof DialogCloseListener) {
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
}
