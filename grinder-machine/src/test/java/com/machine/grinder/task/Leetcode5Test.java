package com.machine.grinder.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Leetcode5Test {
    private Leetcode5 leetcode5 = new Leetcode5();

    @Test
    public void test_solve_sample1() {
        String result = leetcode5.solve("babad");
        assertEquals("bab", result);
    }

    @Test
    public void test_solve_babab() {
        String result = leetcode5.solve("babab");
        assertEquals("babab", result);
    }

    @Test
    public void test_solve_aaaaa() {
        String result = leetcode5.solve("aaaaa");
        assertEquals("aaaaa", result);
    }

    @Test
    public void test_solve_aaabaaa() {
        String result = leetcode5.solve("aaabaaa");
        assertEquals("aaabaaa", result);
    }

    @Test
    public void test_solve_a() {
        String result = leetcode5.solve("a");
        assertEquals("a", result);
    }

    @Test
    public void test_solve_aaa() {
        String result = leetcode5.solve("aaa");
        assertEquals("aaa", result);
    }

    @Test
    public void test_solve_empty() {
        String result = leetcode5.solve("");
        assertEquals("", result);
    }

    @Test
    public void test_solve_null() {
        String result = leetcode5.solve(null);
        assertEquals(null, result);
    }

    @Test
    public void test_solve_bbbb() {
        String result = leetcode5.solve("bbbb");
        assertEquals("bbbb", result);
    }

    @Test
    public void test_solve_abba() {
        String result = leetcode5.solve("abba");
        assertEquals("abba", result);
    }

    @Test
    public void test_solve_aabbaa() {
        String result = leetcode5.solve("aabbaa");
        assertEquals("aabbaa", result);
    }

    @Test
    public void test_solve_xxxxxaabbaa() {
        String result = leetcode5.solve("aabbaa");
        assertEquals("aabbaa", result);
    }

    @Test
    public void test_solve_abcabbacba() {
        String result = leetcode5.solve("abcabbacba");
        assertEquals("abcabbacba", result);
    }

    @Test
    public void test_solve_dabcabbacbae() {
        String result = leetcode5.solve("abcabbacba");
        assertEquals("abcabbacba", result);
    }
}
