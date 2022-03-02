package com.machine.grinder;

import com.machine.grinder.task.Leetcode2;

public class TaskProcessor {
    public static void process() {
        Leetcode2 leetcode2 = new Leetcode2();
        leetcode2.solve(leetcode2.generateList(2, 4, 3), leetcode2.generateList(5, 6, 4));
    }
}
