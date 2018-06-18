package com.example.leetCode;

import java.util.ArrayList;

public class LeetCodeArray {
    private static final String TAG = "LeetCodeArray";
    
    //1. Two Number   O(n2)
    public static ArrayList<Integer> getIndexOfTwoNumber(ArrayList<Integer> nums, int target) {
        ArrayList<Integer> results = new ArrayList<Integer>();
        int sum = 0;
        for (int i = 0; i < nums.size() && nums.get(i) <= target; i++) {
            for (int j = i + 1; j < nums.size() && nums.get(j) <= target; j++) {
                sum = nums.get(i) + nums.get(j);
                if (sum == target) {
                    results.add(i);
                    results.add(j);
                    break;
                }
            }
        }
        
        return results;
    }

    //26. Remove Duplicates from Sorted Array  O(n)
    public static int getNumbersOfUniqueArray(ArrayList<Integer> nums) {
        int sum = nums.size();
        for (int i = 0; i < nums.size() - 1; i++) {
            if (nums.get(i) == nums.get(i + 1)) {
                sum--;
            }
        }
        return sum;
    }
}
