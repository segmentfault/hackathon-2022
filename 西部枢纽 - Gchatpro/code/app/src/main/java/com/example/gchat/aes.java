package com.example.gchat;
 
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
 
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
 
 
/**
 * AES 加密解密类
 * 
 * 
 * 
 */
public class aes {
 
	// KeyGenerator 提供对称密钥生成器的功能
	private static KeyGenerator keygen;
	// SecretKey 负责保存对称密钥
	private static SecretKey deskey;
	// Cipher负责完成加密或解密工作
	private static Cipher c;
	// 该字节数组负责保存加密的结果
	private static byte[] cipherByte;
 
	static {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		// 实例化支持DES算法的密钥生成器(算法名称命名需按规定，否则抛出异常)
		try {
			keygen = KeyGenerator.getInstance("AES");
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 生成密钥
		
		keygen.init(new SecureRandom("这是密匙".getBytes()));
		deskey = keygen.generateKey();
		
		// 生成Cipher对象,指定其支持的DES算法
		try {
			c = Cipher.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 
//	public EncrypAES() throws NoSuchAlgorithmException, NoSuchPaddingException {
//		Security.addProvider(new com.sun.crypto.provider.SunJCE());
//		// 实例化支持DES算法的密钥生成器(算法名称命名需按规定，否则抛出异常)
//		keygen = KeyGenerator.getInstance("AES");
//		// 生成密钥
//		deskey = keygen.generateKey();
//		// 生成Cipher对象,指定其支持的DES算法
//		c = Cipher.getInstance("AES");
//	}
 
	/**
	 * 对字符串加密
	 * 
	 * @param str
	 * @return
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	
	public static String Encrytor(String str) throws InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		// 根据密钥，对Cipher对象进行初始化，ENCRYPT_MODE表示加密模式
		c.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] src = str.getBytes();
		// 加密，结果保存进cipherByte
		cipherByte = c.doFinal(src);
		return parseByte2HexStr(cipherByte);
	}
 
	/**
	 * 对字符串解密
	 * 
	 * @param buff
	 * @return
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String Decryptor(String buff) throws InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		// 根据密钥，对Cipher对象进行初始化，DECRYPT_MODE表示加密模式
		c.init(Cipher.DECRYPT_MODE, deskey);
		cipherByte = c.doFinal(parseHexStr2Byte(buff));
		return new String(cipherByte);
	}
 
	 /**
	 * @param args
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidKeyException
	 */
	 public static void main(String[] args) throws Exception {
	 EncrypAES de1 = new EncrypAES();
	 String msg ="lp;l;l;jmhj";
	 String encontent = de1.Encrytor(msg);
	 String decontent = de1.Decryptor(encontent);
	// System.out.println("明文是:" + msg);
	 //System.out.println("加密后:" + new String(encontent));
	 //System.out.println("解密后:" + new String(decontent));
	 }
 
	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}
 
	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
}