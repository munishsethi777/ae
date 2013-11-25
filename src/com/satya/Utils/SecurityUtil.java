package com.satya.Utils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

public class SecurityUtil {

	private final static String CIPHER_PARAMS = "DES/ECB/PKCS5Padding";
	private final static String password = "m8dwhZQB7+Y=";
	private SecretKey secretKey = null;

	public void init() {
		try {
			DESKeySpec key = new DESKeySpec(password.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			secretKey = keyFactory.generateSecret(key);
		} catch (Exception e) {
		}
	}
	
	private String encryptBase64(String unencryptedString, Cipher encryptCipher)
			throws Exception {
		try {
			// Encode the string into bytes using utf-8
			byte[] unencryptedByteArray = unencryptedString.getBytes("UTF8");
			// Encrypt
			byte[] encryptedBytes = encryptCipher.doFinal(unencryptedByteArray);
			// Encode bytes to base64 to get a string
			byte[] encodedBytes = Base64.encodeBase64(encryptedBytes);	
			return new String(encodedBytes);
		} catch (Throwable e) {
			System.out.print(e.getMessage());
		}
		return null;
	}
	
	private String decryptBase64(String encryptedString, Cipher encryptCipher)
			throws Exception {
		try {
			byte[] encryptedByteArray = encryptedString.getBytes("UTF8");
			byte[] encryptedBytes = Base64.decodeBase64(encryptedByteArray);
			byte[] decryptedBytes = encryptCipher.doFinal(encryptedBytes);
			
			return new String(decryptedBytes);
		} catch (Throwable e) {
			System.out.print(e.getMessage());
		}
		return null;
	}

	public String getEncryptedString(String plainText) {
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_PARAMS);
			cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
			String encryptedString = encryptBase64(plainText, cipher);
			return encryptedString;
		} catch (Throwable t) {
		}
		return null;
	}
	
	public String getDecryptedString(String encryptedText) {
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_PARAMS);
			cipher.init(Cipher.DECRYPT_MODE, this.secretKey);
			String plainString = decryptBase64(encryptedText, cipher);
			return plainString;
		} catch (Throwable t) {
		}
		return null;
	}
	
	
	
	public static boolean isNumeric(String integer) {
		if ((integer == null) || (integer.length() == 0))
			return false;
		int index = 0;
		char character = 0;
		for (; index < integer.length(); index++) {
			if (integer.charAt(index) == '.')
				continue;
			if (((character = integer.charAt(index)) < '0')
					|| (character > '9'))
				return false;
		}
		return true;
	}

	public static boolean IsValidEmailAddress(String emailAddress) {
		if (emailAddress == null)
			return false;
		if (emailAddress.length() < 7)
			return false;
		int atIndex = emailAddress.indexOf('@');
		if ((atIndex < 1) || (atIndex == (emailAddress.length() - 1)))
			return false;
		int dotIndex = emailAddress.indexOf('.', atIndex);
		if ((dotIndex < (atIndex + 2))
				|| (atIndex == (emailAddress.length() - 1)))
			return false;
		char character = 0;
		for (int i = emailAddress.length() - 1; i >= 0; i--) {
			character = emailAddress.charAt(i);
			if (!Character.isLetterOrDigit(character) && (character != '.')
					&& (character != '-') && (character != '_')
					&& (character != '~') && (i != atIndex))
				return false;
		}
		return true;
	}

	/**
	 * Generates a secret key pair based on RSA algorthm. The only change is
	 * that two key primary numbers for generation of public key and primary key
	 * are hard-coded which will result in always and always the same set of
	 * public and private keys. As mentioned in SecurityUtils.encrypt() and
	 * .decrypt() java-doc, we propose to change it to generate public and
	 * private keys from purely random numbers and be done at deployment time.
	 */
	private static String generateSecretKeyString() {
		byte[] keyBytes = null;
		try {
			KeyGenerator desGen = KeyGenerator.getInstance("DES");
			SecretKey desKey = desGen.generateKey();
			SecretKeyFactory desFactory = SecretKeyFactory.getInstance("DES");
			DESKeySpec desSpec = (DESKeySpec) desFactory.getKeySpec(desKey,
					javax.crypto.spec.DESKeySpec.class);
			keyBytes = desSpec.getKey();
			Base64 encoder = new Base64();
			keyBytes = encoder.encode(keyBytes);
		} catch (NoSuchAlgorithmException nse) {
			throw new RuntimeException("Error generating key", nse);
		} catch (InvalidKeySpecException ikse) {
			throw new RuntimeException("Error generating key", ikse);
		}
		return new String(keyBytes);
	}

	private static SecretKey generateSecretKey() {
		String keyStr = generateSecretKeyString();
		try {
			KeyGenerator desGen = KeyGenerator.getInstance("DES");
			SecretKey desKey = desGen.generateKey();
			return desKey;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
