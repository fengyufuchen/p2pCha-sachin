package com.chat.msgpacket;

import java.io.Serializable;

import com.bean.PeerNode;

public class PrivateConversationMessage extends Message implements Serializable {
	private PeerNode to;

	public PrivateConversationMessage(PeerNode from, PeerNode to, String imsg) {
		super(imsg, from);
		// TODO Auto-generated constructor stub
		this.to = to;
	}

	@Override
	public char getTag() {
		// TODO Auto-generated method stub
		return 0;
	}

	public PeerNode getMsgReveiver() {
		return to;
	}

}
