package com.frank_ghost.hotfix;

import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyStyle", "MainActivity.onCreate.路径" + Environment.getExternalStorageDirectory().getAbsolutePath());
        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());
        DexManager.getInstance().setContext(MainActivity.this);
        Log.e("MyStyle", "MainActivity.onCreate.API=" + Build.VERSION.SDK_INT);
    }

    public void jisuan(View v) {
        Caclutor caclutor = new Caclutor();
        caclutor.caculator();
        Log.e("MyStyle", "MainActivity.jisuan.结果:" + caclutor.caculator());
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public void fix(View view) {
        Log.e("MyStyle", "MainActivity.fix.路径" + Environment.getExternalStorageDirectory().getAbsolutePath());
        DexManager.getInstance().loadFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/out.dex"));
    }
}
