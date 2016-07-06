package com.chat.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.chat.controll.Manager;
import com.chat.msgpacket.Message;
import com.sun.crypto.provider.SunJCE;

public abstract class AbstractNetWorkDispatcher {

	// ��������̬�ĳ�������byte���͵��ַ���ʾ���ò���ֻ�����Ƿ���Ҫ���ܡ�
	private static final byte BLOCK_ENCRYPTED = (byte) 0xba;
	private static final byte BLOCK_UNENCRYPTED = (byte) 0xab;

	private boolean keySet = false;
	private Cipher encCipher;
	private Cipher decCipher;
	SecretKeySpec keyIV;
	private PBEParameterSpec paramSpec;
	private SecretKey secretKey;

	// ����һ���ֽ����͵������
	private static byte[] salt = { (byte) 0xc9, (byte) 0x53, (byte) 0x67, (byte) 0x9a, (byte) 0x5b, (byte) 0xc8,
			(byte) 0xae, (byte) 0x18 };

	public void SetKey(String key) {
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

	/**
	 * return_type : void TODO:��������
	 * 
	 * @param iKey����ʾ�ַ������͵�������Ϣ
	 */
	public void setKey(String iKey) {
		if (iKey == null || iKey.length() <= 0) {
			keySet = false;
			return;
		}
		////////////// �˴��������ݼ��ܵĻ��ơ�����������ı���������Java�����̼���//////////////////////
		keySet = true;
		try {
			Provider sunJce = new SunJCE();
			Security.addProvider(sunJce);
			paramSpec = new PBEParameterSpec(salt, 20);

			PBEKeySpec keySpec = new PBEKeySpec(iKey.toCharArray());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			secretKey = keyFactory.generateSecret(keySpec);

			encCipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
			decCipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);

			///////////////////////////////////////////////////////////////////////////////////////////////
		} catch (Exception ex) {
			ex.printStackTrace();
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
			// ��������Ѿ��������ˣ���ô��д��һ���ֽڵ�������
			bOut.write(BLOCK_ENCRYPTED);
		} else {
			bOut.write(BLOCK_UNENCRYPTED);
		}

		OutputStream out = bOut;
		if (encData) {
			out = new CipherOutputStream(bOut, encCipher);
		}
		if (msg.getTag() != 'a')
			System.out.println("�������ݣ�" + msg.getTag() + " �Ƿ���ܣ�" + encData);
		try {
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeObject(msg);
			objOut.close();// ע��close��bOut.toByteArray()֮ǰ���ã�ԭ���ǣ��������close֮��ŻὫ�������е�����ˢ�µ��ֽ����У�Ȼ�����ǵõ��������ֽ����ݣ�����ͣ���������������ݲ����������⣬���ʱ������߾��޷�������ȷ������
			byte[] msgSerializeObj = bOut.toByteArray();

			// System.out.println("������Ϣtag��" + msg.getTag());
			DispathcToAll(msgSerializeObj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void dataReceive(byte[] byteRecv, int length) {
		DataReceived(byteRecv, length);
		// if (byteRecv[0] == BLOCK_ENCRYPTED) {
		// System.out.println("�����Ѿ���������");
		// if (!keySet) {//�������͵�����Ϊkey��ʱ��ż���
		// return;
		// }
		// byte[] outBuffer = new byte[decCipher.getOutputSize(length-1) + 1];
		// try {
		// length = decCipher.doFinal(byteRecv, 1, length-1, outBuffer, 1)+1;
		// } catch (ShortBufferException | IllegalBlockSizeException |
		// BadPaddingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// byteRecv = outBuffer;
		//
		// } else if (byteRecv[0] != BLOCK_UNENCRYPTED) {
		// return;
		//
		// }
		//
		//
		// ByteArrayInputStream bytearrayin = new
		// ByteArrayInputStream(byteRecv,1,length-1);
		//
		// try {
		// ObjectInputStream objIn = new ObjectInputStream(bytearrayin);
		// Message msg = (Message) objIn.readObject();
		// System.out.println("msg���ݣ�"+msg.getMsgContent());
		// bytearrayin.close();
		// Manager.getInsance().dispatchRecevMsg(msg);
		//
		// } catch (IOException | ClassNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	/**
	 * return_type : void TODO:�������ݣ�
	 * 
	 * @param iBuf���洢���ݵĻ�����
	 * @param iLen���������ݵĳ���
	 */
	protected void DataReceived(byte[] iBuf, int iLen) {
		try {

			// ���ռ�������ʱ�Ĵ���
			if (iBuf[0] == BLOCK_ENCRYPTED) {
				if (!keySet) {
					System.out.println("reutrn1");
					return;
				}
				
				// ���ݽ��ܹ���
				byte[] outBuf = new byte[decCipher.getOutputSize(iLen - 1) + 1];
				iLen = decCipher.doFinal(iBuf, 1, iLen - 1, outBuf, 1) + 1;// ����
				
				iBuf = outBuf;
			} else if (iBuf[0] != BLOCK_UNENCRYPTED) {
				System.out.println("reutrn2");
				return;
			}
			ByteArrayInputStream bIn = new ByteArrayInputStream(iBuf, 1, iLen - 1);
			ObjectInputStream ooStream = new ObjectInputStream(bIn);
			Object msgIn = ooStream.readObject();
			ooStream.close();
			Message recMsg = (Message) msgIn;
			if (recMsg.getTag() != 'a') {

				System.out.println("���ܹ�����ϣ���ʼ�ַ���Ϣ,tag:" + recMsg.getTag());
			}

			Manager.getInsance().dispatchRecevMsg(recMsg);

		} catch (BadPaddingException ex) {

			try {
				decCipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
			} catch (Exception exc) {
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
