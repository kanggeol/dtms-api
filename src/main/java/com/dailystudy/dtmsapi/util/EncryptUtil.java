package com.dailystudy.dtmsapi.util;

import com.dailystudy.dtmsapi.exception.BizException;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

public class EncryptUtil {
    private static final String alg = "AES/CBC/PKCS5Padding";
    private static final String key = "KaPdSgVkYp3s6v9y$B&E(H+MbQeThWmZ";
    private static final String phpKey = "ngWddVeyCqFNb7kAAeyuWsCTEBTDc5Vs";
    private static final String iv = key.substring(0, 16);

    /**
     * AES 방식의 암호화
     *
     * @param text
     * @return
     * @throws Exception
     */
    public static String encrypt(String text) throws Exception {
        return encrypt(text, key);
    }


    public static String encrypt(String text, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

        byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String encryptForPHP(String text) {
        String ciphertextBase64 = null;
        try {
            byte[] plaintextByte = text.getBytes("UTF-8");
            byte[] ciphertextByte = encryptByteForPHP(plaintextByte, phpKey);
            ciphertextBase64 = Base64.getEncoder().encodeToString(ciphertextByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ciphertextBase64;
    }

    public static byte[] encryptByteForPHP(byte[] plaintext, String key){
        byte[] ciphertext = null;
        try{
            SecretKeySpec skey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            ciphertext = cipher.doFinal(plaintext);
        }catch(Exception e){
            e.printStackTrace();
        }
        return ciphertext;
    }


    /**
     * AES 방식의 복호화
     *
     * @param encryptedText
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptedText, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, "UTF-8");
    }

    public static String decrypt(String encryptedText) throws Exception {
        return decrypt(encryptedText, key);
    }

    public static String makeSHA256Key(String txt) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.reset();
        messageDigest.update(txt.getBytes("UTF-8"));
        byte[] hash = messageDigest.digest();

        StringBuffer stringBuffer = new StringBuffer();
        for (int i=0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length()==1) stringBuffer.append('0');
            stringBuffer.append(hex);
        }
        return stringBuffer.toString();
    }

    public static String generateShortUriToken(int id) throws Exception {
        String salt = "aub21d1!";
        String stringId = Integer.toString(id);
        String usableText = "abcdefghijkmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ123456789";
        int padding = 3;

        int hex = Integer.parseInt(EncryptUtil.makeSHA256Key(stringId + salt).substring(0, padding), 16);
        int num = hex % (int) Math.round(Math.pow(10, padding));
        num = num == 0? 1 : num;

        long seed = Long.parseLong(StringUtils.rightPad(Integer.toString(num), padding, "0") + stringId);
        long usableTextLength = usableText.length();

        long m = seed % usableTextLength;
        if(m > 2147483647) {
            throw new BizException("시드가 토큰을 생성할 수 없는 범위에 해당합니다.");
        }

        int intM = Long.valueOf(m).intValue();
        if(seed - intM == 0) {
            if(seed > 2147483647) {
                throw new BizException("시드가 토큰을 생성할 수 없는 범위에 해당합니다.");
            }

            return String.valueOf(usableText.charAt(Long.valueOf(seed).intValue()));
        }

        String result = "";
        while (intM > 0 || seed > 0) {
            result = usableText.substring(intM, intM + 1) + result;
            seed = (seed - intM) / usableTextLength;
            intM = Long.valueOf(seed % usableTextLength).intValue();
        }

        return result;
    }
}
