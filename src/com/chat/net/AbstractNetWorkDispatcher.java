package com.chat.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.chat.controll.Manager;
import com.chat.msgpacket.Message;
import com.sun.crypto.provider.SunJCE;

public abstract class AbstractNetWorkDispatcher {

	// 定义两静态的常量，用byte类型的字符表示，用不标只数据是否需要加密。
	private static final byte BLOCK_ENCRYPTED = (byte) 0xba;
	private static final byte BLOCK_UNENCRYPTED = (byte) 0xab;

	private boolean keySet = false;
	private Cipher encCipher;
	private Cipher decCipher;
	SecretKeySpec keyIV;
	private PBEParameterSpec paramSpec;
	private SecretKey secretKey;

	// 定义一个字节类型的随机串
	private static byte[] salt = { (byte) 0xc9, (byte) 0x53, (byte) 0x67, (byte) 0x9a, (byte) 0x5b, (byte) 0xc8,
			(byte) 0xae, (byte) 0x18 };

	public void setKey(String key) {
		if (key == null || key.length() < 1) {

			keySet = false;
			return;
		}

		keySet = true;

		Provider sunJce = new SunJCE();
		Security.addProvider(sunJce);
		paramSpec = new PBEParameterSpec(salt, 20);

		PBEKeySpec keySpec = new PBEKeySpec(key.toCharArray());

		try {
			SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");

			secretKey = keyfactory.generateSecret(keySpec);

			encCipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
			decCipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int getMaxiFileSize() {
		if (keySet)
			return decCipher.getOutputSize(6500);
		else
			return 6500;
	}

	protected AbstractNetWorkDispatcher() {
		try {
			decCipher = Cipher.getInstance("PBEWithMD5AndDES");
			encCipher = Cipher.getInstance("PBEWithMD5AndDES");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected abstract void DispatchToAll(byte[] iBuf, int iSize);

	protected void DispathcToAll(byte[] iBuf) throws Exception {
		DispatchToAll(iBuf, iBuf.length);

	}

	public void DispatchToAll(Message msg) {

		ByteArrayOutputStream bOut = new ByteArrayOutputStream();

		boolean encData = (keySet && !msg.dontEncrypt());

		if (encData) {
			// 如果数据已经被加密了，那么久写入一个字节的数据域
			bOut.write(BLOCK_ENCRYPTED);
		} else {
			bOut.write(BLOCK_UNENCRYPTED);
		}

		OutputStream out = bOut;
		if (encData) {
			out = new CipherOutputStream(out, encCipher);
		}

		try {
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeObject(msg);

			byte[] msgSerializeObj = bOut.toByteArray();

			DispathcToAll(msgSerializeObj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void dataReceive(byte[] byteRecv, int length) {

		if (byteRecv[0] == BLOCK_ENCRYPTED) {
			if (!keySet) {
				return;
			}
			byte[] outBuffer = new byte[decCipher.getOutputSize(length - 1) + 1];
			try {
				length = decCipher.doFinal(byteRecv, 1, length - 1, outBuffer, 1);
			} catch (ShortBufferException | IllegalBlockSizeException | BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byteRecv = outBuffer;

		} else if (byteRecv[0] != BLOCK_UNENCRYPTED) {
			return;

		}

		ByteArrayInputStream bytearrayin = new ByteArrayInputStream(byteRecv);

		try {
			ObjectInputStream objIn = new ObjectInputStream(bytearrayin);
			Message msg = (Message) objIn.readObject();
			bytearrayin.close();
			Manager.getInsance().dispatchRecevMsg(msg);

		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
