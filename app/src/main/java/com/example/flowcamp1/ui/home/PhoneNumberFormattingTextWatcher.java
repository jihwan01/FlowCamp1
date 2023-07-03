package com.example.flowcamp1.ui.home;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class PhoneNumberFormattingTextWatcher implements TextWatcher {
    private EditText editText;
    private boolean isFormatting;

    public PhoneNumberFormattingTextWatcher(EditText editText) {
        this.editText = editText;
        isFormatting = false;
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (isFormatting) {
            return;
        }

        isFormatting = true;

        // 전화번호 형식 적용
        formatPhoneNumber(editable);

        isFormatting = false;
    }

    private void formatPhoneNumber(Editable editable) {
        String input = editable.toString();
        String newNum = "";
        // 숫자만 추출
        String digits = input.replaceAll("\\D", "");
        if(digits.length() >= 8){
            newNum = digits.substring(0,3) + "-" + digits.substring(3, 7) + "-" + digits.substring(7);
        }else if(digits.length() >=4){
            newNum = digits.substring(0,3) + "-" + digits.substring(3);
        }else {
            newNum = digits;
        }

        // 변환된 텍스트를 EditText에 설정
        editText.setText(newNum.toString());
        editText.setSelection(newNum.length()); // 커서 위치 설정

    }
}
