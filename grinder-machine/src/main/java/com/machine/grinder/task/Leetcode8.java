package com.machine.grinder.task;

/**
 * 8. String to Integer (atoi)
 * Implement the myAtoi(string s) function, which converts a string to a 32-bit signed integer
 * (similar to C/C++'s atoi function).
 * <p>
 * Input: s = "42"
 * Output: 42
 */
public class Leetcode8 {
    public int solve(String s) {
        return new Solution().myAtoi(s);
    }

    class Solution {
        public int myAtoi(String s) {
            int result = 0;
            if (s == null) {
                return result;
            }
            s = s.trim();

            if (s.isEmpty()) {
                return result;
            }

            boolean isPositive = true;
            int threshold = Integer.MAX_VALUE / 10;

            int i = 0;
            for (i = 0; i < s.length(); i++) {
                if (i == 0 && (s.charAt(i) == '+' || s.charAt(i) == '-')) {
                    isPositive = checkSign(s.charAt(0));
                    continue;
                }
                int digit = (int) s.charAt(i) - (int) '0';
                if (digit < 0 || digit > 9) {
                    return result;
                }

                if (Math.abs(result) > threshold) {
                    result = isPositive ? Integer.MAX_VALUE : Integer.MIN_VALUE;
                    return result;
                } else if (Math.abs(result) == threshold) {
                    if (isPositive && digit > (Integer.MAX_VALUE % 10)) {
                        return Integer.MAX_VALUE;
                    } else if (!isPositive && digit > Math.abs(Integer.MIN_VALUE % 10)) {
                        return Integer.MIN_VALUE;
                    }
                }
                digit = isPositive ? digit : -digit;
                result = (result * 10) + digit;
            }
            return result;
        }

        private boolean checkSign(char charAt) {
            boolean isPositive = true;
            if (charAt == '-') {
                isPositive = false;
            }
            return isPositive;
        }
    }
}
