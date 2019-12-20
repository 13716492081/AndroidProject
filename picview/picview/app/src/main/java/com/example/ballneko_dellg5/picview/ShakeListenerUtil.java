package com.example.ballneko_dellg5.picview;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.os.Message;

public class ShakeListenerUtil implements SensorEventListener {

    Handler handler;
    private Long oldTime=0L;
    public ShakeListenerUtil(Handler handler) {
        super();
        this.handler=handler;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
        float[] values = event.values;
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            //任一轴的加速度大于17
            if ((Math.abs(values[0]) > 17 )) {      //仅横摇生效
                if (oldTime != 0 && (System.currentTimeMillis() - oldTime < 2000)) {    //距离上一次摇动没有2s，不触发
                    return;
                }
                Message msg=new Message();
                msg.what=1;
                handler.sendMessage(msg);
                oldTime = System.currentTimeMillis();
//                Thread thread=new Thread();
//                thread.start();
//                try {       //等待两秒
//                    thread.join(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
