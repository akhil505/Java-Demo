package com.datamigration.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ResourceBundle;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * @author
 *
 */
public class PasswordCryption {
	static final Logger LOGGER=Logger.getLogger(PasswordCryption.class);
	final static String salt = "DESSSALTKEY";
	final static int iterations = 10000;
	final static int keylength = 128;

	/**
	 * @param plainText
	 * @param key
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidKeySpecException
	 * @throws Exception
	 */
	public static String encrypt(final String password, final String passphrase)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, InvalidKeySpecException {
		String algorithm = DataMigrationConstants.ENCRYPT_ALGORITHM;
		String algOnly = algorithm.split("/")[0];
		SecretKeyFactory factory = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1");
		SecretKeySpec key = new SecretKeySpec(factory.generateSecret(
				new PBEKeySpec(passphrase.toCharArray(), salt.getBytes(),
						iterations, keylength)).getEncoded(), algOnly);
		Cipher crypto = Cipher.getInstance(algorithm);
		crypto.init(Cipher.ENCRYPT_MODE, key);
		byte[] encryptedBytes = crypto.doFinal(password.getBytes());
		String encryptPwd = Base64.encodeBase64String(encryptedBytes);
		return encryptPwd;
	}

	/**
	 * @param encryptedText
	 * @param secretKey
	 * @return
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidKeySpecException
	 * @throws Exception
	 */
	public static String decrypt(final String encryptedText, final String passphrase)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, InvalidKeySpecException {
		String algorithm = DataMigrationConstants.ENCRYPT_ALGORITHM;
		String algOnly = algorithm.split("/")[0];
		SecretKeyFactory factory = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1");
		SecretKeySpec key = new SecretKeySpec(factory.generateSecret(
				new PBEKeySpec(passphrase.toCharArray(), salt.getBytes(),
						iterations, keylength)).getEncoded(), algOnly);
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decryptedByte = cipher.doFinal(Base64.decodeBase64(encryptedText
				.getBytes()));
		String decryptedText = new String(decryptedByte);
		return decryptedText;
	}

	public static void main(String[] args) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException,
			InvalidKeySpecException {
		String key = ResourceBundle.getBundle("parameters").getString("key");
		String encStr = PasswordCryption.decrypt("YmZ2wmR4v4PtK8jrWHo4bA==", key);
		System.out.println(encStr);
	
	}
}