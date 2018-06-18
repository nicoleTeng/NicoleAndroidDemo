package com.example.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nicole.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements View.OnClickListener {
    private static String TAG = "MyAdapter";
    private Context context;
    private List<String> list;
    private OnChildClickListener listener;
    private RecyclerView recyclerView;

    public MyAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnChildClickListener(OnChildClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.v(TAG, "txh onCreateViewHolder");
        View view = null;
        if (viewType == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.recycler_item_header, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        }
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.v(TAG, "txh onBindViewHolder, position = " + position);
        holder.textView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        Log.v(TAG, "txh getItemCount = " + list.size());
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            //return super.getItemViewType(position);
            return 1;
        }
    }

    @Override
    public void onClick(View view) {
        if (recyclerView != null && listener != null) {
            int position = recyclerView.getChildAdapterPosition(view);
            listener.onChildClick(recyclerView, view, position, list.get(position));
        }
    }

    public void remove(int position) {
        list.remove(position);
//        notifyDataSetChanged();
        notifyItemRemoved(position);
    }

    public void add(int position, String data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    public void change(int position, String data) {
        list.remove(position);
        list.add(position, data);
        notifyItemChanged(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView_id);
        }
    }

    interface OnChildClickListener {
        void onChildClick(RecyclerView recyclerView, View view, int Position, String data);
    }
}
