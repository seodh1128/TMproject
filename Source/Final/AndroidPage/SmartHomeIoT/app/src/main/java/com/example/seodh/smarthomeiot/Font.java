package com.example.seodh.smarthomeiot;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by student on 2017-12-13.
 */

public class Font extends AppCompatActivity{

    public static void setGlobalFont(Context context, View view){
        if (view != null) {
            if (view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                int len = vg.getChildCount();
                for (int i = 0; i < len; i++) {
                    View v = vg.getChildAt(i);
                    if (v instanceof TextView) {
                        ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "font.ttf"));
                    }
                    setGlobalFont(context, v);
                }
            }
        }
    }



        /*private static Typeface typeface;


        @Override
        public void setContentView(int layoutResID) {
            super.setContentView(layoutResID);
            if(typeface == null) {
                typeface = Typeface.createFromAsset(this.getAssets(), "font.ttf");
            }
            setGlobalFont(getWindow().getDecorView());
        }

        private void setGlobalFont(View view) {
            if(view != null) {
                if(view instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup)view;
                    int vgCnt = viewGroup.getChildCount();
                    for(int i = 0; i<vgCnt; i++) {
                        View v = viewGroup.getChildAt(i);
                        if(v instanceof TextView) {
                            ((TextView) v).setTypeface(typeface);
                        }
                        setGlobalFont(v);
                    }
                }
            }
        }*/
}
