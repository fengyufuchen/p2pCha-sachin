package com.chat.msgpacket;

import com.bean.PeerNode;

public class ShareFileMessage extends Message {
	private String[] shareFiles;

	public ShareFileMessage(PeerNode from, String[] shareFiles) {
		super(" ", from);

		this.shareFiles = shareFiles;

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public char getTag() {
		// TODO Auto-generated method stub
		return 0;
	}

}
