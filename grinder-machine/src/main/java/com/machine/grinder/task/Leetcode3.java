package com.machine.grinder.task;

/**
 * Given a string s, find the length of the longest substring without repeating characters.
 * Input: s = "abcabcbb"
 * Output: 3
 * Explanation: The answer is "abc", with the length of 3.
 */
public class Leetcode3 {
    public int solve(String input) {
        return new Solution().lengthOfLongestSubstring(input);
    }

    class Solution {
        public int lengthOfLongestSubstring(String s) {
            int i = 0;
            int j = 0;
            int max = 0;

            if (s == null) {
                return 0;
            }

            while (i <= j && j < s.length()) {
                String currStr = s.substring(i, j);
                if (-1 == currStr.indexOf(s.charAt(j))) {
                    int newMax = j + 1 - i;
                    max = newMax > max ? newMax : max;
                    j++;
                } else {

                    i++;
                }
            }
            return max;
        }

    }
}