package com.aiden.pk.util;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class LeetcodeTest {
    /**
     * 给定一个未排序的整数数组，找出最长连续序列的长度
     * @return
     */
    @Test
    public int longestConsecutive(){
        int[] nums = new int[]{5, 200, 1, 3, 101, 2, 444, 4};
        Set<Integer> num_set = new HashSet<>();
        for (int num:nums) {
            num_set.add(num);
        }
        int length = 0;
        for (int num:num_set) {
            if (!num_set.contains(num-1)){
                int startNum = num;
                int currentLength =1;
                while (num_set.contains(startNum+1)){
                    startNum +=1;
                    currentLength +=1;
                }
                length = Math.max(length, currentLength);
            }
        }
        return length;
    }
    /**
     * 在一个长度为 n 的数组 nums 里的所有数字都在 0～n-1 的范围内。数组中某些数字是重复的，但不知道有几个数字重复了，也不知道每个数字重复了几次。请找出数组中任意一个重复的数字。
     * 2 <= n <= 100000
     */
    @Test
    public int findRepeatNumber(int[] nums) {
        Set<Integer> set = new HashSet<>();
        int result =-1;
        for (int num :nums) {
            if (!set.add(num)){
                result = num;
            }
        }
        return result;
    }

    @Test
    /**
     * 在一个 n * m 的二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。请完成一个函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
     */
    public boolean findNumberIn2DArray(int[][] matrix, int target) {
        if(matrix ==null|| matrix.length==0|matrix[0].length==0){
            return false;
        }
        boolean result = false;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j]==target){
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * 请实现一个函数，把字符串 s 中的每个空格替换成"%20"。
     * @param s
     * @return
     */
    @Test
    public String replaceSpace(String s) {
        String result = null;
        if (s!=null&&!s.equals("")){
            result = s.replaceAll(" ", "%20");
        }
        if(s.equals("")){
            result = "";
        }
        return result;
    }
    public class ListNode {
      int val;
      ListNode next;
      ListNode(int x) { val = x; }
  }
    public int[] reversePrint(ListNode head) {
        //先获取链表长度，创建对应长度数组
        ListNode currNode = head;
        int len = 0;
        while(currNode != null){
            len ++;
            currNode = currNode.next;
        }
        int[] result = new int[len];

        //再次遍历链表，将值倒序填充至结果数组
        currNode = head;
        while(currNode != null){
            result[len - 1] = currNode.val;
            len --;
            currNode = currNode.next;
        }
        return result;
    }

}
