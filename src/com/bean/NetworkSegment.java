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
	private MainFrame uiFrame;

	private List<PeerNode> listPeer = new ArrayList<>();
	private static Map<String, NetworkSegment> segmentMap = new TreeMap<String, NetworkSegment>();
	
	
	public NetworkSegment(String name,Frame uiFrame){
		this.name=name;
		
		
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PeerNode getOwnerPeer() {
		return ownerPeer;
	}

	public void setOwnerPeer(PeerNode ownerPeer) {
		this.ownerPeer = ownerPeer;
	}

	public static void createNetworkSegment(String segmentName, String key) {
		NetworkSegment singleSegment = new NetworkSegment();
		singleSegment.setOwnerPeer(Manager.getInsance().getMePeer());// 设置频道的所属peer节点。
		singleSegment.setName(segmentName);
		Manager.getInsance().setADThread(singleSegment);
		

	}

	public static boolean isContainThisSegment(String segmentName) {
		if (segmentMap.containsKey(segmentName)) {
			return true;
		} else
			return false;
	}

}
