package com.syrus.AMFICOM.Client.General.UI;

import oracle.jdeveloper.layout.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class RejectDialog extends JDialog {
  JPanel panel1 = new JPanel();
  String message = "Операция запрещена!";
  JButton jButtonOk = new JButton();
  VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextArea ErrorMessage = new JTextArea();
  BorderLayout borderLayout1 = new BorderLayout();


  public RejectDialog(Frame frame, String title, boolean modal)
  {
	 super(frame, title, modal);
	 try {
		jbInit();
		pack();
	 }
	 catch(Exception ex) {
		ex.printStackTrace();
	 }
  }


  public RejectDialog(String m)
  {
	 this(null, m, true);

	 if(m != null)
		 this.message = m;
	 this.ErrorMessage.setText(m);
	 this.ErrorMessage.setVisible(true);
	 this.setLocation(400,300);
	 this.setSize(400, 145);
	 this.setModal(true);
	 this.setVisible(true);
	 ErrorMessage.setVisible(true);
  }
  private void jbInit() throws Exception
  {
		this.setResizable(false);
	 panel1.setLayout(verticalFlowLayout1);
	 jButtonOk.setText("Ok");
	 jButtonOk.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(ActionEvent e) {
		  jButtonOk_actionPerformed(e);
		}
	 });
	 ErrorMessage.setText("jTextArea1");
	 jPanel2.setLayout(borderLayout1);
	 jPanel2.setBorder(BorderFactory.createEtchedBorder());
	 jPanel2.setPreferredSize(new Dimension(400, 70));
		 this.ErrorMessage.setEnabled(false);
		 this.ErrorMessage.setEditable(false);
		 ErrorMessage.setBackground(new Color(212, 208, 200));
	 this.ErrorMessage.setBorder(BorderFactory.createEtchedBorder());

	 jScrollPane1.setBorder(null);
	 panel1.add(jPanel2, null);
	 jPanel2.add(jScrollPane1, BorderLayout.CENTER);
	 jScrollPane1.getViewport().add(ErrorMessage, null);
	 panel1.add(jPanel1, null);
	 jPanel1.add(jButtonOk, null);
	 this.getContentPane().add(panel1, BorderLayout.CENTER);
  }




  void jButtonOk_actionPerformed(ActionEvent e)
  {
	 this.dispose();
  }

}
