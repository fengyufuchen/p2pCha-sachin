package com.chat.msgpacket;

import java.io.Serializable;

import com.bean.PeerNode;

public abstract class Message implements Serializable {

	private String msgContent;
	private PeerNode from;

	public boolean dontEncrypt() {

		return false;

	}

	public PeerNode getSender() {
		return from;
	}

	public String getMsgContent() {

		return msgContent;

	}

	public  Message(String imsg, PeerNode from) {
		msgContent = imsg;
		this.from = from;
	}
	
	public abstract char getTag();

	
}
