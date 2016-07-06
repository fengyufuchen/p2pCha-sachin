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
 * @date 2016年7月6日
 * 
 */
public class PublicSegmentMessage extends Message {
	private NetworkSegment segment;

	public PublicSegmentMessage(NetworkSegment segment, String imsg, PeerNode from) {
		super(imsg, from);
		this.segment = segment;

	}

	@Override
	public char getTag() {

		return 0;
	}

	public NetworkSegment getTargetNetworkSegment() {
		return this.segment;
	}

	/**
	 * publicSegmentMessage需要实现序列化，那么就要求他的成员需要实现序列化，因为NetworkSegment本身没有实现序列化，
	 * 所以我们提供了下面的两个方法来帮助PublicSegmentMessage实现序列化
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
