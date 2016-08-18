package com.java.component.secure.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ━━━━━━南无阿弥陀佛━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃stay hungry stay foolish
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━萌萌哒━━━━━━
 * Module Desc:com.java.component.secure.common
 * User: zjprevenge
 * Date: 2016/8/17
 * Time: 17:21
 */

public class MessageDigestUtils {
    private static final ConcurrentMap<String, MessageDigest> digestMap = new ConcurrentHashMap<>();

    public static MessageDigest getMessageDigest(String algorithm) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = digestMap.get(algorithm);
        if (messageDigest == null) {
            messageDigest = MessageDigest.getInstance(algorithm);
            MessageDigest origin = digestMap.putIfAbsent(algorithm, messageDigest);
            if (origin != null) {
                messageDigest = origin;
            }
        }
        return messageDigest;
    }

    public static byte[] digest(String algorithm, byte[] plainBytes) throws NoSuchAlgorithmException {
        MessageDigest digest = getMessageDigest(algorithm);
        return digest.digest(plainBytes);
    }

    public static String digestHex(String algorithm, byte[] plainBytes) throws NoSuchAlgorithmException {
        return HexUtils.encodeHexString(digest(algorithm, plainBytes));
    }
}
