package com.syrus.AMFICOM.Client.General.UI;

import java.awt.*;
import javax.swing.*;
import oracle.jdeveloper.layout.*;
import java.awt.event.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class MessageBox extends JDialog {
  JPanel panel1 = new JPanel();
  String message = "Привет!!!";
  JButton jButtonOk = new JButton();
  VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextArea messageArea = new JTextArea();
  BorderLayout borderLayout1 = new BorderLayout();


  private  MessageBox(Frame frame, String title, boolean modal)
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


  public MessageBox(String m)
  {
    this(null, m, true);

    if(m != null)
       this.message = m;
    this.messageArea.setText(m);
    this.messageArea.show(true);
//    this.setLocation(400,300);
//    this.setSize(400, 145);
	setLoc();
    this.setModal(true);
    this.show(true);
    messageArea.show(true);
  }

	private void setLoc()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.height > screenSize.height)
		{
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width)
		{
			frameSize.width = screenSize.width;
		}
		setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
	}

  public MessageBox(String m, int sizeX, int sizeY)
  {
    this(null, m, true);
    if(m != null)
       this.message = m;
    this.messageArea.setText(m);
    this.messageArea.show(true);
//    this.setLocation(400,300);
    this.setSize(sizeX, sizeY);
	setLoc();
    this.setModal(true);
    this.show(true);
    messageArea.show(true);
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
    messageArea.setText("");
    jPanel2.setLayout(borderLayout1);
    jPanel2.setBorder(BorderFactory.createEtchedBorder());
    jPanel2.setPreferredSize(new Dimension(400, 70));
    this.messageArea.setEnabled(false);
//    this.messageArea.setEditable(false);
//    messageArea.setBackground(new Color(212, 208, 200));
	messageArea.setForeground(SystemColor.controlText);
    this.messageArea.setBorder(null);

    jScrollPane1.setBorder(null);
    panel1.add(jPanel2, null);
    jPanel2.add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(messageArea, null);
    panel1.add(jPanel1, null);
    jPanel1.add(jButtonOk, null);
    this.getContentPane().add(panel1, BorderLayout.CENTER);
  }


  void jButtonOk_actionPerformed(ActionEvent e)
  {
    this.dispose();
  }

}
