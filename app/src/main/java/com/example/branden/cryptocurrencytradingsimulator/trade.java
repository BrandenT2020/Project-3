package com.example.branden.cryptocurrencytradingsimulator;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.database.Cursor;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.GraphView;
import android.widget.Toast;
import android.os.Handler;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.zip.DataFormatException;
import java.text.SimpleDateFormat;

public class Trade extends AppCompatActivity {

    private TextView mTextMessage;
    private DatabaseHelper mDataBase;
    EditText mNumToBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);

        GraphView graph = (GraphView) findViewById(R.id.tradeGraph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 6),
                new DataPoint(1, 3),
                new DataPoint(2, 4),
                new DataPoint(3, 4),
                new DataPoint(4, 2)
        });
        graph.addSeries(series);

        mTextMessage = (TextView) findViewById(R.id.message);
        configureNavigationButtons();
    }

    private void configureNavigationButtons(){
        Button homeButton = (Button) findViewById(R.id.homeBtn);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Trade.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        Button settingsButton = (Button) findViewById(R.id.settingsBtn);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Trade.this, Settings.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        Button searchButton = (Button) findViewById(R.id.searchBtn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Trade.this, Search.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void buy(){
        String cryptoName = "";
        String temp = mNumToBuy.getText().toString();
        int numberBought = Integer.parseInt(temp);// 0;
        double currentPrice = 0;

        /*DataFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43*/

        //gets time and date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();//2016/11/16 12:08:43

        String temp1 = dtf.format(now);
        char[] fullDate = temp1.toCharArray();
        String date ="";
        date = date.copyValueOf( fullDate, 0, 9 );//The first 10 charaters are the data
        String time="";
        time = time.copyValueOf( fullDate, 10, 18 );//the last 8 characters are the time

        boolean success = mDataBase.addDataCol(cryptoName, numberBought , currentPrice , date , time);

        if(success)
            toaster("Your trade was successful!", 1500);
        else
            toaster("Trade fail", 1500);
    }

    private void getTrades(){
        Cursor data = mDataBase.getData();

        ArrayList<String> listOfData = new ArrayList<String>;

        while(data.moveToNext()){
            //gets the data from the first colume
            //adds it to arrayList
            listOfData.add(data.getString(1));
        }


    }

    public void toaster(String message, int length){

        final Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, length);
    }

}
