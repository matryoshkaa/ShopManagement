package com.example.shopmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//Activity where monthly/daily sales and profits statistics are shown

public class SalesReports extends AppCompatActivity {
    CollectionReference ref;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_reports);

        //week
        Calendar cal = new GregorianCalendar();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy");
        DateFormat labelFormat = new SimpleDateFormat("EEE", Locale.US);

        cal.add(Calendar.DAY_OF_MONTH, -7);

        //date array
        String[] weekDateArray = new String[7];
        final String[] weekLabelArray = new String[7];
        final String[] weekPriceArray = {"0","0","0","0","0","0","0"};

        db = FirebaseFirestore.getInstance();

        //TEMPORARY DELETE LATER
        String userId = "GUbm4ERwNTdm3TekcQ0UrpRAZYs1";

        //label array
        for (int i = 0; i < 7; i++) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Date sevenDaysAgo = cal.getTime();
            weekDateArray[i] = dateFormat.format(sevenDaysAgo);
            weekLabelArray[i] = labelFormat.format(sevenDaysAgo);

            final DocumentReference doc = db.collection("Users").document(userId).collection("Reports").document(weekDateArray[i]);
            final int finalI = i;
            doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String agg = document.get("Aggregate").toString();
                            TextView aggregate = findViewById(R.id.aggregate);
                            Double ag1 = Double.parseDouble(aggregate.getText().toString()) + Double.parseDouble(agg);

                            aggregate.setText(Double.toString(ag1));
                            weekPriceArray[finalI] = agg;

                            if (finalI ==6 ){
                                double x = Math.ceil(Double.parseDouble(weekPriceArray[finalI]))/7;

                                int[] yaxis = new int[7];
                                for (int n = 0; n<7; n++){
                                     yaxis[n] =  (int) Math.floor(Double.parseDouble(weekPriceArray[n])/x);
                                }

                                final GraphView graph = (GraphView) findViewById(R.id.graph);

                                ArrayList<String> ff = new ArrayList<>();
                                final LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                                        new DataPoint(0, yaxis[0]), //0
                                        new DataPoint(1, yaxis[1]), //1
                                        new DataPoint(2, yaxis[2]), //2
                                        new DataPoint(3, yaxis[3]), //3
                                        new DataPoint(4, yaxis[4]), //4
                                        new DataPoint(5, yaxis[5]), //5
                                        new DataPoint(6, yaxis[6]) //6
                                });

                                series.setThickness(10);
                                series.setColor(Color.parseColor("#ed1c24"));
                                graph.addSeries(series);

                                StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
                                staticLabelsFormatter.setHorizontalLabels(weekLabelArray);
                                staticLabelsFormatter.setVerticalLabels(weekPriceArray);
                                graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
                            }

                        } else {
                        }
                    } else {
                    }
                }
            });
        }





    }

    public void goToSettings(View view) {
        Intent i = new Intent();
        startActivity(new Intent(SalesReports.this, Settings.class));
    }

    public void goBack(View view) {
        finish();
    }
}