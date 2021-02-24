package com.a3.lottery.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class SHA512Util {

    /**
     * 传入文本内容，返回 SHA-512 串
     * 
     * @param strText
     * @return
     */
    public static String SHA512(final String strText) {
        return SHA(strText, "SHA-512");
    }

    /**
     * 字符串 SHA 加密
     * 
     * @param strSourceText
     * @return
     */
    private static String SHA(final String strText, final String strType) {
        // 返回值
        String strResult = null;

        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象 并傳入加密類型
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 類型结果
                byte byteBuffer[] = messageDigest.digest();

                // 將 byte 轉換爲 string
                StringBuffer strHexString = new StringBuffer();
                // 遍歷 byte buffer
                for (int i = 0; i < byteBuffer.length; i++) {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return strResult;
    }

    public static String get360ffcFromSha512Str(String code) {
        String sha512Str = SHA512(StringUtils.trim(code));

        List<Integer> numList = new ArrayList<>();

        for (int i = 0; i < sha512Str.length() - 1; i++) {
            if (numList.size() >= 2) {
                break;
            }
            if (sha512Str.charAt(i) >= 48 && sha512Str.charAt(i) <= 57) {
                Integer charAt = sha512Str.charAt(i) - '0';
                numList.add(charAt);
            }
        }
        for (int i = code.length() - 3; i <= code.length() - 1; i++) {
            Integer charAt = code.charAt(i) - '0';
            numList.add(charAt);
        }
        return StringUtils.join(numList, ",");
    }

    public static void main(String[] args) {
        String k = "3932858";
        String get360ffcFromSha512Str = get360ffcFromSha512Str("1054871320", "2388724");
        System.out.println(get360ffcFromSha512Str);
    }

    public static String get360ffcFromSha512Str(String gzCountStr, String fgjCountStr) {
        List<Integer> numList = new ArrayList<>();

        for (int i = gzCountStr.length() - 3; i <= gzCountStr.length() - 1; i++) {
            Integer charAt = gzCountStr.charAt(i) - '0';
            numList.add(charAt);
        }
        for (int i = fgjCountStr.length() - 2; i <= fgjCountStr.length() - 1; i++) {
            Integer charAt = fgjCountStr.charAt(i) - '0';
            numList.add(charAt);
        }
        return StringUtils.join(numList, ",");
    }
}