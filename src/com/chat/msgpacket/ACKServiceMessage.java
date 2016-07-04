package com.chat.msgpacket;

import com.bean.PeerNode;

/**
 * @author lenovo
 * 发送两种消息，一种是广播消息对所有人，一直用是指定了接收者的消息
 */
public class ACKServiceMessage extends Message {

	private PeerNode to;
	private char tag;

	public static final char chan_avd = 'a';
	public static final char chan_owner = 'o';
	public static final char peernode_name = 'n';
	public static final char chan_name = 'h';
	public static final char peernode_name_used = 'u';
	public static final char chan_name_used = 's';

	public static final char askfor_join = 'j';
	public static final char welcome_join = 'w';

	public ACKServiceMessage( PeerNode from, char tag,String imsg) {
		super(imsg, from);
		this.tag = tag;
		// TODO Auto-generated constructor stub
	}

	public ACKServiceMessage(PeerNode from, PeerNode to,  char tag,String msg) {
		this(from, tag, msg);
		this.to = to;

	}

	public PeerNode getMsgToPeerNdoe() {
		return to;
	}

	public void setMsgToPeer(PeerNode toPeer) {
		this.to = toPeer;
	}

	public boolean dontEncrypt() {
		return false;
	}

	public boolean isBroadcast() {
		return to == null;// to为null 表示是一个广播消息，接受者为所有痛频道的节点
	}

	public char getTag() {
		return tag;
	}

}
