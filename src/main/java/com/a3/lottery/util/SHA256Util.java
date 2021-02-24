package com.a3.lottery.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * SHA256 摘要算法工具类
 * @author Administrator
 *
 */
public class SHA256Util {

    public static String getSHA256StrJava(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    public static String getNumFromSha256Str(String str) {
        // f14864c08e8fabd0fe5ce0d62af2c484b34bc25affddf4c959e7ffae03f5425d
        str = str.trim();
        List<Integer> list = new ArrayList<>();
        if (str != null && !"".equals(str)) {
            for (int i = str.length() - 1; i >= 0; i--) {
                if (list.size() >= 5) {
                    break;
                }
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    Integer charAt = str.charAt(i) - '0';
                    list.add(charAt);
                }
            }
        }
        return StringUtils.join(list, ",");
    }

    public static String getPk10NumFromSha256Str(String str) {
        str = str.trim();
        List<Integer> list = new ArrayList<>();

        if (str != null && !"".equals(str)) {
            for (int i = str.length() - 1; i >= 0; i--) {
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    Integer charAt = str.charAt(i) - '0';
                    list.add(charAt);
                }
            }
        }
        List<String> codeList = new ArrayList<>();

        for (Integer num : list) {
            if (codeList.size() == 10) {
                break;
            }
            String kk = null;
            if (num.intValue() == 0) {
                kk = "10";
            } else if (num.intValue() < 10) {
                kk = "0" + num;
            }
            if (!codeList.contains(kk)) {
                codeList.add(kk);
            }
        }
        if (codeList.size() >= 10) {
            return StringUtils.join(codeList, ",");
        }
        String[] arr = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10" };
        List<String> buWeiArr = new ArrayList<>();
        for (String numStr : arr) {
            if (!codeList.contains(numStr)) {
                buWeiArr.add(numStr);
            }
        }
        codeList.addAll(0, buWeiArr);
        return StringUtils.join(codeList, ",");
    }

    public static void main(String[] args) {
        // List<String> a = new ArrayList<>();
        // List<String> b = new ArrayList<>();
        // a.add("a1");
        // a.add("a2");
        //
        // b.add("b1");
        // b.add("b2");
        // a.addAll(0, b);
        // for (String str : a) {
        // System.out.println(str);
        // }
        String k = "3892873";
        String sha256StrJava = getSHA256StrJava(k);
        System.out.println(sha256StrJava);
    }
}