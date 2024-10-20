package com.hml.utils;

import java.security.KeyPair;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

public class SM2Test {

    public static void test1(){
        String text = "测试aaaaaaaaa";

        SM2 sm2 = SmUtil.sm2();

        //第一种 使用随机密钥对
        String s = sm2.encryptBcd(text, KeyType.PublicKey);
        System.out.println(s);
        String s1 = StrUtil.utf8Str(sm2.decryptFromBcd(s, KeyType.PrivateKey));
        System.out.println(s1);
    }

    public static void test2(){
        String text = "测试aaaaaaaaa";

        System.err.println(text);

        //第二种  使用自定义密钥对
        KeyPair keyPair = SecureUtil.generateKeyPair("SM2");
        byte[] priKey = keyPair.getPrivate().getEncoded();
        byte[] pubKey = keyPair.getPublic().getEncoded();

        System.err.println("=====================================");
        SM2 sm2obj = SmUtil.sm2(priKey,pubKey);
        //公钥加密，私钥解密
        String encStr = sm2obj.encryptBcd(text,KeyType.PublicKey);
        System.err.println(encStr);

        String decStr = StrUtil.utf8Str(sm2obj.decryptFromBcd(encStr,KeyType.PrivateKey));
        System.err.println(decStr);
    }


    public static void test4(){
        // log.error("我wwwww");
         String content = "Hello world!";
         // key必须是16位
         String key="ECfJZzIyYdmv5gXe";
         SymmetricCrypto sm4 = SmUtil.sm4(key.getBytes());
         String encryptHex = sm4.encryptHex(content);
         String decryptStr = sm4.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
         System.out.println(encryptHex+"\r\n"+decryptStr);
     }

    public static void main(String[] args) {
        //第一种 使用随机密钥对
//        test1();

        //第二种  使用自定义密钥对
//        test2();

    	test4();
       
    }

     

}
