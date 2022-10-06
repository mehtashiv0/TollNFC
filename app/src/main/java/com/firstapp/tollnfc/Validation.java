package com.firstapp.tollnfc;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
	static int i;
	static int j;

	// validation for email id
	static boolean email_Validation(EditText editText) {
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		if (editText.getText().toString().matches("")) {
			return false;
		} else {
			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(editText.getText().toString());
			return matcher.matches();
		}

	}

	public static boolean nullValidator(EditText editText) {
		if (editText.getText().toString().matches("")) {
			return false;
		} else {
			return true;
		}
	}

	// validation for username not allow space and accept only character
	static boolean usernamevalidator(EditText editText) {
		final String usernamepattern = "^[\\S][a-zA-Z]+";
		if (editText.getText().toString().matches("")) {
			return false;
		} else {
			Pattern pattern1 = Pattern.compile(usernamepattern);
			Matcher matcher = pattern1.matcher(editText.getText().toString());
			return matcher.matches();
		}
	}


	static boolean namevalidator(EditText editText) {
		final String usernamepattern = "^[a-zA-Z]+";
		if (editText.getText().toString().matches("")) {
			return false;
		} else {
			Pattern pattern1 = Pattern.compile(usernamepattern);
			Matcher matcher = pattern1.matcher(editText.getText().toString());
			return matcher.matches();
		}
	}
	static boolean phoneValidator(EditText editText) {

		final String PHONENO_PATTERN = "^[789]\\d{9}$";
		if (editText.getText().toString().matches("")) {
			return false;
		} else {
			Pattern pattern = Pattern.compile(PHONENO_PATTERN);
			Matcher matcher = pattern.matcher(editText.getText().toString());
			return matcher.matches();
		}
	}

	static boolean pincodeValidator(EditText editText) {

		final String PINCODE_PATTERN = "^\\d{6}$";
		if (editText.getText().toString().matches("")) {
			return false;
		} else {
			Pattern pattern = Pattern.compile(PINCODE_PATTERN);
			Matcher matcher = pattern.matcher(editText.getText().toString());
			return matcher.matches();
		}
	}

	static boolean numberValidator(EditText editText) {

		final String NUMBER_PATTERN = "[0-9]+";
		if (editText.getText().toString().matches("")) {
			return false;
		} else {
			Pattern pattern = Pattern.compile(NUMBER_PATTERN);
			Matcher matcher = pattern.matcher(editText.getText().toString());
			return matcher.matches();
		}
	}

	static void clearAllEditText(ViewGroup root) {
		try {
			j = root.getChildCount();
			for (i = 0; i < j; i++) {
				View view = root.getChildAt(i);
				if (view instanceof EditText) {
					((EditText) view).setText("");
				}
			}
		} catch (Exception e) {
			System.out.println("Error:" + e.toString());
		}
	}

	static boolean spinnerValidator(String currentValue, String defaultValue){
		if(!currentValue.equalsIgnoreCase(defaultValue)){
			return true;
		}
		return false;
	}


	static boolean passwordValidator(EditText editText) {

		final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";;
		if (editText.getText().toString().matches("")) {
			return false;
		} else {
			Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
			Matcher matcher = pattern.matcher(editText.getText().toString());
			return matcher.matches();
		}
	}
}
