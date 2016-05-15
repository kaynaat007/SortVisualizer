package com.example.root.sortvisualizer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.root.sortvisualizer.adapters.BucketSortAdapter;
import com.example.root.sortvisualizer.adapters.HeapSortListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BucketSortActivity extends AppCompatActivity {

    private List<BucketSortModel> mBucketList = new ArrayList<>();
    private List<Integer> mNumbersList = new ArrayList<>();
    private TextView mListTextView;
    private TextView mSortedListView;
    private TextView mFinalText;
    RecyclerView mNumberListView;  // getting it's value as null
    private LinearLayoutManager mLinearLayoutManger;
    private BucketSortAdapter mBucketSortAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_sort);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListTextView = (TextView)findViewById(R.id.list_items);
        mSortedListView = (TextView)findViewById(R.id.sorted_list_items);
        mFinalText = (TextView)findViewById(R.id.final_text);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute();
                Snackbar.make(view, "Bucket Sort Started", Snackbar.LENGTH_LONG)
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
            mNumbersList.add(rn.nextInt(max - min + 1) + min);
        }

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<mNumbersList.size();i++) {
            if(i==mNumbersList.size()-1) {
                sb.append(mNumbersList.get(i)+"");
            } else {
                sb.append(mNumbersList.get(i) + ", ");
            }
        }

        mListTextView.setText(sb);
        BucketSortModel bucketModel;
        List<Integer> list1;
        for(int i=0; i< mNumbersList.size();i++) {
            bucketModel = new BucketSortModel();
            list1 = new ArrayList<>();
            bucketModel.setMbucketList(list1);
            bucketModel.setmSize(list1.size());
            mBucketList.add(bucketModel);
        }

        mNumberListView = (RecyclerView) findViewById(R.id.bucket_list_view); // find RecyclerView
        mLinearLayoutManger = new LinearLayoutManager(this);
        mLinearLayoutManger.setOrientation(LinearLayoutManager.VERTICAL);
        mNumberListView.setLayoutManager(mLinearLayoutManger);
        mBucketSortAdapter = new BucketSortAdapter(mBucketList,this);
        mNumberListView.setAdapter(mBucketSortAdapter);

    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        private int index;
        StringBuilder sb;

        private List<Integer> sortList(List<Integer> list) {
            Collections.sort(list);
            return list;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                // set each item of the array to appropriate bucket
                int hashKey = mNumbersList.size();
                BucketSortModel model;
                for(int i = 0; i<mNumbersList.size();i++) {
                    int bucketNumber = (mNumbersList.get(i) % hashKey);
                    model = new BucketSortModel();
                    List<Integer> list = new ArrayList<>();
                    list = mBucketSortAdapter.getmList().get(bucketNumber).getMbucketList();
                    list.add(mNumbersList.get(i));
                    model.setMbucketList(list);
                    model.setmSize(list.size());
                    index = bucketNumber;
                    mBucketSortAdapter.getmList().set(bucketNumber, model);
                    publishProgress(); // notfiy UI that a progress has been made
                    Thread.sleep(1000);
                }

                // sort each bucket individually  and display to UI

                BucketSortModel model1;
                for(int i=0;i< mBucketSortAdapter.getmList().size();i++) {
                    model1 = new BucketSortModel();
                    model1.setMbucketList(sortList(mBucketSortAdapter.getmList().get(i).getMbucketList()));
                    model1.setmSize(mBucketSortAdapter.getmList().get(i).getmSize());
                    mBucketSortAdapter.getmList().set(i, model1);
                    index = i;
                    publishProgress();
                    Thread.sleep(1000);
                }
                // finally sort the original list and  display to UI . skipping merging of the buckets, as it is not necessary.
                Collections.sort(mNumbersList);
                sb = new StringBuilder();
                for(int i = 0; i<mNumbersList.size();i++) {
                    if(i==mNumbersList.size()-1) {
                        sb.append(mNumbersList.get(i)+"");
                    } else {
                        sb.append(mNumbersList.get(i) + ", ");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            mFinalText.setVisibility(View.VISIBLE);
            mSortedListView.setVisibility(View.VISIBLE);
            mSortedListView.setText(sb);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... text) {
            // tell the UI about changes
            mBucketSortAdapter.notifyItemChanged(index);
        }
    }

}
