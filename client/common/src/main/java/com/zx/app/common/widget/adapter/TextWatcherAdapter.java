package com.zx.app.common.widget.adapter;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * author Afton
 * date 2020/3/7
 */
public abstract class TextWatcherAdapter implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
