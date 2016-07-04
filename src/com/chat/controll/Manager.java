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
	private static final String DEFAULT_BoradcastGroup = "228.1.1.1";// �鲥ip��ַ
	private static final int DEFAULT_BROADCASTPORT = 3305;
	private MulticastSocket mSocket;
	// ����һ�������ļ��Ļ�����
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

			// ����һ���߳����ڼ������ԵȽڵ�㲥��Ϣ

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
		// �����ǰ���յ�����Ϣ���ǹ㲥������Ϣ�Ľ����߲����ң���ô��Ӧ�÷���
		if (!msg.isBroadcast() && !msg.getMsgToPeerNdoe().equals(getMePeer())) {
			return;

		}

		char msgTag = msg.getTag();
		switch (msgTag) {

		case ACKServiceMessage.peernode_name:
			if (getMePeer().getName().equals(msg.getMsgContent()) && getMePeer().getCurState() == PeerNode.AUTHORED) {

				// ����ýڵ�����ƺͷ�����Ϣ�Ľڵ��������ͬ����ô�ͻ�Ӧ�㲥

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
			// �жϱ����Ƶ����������û������һ��Ƶ����
			if (NetworkSegment.isContainThisSegment(msg.getMsgContent())) {

				ACKServiceMessage ackmsg = new ACKServiceMessage(getMePeer(), ACKServiceMessage.chan_name_used,
						msg.getMsgContent());

				Manager.getInsance().getNetworkDispatch().DispatchToAll(ackmsg);

			}

			break;
		case ACKServiceMessage.chan_name_used:
			if (curSegment == null) {// �´����Ľ��û������
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
	 * ͨ�����͹㲥����֤�����Ƿ�Ψһ
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

		// ͣ��һЩʱ��
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
	 * ͨ�����͹㲥����֤Ƶ�������Ƿ�Ψһ
	 * 
	 * @param peerName
	 * @return
	 */
	public boolean tryToIdentifySegmentName(String segmentName) {
		segmentNameIsFree = true;
		ACKServiceMessage ackMsg = new ACKServiceMessage(getMePeer(), ACKServiceMessage.chan_name, segmentName);

		Manager.getInsance().getNetworkDispatch().DispatchToAll(ackMsg);

		// ͣ��һЩʱ��
		try {
			Thread.sleep(DefaultOperTimeOut);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return segmentNameIsFree;

	}

	public static void requestJoinInNetworkSegemnt(String segmentName, String key) {

		// ������Ϣ

		// ����Ҫ���͵���Ϣbean

		ACKServiceMessage ackmsg = new ACKServiceMessage(Manager.getInsance().getMePeer(),
				ACKServiceMessage.askfor_join, segmentName);

		Manager.getInsance().getNetworkDispatch().DispatchToAll(ackmsg);

	}

	private class BoradcastReceiverThread extends Thread {

		private MulticastSocket mSocket;// ���ڽ�������
		private AbstractNetWorkDispatcher mDispatcher;// ���ڽ�������

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
