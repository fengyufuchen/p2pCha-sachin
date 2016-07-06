package com.chat.uiview;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;

import com.chat.controll.Manager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lenovo
 */
public class PrivateConversationFrame extends javax.swing.JFrame {

	/**
	 * Creates new form PrivateConversationFrame
	 */
	public PrivateConversationFrame(String toName) {
		this.setTitle(toName);
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

		jScrollPane1 = new javax.swing.JScrollPane();
		pirvaConRecevTextArea = new javax.swing.JTextArea();
		jScrollPane2 = new javax.swing.JScrollPane();
		privConEditTextArea = new javax.swing.JTextArea();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		pirvaConRecevTextArea.setEditable(false);
		pirvaConRecevTextArea.setColumns(20);
		pirvaConRecevTextArea.setRows(5);
		pirvaConRecevTextArea.setSelectedTextColor(new java.awt.Color(51, 51, 255));
		jScrollPane1.setViewportView(pirvaConRecevTextArea);

		privConEditTextArea.setColumns(20);
		privConEditTextArea.setRows(5);
		jScrollPane2.setViewportView(privConEditTextArea);

		jLabel1.setText("���յ�����Ϣ��");

		jLabel2.setText("���뷢����Ϣ");

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				Manager.getInsance().requestDestoryPrivateConverUIFrame(getTitle());
				super.windowClosing(e);

			}
		});

		privConEditTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				super.keyTyped(e);
				if (e.getKeyChar() == '\n') {
					System.out.println("��������Ϣ");
					sendMsg();
				}
			}

		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addGroup(layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
										.createSequentialGroup().addContainerGap().addGroup(layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380,
														Short.MAX_VALUE)
												.addComponent(jScrollPane2)))
								.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(layout.createSequentialGroup().addGap(18, 18, 18)
														.addComponent(jLabel1))
												.addGroup(layout.createSequentialGroup().addContainerGap()
														.addComponent(jLabel2)))
										.addGap(0, 0, Short.MAX_VALUE)))
								.addContainerGap()));
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								layout.createSequentialGroup().addComponent(jLabel1)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 145,
												Short.MAX_VALUE)
										.addGap(1, 1, 1).addComponent(jLabel2).addGap(2, 2, 2)
										.addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap()));

		pack();
	}// </editor-fold>

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static PrivateConversationFrame CreatePrivateConFrame(String toName) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting
		// code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.
		 * html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(PrivateConversationFrame.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(PrivateConversationFrame.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(PrivateConversationFrame.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(PrivateConversationFrame.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the form */

		PrivateConversationFrame privaConFrame = new PrivateConversationFrame(toName);

		privaConFrame.setVisible(true);

		return privaConFrame;

	}

	private void sendMsg() {
		// TODO Auto-generated method stub

		String msgContent = privConEditTextArea.getText();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		msgContent = df.format(System.currentTimeMillis()).trim() + " \n" + msgContent;
		String sendMsg = "��" + "˵��" + msgContent;
		this.pirvaConRecevTextArea.append(sendMsg);
		this.privConEditTextArea.setText("");

		Manager.getInsance().sendPrivateConMsg(this.getTitle(), msgContent);

	}

	public void showReceiveMsg(String msgContent) {
		// TODO Auto-generated method stub

		pirvaConRecevTextArea.append(msgContent);

	}

	// Variables declaration - do not modify
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTextArea pirvaConRecevTextArea;
	private javax.swing.JTextArea privConEditTextArea;
	// End of variables declaration
}