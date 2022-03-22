package com.machine.grinder.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Leetcode12Test {
    private Leetcode12 leetcode12 = new Leetcode12();

    @Test
    public void test_solve_sample1() {
        String result = leetcode12.solve(3);
        assertEquals("III", result);
    }

    @Test
    public void test_solve_34() {
        String result = leetcode12.solve(34);
        assertEquals("XXXIV", result);
    }

    @Test
    public void test_solve_38() {
        String result = leetcode12.solve(38);
        assertEquals("XXXVIII", result);
    }

    @Test
    public void test_solve_50() {
        String result = leetcode12.solve(50);
        assertEquals("L", result);
    }

    @Test
    public void test_solve_51() {
        String result = leetcode12.solve(51);
        assertEquals("LI", result);
    }

    @Test
    public void test_solve_54() {
        String result = leetcode12.solve(54);
        assertEquals("LIV", result);
    }

    @Test
    public void test_solve_55() {
        String result = leetcode12.solve(55);
        assertEquals("LV", result);
    }

    @Test
    public void test_solve_39() {
        String result = leetcode12.solve(39);
        assertEquals("XXXIX", result);
    }

    @Test
    public void test_solve_339() {
        String result = leetcode12.solve(339);
        assertEquals("CCCXXXIX", result);
    }

    @Test
    public void test_solve_1994() {
        String result = leetcode12.solve(1994);
        assertEquals("MCMXCIV", result);
    }


}
