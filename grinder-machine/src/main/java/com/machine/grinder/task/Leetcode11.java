package com.machine.grinder.task;

/**
 * 11. Container With Most Water
 * You are given an integer array height of length n.
 * There are n vertical lines drawn such that the two endpoints of the ith line are (i, 0) and (i, height[i]).
 * Input: height = [1,8,6,2,5,4,8,3,7]
 * Output: 49
 */
public class Leetcode11 {
    public int solve(int[] height) {
        return new Solution().maxArea(height);
    }

    class Solution {
        public int maxArea(int[] height) {
            if (height == null || height.length == 0) {
                return 0;
            }
            if (height.length == 1) {
                return height[0];
            }

            int max = 0;
            int i = 0;
            int j = height.length - 1;
            while (i <= j) {
                int area = Integer.min(height[i], height[j]) * (j - i);
                max = max > area ? max : area;
                if (height[i] <= height[j]) {
                    i++;
                } else {
                    j--;
                }
            }
            return max;
        }
    }
}
