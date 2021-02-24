package com.a3.lottery.util;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

public class ConvertToKenoUtil {

    public static void main(String[] args) {
        String source = "8,9,4,8,9";
        for (int i = 0; i < 10000; i++) {
            String result = convert(source);
            System.out.println(result);
        }
    }

    public static String convert(String source) {
        String[] srcs1 = source.split(",");
        int[] codes = new int[5];
        for (int i = 0; i < codes.length; i++) {
            codes[i] = Integer.valueOf(srcs1[i]).intValue();
        }
        for (;;) {
            try {
                String result = doAll(codes);

                String check = buildCode(result);
                if (source.equals(check)) {
                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String doAll(int[] target) {
        int[] range1 = { 8, 28 };

        int[] digit1 = do1(target[0], range1, 0);

        int offset1 = digit1[3] + 1;

        int[] range2 = { offset1, 1 };
        if (range2[0] <= 16) {
            range2[1] = (offset1 + 20);
        } else {
            range2[1] = (offset1 + 16);
        }
        range2[0] = (offset1 + 5);

        int[] digit2 = do1(target[1], range2, offset1);

        int offset22 = digit2[3] + 1;
        int[] range22 = { offset22, 1 };
        if (offset22 <= 32) {
            range22[1] = (offset22 + 20);
        } else if (offset22 < 35) {
            range22[1] = (offset22 + 16);
        } else {
            range22[1] = (offset22 + 15);
        }
        range22[0] = (offset22 + 5);

        int[] digit3 = do1(target[2], range22, offset22);

        int offset33 = digit3[3] + 1;
        int[] range33 = { offset33, 1 };
        if (offset33 <= 48) {
            range33[1] = (offset33 + 18);
        } else if (offset33 < 50) {
            range33[1] = (offset33 + 15);
        } else {
            range33[1] = (offset33 + 14);
        }
        range33[0] = (offset33 + 5);

        int[] digit4 = do1(target[3], range33, offset33);

        int offset44 = digit4[3] + 1;

        int[] digit5 = do1(target[4], new int[] { 80, 80 }, offset44);

        int[] result = new int[20];

        System.arraycopy(digit1, 0, result, 0, 4);
        System.arraycopy(digit2, 0, result, 4, 4);
        System.arraycopy(digit3, 0, result, 8, 4);
        System.arraycopy(digit4, 0, result, 12, 4);
        System.arraycopy(digit5, 0, result, 16, 4);

        StringBuilder sb = new StringBuilder(48);
        for (int i = 0; i < result.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            if (result[i] < 10) {
                sb.append('0');
            }
            sb.append(result[i]);
        }
        String kenoResult = sb.toString();

        return kenoResult;
    }

    private static String buildCode(String codeNode) {
        String[] numStrs = codeNode.split(",");
        Integer[] codes = new Integer[numStrs.length];
        for (int i = 0; i < numStrs.length; i++) {
            codes[i] = Integer.valueOf(numStrs[i]);
        }
        Arrays.sort(codes);

        int[] resultNums = new int[5];
        for (int i = 0; i < codes.length; i++) {
            resultNums[(i / 4)] += codes[i].intValue();
        }
        for (int i = 0; i < resultNums.length; i++) {
            resultNums[i] %= 10;
        }
        String resultCode = StringUtils.join(resultNums, ',');

        return resultCode;
    }

    // range1 = { 8, 28 }; rangeOffset 0
    private static int[] do1(int target, int[] range, int rangeOffset) {
        int[] digit1 = new int[4];

        int pc = 0;
        do {
            int offset1 = RandomUtils.nextInt(range[0], range[1]);
            int lastSum = loopRandom(rangeOffset, offset1, 3, digit1);

            int targetSum = target - lastSum;
            for (int i = 1; i <= 8; i++) {
                int tried = i * 10 + targetSum;
                if ((tried >= rangeOffset) && (tried <= range[1]) && (!ArrayUtils.contains(digit1, tried))) {
                    digit1[3] = tried;
                    Arrays.sort(digit1);
                    return digit1;
                }
            }
            pc++;
        } while (pc <= 10000);
        throw new IllegalArgumentException("超过循环次数");

    }

    // start 0;end:10,count:3,
    private static int loopRandom(int start, int end, int count, int[] array) {
        int sum = 0;
        int bingo = 0;
        int pc = 0;
        while (bingo < count) {
            int random = RandomUtils.nextInt(start, end + 1);
            if (!ArrayUtils.contains(array, random)) {
                array[(bingo++)] = random;
                sum += random;
            }
        }
        return sum % 10;
    }
}