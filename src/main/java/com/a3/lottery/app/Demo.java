package com.a3.lottery.app;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

public class Demo {

    public static void main(String[] args) {
        String str = "02,09,13,14,25,26,29,30,41,42,46,47,48,49,54,55,56,64,65,76";
        String buildPCDDCode = buildPCDDCode1(str);
        System.out.println(buildPCDDCode);
    }

    private static String buildPCDDCode(String codeNode) {
        if (codeNode.contains("+")) {
            String[] codes = codeNode.split("\\+");
            codeNode = codes[0];
        }

        String[] numStrs = codeNode.split(",");
        Integer[] codes = new Integer[numStrs.length];

        for (int i = 0; i < numStrs.length; i++) {
            codes[i] = Integer.valueOf(numStrs[i]);
        }

        Arrays.sort(codes);

        int[] resultNums = new int[4];

        for (int i = 0; i < codes.length - 2; i++) {
            resultNums[i / 6] += codes[i];
        }

        for (int i = 0; i < resultNums.length - 1; i++) {
            resultNums[i] = resultNums[i] % 10;
        }
        resultNums[3] = resultNums[0] + resultNums[1] + resultNums[2];
        String resultCode = StringUtils.join(resultNums, ',');
        return resultCode;
    }

    private static String buildPCDDCode1(String codeNode) {
        if (codeNode.contains("+")) {
            String[] codes = codeNode.split("\\+");
            codeNode = codes[0];
        }
        String[] numStrs = codeNode.split(",");
        int sum1 = Integer.valueOf(numStrs[1]) + Integer.valueOf(numStrs[4]) + Integer.valueOf(numStrs[7])
                + Integer.valueOf(numStrs[10]) + Integer.valueOf(numStrs[13]) + Integer.valueOf(numStrs[16]);
        int a = sum1 % 10;

        int sum2 = Integer.valueOf(numStrs[2]) + Integer.valueOf(numStrs[5]) + Integer.valueOf(numStrs[8])
                + Integer.valueOf(numStrs[11]) + Integer.valueOf(numStrs[14]) + Integer.valueOf(numStrs[17]);
        int b = sum2 % 10;

        int sum3 = Integer.valueOf(numStrs[3]) + Integer.valueOf(numStrs[6]) + Integer.valueOf(numStrs[9])
                + Integer.valueOf(numStrs[12]) + Integer.valueOf(numStrs[15]) + Integer.valueOf(numStrs[18]);
        int c = sum3 % 10;

        int d = a + b + c;

        return a + "," + b + "," + c + "," + d;
    }
}
