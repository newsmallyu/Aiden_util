package com.aiden.pk.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Reason.H.Duan
 * @Date: 11/21/2020
 */
public class PrefixKeyTrie {

    // 为了防止干扰，求其子节点中命中率较小的节点
    private final int foundinSize = 100;
    private final int maxChildrenSize = 10;

    private long nodeCount = 0;
    private TrieNode root;

    public PrefixKeyTrie() {
        root = new TrieNode(null, null);
    }

    public void insert(String word) {
        Character c = word.charAt(0);
        TrieNode cn = root.childdren.get(c);
        if(cn == null){
            cn = new TrieNode(root, c);
            nodeCount ++;
            root.childdren.put(c,cn);
        }
        insert(cn, word.substring(1));
    }

    public void insert(TrieNode node, String word){
        node.prefixes ++;
        if(!node.endWord && word.length() != 0){
            Character c = word.charAt(0);
            TrieNode cn = node.childdren.get(c);
            if(cn == null){
                if(node.childdren.size() > maxChildrenSize){
                    node.endWord = true;
                    node.childdren.clear();
                    return;
                }else {
                    cn = new TrieNode(node, c);
                    nodeCount ++;
                    if(nodeCount%10000 == 0){
                        System.out.println(getPrefix(cn)+", nodes "+nodeCount);
                    }
                    node.childdren.put(c, cn);
                }
            }
            insert(cn, word.substring(1));
        }
    }

    // 返回节点中频率不小于minCount的前缀集合
    public List<String> findMaxPrefix(int minCount){
        List<String> prefixList = new ArrayList<>();
        for(TrieNode n : root.childdren.values()){
            findMaxPrefix(prefixList, n, minCount);
        }
        return prefixList;
    }

    public void findMaxPrefix(List<String> prefixList,TrieNode node, int minCount){

        if(node.childdren.isEmpty()){
            prefixList.add(getPrefix(node));
            return;
        }
        boolean isPrefix = false;
        for(Character c : node.childdren.keySet()){
            TrieNode cn = node.childdren.get(c);
            // 为防止干扰，丢弃特别小的节点
            if(cn.prefixes < minCount && cn.prefixes > foundinSize){
                isPrefix = true;
                break;
            }
        }
        // 如果当前节点的子节点的前缀数量小于指定大小，则说明当前节点为一个指定前缀
        if(isPrefix){
            String ps = getPrefix(node);
            System.out.println(ps+"     ->size: "+node.prefixes);
            prefixList.add(ps);
        } else {
            for(Character c : node.childdren.keySet()){
                TrieNode cn = node.childdren.get(c);
                // 小于丢弃标准的直接丢弃
                if(cn.prefixes > foundinSize){
                    findMaxPrefix(prefixList, cn, minCount);
                }
            }
        }
    }

    public void clear(){
        root = null;
    }

    private String getPrefix(TrieNode node) {
        if(node.prentNode != null){
            return getPrefix(node.prentNode) + node.word;
        }else {
            return "";
        }
    }


    class TrieNode {
        protected Map<Character, TrieNode> childdren;
        protected int prefixes;
        protected boolean endWord;
        protected Character word;
        protected TrieNode prentNode;


        public TrieNode(TrieNode prentNode, Character word) {
            this.childdren = new HashMap<>();
            this.endWord = false;
            this.word = word;
            this.prentNode = prentNode;
        }
    }
}


