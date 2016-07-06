package com.chat.controll;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.JFrame;

import com.bean.NetworkSegment;
import com.bean.PeerNode;
import com.chat.msgpacket.ACKServiceMessage;
import com.chat.msgpacket.Message;
import com.chat.net.AbstractNetWorkDispatcher;
import com.chat.net.MultiplyDispatch;
import com.chat.uiview.MainFrame;
import com.chat.uiview.SettingFrame;

public class Manager {

	private static Manager manager;

	public static final int DefaultOperTimeOut = 3000;
	private static final int MAX_RECVFILES_NUM = 50;

	private List<String> segmentList = new ArrayList<String>();

	private TreeMap<PeerNode, Object> treeShareFilesMap = new TreeMap<>();

	private NetworkSegment curSegment;
	private String reqJoinSegmentName;

	private NetworkSegmentBoradcastThread segmentBoradcastThread;

	private PeerNode mePeer;
	private boolean segmentNameIsFree;
	public Object waitObject = new Object();
	private SettingFrame settingFrame;
	private MainFrame mainFrame;

	private AbstractNetWorkDispatcher networkDispatch;

	public static Manager getInsance() {
		return manager;

	}

	public static void createManager(JFrame frame) {
		manager = new Manager();
		manager.settingFrame = (SettingFrame) frame;
	}

	private Manager() {
		this.networkDispatch = new MultiplyDispatch();
		mePeer = PeerNode.mAnonymous;

	}

	public PeerNode getMePeer() {
		return mePeer;
	}

	

	public NetworkSegment getCurrentNetworkSegment() {
		return curSegment;

	}

	public void setMainFrame(MainFrame pMainFrame) {
		this.mainFrame = pMainFrame;

	}

	public MainFrame getMainFrame() {
		return this.mainFrame;

	}

	public void setADThread(NetworkSegment segment) {

		curSegment = segment;
		if (segmentBoradcastThread != null)
			segmentBoradcastThread.stop();

		segmentBoradcastThread = new NetworkSegmentBoradcastThread(curSegment);

		segmentBoradcastThread.start();// ���ڶ�ʱ�㲥��Ƶ��������

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

			if (getMePeer().getCurState() != PeerNode.ASK_AUTHO) {
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
			System.out.println("�յ��û�����������Ϣ");

			if (curSegment == null) {
				reqJoinSegmentName = msg.getMsgContent();
				return;
			}

			if (!curSegment.getName().equals(msg.getMsgContent())) {
				return;
			}
			ACKServiceMessage ackMsg = new ACKServiceMessage(getMePeer(), ACKServiceMessage.welcome_join,
					curSegment.getName());
			System.out.println("���ͻ�ӭ������Ϣ��");
			curSegment.addPerrNode(msg.getSender());
			Manager.getInsance().getNetworkDispatch().DispatchToAll(ackMsg);

			if (curSegment.getOwnerPeer().equals(getMePeer())) {
				// ��������segment�Ĵ�����
				ACKServiceMessage ackmsg = new ACKServiceMessage(getMePeer(), ACKServiceMessage.chan_owner,
						curSegment.getName());

				Manager.getInsance().getNetworkDispatch().DispatchToAll(ackmsg);
				System.out.println("Ƶ������֪ͨ");
			}

			break;

		case ACKServiceMessage.welcome_join:

			if (curSegment == null && reqJoinSegmentName.length() > 0 && reqJoinSegmentName.equals(msg.getMsgContent())) {

				curSegment = new NetworkSegment(reqJoinSegmentName);

				curSegment.addPerrNode(getMePeer());
				// �������������߲㣬һ��������ר�Ÿ���������չ㲥��Ϣ���̣߳����߳����Ƚ��յ���ϢȻ�������Ϣ��Ȼ���Ƿַ�������Ϣ��manger�Ĵ�����Ϣ�ķ�������������߳���ִ�е�
				// ����һ���߳����������̡߳��������������߳������������1������ɹ�����ʱ���ɴ�����Ϣ���߳������������̣߳�2
				// �����벻�䣬���Դ����߳��޷�����ֻ�ܵȴ���Ϊ�˱��ⳤʱ��ĵȴ�����ʹ���˴���ʱ���wait����
				synchronized (waitObject) {
					waitObject.notify();
				}

			} else if (curSegment == null && reqJoinSegmentName.length() < 1)
				return;

			System.out.println(NetworkSegment.listPeer.contains(msg.getSender()));
			NetworkSegment.addPerrNode(msg.getSender());
			reqJoinSegmentName = "";

			break;
		case ACKServiceMessage.chan_owner:
			if (curSegment == null || !curSegment.getName().equals(msg.getMsgContent())) {
				return;
			}

			curSegment.setOwnerPeer(msg.getSender());

			break;

		case ACKServiceMessage.chan_avd:
			if (segmentList.contains(msg.getMsgContent())) {
				return;
			} else
				segmentList.add(msg.getMsgContent());
			settingFrame.updateCurrnetSegmentList(segmentList);

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

		this.mePeer=pPeerNode;
				
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
		Manager.getInsance().getNetworkDispatch().setKey(key);

		// ����Ҫ���͵���Ϣbean

		ACKServiceMessage ackmsg = new ACKServiceMessage(Manager.getInsance().getMePeer(),
				ACKServiceMessage.askfor_join, segmentName);

		Manager.getInsance().getNetworkDispatch().DispatchToAll(ackmsg);

	}

}
