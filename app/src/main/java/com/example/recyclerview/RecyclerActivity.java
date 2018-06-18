package com.example.recyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.example.nicole.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecyclerActivity extends Activity {

    private MyAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_id);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1009; i++) {
            list.add(String.format(Locale.CHINA, "第%03d条数据", i));
        }
        mAdapter = new MyAdapter(this, list);
        recyclerView.setAdapter(mAdapter);
    }
}
