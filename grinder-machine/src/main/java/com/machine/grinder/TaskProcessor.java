package com.machine.grinder;

import com.machine.grinder.task.*;

public class TaskProcessor {
    public static void process() {
        Leetcode2 leetcode2 = new Leetcode2();
        leetcode2.solve(leetcode2.generateList(2, 4, 3), leetcode2.generateList(5, 6, 4));

        Leetcode3 leetcode3 = new Leetcode3();
        leetcode3.solve("abcabcbb");

        Leetcode5 leetcode5 = new Leetcode5();
        leetcode5.solve("babad");

        Leetcode6 leetcode6 = new Leetcode6();
        leetcode6.solve("PAYPALISHIRING", 3);

        Leetcode7 leetcode7 = new Leetcode7();
        leetcode7.solve(123);

        Leetcode8 leetcode8 = new Leetcode8();
        leetcode8.solve("42");

    }
}
