package com.machine.grinder.task;

import java.nio.file.LinkPermission;
import java.util.HashMap;
import java.util.Map;

/**
 * 12. Integer to Roman
 * Roman numerals are represented by seven different symbols: I, V, X, L, C, D and M.
 * <p>
 * Input: num = 3
 * Output: "III"
 */
public class Leetcode12 {
    public String solve(int num) {
        return new Leetcode12.Solution().intToRoman(num);
    }

    class Solution {
        public String intToRoman(int num) {
            int[] ints = new int[]{1, 3, 4, 5, 9, 10, 30, 40, 50, 90, 100, 300, 400, 500, 900, 1000, 3000};
            String[] romans = new String[]{"I", "III", "IV", "V", "IX", "X", "XXX", "XL", "L", "XC", "C", "CCC", "D", "CD", "CM", "M", "MMM"};

            String result = "";
            int i = ints.length - 1;
            while (i >= 0 && num % ints[i] != 0) {
                int frequency = num / ints[i];
                num = num % ints[i];
                for (int j = 0; j < frequency; j++) {
                    result = result + romans[i];
                }
                i--;
            }
            if (i >= 0 && num % ints[i] == 0) {
                result = result + romans[i];
            }
            return result;
        }
    }
}
