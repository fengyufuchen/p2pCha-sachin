package com.chat.controll;

import com.bean.NetworkSegment;
import com.chat.msgpacket.ACKServiceMessage;

public class NetworkSegmentBoradcastThread extends Thread {
	private NetworkSegment segment;

	

	public NetworkSegmentBoradcastThread(NetworkSegment segment) {
		this.segment = segment;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		// ��ʱ�㲥Ƶ����Ϣ������Ƶ��������

		while (true) {
			ACKServiceMessage msg = new ACKServiceMessage(Manager.getInsance().getMePeer(), ACKServiceMessage.chan_avd,
					segment.getName());

			Manager.getInsance().getNetworkDispatch().DispatchToAll(msg);

			try {
				Thread.sleep(2000);
			
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
