package com.machine.grinder.task;

import java.util.List;

/**
 * https://leetcode.com/problems/add-two-numbers/
 * <p>
 * You are given two non-empty linked lists representing two non-negative integers.
 * The digits are stored in reverse order, and each of their nodes contains a single digit.
 * Add the two numbers and return the sum as a linked list.
 */
public class Leetcode2 {

    public int solve(ListNode l1, ListNode l2) {
        return new Solution().addTwoNumbers(l1, l2);
    }

    class Solution {
        public int addTwoNumbers(ListNode l1, ListNode l2) {
            ListNode p1 = l1;
            ListNode p2 = l2;
            ListNode resultHead = null;
            ListNode p3 = null;

            int val = 0;
            int carry = 0;
            while (p1 != null || p2 != null) {
                val = (p1 != null ? p1.val : 0) + (p2 != null ? p2.val : 0) + carry;
                carry = val / 10;
                val = val % 10;
                if (p3 == null) {
                    p3 = new ListNode(val);
                    resultHead = p3;
                } else {
                    p3.next = new ListNode(val);
                    p3 = p3.next;
                }
                if (p1 != null) {
                    p1 = p1.next;
                }
                if (p2 != null) {
                    p2 = p2.next;
                }

            }

            if (carry == 1) {
                p3.next = new ListNode(carry);
            }
            return convertToNum(resultHead);
        }

        public int convertToNum(ListNode head) {
            int result = 0;
            int counter = 0;
            while (head != null) {
                result = head.val * (int) Math.pow(10, counter++) + result;
                head = head.next;
            }
            return result;
        }
    }

    public ListNode generateList(int... args) {
        if (args.length == 0) {
            return null;
        }

        ListNode head = new ListNode(args[0]);
        ListNode l = head;
        for (int i = 1; i < args.length; i++) {
            l.next = new ListNode(args[i]);
            l = l.next;
        }
        return head;
    }


    public ListNode getSampleSolution() {
        ListNode l1 = new ListNode(7);
        ListNode l2 = new ListNode(0);
        ListNode l3 = new ListNode(8);
        l1.next = l2;
        l2.next = l3;
        return l1;
    }

    class ListNode {
        int val;
        public ListNode next;

        public ListNode() {
        }

        public ListNode(int val) {
            this.val = val;
        }

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}

