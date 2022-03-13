package com.machine.grinder.task;

/**
 * 7. Reverse Integer
 * Given a signed 32-bit integer x, return x with its digits reversed.
 * If reversing x causes the value to go outside the signed 32-bit integer range
 * [-231, 231 - 1], then return 0.
 * <p>
 * Input: x = 123
 * Output: 321
 */
public class Leetcode7 {
    public int solve(int x) {
        return new Solution().reverse(x);
    }

    class Solution {
        public int reverse(int x) {
            int result = 0;
            if (x == Integer.MIN_VALUE || x == Integer.MIN_VALUE) {
                return result;
            }
            int abs = Math.abs(x);
            boolean isPositive = x >= 0;
            int threshold = Integer.MAX_VALUE / 10;

            //reverse till n-1 digit
            while (abs >= 10) {
                int remainder = abs % 10;
                abs = abs / 10;
                result = (result * 10) + remainder;
            }

            // check if adding n digit will exceed integer range
            if (result == threshold) {
                if (isPositive) {
                    result = abs > (Integer.MAX_VALUE % 10) ? 0 : (result * 10) + abs;
                } else {
                    result = abs > Math.abs(Integer.MIN_VALUE % 10) ? 0 : (result * 10) + abs;
                }
            } else if (result < threshold) {
                result = (result * 10) + abs;
            } else {
                result = 0;
            }
            return isPositive ? result : -result;
        }
    }
}
