package com.machine.grinder.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 16. 3Sum Closest
 * Given an integer array nums of length n and an integer target,
 * find three integers in nums such that the sum is closest to target.
 * <p>
 * Return the sum of the three integers.
 * Input: nums = [-1,2,1,-4], target = 1
 * Output: 2
 * Explanation: The sum that is closest to the target is 2. (-1 + 2 + 1 = 2).
 */
public class Leetcode16 {
    public int solve(int[] nums, int target) {
        return new Leetcode16.Solution().threeSumClosest(nums, target);
    }

    class Solution {
        public int threeSumClosest(int[] nums, int target) {

            //sort the array
            //for each element starting i=0 till i<length-3
            //iterate pointers p=i+1, q=length-1 until p==q, p
            //skip element if matching p & p-1 elements
            // if sum difference is nearest update the target

            List<Integer> sumList = new ArrayList<>();
            int min = Integer.MAX_VALUE;

            if (nums == null || nums.length < 3) {
                return 0;
            } else if (nums.length == 3) {
                int sum = nums[0] + nums[1] + nums[2];
                if (sum - target == 0) {
                    return sum;
                }
                return sum;
            }

            Arrays.sort(nums);
            int closestSum = 0;

            for (int i = 0; i < nums.length - 2; i++) {
                int p = i + 1;
                int q = nums.length - 1;
                while (p < q) {
                    int sum = nums[i] + nums[p] + nums[q];

                    if (sum == target) {
                        return sum;
                    } else if (sum > target) {
                        q--;
                    } else {
                        p++;
                    }

                    if (Math.abs(sum - target) < min) {
                        min = Math.abs(sum - target);
                        closestSum = sum;
                    }
                }
            }
            return closestSum;
        }
    }
}