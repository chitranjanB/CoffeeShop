package com.machine.grinder.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Leetcode11Test {
    private Leetcode11 leetcode11 = new Leetcode11();

    @Test
    public void test_solve_sample1() {
        int result = leetcode11.solve(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7});
        assertEquals(49, result);
    }

    @Test
    public void test_solve_sample2() {
        int result = leetcode11.solve(new int[]{1, 1});
        assertEquals(1, result);
    }

    @Test
    public void test_solve_sample3() {
        int result = leetcode11.solve(new int[]{1, 3, 2, 4, 5, 5, 5});
        assertEquals(15, result);
    }

    @Test
    public void test_solve_sample4_failedTest() {
        int result = leetcode11.solve(new int[]{1, 3, 2, 5, 25, 24, 5});
        assertEquals(24, result);
    }

    @Test
    public void test_solve_null() {
        int result = leetcode11.solve(null);
        assertEquals(0, result);
    }

    @Test
    public void test_solve_empty() {
        int result = leetcode11.solve(new int[]{});
        assertEquals(0, result);
    }

    @Test
    public void test_solve_length_1() {
        int result = leetcode11.solve(new int[]{1});
        assertEquals(1, result);
    }

    @Test
    public void test_solve_length_1_100() {
        int result = leetcode11.solve(new int[]{100});
        assertEquals(100, result);
    }

    @Test
    public void test_solve_2_1() {
        int result = leetcode11.solve(new int[]{2, 1});
        assertEquals(1, result);
    }

    @Test
    public void test_solve_asc() {
        int result = leetcode11.solve(new int[]{1, 2, 3, 4, 5, 6, 7});
        assertEquals(12, result);
    }

    @Test
    public void test_solve_desc() {
        int result = leetcode11.solve(new int[]{7, 6, 5, 4, 3, 2, 1});
        assertEquals(12, result);
    }

    @Test
    public void test_solve_asc_threeDigits() {
        int result = leetcode11.solve(new int[]{199, 299, 399, 499, 599, 699, 799});
        assertEquals(1596, result);
    }

    @Test
    public void test_solve_desc_threeDigits() {
        int result = leetcode11.solve(new int[]{799, 699, 599, 499, 399, 299, 199});
        assertEquals(1596, result);
    }
}
