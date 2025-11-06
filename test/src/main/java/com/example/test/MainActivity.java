package com.example.test;



import static android.os.Build.VERSION.SDK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.util.CloseGuard;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;


//import com.swift.sandhook.xposedcompat.XposedCompat;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;


import com.example.test3.R;

import net.security.device.api.SecurityDevice;
import net.security.device.api.SecurityInitListener;

import org.lsposed.hiddenapibypass.HiddenApiBypass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

//import de.robv.android.xposed.XC_MethodHook;
//import de.robv.android.xposed.XposedHelpers;


public class MainActivity extends Activity {

    static {
        System.loadLibrary("testQC");
    }

    static class MyClass {
        int a;
        int b;
        public MyClass(int a,int b){
            this.a =a;
            this.b =b;
        }

        @NonNull
        @Override
        public String toString() {
            return "MyClass{" +
                    "a=" + a +
                    ", b=" + b +
                    '}';
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyClass clazz1 = new MyClass(111, 222);
        MyClass clazz2 = new MyClass(444, 555);
        MyClass clazz3 = new MyClass(777, 888);
        ArrayList<MyClass> choose = ChooseUtils.choose(MyClass.class);
        Log.e("Zhenxi",Build.VERSION.SDK_INT+" "+choose.toString());
    }


}