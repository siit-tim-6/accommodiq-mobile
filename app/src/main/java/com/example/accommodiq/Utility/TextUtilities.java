package com.example.accommodiq.Utility;

import android.widget.TextView;
import androidx.core.text.HtmlCompat;

public class TextUtilities {
    public static void makeTextUnderlined(TextView textView) {
        String text = "<u>" + textView.getText().toString() + "</u>";
        textView.setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }
}
