package com.machine.grinder.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Leetcode7Test {
    private Leetcode7 leetcode7 = new Leetcode7();

    @Test
    public void test_solve_sample1() {
        int result = leetcode7.solve(123);
        assertEquals(321, result);
    }

    @Test
    public void test_solve_1234567() {
        int result = leetcode7.solve(1234567);
        assertEquals(7654321, result);
    }

    @Test
    public void test_solve_12345678901() {
        int result = leetcode7.solve(1234567890);
        assertEquals(987654321, result);
    }

    @Test
    public void test_solve_1000() {
        int result = leetcode7.solve(1000);
        assertEquals(1, result);
    }

    @Test
    public void test_solve_9() {
        int result = leetcode7.solve(0);
        assertEquals(0, result);
    }

    @Test
    public void test_solve_negative() {
        int result = leetcode7.solve(-123);
        assertEquals(-321, result);
    }

    @Test
    public void test_solve_negative_1() {
        int result = leetcode7.solve(-1);
        assertEquals(-1, result);
    }

    @Test
    public void test_solve_negative_11() {
        int result = leetcode7.solve(-11);
        assertEquals(-11, result);
    }

    @Test
    public void test_solve_negative_100() {
        int result = leetcode7.solve(-100);
        assertEquals(-1, result);
    }

    @Test
    public void test_solve_INT_MIN() {
        int result = leetcode7.solve(-2147483648);
        assertEquals(0, result);
    }

    @Test
    public void test_solve_INT_MAX() {
        int result = leetcode7.solve(2147483647);
        assertEquals(0, result);
    }

    @Test
    public void test_solve_900000() {
        int result = leetcode7.solve(900000);
        assertEquals(9, result);
    }



}
