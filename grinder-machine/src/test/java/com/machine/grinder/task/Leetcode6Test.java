package com.machine.grinder.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Leetcode6Test {
    private Leetcode6 leetcode6 = new Leetcode6();

    @Test
    public void test_solve_sample1() {
        String result = leetcode6.solve("PAYPALISHIRING", 3);
        assertEquals("PAHNAPLSIIGYIR", result);
    }

    @Test
    public void test_solve_sample2() {
        String result = leetcode6.solve("PAYPALISHIRING", 4);
        assertEquals("PINALSIGYAHRPI", result);
    }

    @Test
    public void test_solve_numRows_1() {
        String result = leetcode6.solve("PAYPALISHIRING", 1);
        assertEquals("PAYPALISHIRING", result);
    }

    @Test
    public void test_solve_ABABABAB() {
        String result = leetcode6.solve("ABABABAB", 2);
        assertEquals("AAAABBBB", result);
    }

    @Test
    public void test_solve_AAAAAA_numRows_2() {
        String result = leetcode6.solve("AAAAAA", 2);
        assertEquals("AAAAAA", result);
    }

    @Test
    public void test_solve_ABCD_numRows_2() {
        String result = leetcode6.solve("ABCD", 4);
        assertEquals("ABCD", result);
    }

    @Test
    public void test_solve_AB_numRows_4() {
        String result = leetcode6.solve("AB", 4);
        assertEquals("AB", result);
    }

    @Test
    public void test_solve_ABC_numRows_4() {
        String result = leetcode6.solve("ABC", 4);
        assertEquals("ABC", result);
    }

    @Test
    public void test_solve_ABC_numRows_2() {
        String result = leetcode6.solve("ABC", 2);
        assertEquals("ACB", result);
    }
}
