package com.aiden.pk.leetcode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * level : Hard
 * 给你一个整数数组 nums​​​ 和一个整数 k​​​​​ 。区间 [left, right]（left <= right）的 异或结果 是对下标位于 left 和 right（包括 left 和 right ）之间所有元素进行 XOR 运算的结果：nums[left] XOR nums[left+1] XOR ... XOR nums[right] 。
 *
 * 返回数组中 要更改的最小元素数 ，以使所有长度为 k 的区间异或结果等于零。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/make-the-xor-of-all-segments-equal-to-zero
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Solution1 {
    public int minChanges(int[] nums, int k) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //2.通过ServerSocketChannel绑定ip地址和port(端口号)
        ssc.socket().bind(new InetSocketAddress("127.0.0.1", 3333));
        Selector selector = Selector.open();
        SelectionKey register = ssc.register(selector, SelectionKey.OP_READ);

        return 0;
    }

    public static void main(String[] args) {

        System.out.println(8|4);
    }
}
