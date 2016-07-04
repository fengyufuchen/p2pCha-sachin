package com.chat.controll;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.channels.AcceptPendingException;
import java.security.Provider.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.text.Segment;

import com.bean.NetworkSegment;
import com.bean.PeerNode;
import com.chat.msgpacket.ACKServiceMessage;
import com.chat.msgpacket.Message;
import com.chat.net.AbstractNetWorkDispatcher;

public class Manager {

	private static Manager manager;

	public static final int DefaultOperTimeOut = 3000;
	private static final int MAX_RECVFILES_NUM = 50;
	private static final String DEFAULT_BoradcastGroup = "228.1.1.1";// 组播ip地址
	private static final int DEFAULT_BROADCASTPORT = 3305;
	private MulticastSocket mSocket;
	// 定义一个接收文件的缓冲区
	private static final int RecvBufSize = 65536;

	private List<NetworkSegment> segmentList = new ArrayList<NetworkSegment>();

	private TreeMap<PeerNode, Object> treeShareFilesMap = new TreeMap<>();

	private NetworkSegment curSegment;
	private String reqSegmentName;

	private NetworkSegmentBoradcastThread segmentBoradcastThread;

	private PeerNode mePeer;
	private boolean segmentNameIsFree;
	public Object waitObject;

	private AbstractNetWorkDispatcher networkDispatch;

	public static Manager getInsance() {
		if (manager == null)
			manager = new Manager();

		return manager;

	}

	private Manager() {
		try {
			InetAddress addr = InetAddress.getByAddress(DEFAULT_BoradcastGroup.getBytes("UTF-8"));
			mSocket = new MulticastSocket(DEFAULT_BROADCASTPORT);
			mSocket.joinGroup(addr);

			// 启动一个线程用于监其他对等节点广播消息

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public PeerNode getMePeer() {
		return mePeer;
	}

	public void setMePeer(PeerNode mePeer) {
		this.mePeer = mePeer;
	}

	public NetworkSegment getCurrentNetworkSegment() {
		return curSegment;

	}

	public void setADThread(NetworkSegment segment) {

		curSegment = segment;
		if (segmentBoradcastThread != null)
			segmentBoradcastThread.stop();

		segmentBoradcastThread = new NetworkSegmentBoradcastThread();

	}

	public AbstractNetWorkDispatcher getNetworkDispatch() {
		return networkDispatch;
	}

	public void setNetworkDispatch(AbstractNetWorkDispatcher networkDispatch) {
		this.networkDispatch = networkDispatch;
	}

	public void dispatchRecevMsg(Message msg) {

		if (msg instanceof ACKServiceMessage) {
			dealACKServiceManager((ACKServiceMessage) msg);

		}

	}

	private void dealACKServiceManager(ACKServiceMessage msg) {
		// 如果当前接收到的消息不是广播并且消息的接收者不是我，那么我应该返回
		if (!msg.isBroadcast() && !msg.getMsgToPeerNdoe().equals(getMePeer())) {
			return;

		}

		char msgTag = msg.getTag();
		switch (msgTag) {

		case ACKServiceMessage.peernode_name:
			if (getMePeer().getName().equals(msg.getMsgContent()) && getMePeer().getCurState() == PeerNode.AUTHORED) {

				// 如果该节点的名称和发送消息的节点的名称相同，那么就回应广播

				ACKServiceMessage replayMsg = new ACKServiceMessage(getMePeer(), ACKServiceMessage.peernode_name_used,
						getMePeer().getName());
				this.networkDispatch.DispatchToAll(replayMsg);

			}

			break;
		case ACKServiceMessage.peernode_name_used:

			if (!getMePeer().getName().equals(msg.getMsgContent()) && getMePeer().getCurState() == PeerNode.ASK_AUTHO) {
				return;
			}
			getMePeer().setCurState(PeerNode.NICKNAME_AUTHOFAILED);

			break;
		case ACKServiceMessage.chan_name:
			// 判断保存的频道集合中有没有这样一个频道？
			if (NetworkSegment.isContainThisSegment(msg.getMsgContent())) {

				ACKServiceMessage ackmsg = new ACKServiceMessage(getMePeer(), ACKServiceMessage.chan_name_used,
						msg.getMsgContent());

				Manager.getInsance().getNetworkDispatch().DispatchToAll(ackmsg);

			}

			break;
		case ACKServiceMessage.chan_name_used:
			if (curSegment == null) {// 新创建的结点没有所属
				segmentNameIsFree = false;
			} else {
				segmentNameIsFree = true;
			}

			break;
		case ACKServiceMessage.askfor_join:
			if (curSegment == null)
				reqSegmentName = msg.getMsgContent();
			if (!curSegment.getName().equals(msg.getMsgContent())) {
				return;
			}
			ACKServiceMessage ackMsg = new ACKServiceMessage(getMePeer(), ACKServiceMessage.welcome_join,
					curSegment.getName());
			Manager.getInsance().getNetworkDispatch().DispatchToAll(ackMsg);
			
			if(curSegment.)
			

			break;

		case ACKServiceMessage.welcome_join:
			break;
		case ACKServiceMessage.chan_owner:
			break;

		case ACKServiceMessage.chan_avd:
			break;

		}
	}

	/**
	 * 通过发送广播来验证名称是否唯一
	 * 
	 * @param peerName
	 * @return
	 */
	public boolean tryToIdentifyPeerName(String peerName) {

		PeerNode pPeerNode = new PeerNode(PeerNode.ASK_AUTHO, peerName);

		setMePeer(pPeerNode);
		ACKServiceMessage ackMsg = new ACKServiceMessage(pPeerNode.mAnonymous, ACKServiceMessage.peernode_name,
				peerName);

		Manager.getInsance().getNetworkDispatch().DispatchToAll(ackMsg);

		// 停顿一些时间
		try {
			Thread.sleep(DefaultOperTimeOut);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (mePeer.getCurState() == PeerNode.NICKNAME_AUTHOFAILED) {
			return false;
		}
		mePeer.setCurState(PeerNode.AUTHORED);
		return true;

	}

	/**
	 * 通过发送广播来验证频道名称是否唯一
	 * 
	 * @param peerName
	 * @return
	 */
	public boolean tryToIdentifySegmentName(String segmentName) {
		segmentNameIsFree = true;
		ACKServiceMessage ackMsg = new ACKServiceMessage(getMePeer(), ACKServiceMessage.chan_name, segmentName);

		Manager.getInsance().getNetworkDispatch().DispatchToAll(ackMsg);

		// 停顿一些时间
		try {
			Thread.sleep(DefaultOperTimeOut);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return segmentNameIsFree;

	}

	public static void requestJoinInNetworkSegemnt(String segmentName, String key) {

		// 发送消息

		// 创建要发送的消息bean

		ACKServiceMessage ackmsg = new ACKServiceMessage(Manager.getInsance().getMePeer(),
				ACKServiceMessage.askfor_join, segmentName);

		Manager.getInsance().getNetworkDispatch().DispatchToAll(ackmsg);

	}

	private class BoradcastReceiverThread extends Thread {

		private MulticastSocket mSocket;// 用于接收数据
		private AbstractNetWorkDispatcher mDispatcher;// 用于解密数据

		public BoradcastReceiverThread(MulticastSocket pSocket, AbstractNetWorkDispatcher pDispather) {
			this.mSocket = pSocket;
			this.mDispatcher = pDispather;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			byte[] buffer = new byte[RecvBufSize];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

			try {
				mSocket.receive(packet);
				mDispatcher.dataReceive(buffer, buffer.length);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
