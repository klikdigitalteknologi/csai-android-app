package id.klikdigital.csaiapp.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.klikdigital.csaiapp.R;

public class HeaderLayout extends LinearLayout {

    public HeaderLayout(Context context, AttributeSet attrs) {
        super(context,attrs);
        inflate(context, R.layout.nav_header,this);

    }

}
