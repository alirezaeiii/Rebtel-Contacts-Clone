package com.sample.android.contact.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sample.android.contact.R;

public class DialpadFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private TextView digits;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dialpad, container, false);
        root.findViewById(R.id.button1).setOnClickListener(this);
        root.findViewById(R.id.button2).setOnClickListener(this);
        root.findViewById(R.id.button3).setOnClickListener(this);
        root.findViewById(R.id.button4).setOnClickListener(this);
        root.findViewById(R.id.button5).setOnClickListener(this);
        root.findViewById(R.id.button6).setOnClickListener(this);
        root.findViewById(R.id.button7).setOnClickListener(this);
        root.findViewById(R.id.button8).setOnClickListener(this);
        root.findViewById(R.id.button9).setOnClickListener(this);
        root.findViewById(R.id.button10).setOnClickListener(this);
        root.findViewById(R.id.button12).setOnClickListener(this);
        View button11 = root.findViewById(R.id.button11);
        button11.setOnClickListener(this);
        button11.setOnLongClickListener(this);
        View deleteButton = root.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);
        deleteButton.setOnLongClickListener(this);
        digits = root.findViewById(R.id.digits);

        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                digits.append("1");
                break;
            case R.id.button2:
                digits.append("2");
                break;
            case R.id.button3:
                digits.append("3");
                break;
            case R.id.button4:
                digits.append("4");
                break;
            case R.id.button5:
                digits.append("5");
                break;
            case R.id.button6:
                digits.append("6");
                break;
            case R.id.button7:
                digits.append("7");
                break;
            case R.id.button8:
                digits.append("8");
                break;
            case R.id.button9:
                digits.append("9");
                break;
            case R.id.button10:
                digits.append("*");
                break;
            case R.id.button11:
                digits.append("0");
                break;
            case R.id.button12:
                digits.append("#");
                break;
            case R.id.deleteButton:
                String input = digits.getText().toString();
                if (!input.isEmpty()) {
                    input = input.substring(0, input.length() - 1);
                    digits.setText(input);
                }
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.button11:
                digits.append("+");
                break;
            case R.id.deleteButton:
                digits.setText("");
                break;
        }
        return true;
    }
}
