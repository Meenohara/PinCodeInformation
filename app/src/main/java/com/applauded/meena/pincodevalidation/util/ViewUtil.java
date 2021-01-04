package com.applauded.meena.pincodevalidation.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public final class ViewUtil {

    private ViewUtil() {
        throw new AssertionError("ViewUtil object not allowed");
    }

    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
