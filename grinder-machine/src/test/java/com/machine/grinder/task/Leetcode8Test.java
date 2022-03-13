package com.machine.grinder.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Leetcode8Test {
    private Leetcode8 leetcode8 = new Leetcode8();

    @Test
    public void test_solve_sample1() {
        int result = leetcode8.solve("42");
        assertEquals(42, result);
    }

    @Test
    public void test_solve_sample2() {
        int result = leetcode8.solve("   -42");
        assertEquals(-42, result);
    }

    @Test
    public void test_solve_sample3() {
        int result = leetcode8.solve("4193 with words");
        assertEquals(4193, result);
    }

    @Test
    public void test_solve_123() {
        int result = leetcode8.solve("123");
        assertEquals(123, result);
    }

    @Test
    public void test_solve_space_123() {
        int result = leetcode8.solve("   123");
        assertEquals(123, result);
    }

    @Test
    public void test_solve_space_123_space() {
        int result = leetcode8.solve("   123   ");
        assertEquals(123, result);
    }

    @Test
    public void test_solve_12345678() {
        int result = leetcode8.solve("123456789");
        assertEquals(123456789, result);
    }

    @Test
    public void test_solve_123x() {
        int result = leetcode8.solve("123x");
        assertEquals(123, result);
    }

    @Test
    public void test_solve_x123x() {
        int result = leetcode8.solve("x123x");
        assertEquals(0, result);
    }

    @Test
    public void test_solve_negative_x123x() {
        int result = leetcode8.solve("-x123x");
        assertEquals(0, result);
    }

    @Test
    public void test_solve_INT_MIN() {
        int result = leetcode8.solve("-2147483648");
        assertEquals(Integer.MIN_VALUE, result);
    }

    @Test
    public void test_solve_outOfNegativeRange() {
        int result = leetcode8.solve("-2147483649");
        assertEquals(Integer.MIN_VALUE, result);
    }

    @Test
    public void test_solve_INT_MAX() {
        int result = leetcode8.solve("2147483647");
        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    public void test_solve_outOfPositiveRange() {
        int result = leetcode8.solve("2147483648");
        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    public void test_solve_failedTest_leetcode() {
        int result = leetcode8.solve("21474836460");
        assertEquals(2147483647, result);
    }

    @Test
    public void test_solve_failedTest2_leetcode() {
        int result = leetcode8.solve(" -1010023630o4");
        assertEquals(-1010023630, result);
    }


    @Test
    public void test_solve_2147483646aa() {
        int result = leetcode8.solve("2147483646aa");
        assertEquals(2147483646, result);
    }

    @Test
    public void test_solve_21474836460a() {
        int result = leetcode8.solve("21474836460a");
        assertEquals(2147483647, result);
    }


}
