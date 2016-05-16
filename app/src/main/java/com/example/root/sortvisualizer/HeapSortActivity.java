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

public class HeapSortActivity extends AppCompatActivity {
    /*

     This Activity handles HeapSort
     RV ---> RecyclerView

     */

    RecyclerView mNumberListView;  // using recycler view to display elements
    private LinearLayoutManager mLinearLayoutManger; // layout manager for RV
    private HeapSortListAdapter mHeapSortAdapter; // Adaptor class for RV
    private List<HeapSortModel> mNumbersList = new ArrayList<>(11); // a list of data type 'HeapSortModel' whic is a data structure to store element and it's color.
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
             // things start when u click the floating button
                AsyncTaskRunner runner = new AsyncTaskRunner(); // process sorting in background
                runner.execute();
                Snackbar.make(view, "Heap sort Started", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show(); // display a message when it all starts
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent in = getIntent();
        String size = in.getStringExtra("Size"); // get the size of List from main activity
        final int max = 100; // numbers max limit
        final int min = 1; // numbers min limit
        final Random rn = new Random(); // we will deal with random numbers
        mNumbersList.clear(); // clear the list before proceeding

        for( int  i = 1; i <= Integer.parseInt(size); i++ ) {
            mNumbersList.add(new HeapSortModel(rn.nextInt(max - min + 1) + min,0)); // populate the List with instances of heapSortModel , color of each element is balck(0) initially.
        }
        mNumberListView = (RecyclerView) findViewById(R.id.numbers_list_view); // find RecyclerView
        mLinearLayoutManger = new LinearLayoutManager(this); // get LinearLayout
        mLinearLayoutManger.setOrientation(LinearLayoutManager.VERTICAL); // set orientation
        mNumberListView.setLayoutManager(mLinearLayoutManger); // Tell the RecyclerView about Linear Layout manager
        mHeapSortAdapter = new HeapSortListAdapter(mNumbersList,this); // get The adaptor
        mNumberListView.setAdapter(mHeapSortAdapter); // set RV  view with the adaptor

    }

    void swap(int first, int second) {
        /*
         Function to swap elements at index first, second
         */
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
        /*
         This class runs processing of elements in background and displays the in-between results to android UI
         */

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

                    min_index = get_min(i); // find the minimum element index in list ( i....size)
                    mHeapSortAdapter.getmList().set(i, new HeapSortModel(mNumbersList.get(i).getmNumber(), 1)); // color the ith element red
                    index = i;
                    publishProgress(); // display to UI
                    Thread.sleep(500); // sleep
                    mHeapSortAdapter.getmList().set(min_index, new HeapSortModel(mNumbersList.get(min_index).getmNumber(), 1)); // color the min_index element red
                    index = min_index;

                    publishProgress(); // display to UI
                    Thread.sleep(1000);
                    swap(min_index,i); // swap both elements

                    mHeapSortAdapter.getmList().set(min_index, new HeapSortModel(mNumbersList.get(min_index).getmNumber(),0 )); // color the element at min_index black again
                    index = min_index;
                    publishProgress(); // display to UI
                    Thread.sleep(500);
                    mHeapSortAdapter.getmList().set(i, new HeapSortModel(mNumbersList.get(i).getmNumber(), 2)); // color the element at index i Green to mark it as sorted
                    index = i;
                    publishProgress(); // display to UI
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
            /*
              to display intermediate changes to UI
             */
            mHeapSortAdapter.notifyItemChanged(index);
        }
    }

}
