package com.chat.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.spi.CurrencyNameProvider;

public class MultiplyDispatch extends AbstractNetWorkDispatcher {
	private static final String DEFAULT_BoradcastGroup = "230.1.1.1";// 组播ip地址
	private static final int DEFAULT_BROADCASTPORT = 1314;
	private MulticastSocket mSocket;
	// 定义一个接收文件的缓冲区
	private static final int RecvBufSize = 65536;
	InetAddress addr;

	public MultiplyDispatch() {
		
	
		try {
			addr = InetAddress.getByName(DEFAULT_BoradcastGroup);
			mSocket = new MulticastSocket(DEFAULT_BROADCASTPORT);
			mSocket.joinGroup(addr);
			// 启动一个线程用于监其他对等节点广播消息
			BroadcastReceiverThread broadcastReceiver = new BroadcastReceiverThread(mSocket, this);
			broadcastReceiver.start();// 用于接收其他对等结点发送的广播消息
		} catch (UnknownHostException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	private class BroadcastReceiverThread extends Thread {

		private MulticastSocket mSocket;// 用于接收数据
		private AbstractNetWorkDispatcher mDispatcher;// 用于解密数据

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
		DatagramPacket packet=new DatagramPacket(iBuf, iSize,addr,DEFAULT_BROADCASTPORT);//这个地方在创建数据包的时候如果不指定端口和地址，那么程序会报错：java.lang.NullPointerException: null address || null buffer
		
		try {
			mSocket.send(packet);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
