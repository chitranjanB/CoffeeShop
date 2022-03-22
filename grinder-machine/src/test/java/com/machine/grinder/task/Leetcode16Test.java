package com.machine.grinder.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Leetcode16Test {
    private Leetcode16 leetcode16 = new Leetcode16();

    @Test
    public void test_solve_sample1() {
        int actual = leetcode16.solve(new int[]{-1, 2, 1, -4}, 4);
        assertEquals(2, actual);
    }

    @Test
    public void test_solve_sample2() {
        int actual = leetcode16.solve(new int[]{0, 0, 0}, 1);
        assertEquals(0, actual);
    }

    @Test
    public void test_solve_failed_1() {
        int actual = leetcode16.solve(new int[]{-1, 2, 1, -4}, 1);
        assertEquals(2, actual);
    }

    @Test
    public void test_solve_failed_2() {
        int actual = leetcode16.solve(new int[]{0, 1, 2}, 3);
        assertEquals(3, actual);
    }

    @Test
    public void test_solve_multipleMin() {
        int actual = leetcode16.solve(new int[]{1, 1, 1, 0, 2, -1, 2}, 1);
        assertEquals(1, actual);
    }

    @Test
    public void test_solve_multipleMin_negative() {
        int actual = leetcode16.solve(new int[]{-1, 0, 0, -3, 4, 2}, -1);
        assertEquals(-1, actual);
    }

    @Test
    public void test_solve_multiple_0() {
        int actual = leetcode16.solve(new int[]{}, 0);
        assertEquals(0, actual);
    }

    @Test
    public void test_solve_multiple_null() {
        int actual = leetcode16.solve(null, 0);
        assertEquals(0, actual);
    }
}
