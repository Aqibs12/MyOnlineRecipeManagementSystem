package com.example.fitrecipes.Util;

import android.text.TextUtils;
import android.widget.EditText;


public class ValidationChecks
{
    public boolean validateAnyName(EditText editText, String message) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError(message);
            return false;
        } else {
            return true;
        }

    }
    public boolean validateEmail(EditText editText, String message) {
        String email = editText.getText().toString().trim();

        if (!isValidEmail(email)) {
            editText.setError("please enter valid email");
            return false;
        } else {
            return true;
        }

    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean validatePasswordLenght(EditText editText, String message)
    {
        String password = editText.getText().toString().trim();
        if (password.length()<6) {
            editText.setError("Password Lenght Must Me Be More Thank 6 Charachter");
            return false;
        } else {
            return true;
        }
    }


}
