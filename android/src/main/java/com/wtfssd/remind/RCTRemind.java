package com.wtfssd.remind;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;

import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.util.Log;


import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by wtfssd on 2018/1/11.
 */

public class RCTRemind extends ReactContextBaseJavaModule {


    private static final String TAG = "remind";
    private static final String kTitle ="title";
    private static final String kLocation ="location";
    private static final String kStartDate ="startDate";
    private static final String kEndDate ="endDate";
    private static final String kAllDay ="allDay";
    private static final String kURL="URL";
    private static final String kNotes="notes";
    private static  String CALANDER_URL = "content://com.android.calendar/calendars";
    private static  String CALANDER_EVENT_URL = "content://com.android.calendar/events";
    private static  String CALANDER_REMIDER_URL = "content://com.android.calendar/reminders";

    private Promise promise;

    private Runnable runnable;

    public static final int DATE_TYPE = 1;
    public static final int TIME_TYPE = 2;
    public static final int TIME_STAMP = 3;
    public static final int DATE_TIME_TYPE = 4;
    public RCTRemind(ReactApplicationContext reactContext) {
        super(reactContext);
        Log.i("init","init");
    }

    @Override
    public String getName() {
        return "RCTRemind";
    }


    @ReactMethod
    public void addEvent(ReadableMap params, Promise promise){
        this.promise = promise;
        try {

            boolean pre = true;
            pre = ContextCompat.checkSelfPermission(getReactApplicationContext(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_DENIED;
            pre = ContextCompat.checkSelfPermission(getReactApplicationContext(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_DENIED;

            if(!pre){
                promise.reject("403","ALENDAR Permission DENIED");
                return;
            }

            final Intent intent = new Intent(Intent.ACTION_INSERT);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            intent.setData(CalendarContract.Events.CONTENT_URI);

            if(params.getString(kTitle)!=null){
                intent.putExtra(CalendarContract.Events.TITLE, params.getString(kTitle));
            }
            if(params.hasKey(kNotes)){
                intent.putExtra(CalendarContract.Events.DESCRIPTION, params.getString(kNotes));
            }
            if(params.hasKey(kLocation)){
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, params.getString(kLocation));
            }
            if(params.hasKey(kStartDate)){

                try {
                    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, Long.valueOf(findTime(params.getString(kStartDate),TIME_STAMP)));
                }catch (ParseException e){
                  Log.e(TAG,e.toString());
                }
            }
            if(params.hasKey(kEndDate)){
                try {
                    intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, Long.valueOf(findTime(params.getString(kEndDate),TIME_STAMP)));
                }catch (ParseException e){
                    Log.e(TAG,e.toString());
                }
            }
            if(params.hasKey(kAllDay)){
                intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, Long.valueOf(params.getString(kAllDay)));
            }
            if(params.hasKey(kURL)){
                intent.putExtra(CalendarContract.EXTRA_CUSTOM_APP_URI, Long.valueOf(params.getString(kURL)));
            }


            runnable = new Runnable() {
                @Override
                public void run() {
                    getReactApplicationContext().startActivity(intent);
                }
            };

            getReactApplicationContext().runOnUiQueueThread(runnable);



//            final Message message = new Message();
//            message.obj = intent;
//
//            handler = new Handler(){
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//
//                }
//            };
//
//
//         handler.post(runnable);
            Log.i(TAG,params.toString());
        }catch (Exception e){
            Log.e(TAG,e.toString());
           this.promise.reject("400","添加失败");
        }
    }

    public static String findTime(String serverDate, int Type) throws ParseException {
        String result = "";
        serverDate = serverDate.replace("Z", " UTC");//注意是空格+UTC
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
        Date d = format.parse(serverDate);

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String date = format1.format(d);
        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
        String time = format2.format(d);

        SimpleDateFormat format3 = new SimpleDateFormat("yy-MM-dd HH:mm");
        String dateAndTime = format3.format(d);
        switch (Type) {
            case DATE_TYPE:
                result = date;
                break;
            case TIME_TYPE:
                result = time;
                break;
            case TIME_STAMP:
                result = String.valueOf(d.getTime());
                break;
            case DATE_TIME_TYPE:
                result = dateAndTime;
            default:
                break;
        }
        return result;
    }

}
