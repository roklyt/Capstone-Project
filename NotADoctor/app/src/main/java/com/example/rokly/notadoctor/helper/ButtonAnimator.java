package com.example.rokly.notadoctor.helper;

import android.widget.ImageButton;

public class ButtonAnimator {

    public static void imageButtonAnimator(ImageButton imageButton){
        imageButton.animate().setStartDelay(800);
        imageButton.animate().setDuration(500);
        imageButton.animate().alpha(1);
    }

}
