package com.chat.msgpacket;

import java.io.Serializable;

import com.bean.PeerNode;

/**
 * @author lenovo ����������Ϣ��һ���ǹ㲥��Ϣ�������ˣ�һֱ����ָ���˽����ߵ���Ϣ
 */
public class ACKServiceMessage extends Message implements Serializable {

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

	// �����ж���Щ��Ϣ�ǲ���Ҫ���ܵ�
	public boolean dontEncrypt() {
		if (tag == chan_avd || tag == peernode_name || tag == chan_name || tag == peernode_name_used
				|| tag == chan_name_used)
			return true;
		return false;
	}

	public ACKServiceMessage(PeerNode from, char tag, String imsg) {
		super(imsg, from);
		this.tag = tag;
		// TODO Auto-generated constructor stub
	}

	public ACKServiceMessage(PeerNode from, PeerNode to, char tag, String msg) {
		this(from, tag, msg);
		this.to = to;
		
		

	}

	public PeerNode getMsgToPeerNdoe() {
		return to;
	}

	public void setMsgToPeer(PeerNode toPeer) {
		this.to = toPeer;
	}

	public boolean isBroadcast() {
		return to == null;// toΪnull ��ʾ��һ���㲥��Ϣ��������Ϊ����ʹƵ���Ľڵ�
	}

	public char getTag() {
		return tag;
	}



}
