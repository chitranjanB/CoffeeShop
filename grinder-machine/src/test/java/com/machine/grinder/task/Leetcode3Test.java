package com.machine.grinder.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Given a string s, find the length of the longest substring without repeating characters.
 * Input: s = "abcabcbb"
 * Output: 3
 * Explanation: The answer is "abc", with the length of 3.
 */

public class Leetcode3Test {
    private Leetcode3 leetcode3 = new Leetcode3();

    @Test
    public void test_solve_sample1() {
        int result = leetcode3.solve("abcabcbb");
        assertEquals(3, result);
    }

    @Test
    public void test_solve_sample2() {
        int result = leetcode3.solve("bbbbb");
        assertEquals(1, result);
    }

    @Test
    public void test_solve_input_a() {
        int result = leetcode3.solve("a");
        assertEquals(1, result);
    }

    @Test
    public void test_solve_input_ab() {
        int result = leetcode3.solve("ab");
        assertEquals(2, result);
    }

    @Test
    public void test_solve_input_abcd() {
        int result = leetcode3.solve("abcd");
        assertEquals(4, result);
    }

    @Test
    public void test_solve_input_aabb() {
        int result = leetcode3.solve("aabb");
        assertEquals(2, result);
    }

    @Test
    public void test_solve_input_null() {
        int result = leetcode3.solve(null);
        assertEquals(0, result);
    }

    @Test
    public void test_solve_input_empty() {
        int result = leetcode3.solve("");
        assertEquals(0, result);
    }
}
