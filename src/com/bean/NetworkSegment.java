package com.bean;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.chat.controll.Manager;
import com.chat.uiview.MainFrame;

public class NetworkSegment {

	private String name;
	private PeerNode ownerPeer;

	public static List<PeerNode> listPeer = new ArrayList<>();
	private static Map<String, NetworkSegment> segmentMap = new TreeMap<String, NetworkSegment>();

	public NetworkSegment(String name) {
		this.name = name;
		System.out.println("频道名称：" + name);

	}

	public String getName() {
		return name;
	}

	public PeerNode getOwnerPeer() {
		return ownerPeer;
	}

	public void setOwnerPeer(PeerNode ownerPeer) {
		if (this.ownerPeer != null && this.ownerPeer.getName().equals(ownerPeer))
			return;
		System.out.println("设置频道的主任" + ownerPeer.getName());
		this.ownerPeer = ownerPeer;
	}

	public static void addPerrNode(PeerNode peerNode) {
		if (listPeer.contains(peerNode))
			return;
		System.out.println("添加对等节点：" + peerNode.getName());
		listPeer.add(peerNode);
		if (Manager.getInsance().getMainFrame() != null) {
			Manager.getInsance().getMainFrame().updatePeerNodesTree(listPeer);

		}

	}

	public static void createNetworkSegment(String segmentName, String key) {
		NetworkSegment lsegment = new NetworkSegment(segmentName);

		segmentMap.put(segmentName, lsegment);

		// 设置该频道的密码：
		Manager.getInsance().getNetworkDispatch().setKey(key);

		lsegment.setOwnerPeer(Manager.getInsance().getMePeer());// 设置频道的所属peer节点。

		Manager.getInsance().setADThread(lsegment);

	}

	public static boolean isContainThisSegment(String segmentName) {
		if (segmentMap.containsKey(segmentName)) {
			return true;
		} else
			return false;
	}
	public static PeerNode getPeerNodeByName(String name) {

		for (PeerNode peer : listPeer) {

			if (peer.getName().equals(name))

				return peer;
		}
		return null;

	}

}
