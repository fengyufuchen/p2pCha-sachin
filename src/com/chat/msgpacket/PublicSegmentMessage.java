package com.chat.msgpacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.bean.NetworkSegment;
import com.bean.PeerNode;

/**
 * @Description: TODO
 * @author Sachin
 * @date 2016��7��6��
 * 
 */
public class PublicSegmentMessage extends Message {
	private NetworkSegment segment;

	protected PublicSegmentMessage(NetworkSegment segment, String imsg, PeerNode from) {
		super(imsg, from);
		this.segment = segment;

	}

	@Override
	public char getTag() {

		return 0;
	}

	/**
	 * publicSegmentMessage��Ҫʵ�����л�����ô��Ҫ�����ĳ�Ա��Ҫʵ�����л�����ΪNetworkSegment����û��ʵ�����л���
	 * ���������ṩ���������������������PublicSegmentMessageʵ�����л�
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeObject(segment.getName());
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		String segmentName = (String) in.readObject();
		this.segment = NetworkSegment.segmentMap.get(segmentName);
	}
}
