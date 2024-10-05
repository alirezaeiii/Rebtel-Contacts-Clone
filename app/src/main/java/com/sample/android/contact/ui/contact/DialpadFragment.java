package com.sample.android.contact.ui.contact;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sample.android.contact.R;
import com.sample.android.contact.databinding.FragmentDialpadBinding;

public class DialpadFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private FragmentDialpadBinding mBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentDialpadBinding.inflate(inflater, container, false);
        mBinding.button1.setOnClickListener(this);
        mBinding.button2.setOnClickListener(this);
        mBinding.button3.setOnClickListener(this);
        mBinding.button4.setOnClickListener(this);
        mBinding.button5.setOnClickListener(this);
        mBinding.button6.setOnClickListener(this);
        mBinding.button7.setOnClickListener(this);
        mBinding.button8.setOnClickListener(this);
        mBinding.button9.setOnClickListener(this);
        mBinding.button10.setOnClickListener(this);
        mBinding.button12.setOnClickListener(this);
        mBinding.button11.setOnClickListener(this);
        mBinding.button11.setOnLongClickListener(this);

        mBinding.display.deleteButton.setOnClickListener(this);
        mBinding.display.deleteButton.setOnLongClickListener(this);
        mBinding.display.digits.setShowSoftInputOnFocus(false);
        mBinding.display.digits.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        return mBinding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                mBinding.display.digits.append("1");
                break;
            case R.id.button2:
                mBinding.display.digits.append("2");
                break;
            case R.id.button3:
                mBinding.display.digits.append("3");
                break;
            case R.id.button4:
                mBinding.display.digits.append("4");
                break;
            case R.id.button5:
                mBinding.display.digits.append("5");
                break;
            case R.id.button6:
                mBinding.display.digits.append("6");
                break;
            case R.id.button7:
                mBinding.display.digits.append("7");
                break;
            case R.id.button8:
                mBinding.display.digits.append("8");
                break;
            case R.id.button9:
                mBinding.display.digits.append("9");
                break;
            case R.id.button10:
                mBinding.display.digits.append("*");
                break;
            case R.id.button11:
                mBinding.display.digits.append("0");
                break;
            case R.id.button12:
                mBinding.display.digits.append("#");
                break;
            case R.id.deleteButton:
                String input = mBinding.display.digits.getText().toString();
                if (!input.isEmpty()) {
                    input = input.substring(0, input.length() - 1);
                    mBinding.display.digits.setText(input);
                }
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.button11:
                mBinding.display.digits.append("+");
                break;
            case R.id.deleteButton:
                mBinding.display.digits.setText("");
                break;
        }
        return true;
    }
}
