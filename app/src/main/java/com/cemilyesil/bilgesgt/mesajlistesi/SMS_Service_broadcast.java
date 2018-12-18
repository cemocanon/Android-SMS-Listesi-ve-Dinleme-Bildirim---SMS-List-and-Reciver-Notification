package com.cemilyesil.bilgesgt.mesajlistesi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMS_Service_broadcast extends BroadcastReceiver
{

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
            try {

                if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
                    Bundle bundle = intent.getExtras();

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);


                    // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                            "senderNum: "+ senderNum + ", message: " + message, duration);
                    toast.show();
                    if(intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
                        int NOTIFICATION=R.string.app_name;
                        PendingIntent contentIntent =
                                PendingIntent.getActivity(context, 0, new Intent(context, Mesaj_Listesi.class), 0);

                        //notification creating
                        Notification.Builder notificationBuilder = new Notification.Builder(context)
                                .setContentText( senderNum )
                                .setContentTitle(message)

                                .setSmallIcon(R.mipmap.ic_launcher);
                        Notification note = notificationBuilder.build();
                        note.contentIntent = contentIntent;
                        //getting system service
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                        //displaying notification
                        notificationManager.notify(NOTIFICATION, note);
                     }

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}