package com.machine.grinder.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * https://leetcode.com/problems/add-two-numbers/
 * <p>
 * You are given two non-empty linked lists representing two non-negative integers.
 * The digits are stored in reverse order, and each of their nodes contains a single digit.
 * Add the two numbers and return the sum as a linked list.
 */
public class Leetcode2Test {

    private Leetcode2 leetcode2 = new Leetcode2();

    @Test
    public void test_solve_sample() {
        int result = leetcode2.solve(leetcode2.generateList(2, 4, 3), leetcode2.generateList(5, 6, 4));
        assertNotNull(result);
    }

    @Test
    public void test_solve_12_100_112() {
        int result = leetcode2.solve(leetcode2.generateList(2, 1), leetcode2.generateList(0, 0, 1));
        assertEquals(112, result);
    }

    @Test
    public void test_solve_100_12_112() {
        int result = leetcode2.solve(leetcode2.generateList(0, 0, 1), leetcode2.generateList( 2, 1));
        assertEquals(112, result);
    }

    @Test
    public void test_solve_1_100_101() {
        int result = leetcode2.solve(leetcode2.generateList( 1), leetcode2.generateList( 0, 0, 1));
        assertEquals(101, result);
    }

    @Test
    public void test_solve_null_100_100() {
        int result = leetcode2.solve(leetcode2.generateList(), leetcode2.generateList( 0, 0, 1));
        assertEquals(100, result);
    }

    @Test
    public void test_solve_100_null_100() {
        int result = leetcode2.solve(leetcode2.generateList( 0, 0, 1), leetcode2.generateList());
        assertEquals(100, result);
    }

    @Test
    public void test_generate_list() {
        Leetcode2.ListNode listNode = leetcode2.generateList(1, 2, 3);
        int result = leetcode2.new Solution().convertToNum(listNode);
        assertEquals(321, result);
    }

    @Test
    public void test_generate_list_null() {
        Leetcode2.ListNode listNode = leetcode2.generateList();
        int result = leetcode2.new Solution().convertToNum(listNode);
        assertEquals(0, result);
    }

    @Test
    public void test_generate_list_1() {
        Leetcode2.ListNode listNode = leetcode2.generateList(1);
        int result = leetcode2.new Solution().convertToNum(listNode);
        assertEquals(1, result);
    }

    @Test
    public void test_convertToNum_342() {
        int result = leetcode2.new Solution().convertToNum(leetcode2.generateList(2, 4, 3));
        assertEquals(342, result);
    }

    @Test
    public void test_convertToNum_465() {
        int result = leetcode2.new Solution().convertToNum(leetcode2.generateList(5, 6, 4));
        assertEquals(465, result);
    }

}

