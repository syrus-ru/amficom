/*
 * $Id: PasswordVerifier.java,v 1.1 2004/07/06 15:52:40 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.security;

import java.security.*;
import java.util.Arrays;
//import java.util.regex.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/07/06 15:52:40 $
 * @author $Author: bass $
 * @module servermisc_v1
 */
public final class PasswordVerifier {
	private static final byte BASE64_TABLE[] = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".getBytes();

	private static final int DIGEST_LENGTH = 16;

	/**
	 * Length of the encoded key without salt.
	 */
	private static final int ENCODED_KEY_LENGTH = 22;

	private static final int MAX_MD5_SALT_LENGTH = 8;

//	private static final Pattern MD5_SALT_PATTERN = Pattern.compile("^\\$1\\$([^\\$]{0,8})\\$[a-zA-Z0-9./]{22}$");

	private static final String MD5_SALT_PREFIX = "$1$";

	private static final byte[] MD5_SALT_PREFIX_BYTES = MD5_SALT_PREFIX.getBytes();

	private static final int MD5_SALT_PREFIX_LENGTH = MD5_SALT_PREFIX.length();

	private static final char MD5_SALT_TERMINATOR = '$';

	private static final byte[] DEFAULT_MD5_SALT = (MD5_SALT_PREFIX + "ABCDEFGH" + MD5_SALT_TERMINATOR).getBytes();

	/**
	 * This class realizes singleton pattern. 
	 */
	private PasswordVerifier() {
	}

	/**
	 * Boolean flag, indicating whether native library was loaded (linux
	 * glibc only).
	 */
	private static final boolean LIBRARY_LOADED;

	static {
		boolean libraryLoaded;
		try {
			System.loadLibrary("nativecrypt");
			libraryLoaded = true;
		} catch (UnsatisfiedLinkError ule) {
			libraryLoaded = false;
		}
		LIBRARY_LOADED = libraryLoaded;
	}

	/**
	 * Common public interface to crypt: depending on whether native library
	 * is loaded, either native (faster) or portable implementation will be
	 * used. See crypt(3) manual page for details.
	 */
	private static byte[] crypt(final byte key[], final byte salt[]) throws NoSuchAlgorithmException {
		if (LIBRARY_LOADED)
			return nativeCrypt(key, salt);
		else
			return portableCrypt(key, salt);
	}

	/**
	 * Interface to the native implementation.
	 */
	private static native byte[] nativeCrypt(final byte key[], final byte salt[]);

	/**
	 * Portable implementation.
	 */
	private static byte[] portableCrypt(final byte key[], final byte salt[]) throws NoSuchAlgorithmException {
		String saltString = new String(salt);
		if (saltString.startsWith(MD5_SALT_PREFIX))
			saltString = saltString.substring(MD5_SALT_PREFIX_LENGTH);
		int saltLength = saltString.indexOf(MD5_SALT_TERMINATOR);
		if (saltLength == -1)
			saltLength = saltString.length();
		saltLength = Math.min(saltLength, MAX_MD5_SALT_LENGTH);
		byte innerSalt[] = saltString.substring(0, saltLength).getBytes();
		int keyLength = key.length;

		MessageDigest ctx = MessageDigest.getInstance("MD5");
		ctx.update(key);
		ctx.update(innerSalt);
		ctx.update(key);
		byte altResult[] = ctx.digest();

		ctx.reset();
		ctx.update(key);
		ctx.update(MD5_SALT_PREFIX_BYTES);
		ctx.update(innerSalt);

		int cnt;
		for (cnt = keyLength; cnt > DIGEST_LENGTH; cnt -= DIGEST_LENGTH)
			ctx.update(altResult);
		ctx.update(altResult, 0, cnt);

		altResult[0] = '\0';

		for (cnt = keyLength; cnt > 0; cnt >>= 1)
			ctx.update((cnt & 1) != 0 ? altResult : key, 0, 1);

		altResult = ctx.digest();

		for (cnt = 0; cnt < 1000; ++cnt) {
			ctx.reset();

			if ((cnt & 1) != 0)
				ctx.update(key);
			else
				ctx.update(altResult);

			if (cnt % 3 != 0)
				ctx.update(innerSalt);

			if (cnt % 7 != 0)
				ctx.update(key);

			if ((cnt & 1) != 0)
				ctx.update(altResult);
			else
				ctx.update(key);

			altResult = ctx.digest();
		}

		byte buffer[] = new byte[MD5_SALT_PREFIX_LENGTH + saltLength + 1 + ENCODED_KEY_LENGTH];

		int bufferPosition = 0;

		System.arraycopy(MD5_SALT_PREFIX_BYTES, 0, buffer, bufferPosition, MD5_SALT_PREFIX_LENGTH);
		bufferPosition += MD5_SALT_PREFIX_LENGTH;

		System.arraycopy(innerSalt, 0, buffer, bufferPosition, saltLength);
		bufferPosition += saltLength;

		buffer[bufferPosition] = MD5_SALT_TERMINATOR;
		bufferPosition++;

		base64From24Bit(altResult[0], altResult[6], altResult[12], buffer, bufferPosition, 4);
		bufferPosition += 4;

		base64From24Bit(altResult[1], altResult[7], altResult[13], buffer, bufferPosition, 4);
		bufferPosition += 4;

		base64From24Bit(altResult[2], altResult[8], altResult[14], buffer, bufferPosition, 4);
		bufferPosition += 4;

		base64From24Bit(altResult[3], altResult[9], altResult[15], buffer, bufferPosition, 4);
		bufferPosition += 4;

		base64From24Bit(altResult[4], altResult[10], altResult[5], buffer, bufferPosition, 4);
		bufferPosition += 4;

		base64From24Bit((byte) 0, (byte) 0, altResult[11], buffer, bufferPosition, 2);

		return buffer;
	}

	/**
	 * Generates a random salt.
	 * @todo Implement this method. Salt mustn't contain colons, as
	 *       /etc/shadow uses them for field separation.
	 */
	private static byte[] generateSalt() {
		return DEFAULT_MD5_SALT;
	}

	/**
	 * Extracts salt from an encrypted password.
	 */
	private static byte[] extractSalt(byte encryptedKey[]) {
//		Matcher m = MD5_SALT_PATTERN.matcher(new String(encryptedKey));
//		m.matches();
//		return m.group(1).getBytes();
		throw new UnsupportedOperationException();
	}

	public static byte[] setPassword(byte oldKey[], byte oldEncryptedKey[], byte newKey[]) throws GeneralSecurityException {
		checkPassword(oldKey, oldEncryptedKey);
		return crypt(newKey, generateSalt());
	}

	/**
	 * Checks whether <code>key</code> matches <code>encryptedKey</code>.
	 * After the <code>key</code> is encrypted, it is compared with
	 * <code>encryptedKey</code>. If the result is negative, a
	 * {@link GeneralSecurityException} is thrown.
	 * 
	 * @param key user-typed password.
	 * @param encryptedKey encrypted password.
	 * @throws GeneralSecurityException if password is invalid.
	 */
	public static void checkPassword(byte key[], byte encryptedKey[]) throws GeneralSecurityException {
		if (!Arrays.equals(encryptedKey, crypt(key, extractSalt(encryptedKey))))
			throw new GeneralSecurityException("Invalid password");
	}

	private static void base64From24Bit(byte b2, byte b1, byte b0, byte buffer[], int bufferPosition, int byteCount) {
		int w = ((b2 & 0xff) << 16) | ((b1 & 0xff) << 8) | (b0 & 0xff);
		while (byteCount-- > 0) {
			buffer[bufferPosition++] = BASE64_TABLE[w & 0x3f];
			w >>= 6;
		}						
	}
}
