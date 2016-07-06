package com.chat.controll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;

import com.bean.NetworkSegment;
import com.bean.PeerNode;
import com.chat.fun.PrivateConversationContext;
import com.chat.msgpacket.ACKServiceMessage;
import com.chat.msgpacket.Message;
import com.chat.msgpacket.PrivateMessage;
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
	private static Map<String, PrivateConversationContext> priConMap = new HashMap<String, PrivateConversationContext>();

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

	private Manager() {
		this.networkDispatch = new MultiplyDispatch();
		mePeer = PeerNode.mAnonymous;

	}

	public static void createManager(JFrame frame) {
		manager = new Manager();
		manager.settingFrame = (SettingFrame) frame;
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

		segmentBoradcastThread.start();// 用于定时广播该频道的名称

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
		if (msg instanceof PrivateMessage) {
			dealPrivateConManager((PrivateMessage) msg);

		}

	}

	private void dealPrivateConManager(PrivateMessage msg) {
		if (!msg.getMsgReveiver().getName().equals(getMePeer().getName()))
			return;

		PrivateConversationContext priConCtx = priConMap.get(msg.getSender().getName());
		if (priConCtx == null) {
			PrivateConversationContext privateConversationCtx = new PrivateConversationContext(
					msg.getSender().getName());
			priConMap.put(msg.getSender().getName(), privateConversationCtx);
			priConCtx = privateConversationCtx;
		}
		String receiverTxt = msg.getSender().getName() + "说：" + msg.getMsgContent();

		priConCtx.addReceiveMsg(receiverTxt);

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

			if (getMePeer().getCurState() != PeerNode.ASK_AUTHO) {
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
			System.out.println("收到用户请求加入的消息");

			if (curSegment == null) {
				reqJoinSegmentName = msg.getMsgContent();
				return;
			}

			if (!curSegment.getName().equals(msg.getMsgContent())) {
				return;
			}
			ACKServiceMessage ackMsg = new ACKServiceMessage(getMePeer(), ACKServiceMessage.welcome_join,
					curSegment.getName());
			System.out.println("发送欢迎加入消息：");
			curSegment.addPerrNode(msg.getSender());
			Manager.getInsance().getNetworkDispatch().DispatchToAll(ackMsg);

			if (curSegment.getOwnerPeer().equals(getMePeer())) {
				// 发现他是segment的创建者
				ACKServiceMessage ackmsg = new ACKServiceMessage(getMePeer(), ACKServiceMessage.chan_owner,
						curSegment.getName());

				Manager.getInsance().getNetworkDispatch().DispatchToAll(ackmsg);
				System.out.println("频道主人通知");
			}

			break;

		case ACKServiceMessage.welcome_join:

			if (curSegment == null && reqJoinSegmentName.length() > 0
					&& reqJoinSegmentName.equals(msg.getMsgContent())) {

				curSegment = new NetworkSegment(reqJoinSegmentName);

				curSegment.addPerrNode(getMePeer());
				// 在这里有两个线层，一个是用于专门负责监听接收广播消息的线程，该线程首先接收到消息然后解密消息，然后是分发处理消息，manger的处理消息的方法就是在这个线程中执行的
				// 另外一个线程是主界面线程。在这里主界面线程有两种情况：1是密码成功，此时会由处理消息的线程主动解锁主线程，2
				// 是密码不配，所以处理线程无法解锁只能等待。为了避免长时间的等待我们使用了带有时间的wait方法
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
	 * 通过发送广播来验证名称是否唯一
	 * 
	 * @param peerName
	 * @return
	 */
	public boolean tryToIdentifyPeerName(String peerName) {

		PeerNode pPeerNode = new PeerNode(PeerNode.ASK_AUTHO, peerName);

		this.mePeer = pPeerNode;

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
		Manager.getInsance().getNetworkDispatch().setKey(key);

		// 创建要发送的消息bean

		ACKServiceMessage ackmsg = new ACKServiceMessage(Manager.getInsance().getMePeer(),
				ACKServiceMessage.askfor_join, segmentName);

		Manager.getInsance().getNetworkDispatch().DispatchToAll(ackmsg);

	}

	public static void requestEstablishPrivateConverUIFrame(String peerNodeName) {

		// PrivateConversationFrame privConFrame =
		// PrivateConversationFrame.CreatePrivateConFrame(peerNodeName);
		if (!priConMap.containsKey(peerNodeName)) {
			PrivateConversationContext priConCtx = new PrivateConversationContext(peerNodeName);
			priConMap.put(peerNodeName, priConCtx);

		}
		PrivateConversationContext priConCtx = priConMap.get(peerNodeName);
		priConCtx.setConversationUIFrameVisible();

	}

	public static void requestDestoryPrivateConverUIFrame(String peerNodeName) {
		System.out.println("销毁对话框");

		// PrivateConversationFrame privConFrame =
		// PrivateConversationFrame.CreatePrivateConFrame(peerNodeName);
		if (!priConMap.containsKey(peerNodeName)) {
			return;

		}
		priConMap.remove(peerNodeName);

	}

	public void sendPrivateConMsg(String toName, String msgContent) {
		PeerNode peerNode = NetworkSegment.getPeerNodeByName(toName);
		PrivateMessage priconMsg = new PrivateMessage(Manager.getInsance().getMePeer(), peerNode, msgContent);

		Manager.getInsance().getNetworkDispatch().DispatchToAll(priconMsg);

	}

}
