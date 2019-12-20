package com.example.ballneko_dellg5.picview;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;

public class ShowActivity extends AppCompatActivity {
    private Intent intent;
    private Bundle bundle;
    ImageView imageView;
    SensorManager mSensorManager;
//    public long xs;
//    public class Adapter_X extends BaseAdapter{
//        private Context mContext;
//        public Adapter_X(Context c){
//            mContext = c;
//        }
//        public boolean areAllItemsEnabled() {
//            return true;
//        }
//        public boolean isEnabled(int position) {
//            return true;
//        }
//        public long getItemId(int a)
//        {
//            return 0;
//        }
//        public Object getItem(int position)
//        {
//            return null;
//        }
//        public int getCount(){
//            return 1;
//        }
//        public View getView(int position, View convertView, ViewGroup parent) {
//            position = 1;
//            ImageView imageView;
//            imageView = new ImageView(mContext);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setLayoutParams(new GridView.LayoutParams(1300,1300));
//            imageView.setImageResource((int)xs);
//            return imageView;
//        }
//        public int getItemViewType(int position) {
//            return 0;
//        }
//        public int getViewTypeCount() {
//            return 1;
//        }
//        public boolean isEmpty() {
//            return getCount() == 0;
//        }
//    }
    ShakeListenerUtil shakeListenerUtil;
    private Handler shake_handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    finish();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_pic);
        intent = getIntent();
        bundle = intent.getExtras();
        imageView=findViewById(R.id.shown);
        ByteArrayInputStream in=new ByteArrayInputStream(bundle.getByteArray("pic"));
        Bitmap map=BitmapFactory.decodeStream(in);
        imageView.setMaxHeight(map.getHeight());
        imageView.setMaxWidth(map.getWidth());
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageBitmap(map);

        Button button=findViewById(R.id.paint_black);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorMatrix col=new ColorMatrix();
                col.setSaturation(0);
                ColorMatrixColorFilter cm=new ColorMatrixColorFilter(col);
                Log.i("转黑白","");
                imageView.setColorFilter(cm);
            }
        });
        Button button1=findViewById(R.id.paint_back);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setColorFilter(null);
            }
        });
//        xs = bundle.getLong("names");
//        GridView gridview = (GridView)findViewById(R.id.gridViewx);
//        gridview.setAdapter(new Adapter_X(this));

        //摇动感应
        shakeListenerUtil=new ShakeListenerUtil(shake_handler);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //获取传感器管理服务
        mSensorManager = (SensorManager) this
                .getSystemService(Service.SENSOR_SERVICE);
        //加速度传感器
        mSensorManager.registerListener(shakeListenerUtil,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                //还有SENSOR_DELAY_UI、SENSOR_DELAY_FASTEST、SENSOR_DELAY_GAME等，
                //根据不同应用，需要的反应速率不同，具体根据实际情况设定
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(shakeListenerUtil);
        super.onPause();
    }
}
