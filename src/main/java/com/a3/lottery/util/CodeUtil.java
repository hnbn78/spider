package com.a3.lottery.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class CodeUtil {

    private static final String[] CHARS = new String[] { "a", "b", "c", "d", "e", "f"};

    private static final String[] INTS = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" };


    public static String generateTronHashStr(String hash) {
    	 int total = 51;
         Random rand = new Random();
         int numCount = rand.nextInt(5) + 29;
         int strCount = total - numCount;
         List<String> list = new ArrayList<>();

         for (int i = 0; i < numCount; i++) {
             int num = rand.nextInt(INTS.length);
             list.add(INTS[num]);
         }
         for (int i = 0; i < strCount; i++) {
             int num = rand.nextInt(CHARS.length);
             list.add(CHARS[num]);
         }
         Collections.shuffle(list);
         String prefix = hash.substring(0, 13);
         String hash2 = prefix + StringUtils.join(list, "");
         return hash2;
    }

    public static Set<String> generateEosHashSet(String eosHash, int count) {
        Set<String> hashSet = new HashSet<>();

        while (hashSet.size() < count) {
            String generate64EosStr = generateTronHashStr(eosHash);

            if (hashSet.contains(generate64EosStr)) {
                continue;
            }
            hashSet.add(generate64EosStr);
        }
        return hashSet;
    }

    public static void main(String[] args) {
        String eosHash = "0000000001e8dd22d550d78182911e4afd689ef108ca8d60f0b4f42276ccd812";
       // Set<String> hashSet = generateEosHashSet(eosHash, 100);
        
        System.out.println(eosHash.substring(0, 13));
        String generateTronHashStr = generateTronHashStr(eosHash);
        System.out.println(generateTronHashStr);
        System.out.println(generateTronHashStr.length());
       // for (String str : hashSet) {
         //   System.out.println(str);
       // }
    }
    
    public static void mainyyu(String[] args) {
    	String eosHash = "0000000001e8dd24564a696b8d451948f6049a7f5904ececad015b6fc24e0635";
	}

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

}
