/*
 * $Id: JAlertingMessageDialog.java,v 1.4 2004/09/27 13:28:51 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Object.ui;

import com.syrus.AMFICOM.Client.General.Event.OpenModuleEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.corba.portable.alarm.Message;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.*;

/**
 * @version $Revision: 1.4 $, $Date: 2004/09/27 13:28:51 $
 * @author $Author: bass $
 * @module generalclient_v1
 */
public final class JAlertingMessageDialog extends JDialog {
	private static final long serialVersionUID = 8756613248708170516L;

	private JAlertingMessageTableModel jAlertingMessageTableModel1 = new JAlertingMessageTableModel();

	public JAlertingMessageDialog(Frame parent, boolean modal) {
		super(parent, modal);
		((JComponent) getContentPane()).setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(221, 222, 230), (Color) (UIManager.get("window")), new Color(94, 96, 105), (Color) (UIManager.get("window"))));
		initComponents();
	}

	private void initComponents() {//GEN-BEGIN:initComponents
		java.awt.GridBagConstraints gridBagConstraints;
		
		jScrollPane1 = new JScrollPane();
		jTable1 = new JTable() {
			public void updateUI() {
				/*
				 * Calculate and set table row height according to the maximum
				 * JOptionPane icon height. Native GTK Look & Feel has no such resources
				 * available.
				 *
				 * See also:
				 * javax.swing.UIManager#installLookAndFeel(String, String)
				 * http://java.sun.com/developer/JDCTechTips/2004/tt0309.html#1
				 */
				Icon errorIcon = UIManager.getIcon("OptionPane.errorIcon");
				Icon informationIcon = UIManager.getIcon("OptionPane.informationIcon");
				Icon questionIcon = UIManager.getIcon("OptionPane.questionIcon");
				Icon warningIcon = UIManager.getIcon("OptionPane.warningIcon");
				int errorIconHeight;
				int informationIconHeight;
				int questionIconHeight;
				int warningIconHeight;
				try {
					errorIconHeight = errorIcon.getIconHeight();
				} catch (NullPointerException npe) {
					errorIconHeight = 0;
				}
				try {
					informationIconHeight = informationIcon.getIconHeight();
				} catch (NullPointerException npe) {
					informationIconHeight = 0;
				}
				try {
					questionIconHeight = questionIcon.getIconHeight();
				} catch (NullPointerException npe) {
					questionIconHeight = 0;
				}
				try {
					warningIconHeight = warningIcon.getIconHeight();
				} catch (NullPointerException npe) {
					warningIconHeight = 0;
				}
				if (errorIconHeight < informationIconHeight)
					errorIconHeight = informationIconHeight;
				if (errorIconHeight < questionIconHeight)
					errorIconHeight = questionIconHeight;
				if (errorIconHeight < warningIconHeight)
					errorIconHeight = warningIconHeight;
				if (errorIconHeight != 0)
					/*
					 * Maximum icon height + 2px (2 x focus border thickness) + 1px.
					 */
					setRowHeight(errorIconHeight + 3);
				super.updateUI();
			}
		};
		jSeparator1 = new JSeparator();
		jButton1 = new JButton();
		
		getContentPane().setLayout(new java.awt.GridBagLayout());
		
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Messages"));
		setModal(true);
		setResizable(false);
		jTable1.setModel(jAlertingMessageTableModel1);
		jTable1.getTableHeader().setReorderingAllowed(false);
		TableColumnModel tableColumnModel = jTable1.getColumnModel();
		TableCellRenderer cellRenderer = new JAlertingMessageTableCellRenderer();
		TableCellRenderer headerRenderer = new JAlertingMessageTableHeaderRenderer();
		int columnCount = tableColumnModel.getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			TableColumn tableColumn = tableColumnModel.getColumn(i);
			tableColumn.setCellRenderer(cellRenderer);
			tableColumn.setHeaderRenderer(headerRenderer);
		}
		jScrollPane1.setViewportView(jTable1);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(10, 10, 5, 10);
		getContentPane().add(jScrollPane1, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
		getContentPane().add(jSeparator1, gridBagConstraints);
		
		jButton1.setMnemonic(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Dismiss.Mnemonic").charAt(0));
		jButton1.setText(ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle").getString("Dismiss"));
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				jButton1ActionPerformed(e);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
		getContentPane().add(jButton1, gridBagConstraints);
		
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width-1024)/2, (screenSize.height-768)/2, 1024, 768);
	}//GEN-END:initComponents

	private void jButton1ActionPerformed(java.awt.event.ActionEvent e) {//GEN-FIRST:event_jButton1ActionPerformed
		synchronized (this) {
			Environment.getDispatcher().notify(jAlertingMessageTableModel1.getDelegate());
			jAlertingMessageTableModel1.setDelegate(new OpenModuleEvent());
			dispose();
		}
	}//GEN-LAST:event_jButton1ActionPerformed

	public void appendMessageSeq(final Collection messageSeq) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jButton1.setEnabled(false);
				jAlertingMessageTableModel1.addAll(messageSeq);
				jTable1.revalidate();
				jButton1.setEnabled(true);
			}
		});
	}

	public void appendMessage(final Message message) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jButton1.setEnabled(false);
				jAlertingMessageTableModel1.add(message);
				jTable1.revalidate();
				jButton1.setEnabled(true);
			}
		});
	}

	public void setMessageSeq(final Collection messageSeq) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jButton1.setEnabled(false);
				jAlertingMessageTableModel1.clear();
				jAlertingMessageTableModel1.addAll(messageSeq);
				jTable1.revalidate();
				jButton1.setEnabled(true);
			}
		});
	}

	public void setMessage(final Message message) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jButton1.setEnabled(false);
				jAlertingMessageTableModel1.clear();
				jAlertingMessageTableModel1.add(message);
				jTable1.revalidate();
				jButton1.setEnabled(true);
			}
		});
	}

	public void show() {
		beepingThread = new BeepingThread();
		beepingThread.start();
		jButton1.setEnabled(true);
		super.show();
	}

	public void hide() {
		synchronized (this) {
			try {
				beepingThread.running = false;
			} catch (NullPointerException npe) {
				/*
				 * Somehow on client exit show() and hide() are invoked
				 * sequentially.
				 */
				;
			}
			jButton1.setEnabled(false);
			super.hide();
		}
	}

	private BeepingThread beepingThread;

	private final class BeepingThread extends Thread {
		private boolean running;

		BeepingThread() {
			super("BeepingThread");
		}

		public void run() {
			running = true;
			while (running) {
				for (int i = 0; i < 3; i++) {
					Toolkit.getDefaultToolkit().beep();
					try {
						sleep(100);
					} catch (InterruptedException ie) {
						;
					}
				}
				try {
					sleep(1000);
				} catch (InterruptedException ie) {
					;
				}
			}
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButton1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JTable jTable1;
	// End of variables declaration//GEN-END:variables
}
