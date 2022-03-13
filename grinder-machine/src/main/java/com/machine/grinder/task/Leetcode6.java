package com.machine.grinder.task;

/**
 * Zigzag Conversion
 * Input: s = "PAYPALISHIRING", numRows = 3
 * Output: "PAHNAPLSIIGYIR"
 */
public class Leetcode6 {
    public String solve(String s, int numRows) {
        return new Solution().convert(s, numRows);
    }

    class Solution {
        public String convert(String s, int numRows) {
            if(numRows==1){
                return s;
            }

            StringBuilder sb = new StringBuilder();
            int max = (2 * numRows - 2);
            for (int i = 0; i < numRows; i++) {
                int j = i;
                boolean flag = true;
                while (j < s.length()) {
                    sb.append(s.charAt(j));
                    int diff = (2 * numRows - 2) - 2 * i;
                    if (diff == max|| diff==0) {
                        j = j + max;
                    } else {
                        if (flag) {
                            j = j + diff;
                            flag = !flag;
                        } else {
                            j = j + (max - diff);
                            flag = !flag;
                        }
                    }
                }
            }
            return sb.toString();
        }
    }
}
