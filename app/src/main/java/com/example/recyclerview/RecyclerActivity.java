package com.example.recyclerview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.nicole.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecyclerActivity extends Activity implements MyAdapter.OnChildClickListener {

    private MyAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        List<String> list = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_id);

        for (int i = 0; i < 100; i++) {
            list.add(String.format(Locale.CHINA, "第%03d条数据%s", i, i % 2 == 0 ? "" : ""));
        }

        mAdapter = new MyAdapter(this, list);
        mAdapter.setOnChildClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 3;
                }
                if (position == 1) {
                    return 2;
                }
                return 1;
            }
        });
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
                3, StaggeredGridLayoutManager.VERTICAL);

        MyItemAnimator animator = new MyItemAnimator();
        animator.setSupportsChangeAnimations(true);
        animator.setRemoveDuration(3000);
        animator.setMoveDuration(3000);
        animator.setAddDuration(3000);
        animator.setChangeDuration(3000);
        recyclerView.setItemAnimator(animator);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
                c.drawColor(Color.BLACK);
            }

            @Override
            public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDrawOver(c, parent, state);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                c.drawBitmap(bitmap, c.getWidth() / 2 - bitmap.getWidth() /2,
                        c.getHeight() / 2 - bitmap.getHeight() / 2, null);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position == 0) {
                    outRect.set(0,20,0,20);
                } else {
                    outRect.set(0, 0, 0, 3);
                }
            }
        });
    }

    @Override
    public void onChildClick(RecyclerView recyclerView, View view, int position, String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        mAdapter.remove(position);
        //mAdapter.add(position, "新增数据");
        //mAdapter.change(position, "新增数据");
    }
}
