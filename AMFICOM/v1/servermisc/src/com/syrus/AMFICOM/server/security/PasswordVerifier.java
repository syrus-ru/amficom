/*
 * $Id: PasswordVerifier.java,v 1.4 2004/08/04 06:10:02 bass Exp $
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
 * This class brings the same functionality as the Linux crypt() function does.
 * See <b><tt>crypt(3)</tt></b> manual entry for details.
 *
 * @version $Revision: 1.4 $, $Date: 2004/08/04 06:10:02 $
 * @author $Author: bass $
 * @module servermisc_v1
 * @see "<b><tt>crypt(3)</tt></b> manual entry."
 */
public final class PasswordVerifier {
	/**
	 * Table of characters which can be used to BASE64-encode data.
	 */	
	private static final byte BASE64_TABLE[]
		= "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
		.getBytes();

	/**
	 * MD5-digest length.
	 *
	 * Value: {@value}
	 */
	private static final int DIGEST_LENGTH = 16;

	/**
	 * Length of the encoded key without salt.
	 *
	 * Value: {@value}
	 */
	private static final int ENCODED_KEY_LENGTH = 22;

	/**
	 * Maximum salt length.
	 *
	 * Value: {@value}
	 */
	private static final int MAX_MD5_SALT_LENGTH = 8;

//	/**
//	 * Regexp pattern encrypted key should match.
//	 */
//	private static final Pattern MD5_SALT_PATTERN
//		= Pattern.compile("^\\$1\\$([^\\$]{0,8})\\$[a-zA-Z0-9./]{22}$");

	/**
	 * MD5 salt prefix. Every encrypted key starts with this sequence.
	 *
	 * Value: {@value}
	 */
	private static final String MD5_SALT_PREFIX = "$1$";

	/**
	 * {@link #MD5_SALT_PREFIX} represented as byte array.
	 */
	private static final byte[] MD5_SALT_PREFIX_BYTES
		= MD5_SALT_PREFIX.getBytes();

	/**
	 * Length of {@link #MD5_SALT_PREFIX}.
	 */
	private static final int MD5_SALT_PREFIX_LENGTH
		= MD5_SALT_PREFIX.length();

	/**
	 * This character separates salt from encrypted key within the string
	 * containing both of them.
	 *
	 * Value: {@value}
	 */
	private static final char MD5_SALT_TERMINATOR = '$';

	/**
	 * Default MD5 salt.
	 */
	private static final byte[] DEFAULT_MD5_SALT
		= (MD5_SALT_PREFIX + "ABCDEFGH" + MD5_SALT_TERMINATOR)
		.getBytes();

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
	 * used. See <b><tt>crypt(3)</tt></b> manual page for details.
	 *
	 * @param key user-typed password.
	 * @param salt MD5 salt.
	 * @throws NoSuchAlgorithmException if portable implementation is used
	 *         and MD5 algorithm is unavailable.
	 * @return encrypted password.
	 * @see "<b><tt>crypt(3)</tt></b> manual entry."
	 */
	private static byte[] crypt(final byte key[], final byte salt[])
			throws NoSuchAlgorithmException {
		if (LIBRARY_LOADED)
			return nativeCrypt(key, salt);
		return portableCrypt(key, salt);
	}

	/**
	 * Interface to the native implementation.
	 *
	 * @param key user-typed password.
	 * @param salt MD5 salt.
	 * @return encrypted password.
	 */
	private static native byte[] nativeCrypt(final byte key[],
			final byte salt[]);

	/**
	 * Portable implementation.
	 *
	 * @param key user-typed password.
	 * @param salt MD5 salt.
	 * @throws NoSuchAlgorithmException if MD5 algorithm is unavailable.
	 * @return encrypted password.
	 */
	private static byte[] portableCrypt(final byte key[], final byte salt[])
			throws NoSuchAlgorithmException {
		String saltString = new String(salt);
		if (saltString.startsWith(MD5_SALT_PREFIX))
			saltString = saltString.
				substring(MD5_SALT_PREFIX_LENGTH);
		int saltLength = saltString.indexOf(MD5_SALT_TERMINATOR);
		if (saltLength == -1)
			saltLength = saltString.length();
		saltLength = Math.min(saltLength, MAX_MD5_SALT_LENGTH);
		byte innerSalt[] = saltString.substring(0, saltLength).
			getBytes();
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

		byte buffer[] = new byte[MD5_SALT_PREFIX_LENGTH + saltLength + 1
			+ ENCODED_KEY_LENGTH];

		int bufferPosition = 0;

		System.arraycopy(MD5_SALT_PREFIX_BYTES, 0, buffer,
			bufferPosition, MD5_SALT_PREFIX_LENGTH);
		bufferPosition += MD5_SALT_PREFIX_LENGTH;

		System.arraycopy(innerSalt, 0, buffer, bufferPosition,
			saltLength);
		bufferPosition += saltLength;

		buffer[bufferPosition] = MD5_SALT_TERMINATOR;
		bufferPosition++;

		base64From24Bit(altResult[0], altResult[6], altResult[12],
			buffer, bufferPosition, 4);
		bufferPosition += 4;

		base64From24Bit(altResult[1], altResult[7], altResult[13],
			buffer, bufferPosition, 4);
		bufferPosition += 4;

		base64From24Bit(altResult[2], altResult[8], altResult[14],
			buffer, bufferPosition, 4);
		bufferPosition += 4;

		base64From24Bit(altResult[3], altResult[9], altResult[15],
			buffer, bufferPosition, 4);
		bufferPosition += 4;

		base64From24Bit(altResult[4], altResult[10], altResult[5],
			buffer, bufferPosition, 4);
		bufferPosition += 4;

		base64From24Bit((byte) 0, (byte) 0, altResult[11], buffer,
			bufferPosition, 2);

		return buffer;
	}

	/**
	 * Generates a random salt.
	 *
	 * @todo Implement this method. Salt mustn't contain colons, as
	 *       /etc/shadow uses them for field separation.
	 * @return random MD5 salt.
	 */
	private static byte[] generateSalt() {
		return DEFAULT_MD5_SALT;
	}

	/**
	 * Extracts salt from an encrypted password.
	 *
	 * @todo Revert to the original implementation as soon as possible.
	 * @param encryptedKey encrypted password.
	 * @return MD5 salt.
	 */
	private static byte[] extractSalt(byte encryptedKey[]) {
//		Matcher m = MD5_SALT_PATTERN.matcher(new String(encryptedKey));
//		m.matches();
//		return m.group(1).getBytes();
		/*
		 * No error checking is done: this is just a temporary
		 * implementation.
		 */
		int saltLength = encryptedKey.length - MD5_SALT_PREFIX_LENGTH
			- ENCODED_KEY_LENGTH - 1;
		byte salt[] = new byte[saltLength];
		System.arraycopy(encryptedKey, MD5_SALT_PREFIX_LENGTH, salt, 0,
			saltLength);
		return salt;
	}

	/**
	 * Changes the user's password to a new one provided the old one
	 * supplied.
	 *
	 * @param oldKey old user-typed password.
	 * @param oldEncryptedKey old password encrypted.
	 * @param newKey new user-typed password.
	 * @throws GeneralSecurityException if old password supplied is invalid.
	 * @return new password encrypted.
	 */	
	public static byte[] setPassword(byte oldKey[], byte oldEncryptedKey[],
			byte newKey[]) throws GeneralSecurityException {
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
	public static void checkPassword(byte key[], byte encryptedKey[])
			throws GeneralSecurityException {
		if (!Arrays.equals(encryptedKey,
				crypt(key, extractSalt(encryptedKey))))
			throw new GeneralSecurityException("Invalid password");
	}

	/**
	 * Encodes <code>b0</code>, <code>b1</code> and <code>b2</code> in a way
	 * similar to BASE64 encoding and stores the resulting data (of length
	 * <code>byteCount</code>) in <code>buffer</code>, starting from
	 * <code>bufferPosition</code>.
	 *
	 * @param b2 a byte in a 3-byte (24-bit) sequence.
	 * @param b1 a byte in a 3-byte (24-bit) sequence.
	 * @param b0 a byte in a 3-byte (24-bit) sequence.
	 * @param buffer the buffer to which encoded data should be recorded.
	 * @param bufferPosition offset within the buffer. Encoded data is
	 *        written to the subsequent buffer entries.
	 * @param byteCount number of bytes (of encoded data) to store in
	 *        buffer.
	 */	
	private static void base64From24Bit(byte b2, byte b1, byte b0,
			byte buffer[], int bufferPosition, int byteCount) {
		int w = ((b2 & 0xff) << 16) | ((b1 & 0xff) << 8) | (b0 & 0xff);
		while (byteCount-- > 0) {
			buffer[bufferPosition++] = BASE64_TABLE[w & 0x3f];
			w >>= 6;
		}						
	}
}
