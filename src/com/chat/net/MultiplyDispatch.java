package com.chat.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.spi.CurrencyNameProvider;

public class MultiplyDispatch extends AbstractNetWorkDispatcher {
	private static final String DEFAULT_BoradcastGroup = "230.1.1.1";// �鲥ip��ַ
	private static final int DEFAULT_BROADCASTPORT = 1314;
	private MulticastSocket mSocket;
	// ����һ�������ļ��Ļ�����
	private static final int RecvBufSize = 65536;
	InetAddress addr;

	public MultiplyDispatch() {
		
	
		try {
			addr = InetAddress.getByName(DEFAULT_BoradcastGroup);
			mSocket = new MulticastSocket(DEFAULT_BROADCASTPORT);
			mSocket.joinGroup(addr);
			// ����һ���߳����ڼ������ԵȽڵ�㲥��Ϣ
			BroadcastReceiverThread broadcastReceiver = new BroadcastReceiverThread(mSocket, this);
			broadcastReceiver.start();// ���ڽ��������ԵȽ�㷢�͵Ĺ㲥��Ϣ
		} catch (UnknownHostException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	private class BroadcastReceiverThread extends Thread {

		private MulticastSocket mSocket;// ���ڽ�������
		private AbstractNetWorkDispatcher mDispatcher;// ���ڽ�������

		public BroadcastReceiverThread(MulticastSocket pSocket, AbstractNetWorkDispatcher multiplyDispatch) {
			this.mSocket = pSocket;
			this.mDispatcher = multiplyDispatch;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			byte[] buffer = new byte[RecvBufSize];
			DatagramPacket packet = new DatagramPacket(buffer, RecvBufSize);

			try {
			for(;;){
				mSocket.receive(packet);
				mDispatcher.dataReceive(packet.getData(), packet.getLength());
				
			}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     
			
		}

	}

	@Override
	protected void DispatchToAll(byte[] iBuf, int iSize) {
		// TODO Auto-generated method stub
		DatagramPacket packet=new DatagramPacket(iBuf, iSize,addr,DEFAULT_BROADCASTPORT);//����ط��ڴ������ݰ���ʱ�������ָ���˿ں͵�ַ����ô����ᱨ��java.lang.NullPointerException: null address || null buffer
		
		try {
			mSocket.send(packet);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
