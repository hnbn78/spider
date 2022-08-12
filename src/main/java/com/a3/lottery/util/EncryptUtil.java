package com.a3.lottery.util;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;



/**
 * 
 * 
 * Copyright 漏 2014 weststar-inc. All rights reserved.
 * 
 * @Description:<p> 鍔犲瘑Util</p>
 * 
 * @author: Anson
 * 
 * @date: 2014骞�10鏈�22鏃� 涓婂崍11:45:59
 */
public class EncryptUtil {

    /** 榛樿瀵嗛挜 */
    private static String defaultKey = "(sobet*&abc123*^&*&*!hello*(ing))";

    /** 鍔犲瘑宸ュ叿 */
    private Cipher encryptCipher;

    /** 瑙ｅ瘑宸ュ叿 */
    private Cipher decryptCipher;

    /**
     * 鎸囧畾瀵嗛挜
     * 
     * @param strKey
     * @throws Exception java.lang.Exception
     */
    public EncryptUtil(String strKey) throws Exception {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        Key key = getKey(strKey.getBytes());

        encryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        decryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }

    /**
     * <p>
     * Title: encryptByDefault
     * </p>
     * <p>
     * AEScription:鐢ㄩ粯璁ょ殑瀵嗛挜杩涜鍔犲瘑
     * </p>
     * 
     * @param str String 琚姞瀵嗙殑瀛楃
     * @return String 鍔犲瘑鍚庣殑瀛楃
     */
    public static String encryptByDefault(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }
        try {
            return new EncryptUtil(defaultKey).encrypt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>
     * Title: encryptByKey
     * </p>
     * <p>
     * AEScription: 鐢ㄦ寚瀹氱殑瀵嗛挜杩涜鍔犲瘑
     * </p>
     * 
     * @param strKey 鎸囧畾鐨勫瘑
     * @param str 琚姞瀵嗙殑瀛楃
     * @return 鍔犲瘑鍚庣殑瀛楃
     */
    public static String encryptByKey(String strKey, String str) {
        if (strKey == null || "".equals(strKey) || str == null || "".equals(str)) {
            return null;
        }
        try {
            return new EncryptUtil(strKey).encrypt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 鍔犲瘑瀛楃
     * 
     * @param strIn 鍔犲瘑鐨勫瓧绗�
     * @return 鍔犲瘑鍚庣殑瀛楃
     * @throws Exception java.lang.Exception
     */
    private String encrypt(String strIn) throws Exception {
        return byteArr2HexStr(encrypt(strIn.getBytes("utf-8")));
    }

    /**
     * 鍔犲瘑瀛楄妭鏁扮粍
     * 
     * @param arrB 鍔犲瘑鐨勫瓧鑺傛暟缁�
     * @return 鍔犲瘑鍚庣殑瀛楄妭鏁扮粍
     * @throws Exception java.lang.Exception
     */
    private byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }

    /**
     * <p>
     * Title: decryptByDefault
     * </p>
     * <p>
     * AEScription: 鐢ㄩ粯璁ょ殑瀵嗛挜杩涜瑙ｅ瘑
     * </p>
     * 
     * @param str String 琚В瀵嗙殑瀛楃
     * @return String 瑙ｅ瘑鍚庣殑瀛楃
     */
    public static String decryptByDefault(String str) {
        try {
            return new EncryptUtil(defaultKey).decrypt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>
     * Title: decryptByKey
     * </p>
     * <p>
     * AEScription: 鐢ㄦ寚瀹氱殑瀵嗛挜杩涜瑙ｅ瘑
     * </p>
     * 
     * @param strKey 鎸囧畾鐨勫瘑
     * @param str 琚В瀵嗙殑瀛楃
     * @return 瑙ｅ瘑鍚庣殑瀛楃
     */
    public static String decryptByKey(String strKey, String str) {
        try {
            return new EncryptUtil(strKey).decrypt(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 瑙ｅ瘑瀛楃
     * 
     * @param strIn 鍔犲瘑鐨勫瓧绗�
     * @return 瑙ｅ瘑鍚庣殑瀛楃
     * @throws Exception java.lang.Exception
     */
    private String decrypt(String strIn) throws Exception {
        return new String(decrypt(hexStr2ByteArr(strIn)), "utf-8");
    }

    /**
     * 瑙ｅ瘑瀛楄妭鏁扮粍
     * 
     * @param arrB 瑙ｅ瘑鐨勫瓧鑺傛暟缁�
     * @return 瑙ｅ瘑鍚庣殑瀛楄妭鏁扮粍
     * @throws Exception java.lang.Exception
     */
    private byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }

    /**
     * 浠庢寚瀹氬瓧绗︿覆鐢熸垚瀵嗛挜锛屽瘑閽ユ墍瀛楄妭鏁扮粍闀垮害涓嶈冻8浣嶆椂鍚庨潰锛岃秴浣嶅彧鍙栧墠8
     * 
     * @param arrBTmp 鏋勬垚璇ュ瓧绗︿覆鐨勫瓧鑺傛暟
     * @return 鐢熸垚鐨勫瘑
     * @throws Exception java.lang.Exception
     */
    private Key getKey(byte[] arrBTmp) throws Exception {
        // 鍒涘缓瀵嗛挜鐢熸垚鍣�
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        // 鍒濆鍖栧瘑閽�
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(arrBTmp);
        keyGenerator.init(128, secureRandom);
        // 鐢熸垚瀵嗛挜
        SecretKey getKey = keyGenerator.generateKey();

        return getKey;
    }

    /**
     * <p>
     * Title: hexStr2ByteArr
     * </p>
     * <p>
     * AEScription: 灏嗚〃6杩涘埗鍊肩殑瀛楃涓茶浆鎹负byte鏁扮粍鍜宲ublic static String byteArr2HexStr(byte[] arrB) 浜掍负鍙殑杞崲杩�/p>
     * 
     * @param strIn 杞崲鐨勫瓧绗︿覆
     * @return 杞崲鍚庣殑byte鏁扮粍
     * @throws Exception 鏈柟娉曚笉澶勭悊浠讳綍寮傚父锛屾墍鏈夊紓甯稿叏閮ㄦ姏
     */
    private static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;

        // 涓や釜瀛楃琛ㄧず瀛楄妭锛屾墍浠ュ瓧鑺傛暟缁勯暱搴︽槸瀛楃涓查暱搴﹂櫎
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }


    /**
     * <p>
     * Title: byteArr2HexStr
     * </p>
     * <p>
     * AEScription: 灏哹yte鏁扮粍杞崲涓鸿〃6杩涘埗鍊肩殑瀛楃涓诧紝 濡傦細byte[]{8,18}杞崲涓猴細0813鍜宲ublic static byte[] hexStr2ByteArr(String strIn) 浜掍负鍙殑杞崲杩�/p>
     * 
     * @param arrB 杞崲鐨刡yte鏁扮粍
     * @return 杞崲鍚庣殑瀛楃
     * @throws Exception 鏈柟娉曚笉澶勭悊浠讳綍寮傚父锛屾墍鏈夊紓 甯稿叏閮ㄦ姏
     */
    private static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 姣忎釜byte鐢ㄤ袱涓瓧绗︽墠鑳借〃绀猴紝瀛楃涓茬殑闀垮害鏄暟缁勯暱搴︾殑涓�
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 鎶婅礋鏁拌浆鎹负姝ｆ暟
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 灏忎簬0F鐨勬暟鍦ㄥ墠闈㈣ˉ0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }


    /**
     * MD5鍔犲瘑瀛楃
     * 
     * @param param
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String encrpty2MD5(String param) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(param.getBytes());
        byte[] arrayOfByte = messageDigest.digest();

        String str = "";
        String zero = "0";
        int i = 0;
        for (int j = 0; j < arrayOfByte.length; j += 2) {
            i = arrayOfByte[j] & 0xFF;
            if (i < 16) {
                str = str + zero + Integer.toHexString(i);
            } else {
                str = str + Integer.toHexString(i);
            }
            i = arrayOfByte[j + 1] & 0xFF;
            if (i < 16) {
                str = str + zero + Integer.toHexString(i);
            } else {
                str = str + Integer.toHexString(i);
            }
        }
        return str.trim().toLowerCase();
    }


    /**
     * 鐩愬�煎拰鍔ㄦ�丠ash鍔犲瘑绠楁硶
     * 
     * @param userName
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String encryptPassword(String userName, String password)
        throws NoSuchAlgorithmException {
        String salt = encryptByKey(userName, password);

        byte[] saltBytes = salt.getBytes();
        int count = (saltBytes[0] + saltBytes[saltBytes.length - 1]) / 2;

        String respectStr = salt + encryptByKey(salt, password);

        for (int i = 0; i < count; i++) {
            respectStr = encrpty2MD5(respectStr);
        }
        return respectStr;
    }

}
