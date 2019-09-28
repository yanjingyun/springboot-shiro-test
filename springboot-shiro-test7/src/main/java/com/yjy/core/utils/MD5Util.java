package com.yjy.core.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class MD5Util {

	private static final String ALGORITH_NAME = "md5";
	private static final int HASH_ITERATIONS = 2; //循环次数

	public static String encrypt(String username, String password) {
		return new SimpleHash(ALGORITH_NAME, password, ByteSource.Util.bytes(username.toLowerCase() + password),
				HASH_ITERATIONS).toHex();
	}

	public static void main(String[] args) {
		System.out.println(encrypt("user","123456"));
	}
}
