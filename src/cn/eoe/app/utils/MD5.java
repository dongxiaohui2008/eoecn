package cn.eoe.app.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.http.impl.auth.UnsupportedDigestAlgorithmException;
import android.util.Log;

public final class MD5 {
	
	private static final String LOG_TAG = "MD5";
	private static final String ALGORITHM = "MD5";

	private static char sHexDigits[] = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };
	
	private static MessageDigest sDigest;//Java 加密技术：消息摘要

	static {
		try {
			sDigest = MessageDigest.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			Log.e(LOG_TAG, "Get MD5 Digest failed.");
			throw new UnsupportedDigestAlgorithmException(ALGORITHM, e);
		}
	}

	/**
	 * MD5加密
	 */
	final public static String encode(String source) {
		byte[] btyes = source.getBytes();
		byte[] encodedBytes = sDigest.digest(btyes);
		return Utility.hexString(encodedBytes);
	}
}
