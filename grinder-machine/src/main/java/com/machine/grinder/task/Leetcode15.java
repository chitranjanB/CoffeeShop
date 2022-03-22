package com.machine.grinder.task;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 15. 3Sum
 * Given an integer array nums, return all the triplets [nums[i], nums[j], nums[k]].
 * such that i != j, i != k, and j != k, and nums[i] + nums[j] + nums[k] == 0.
 * <p>
 * Input: nums = [-1,0,1,2,-1,-4]
 * Output: [[-1,-1,2],[-1,0,1]]
 */
public class Leetcode15 {
    public List<List<Integer>> solve(int[] nums) {
        return new Leetcode15.Solution().threeSum(nums);
    }

    class Solution {
        public List<List<Integer>> threeSum(int[] nums) {

            //sort the array
                //for each element starting i=0 till i<length-3
                //iterate pointers p=i+1, q=length-1 until p==q, p
                //skip element if matching p & p-1 elements
                //add array[p]+array[q] = -array[i]
                // push the list [array[i], array[p], array[q]] to result list
            List<List<Integer>> result = new ArrayList<>();

            if (nums == null || nums.length < 3) {
                return result;
            } else if (nums.length == 3) {
                int sum = nums[0] + nums[1] + nums[2];
                if (sum == 0) {
                    result.add(Arrays.asList(nums[0], nums[1], nums[2]));
                }
                return result;
            }
            Arrays.sort(nums);
            for (int i = 0; i < nums.length - 2; i++) {
                int p = i + 1;
                //move i to next unique element
                while (i < nums.length - 1 && nums[i] == nums[i + 1]) {
                    i++;
                }
                int q = nums.length - 1;
                int temp = Integer.MIN_VALUE;
                while (p < q) {
                    //skip when element at p is already visited for this i
                    if (nums[p] == temp) {
                        p++;
                        continue;
                    }
                    int remainingSum = nums[p] + nums[q];
                    if (remainingSum == -nums[i]) {
                        result.add(Arrays.asList(nums[i], nums[p], nums[q]));
                        temp = nums[p];
                        p++;
                    } else if (remainingSum > Math.abs(nums[i])) {
                        q--;
                    } else if (remainingSum <= Math.abs(nums[i])) {
                        p++;
                    }
                }
                temp = Integer.MIN_VALUE;
            }
            return result;
        }
    }
}