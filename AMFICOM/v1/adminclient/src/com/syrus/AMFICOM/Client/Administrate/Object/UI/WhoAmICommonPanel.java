package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.awt.*;
import java.text.*;

import javax.swing.*;

import oracle.jdeveloper.layout.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;


public class WhoAmICommonPanel extends JPanel {
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  ObjectResourceTextField jTextFieldLogin = new ObjectResourceTextField();
  ObjectResourceTextField jTextFieldProfileID = new ObjectResourceTextField();
  ObjectResourceTextField jTextFieldUserID = new ObjectResourceTextField();
  JPanel jPanel1 = new JPanel();
  VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  JPanel jPanel2 = new JPanel();
  VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
  BorderLayout borderLayout1 = new BorderLayout();

  public WhoAmICommonPanel()
  {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  void jbInit() throws Exception
  {
    this.setPreferredSize(new Dimension(220, 95));
    this.setName("Общие");
    this.setLayout(borderLayout1);

    jLabel1.setPreferredSize(new Dimension(33, 21));
    jLabel1.setText("Логин");
    jLabel2.setPreferredSize(new Dimension(66, 21));
    jLabel2.setText("ID профиля");
    jLabel3.setPreferredSize(new Dimension(95, 21));
    jLabel3.setText("ID пользователя");

    jTextFieldLogin.setText("");
    jTextFieldProfileID.setText("");
    jTextFieldUserID.setText("");
/*
    jTextFieldLogin.setForeground(Color.gray);
    jTextFieldLogin.setEditable(false);
    jTextFieldProfileID.setForeground(Color.gray);
    jTextFieldProfileID.setEditable(false);
    jTextFieldUserID.setForeground(Color.gray);
    jTextFieldUserID.setEditable(false);
*/
    jTextFieldLogin.setEnabled(false);
    jTextFieldProfileID.setEnabled(false);
	jTextFieldUserID.setEnabled(false);

    jPanel1.setPreferredSize(new Dimension(110, 500));
    jPanel1.setLayout(verticalFlowLayout1);
    jPanel2.setPreferredSize(new Dimension(100, 500));
    jPanel2.setLayout(verticalFlowLayout2);
    jPanel1.add(jLabel1, null);
    jPanel1.add(jLabel2, null);
    jPanel1.add(jLabel3, null);
    this.add(jPanel2, BorderLayout.CENTER);
    jPanel2.add(jTextFieldLogin, null);
    jPanel2.add(jTextFieldProfileID, null);
    jPanel2.add(jTextFieldUserID, null);
    this.add(jPanel1, BorderLayout.WEST);
/*
    this.jTextFieldLogin.setBackground(Color.white);
    this.jTextFieldProfileID.setBackground(Color.white);
    this.jTextFieldUserID.setBackground(Color.white);
*/
  }

  public void setData(User user, OperatorProfile op)
  {
    if(user == null || op == null)
      return;

    SimpleDateFormat sdf = new SimpleDateFormat();
    this.jTextFieldLogin.setText(op.login);
//    this.jTextFieldModified.setText(sdf.format(new Date(op.modified)));
    this.jTextFieldProfileID.setText(op.id);
    this.jTextFieldUserID.setText(user.id);
  }
}