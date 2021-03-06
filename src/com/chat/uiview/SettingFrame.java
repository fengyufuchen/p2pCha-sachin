package com.chat.uiview;

import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.bean.NetworkSegment;
import com.chat.controll.Manager;

/**
 *
 * @author lenovo
 */
public class SettingFrame extends javax.swing.JFrame {

	/**
	 * Creates new form ChatFrame
	 */
	public SettingFrame() {
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		jTabbedPane1 = new javax.swing.JTabbedPane();
		jPanel2 = new javax.swing.JPanel();
		jLabel2 = new javax.swing.JLabel();
		newRoomName = new javax.swing.JTextField();
		jLabel3 = new javax.swing.JLabel();
		newRoomPassword = new javax.swing.JTextField();
		jScrollPane2 = new javax.swing.JScrollPane();
		newRoomDeclare = new javax.swing.JTextArea();
		createNewRoomBtn = new javax.swing.JButton();
		jLabel6 = new javax.swing.JLabel();
		jPanel3 = new javax.swing.JPanel();
		jLabel4 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		enterRoomPassword = new javax.swing.JTextField();
		jScrollBar1 = new javax.swing.JScrollBar();
		enterSureBtn = new javax.swing.JButton();
		updateRoomList = new javax.swing.JButton();
		curRoomList = new javax.swing.JComboBox<>();
		jLabel7 = new javax.swing.JLabel();
		userName = new javax.swing.JTextField();
		jLabel9 = new javax.swing.JLabel();
		curStatement = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("CQQ");

		jLabel1.setText("配置登陆信息");

		jLabel2.setText("名称：");

		jLabel3.setText("密码：");

		newRoomDeclare.setColumns(20);
		newRoomDeclare.setRows(5);
		jScrollPane2.setViewportView(newRoomDeclare);

		createNewRoomBtn.setText("确认");
		createNewRoomBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				createNewRoomBtnActionPerformed(evt);
			}
		});

		jLabel6.setText("说明：");

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addGap(6, 6, 6).addComponent(jLabel2)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(newRoomName, javax.swing.GroupLayout.PREFERRED_SIZE, 55,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(33, 33, 33).addComponent(jLabel3)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(newRoomPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 77,
								javax.swing.GroupLayout.PREFERRED_SIZE)

						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(createNewRoomBtn).addContainerGap())
				.addGroup(jPanel2Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 339,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel6))
						.addContainerGap(45, Short.MAX_VALUE)));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel2)
								.addComponent(newRoomName, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel3)
								.addComponent(newRoomPassword, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(createNewRoomBtn))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jLabel6)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane2,
								javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));

		jTabbedPane1.addTab("创建用户组", jPanel2);

		jLabel4.setText("选择用户组：");

		jLabel5.setText("请输入密码：");

		enterRoomPassword.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				enterRoomPasswordActionPerformed(evt);
			}
		});

		enterSureBtn.setText("确认加入");
		enterSureBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				enterSureBtnActionPerformed(evt);
			}
		});

		updateRoomList.setText("更新聊天室");

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel3Layout.createSequentialGroup().addGap(60, 60, 60).addComponent(updateRoomList))
						.addGroup(jPanel3Layout.createSequentialGroup().addComponent(jLabel4).addGap(18, 18, 18)
								.addComponent(jLabel5).addGap(18, 18, 18)
								.addComponent(enterRoomPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 60,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(18, 18, 18).addComponent(enterSureBtn))
						.addGroup(jPanel3Layout.createSequentialGroup().addGap(21, 21, 21).addComponent(curRoomList,
								javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jScrollBar1, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jScrollBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
						Short.MAX_VALUE)
				.addGroup(jPanel3Layout.createSequentialGroup()
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel4).addComponent(jLabel5)
								.addComponent(enterRoomPassword, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(enterSureBtn))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(curRoomList, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
						.addComponent(updateRoomList)));

		jTabbedPane1.addTab("加入到已有用户组", jPanel3);

		jLabel7.setText("用户名：");

		jLabel9.setText("当前状态：");

		curStatement.setText("离线");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING).addGroup(layout
										.createSequentialGroup().addContainerGap()
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(jLabel1)
												.addGroup(layout.createSequentialGroup().addComponent(jLabel7)
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addComponent(userName).addGap(56, 56, 56).addComponent(jLabel9)
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
														.addComponent(curStatement)))
										.addGap(0, 0, Short.MAX_VALUE)))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7)
						.addComponent(userName).addComponent(jLabel9).addComponent(curStatement))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTabbedPane1,
						javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addContainerGap(43, Short.MAX_VALUE)));

		pack();
	}// </editor-fold>

	private void enterSureBtnActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		tryConnect();
	}

	private void createNewRoomBtnActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		if (newRoomName.getText() == null || newRoomName.getText().trim() == "") {
			return;
		}
		JDialog jwait = new JDialog(this, "正在创建聊天室");
		jwait.setResizable(false);

		jwait.setAlwaysOnTop(true);
		jwait.setBounds(200, 200, 400, 200);
		jwait.setVisible(true);
		jwait.requestFocus();
		jwait.show();

		tryConnect();
		jwait.dispose();

	}

	/**
	 * 
	 */
	private void tryConnect() {
		// 检查是否有同名peer
		boolean peerNameIsAvaiable = Manager.getInsance().tryToIdentifyPeerName(userName.getText());
		boolean segmentNameIsAvaiable = true;
		// 检查是否有同名的channel
		if (newRoomName.getText().length() > 0) {

			segmentNameIsAvaiable = Manager.getInsance().tryToIdentifySegmentName(newRoomName.getText());
			System.out.println("频道名称：" + newRoomName.getText() + " 能够使用" + segmentNameIsAvaiable);

		}

		if (!peerNameIsAvaiable) {
			// 名称不可以使用

			JOptionPane.showMessageDialog(this, "用户名名称已被使用", "错误", JOptionPane.ERROR_MESSAGE);
			return;
		}
		// 名称可以使用，并且创建了相应的peer
		if (!NetworkSegment.listPeer.contains(Manager.getInsance().getMePeer())) {
			NetworkSegment.addPerrNode(Manager.getInsance().getMePeer());
		
		

		}

		if (newRoomName.getText() != null && newRoomName.getText().trim().length() > 0) {
			if (segmentNameIsAvaiable) {
				NetworkSegment.createNetworkSegment(newRoomName.getText().trim(), newRoomPassword.getText().trim());
				System.out.println("频道创建成功");

				// System.out.println("Manager.getInsance().getMePeer()"+Manager.getInsance().getMePeer().getName());

			} else {
				JOptionPane.showMessageDialog(this, "网络段名已被占用", "错误", JOptionPane.ERROR_MESSAGE);
				return;
			}

		} else {
			// 用户申请加入其它组

			String segmentName = (String) curRoomList.getSelectedItem();
			System.out.println("用户申请加入其它组" + segmentName + " " + enterRoomPassword.getText().trim());
			Manager.getInsance().requestJoinInNetworkSegemnt(segmentName, enterRoomPassword.getText().trim());

			synchronized (Manager.getInsance().waitObject) {
				try {
					try {
						Manager.getInsance().waitObject.wait(Manager.DefaultOperTimeOut * 4);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			if (Manager.getInsance().getCurrentNetworkSegment() == null) {
				JOptionPane.showMessageDialog(this, "连接超时", "错误", JOptionPane.ERROR_MESSAGE);
				return;
			}

		}
		// 开启聊天主界面，关闭当前的界面
		this.dispose();
		MainFrame.CreateMainFrame(NetworkSegment.listPeer);

	}

	private void enterRoomPasswordActionPerformed(java.awt.event.ActionEvent evt) {

	}

	/*
	 * 更新显示当前segment列表
	 * 
	 * @param pListSegments
	 */
	public void updateCurrnetSegmentList(List<String> pListSegments) {
		comboBoxModel = new SegmentComboBoxModel(pListSegments);
		curRoomList.setModel(comboBoxModel);

	}

	private class SegmentComboBoxModel extends DefaultComboBoxModel {

		private List<String> segmentList;

		public SegmentComboBoxModel(List<String> pListSegments) {
			this.segmentList = pListSegments;
			for (String str : pListSegments) {
				this.addElement(str);
			}

		}

	}

	private SegmentComboBoxModel comboBoxModel;

	private javax.swing.JButton createNewRoomBtn;
	private javax.swing.JComboBox<String> curRoomList;
	private javax.swing.JLabel curStatement;
	private javax.swing.JTextField enterRoomPassword;
	private javax.swing.JButton enterSureBtn;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JScrollBar jScrollBar1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTabbedPane jTabbedPane1;
	private javax.swing.JTextArea newRoomDeclare;
	private javax.swing.JTextField newRoomName;
	private javax.swing.JTextField newRoomPassword;
	private javax.swing.JButton updateRoomList;
	private javax.swing.JTextField userName;
	// End of variables declaration
}
