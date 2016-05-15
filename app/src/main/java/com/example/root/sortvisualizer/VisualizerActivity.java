package com.example.root.sortvisualizer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.root.sortvisualizer.adapters.HeapSortListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import java.util.ArrayList;

public class VisualizerActivity extends AppCompatActivity {

    RecyclerView mNumberListView;  // getting it's value as null
    private LinearLayoutManager mLinearLayoutManger;
    private HeapSortListAdapter mHeapSortAdapter;
    private List<HeapSortModel> mNumbersList = new ArrayList<>(11);
    int min_index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sortlogic2();
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute();
                Snackbar.make(view, "Heap sort Started", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent in = getIntent();
        String size = in.getStringExtra("Size");
        final int max = 100;
        final int min = 1;
        final Random rn = new Random();
        mNumbersList.clear();

        for( int  i = 1; i <= Integer.parseInt(size); i++ ) {
            mNumbersList.add(new HeapSortModel(rn.nextInt(max - min + 1) + min,0));
        }
        mNumberListView = (RecyclerView) findViewById(R.id.numbers_list_view); // find RecyclerView
        mLinearLayoutManger = new LinearLayoutManager(this);
        mLinearLayoutManger.setOrientation(LinearLayoutManager.VERTICAL);
        mNumberListView.setLayoutManager(mLinearLayoutManger);
        mHeapSortAdapter = new HeapSortListAdapter(mNumbersList,this);
        mNumberListView.setAdapter(mHeapSortAdapter);

    }

    void swap(int first, int second) {

        HeapSortModel temp = mNumbersList.get(first);
        mNumbersList.set(first, mNumbersList.get(second));
        mNumbersList.set(second, temp);
    }

    private int  get_min( int start ) {

        int i;
        int min = 1000000;
        int index = -1;
        for ( i = start; i < mNumbersList.size(); i++ ) {
            if ( mNumbersList.get(i).getmNumber() < min ) {
                min = mNumbersList.get(i).getmNumber();
                index = i;
            }
        }
        return index;
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        private int index;

        @Override
        protected String doInBackground(String... params) {
            try {
                // Do your long operations here and return the result
                // Sleeping for given time period
                for (int i = 1; i < mNumbersList.size(); i++) {
//            i = 1;'
                    //do your stuff here after DELAY sec

                    min_index = get_min(i);
                    mHeapSortAdapter.getmList().set(i, new HeapSortModel(mNumbersList.get(i).getmNumber(), 1));
                    index = i;
                    publishProgress();
                    Thread.sleep(500);
                    mHeapSortAdapter.getmList().set(min_index, new HeapSortModel(mNumbersList.get(min_index).getmNumber(), 1));
                    index = min_index;

                    publishProgress();
                    Thread.sleep(1000);
                    swap(min_index,i);

                    mHeapSortAdapter.getmList().set(min_index, new HeapSortModel(mNumbersList.get(min_index).getmNumber(),0 ));
                    index = min_index;
                    publishProgress();
                    Thread.sleep(500);
                    mHeapSortAdapter.getmList().set(i, new HeapSortModel(mNumbersList.get(i).getmNumber(), 2));
                    index = i;
                    publishProgress();
                    publishProgress();
                    Thread.sleep(2000);
                    resp = "Slept for 2000 milliseconds";
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... text) {
            mHeapSortAdapter.notifyItemChanged(index);
        }
    }

}
