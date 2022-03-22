package com.machine.grinder.task;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Leetcode15Test {
    private Leetcode15 leetcode15 = new Leetcode15();

    @Test
    public void test_solve_sample1() {
        List<List<Integer>> actual = leetcode15.solve(new int[]{-1, 0, 1, 2, -1, -4});
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(Arrays.asList(-1, -1, 2));
        expected.add(Arrays.asList(-1, 0, 1));
        assertEquals(expected, actual);
    }

    @Test
    public void test_solve_sample2() {
        List<List<Integer>> actual = leetcode15.solve(new int[]{});
        List<List<Integer>> expected = new ArrayList<>();
        assertEquals(expected, actual);
    }

    @Test
    public void test_solve_sample3() {
        List<List<Integer>> actual = leetcode15.solve(new int[]{0});
        List<List<Integer>> expected = new ArrayList<>();
        assertEquals(expected, actual);
    }

    @Test
    public void test_solve_allSame() {
        List<List<Integer>> actual = leetcode15.solve(new int[]{-1, -1, -1, -1, -1, -1});
        List<List<Integer>> expected = new ArrayList<>();
        assertEquals(expected, actual);
    }

    @Test
    public void test_solve_null() {
        List<List<Integer>> actual = leetcode15.solve(null);
        List<List<Integer>> expected = new ArrayList<>();
        assertEquals(expected, actual);
    }

    @Test
    public void test_solve_duplicates() {
        List<List<Integer>> actual = leetcode15.solve(new int[]{-2, -2, -2, -2, 1, 1, 1, 1, 2, 2, 4, 4, 4, 4});
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(Arrays.asList(-2, -2, 4));
        expected.add(Arrays.asList(-2, 1, 1));

        assertEquals(expected, actual);
    }

    @Test
    public void test_solve_testsample1() {
        List<List<Integer>> actual = leetcode15.solve(new int[]{-2, -1, -1, 0, 1, 2});
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(Arrays.asList(-2, 0, 2));
        expected.add(Arrays.asList(-1, -1, 2));
        expected.add(Arrays.asList(-1, 0, 1));

        assertEquals(expected, actual);
    }

    @Test
    public void test_solve_testsample2() {
        List<List<Integer>> actual = leetcode15.solve(new int[]{-2, 0, 2});
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(Arrays.asList(-2, 0, 2));

        assertEquals(expected, actual);
    }
}
