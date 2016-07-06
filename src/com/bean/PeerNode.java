package com.bean;

import java.io.Serializable;

public class PeerNode implements Serializable, Comparable<PeerNode> {
	public static PeerNode mAnonymous = new PeerNode();

	private boolean isAnonymous = false;
	public static final int WIOUTH_AUTHO = 0;
	public static final int ASK_AUTHO = 1;
	public static final int NICKNAME_AUTHOFAILED = 2;
	public static final int AUTHORED = 3;

	private int curState = WIOUTH_AUTHO;

	private String name;

	public PeerNode(int curState, String name) {
		super();
		this.curState = curState;
		this.name = name;
	}

	public PeerNode(String name) {
		super();

		this.name = name;
	}

	public PeerNode() {
		super();
		isAnonymous = true;
		name = "???";
	}

	public int getCurState() {
		return curState;
	}

	public void setCurState(int curState) {
		this.curState = curState;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(PeerNode o) {
		// TODO Auto-generated method stub
		if (o == null)
			return 0;
		return this.name.compareTo(o.name);
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		PeerNode pObj=(PeerNode) obj;
		return pObj.getName().equals(this.getName());
	}

}
