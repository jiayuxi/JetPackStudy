package com.jiayx.databinding.util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import androidx.databinding.BindingConversion;

import org.jetbrains.annotations.NotNull;

/**
 * Created by yuxi_
 * on 2022/5/1
 * java 类实现
 */
public class ColorConversion {

    @BindingConversion
    public static Drawable convertStringToDrawable(@NotNull String str) {
        switch (str) {
            case "红色":
                return new ColorDrawable(Color.parseColor("#FF4081"));
            case "蓝色":
                return new ColorDrawable(Color.parseColor("#3F51B5"));
            default:
                return new ColorDrawable(Color.parseColor("#344567"));
        }
    }

    @BindingConversion
    public static int convertStringToColor(@NotNull String str) {
        switch (str) {
            case "红色":
                return Color.parseColor("#FF4081");
            case "蓝色":
                return Color.parseColor("#3F51B5");
            default:
                return Color.parseColor("#344567");
        }
    }
}
