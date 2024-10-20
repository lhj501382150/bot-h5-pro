package com.hml.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings({ "restriction" })
public class ThreeDES {
    private static final String IV = "1234567-";
    public static final String KEY = "uatspdbcccgame2014061800";

    /**
     * DESCBC加密
     *
     * @param src
     *            数据源
     * @param key
     *            密钥，长度必须是8的倍数
     * @return 返回加密后的数据
     * @throws Exception
     */
    public static String encryptDESCBC(final String src, final String key) throws Exception {

        // --生成key,同时制定是des还是DESede,两者的key长度要求不同
        final DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        final SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

        // --加密向量
        final IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));

        // --通过Chipher执行加密得到的是一个byte的数组,Cipher.getInstance("DES")就是采用ECB模式,cipher.init(Cipher.ENCRYPT_MODE,
        // secretKey)就可以了.
        final Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        final byte[] b = cipher.doFinal(src.getBytes("UTF-8"));

        // --通过base64,将加密数组转换成字符串
        final BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(b);
    }

    /**
     * DESCBC解密
     *
     * @param src
     *            数据源
     * @param key
     *            密钥，长度必须是8的倍数
     * @return 返回解密后的原始数据
     * @throws Exception
     */
    public static String decryptDESCBC(final String src, final String key) throws Exception {
        // --通过base64,将字符串转成byte数组
        final BASE64Decoder decoder = new BASE64Decoder();
        final byte[] bytesrc = decoder.decodeBuffer(src);

        // --解密的key
        final DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        final SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

        // --向量
        final IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));

        // --Chipher对象解密Cipher.getInstance("DES")就是采用ECB模式,cipher.init(Cipher.DECRYPT_MODE,
        // secretKey)就可以了.
        final Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        final byte[] retByte = cipher.doFinal(bytesrc);

        return new String(retByte);

    }

    // 3DESECB加密,key必须是长度大于等于 3*8 = 24 位哈
    public static String encryptThreeDESECB(final String src, final String key) throws Exception {
        final DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        final SecretKey securekey = keyFactory.generateSecret(dks);

        final Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, securekey);
        final byte[] b = cipher.doFinal(src.getBytes());

        final BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(b).replaceAll("\r", "").replaceAll("\n", "");

    }

    // 3DESECB解密,key必须是长度大于等于 3*8 = 24 位哈
    public static String decryptThreeDESECB(final String src, final String key) throws Exception {
        // --通过base64,将字符串转成byte数组
        final BASE64Decoder decoder = new BASE64Decoder();
        final byte[] bytesrc = decoder.decodeBuffer(src);
        // --解密的key
        final DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        final SecretKey securekey = keyFactory.generateSecret(dks);

        // --Chipher对象解密
        final Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, securekey);
        final byte[] retByte = cipher.doFinal(bytesrc);

        return new String(retByte);
    }

//    public static void main(String[] args) throws Exception {
//		String key = "77C285E3023F46FCBCF40116";
//		String str = "agent_id=2139365&batch_amt=9.80&batch_no=1014833305407070208&batch_num=1&detail_data=2022083100000004^5^0^6217858000017256600^韩先知^9.80^拍卖结算^河南省^郑州市^郑州高新技术开发区支行&ext_param1=1014833305407070208&key=4923F1D9A7074FE3B9941D76&notify_url=https://www.gaoda123.com/iface/pay/outMoneyNotify&version=3";
//		String aa = encryptThreeDESECB(str,key);
//		System.out.println(aa);
//		String st = decryptThreeDESECB(aa, key);
//		System.out.println(st);
//	}
}
