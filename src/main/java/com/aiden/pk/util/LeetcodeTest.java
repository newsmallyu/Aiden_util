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
     * 给定一个数组，
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
}
