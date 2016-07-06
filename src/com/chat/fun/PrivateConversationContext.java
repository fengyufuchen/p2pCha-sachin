package com.chat.fun;

import com.bean.PeerNode;
import com.chat.uiview.PrivateConversationFrame;

public class PrivateConversationContext {
	private PrivateConversationFrame conFrame;
	PeerNode conTo;
	private String conToPeerName;

	public PrivateConversationContext(String toName) {
		this.conToPeerName = toName;
		conFrame = new PrivateConversationFrame(toName);
		conFrame.setVisible(true);

	}

	public PrivateConversationFrame getConFrame() {
		return conFrame;
	}

	public void setConFrame(PrivateConversationFrame conFrame) {
		this.conFrame = conFrame;
	}

	public PeerNode getConTo() {
		return conTo;
	}

	public void setConTo(PeerNode conTo) {
		this.conTo = conTo;
	}

	public void addReceiveMsg(String msgContent) {
		conFrame.setVisible(true);

		conFrame.showReceiveMsg(msgContent);

	}

	public void setConversationUIFrameVisible() {
		this.conFrame.setVisible(true);
	}

}
