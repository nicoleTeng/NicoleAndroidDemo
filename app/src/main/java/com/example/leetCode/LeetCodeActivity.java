package com.example.leetCode;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.nicole.R;

public class LeetCodeActivity extends Activity {
    private static final String TAG = "LeetCodeActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.leetcode_demo);
        
        LeetCodeArray();
    }
    
    private void LeetCodeArray() {
        // 1. Two number
        ArrayList<Integer> nums = new ArrayList<Integer>();
        nums.add(1);
        nums.add(3);
        nums.add(7);
        nums.add(8);
        nums.add(8);
        nums.add(10);
        nums.add(11);
        
        int target = 9;
        ArrayList<Integer> results = LeetCodeArray.getIndexOfTwoNumber(nums, target);
        Log.v(TAG, "txh Two Number: " + results.toString());
        
        int count = LeetCodeArray.getNumbersOfUniqueArray(nums);
        Log.v(TAG, "txh Remove Duplicates from Sorted Array: " + count);
    }

}
