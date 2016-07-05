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
			out = new CipherOutputStream(bOut, encCipher);
		}

		try {
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeObject(msg);

			byte[] msgSerializeObj = bOut.toByteArray();
			objOut.close();
			System.out.println("msgSerializeObj" + msgSerializeObj);
			DispathcToAll(msgSerializeObj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void dataReceive(byte[] byteRecv, int length) {
		DataReceived(byteRecv,length);
//		if (byteRecv[0] == BLOCK_ENCRYPTED) {
//			System.out.println("数据已经被加密了");
//			if (!keySet) {//仅当传送的数据为key的时候才加密
//				return;
//			}
//			byte[] outBuffer = new byte[decCipher.getOutputSize(length - 1) + 1];
//			try {
//				length = decCipher.doFinal(byteRecv, 1, length - 1, outBuffer, 1);
//			} catch (ShortBufferException | IllegalBlockSizeException | BadPaddingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			byteRecv = outBuffer;
//
//		} else if (byteRecv[0] != BLOCK_UNENCRYPTED) {
//			return;
//
//		}
//		
//
//		ByteArrayInputStream bytearrayin = new ByteArrayInputStream(byteRecv,1,length-1);
//
//		try {
//			ObjectInputStream objIn = new ObjectInputStream(bytearrayin);
//			Message msg = (Message) objIn.readObject();
//			System.out.println("msg内容："+msg.getMsgContent());
//			bytearrayin.close();
//			Manager.getInsance().dispatchRecevMsg(msg);
//
//		} catch (IOException | ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
	/**
     * return_type : void
     * TODO:接收数据，
     * @param iBuf，存储数据的缓冲区
     * @param iLen，接收数据的长度
     */
    protected void DataReceived(byte []iBuf,int iLen){
        try {   
        	System.out.println("收到消息，开始解密");
        	//接收加密数据时的处理
            if(iBuf[0]==BLOCK_ENCRYPTED){
                if(!keySet)
                    return;
                //数据解密过程
                 byte []outBuf=new byte[decCipher.getOutputSize(iLen-1)+1];
                 iLen=decCipher.doFinal(iBuf,1,iLen-1,outBuf,1)+1;//解密
                 iBuf=outBuf;
            }else if(iBuf[0]!=BLOCK_UNENCRYPTED){
            	
                return;
            }
            ByteArrayInputStream bIn=new ByteArrayInputStream(iBuf,1,iLen-1);
            ObjectInputStream ooStream=new ObjectInputStream(bIn);
            Object msgIn=ooStream.readObject();
            ooStream.close();
            Message recMsg=(Message)msgIn;
            System.out.println("解密过程完毕，开始分发消息");
            Manager.getInsance().dispatchRecevMsg(recMsg);
            
        } 
        catch(BadPaddingException ex){
            
            try {
                decCipher.init(Cipher.DECRYPT_MODE,secretKey, paramSpec);
            } catch (Exception exc) {}
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        
    }

}
