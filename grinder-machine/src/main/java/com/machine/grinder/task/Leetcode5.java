package com.machine.grinder.task;

/**
 * Given a string s, return the longest palindromic substring in s.
 * Input: s = "babad"
 * Output: "bab"
 * Explanation: "aba" is also a valid answer.
 */
public class Leetcode5 {
    public String solve(String input) {
        return new Solution().longestPalindrome(input);
    }

    class Solution {
        public String longestPalindrome(String s) {
            if (s == null || s.length() == 0) {
                return s;
            }
            String result = "" + s.charAt(0);

            //odd number
            for (int i = 0; i < s.length(); i++) {
                int start = i;
                int end = i;

                while (start > 0
                        && end < s.length() - 1
                        && s.charAt(start - 1) == s.charAt(end + 1) && start <= end) {
                    start--;
                    end++;
                }

                String temp = s.substring(start, end + 1);
                if (temp.length() > (result != null ? result.length() : 0)) {
                    result = temp;
                }
            }

            //even number
            for (int i = 1; i < s.length(); i++) {
                int start = i - 1;
                int end = i;

                while (start >= 0
                        && end <= s.length() - 1
                        && s.charAt(start) == s.charAt(end) && start <= end) {
                    String temp = s.substring(start, end + 1);
                    if (temp.length() > (result != null ? result.length() : 0)) {
                        result = temp;
                    }
                    start--;
                    end++;
                }
            }

            return result;
        }
    }
}
