package com.cemilyesil.bilgesgt.mesajlistesi;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class Mesaj_Listesi extends AppCompatActivity {

    Mesaj_adaptor Mesaj_adaptor;
    RecyclerView mesaj_listview;
    GridLayoutManager mLayoutManager;
    public static ArrayList<HashMap<String,String>> mesaj_Listesi=new ArrayList<HashMap<String,String>>();

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesaj_listesi);

        mesaj_listview = (RecyclerView)findViewById(R.id.mesaj_listview);
        mesaj_listview.setHasFixedSize(true);
        //  LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mLayoutManager = new GridLayoutManager(this, 1);
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mesaj_listview.setLayoutManager(mLayoutManager);
        mesaj_listview.setItemAnimator(new DefaultItemAnimator());
        Mesaj_adaptor = new Mesaj_adaptor(this, mesaj_Listesi);

        mesaj_listview.setAdapter(Mesaj_adaptor);

        insertDummyContactWrapper();

    }

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void insertDummyContactWrapper() {
        int READ_SMS = checkSelfPermission(Manifest.permission.READ_SMS);
        int Reciver_SMS = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
        int SEND_SMS = checkSelfPermission(Manifest.permission.SEND_SMS);

        if (Reciver_SMS != PackageManager.PERMISSION_GRANTED   ) {
            requestPermissions(new String[] {Manifest.permission.SEND_SMS,Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }

        getAllSms();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    getAllSms();
                } else {
                    // Permission Denied
                    Toast.makeText(Mesaj_Listesi.this, "Mesaj Listesi Okuması İçin İzin Verilmedi", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    int totalSMS;
    void getAllSms() {

        HashMap<String, String> objSms;
        //mcontext.startManagingCursor(c);


        Uri uriSMSURI = Uri.parse("content://sms/");
        Cursor c = getContentResolver().query(uriSMSURI, null, null, null, null);
        totalSMS = c.getCount();
        while (c != null && c.moveToNext()) {

                objSms = new HashMap<String, String>();
                objSms.put("displayName",c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.put("address",c.getString(c.getColumnIndexOrThrow("address")));
                objSms.put("msg",c.getString(c.getColumnIndexOrThrow("body")));
                objSms.put("threadId",c.getString(c.getColumnIndex("read")));
                objSms.put("date",c.getString(c.getColumnIndexOrThrow("date")));
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.put("type","inbox");
                } else {
                    objSms.put("type","sent");
                }

                mesaj_Listesi.add(objSms);

                c.moveToNext();

        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close();
        Mesaj_adaptor.notifyDataSetChanged();
        Log.e("apps",mesaj_Listesi.size()+"");

    }
    public class Mesaj_adaptor extends RecyclerView.Adapter<Mesaj_adaptor.MyViewHolder> {

        private ArrayList<HashMap<String, String>> moviesList;


        public class MyViewHolder extends RecyclerView.ViewHolder {

            RecyclerView urunlistView;
            TextView baslik;
            TextView detay;
            public MyViewHolder(View view) {
                super(view);
                baslik = (TextView) view.findViewById(R.id.baslik);
                detay = (TextView) view.findViewById(R.id.detay);

            }
        }


        private Context mContext;
        public Mesaj_adaptor(Context  wop_liste, ArrayList<HashMap<String, String>> moviesList) {
            this.moviesList = moviesList;

            this.mContext=wop_liste;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.content_mesaj_listesi_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            try {
                final HashMap<String, String> tempMap = moviesList.get(position);

                holder.baslik.setText(tempMap.get("address"));
                holder.detay.setText(tempMap.get("msg"));



                // UrunAdaptorum.notifyDataSetChanged();

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }




    }
}
