package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import oracle.jdeveloper.layout.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;





public class WhoAmIPasswordPanel extends JPanel
{
  private BorderLayout borderLayout1 = new BorderLayout();
  private JPanel labelsPanel = new JPanel();
  private JPanel textPanel = new JPanel();
  private JPanel jPanel1 = new JPanel();
  private JLabel jLabel1 = new JLabel();
  private JLabel jLabel2 = new JLabel();
  private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  private JLabel jLabel3 = new JLabel();
  private VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
  private JPasswordField jOldPasswordField = new JPasswordField();
  private JPasswordField jPasswordField1 = new JPasswordField();
  private JPasswordField jPasswordField2 = new JPasswordField();
  private JPanel jPanel2 = new JPanel();
  private JButton applyButton = new JButton();
  private JLabel jLabelError = new JLabel();
  private XYLayout xYLayout1 = new XYLayout();
  private JLabel jLabelOK = new JLabel();


  String PASSWORD;

  String error         = "Ошибка! Пароль не был изменен.";
  String errorShort    = "Ошибка! Пароль короче 3-х знаков!";
  String errorWrongOld = "Ошибка! Неправильный старый пароль!";
  String errorNonEqual = "Ошибка! Введенные пароли отличаются!";
  String ok = "Пароль успешно изменен.";

  User user;
  OperatorProfile op;
  ApplicationContext aContext;
  Dispatcher dispatcher;


  public WhoAmIPasswordPanel()
  {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }

  }
  private void jbInit() throws Exception {
    this.setLayout(borderLayout1);
    this.setName("Пароль");
    textPanel.setLayout(verticalFlowLayout1);
    jLabel1.setPreferredSize(new Dimension(89, 21));
    jLabel1.setText("Старый пароль");
    jLabel2.setPreferredSize(new Dimension(44, 21));
    jLabel2.setText("Пароль");
    jLabel3.setPreferredSize(new Dimension(106, 21));
    jLabel3.setText("пароль (повторно)");
    jPanel1.setLayout(verticalFlowLayout2);
    applyButton.setText("Применить");
    jLabelError.setText("   ");
    labelsPanel.setLayout(xYLayout1);
//    labelsPanel.setPreferredSize(new Dimension(46, 23));
    jLabelOK.setText("   ");
//    jPanel1.setPreferredSize(new Dimension(119, 125));
//    textPanel.setPreferredSize(new Dimension(116, 105));
//    this.setPreferredSize(new Dimension(235, 145));
    this.add(labelsPanel, BorderLayout.SOUTH);
    this.add(textPanel, BorderLayout.WEST);
    this.add(jPanel1, BorderLayout.CENTER);
    jPanel1.add(jOldPasswordField, null);
    textPanel.add(jLabel1, null);
    textPanel.add(jLabel2, null);
    textPanel.add(jLabel3, null);
    jPanel1.add(jPasswordField1, null);
    jPanel1.add(jPasswordField2, null);
    jPanel1.add(jPanel2, null);
    jPanel2.add(applyButton, null);
    labelsPanel.add(jLabelError, new XYConstraints(5, 2, -1, -1));
    labelsPanel.add(jLabelOK, new XYConstraints(5, 2, -1, -1));

    applyButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ApplyButton_actionPerformed(e);
      }
    });

    jLabelError.setForeground(Color.red);
    jLabelOK.setForeground(Color.blue);

  }


  public void setAContext(ApplicationContext aContext)
  {
    this.aContext = aContext;
    this.dispatcher = this.aContext.getDispatcher();
  }


  public void setData(User user, OperatorProfile op)
  {
    if(user == null || op == null)
      return;
    this.op = op;
    this.user = user;

    String ok = "Пароль для " + op.login + " успешно изменен.";
  }


  void ApplyButton_actionPerformed(ActionEvent e)
  {
    String p1 = new String(this.jPasswordField1.getPassword());
    String p2 = new String(this.jPasswordField2.getPassword());

    if(p1.equals(p2) && p1.length() >=3 && new String(jOldPasswordField.getPassword()).equals(op.password))
    {
      this.PASSWORD = p1;
      modify();

      this.jLabelOK.setText(ok);
      this.jLabelError.setText("");
      repaint();
    }
    else if(!new String(jOldPasswordField.getPassword()).equals(op.password))
    {
      this.jLabelError.setText(errorWrongOld);
      this.jLabelOK.setText("");
      repaint();
    }
    else if(!p1.equals(p2))
    {
      this.jLabelError.setText(errorNonEqual);
      this.jLabelOK.setText("");
      repaint();
    }
    else if(p1.length()<3)
    {
      this.jLabelError.setText(errorShort);
      this.jLabelOK.setText("");
      repaint();
    }
    else
    {
      this.jLabelError.setText(error);
      this.jLabelOK.setText("");
      repaint();
    }
  }


  private void modify()
  {
    Date d = new Date();
    this.op.password = this.PASSWORD;
    this.op.modified_by = user.id;
    this.op.modified = d.getTime();
    Pool.put(OperatorProfile.typ, op.id, op);
    this.aContext.getDataSourceInterface().SaveOperatorProfile(op.id);
    dispatcher.notify(new OperationEvent(this, 0, OperatorProfile.typ+"updated"));
  }


}



/*

public class WhoAmIPasswordPanel extends JPanel
{
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JPasswordField jPasswordField1 = new JPasswordField();
  JPasswordField jPasswordField2 = new JPasswordField();
  JButton ApplyButton = new JButton();
//  JButton RepeatButton = new JButton();
  JLabel jLabelError = new JLabel();
  JLabel jLabelOK = new JLabel();
  String PASSWORD;

  String error         = "Ошибка! Пароль не был изменен.";
  String errorShort    = "Ошибка! Пароль короче 3-х знаков!";
  String errorWrongOld = "Ошибка! Неправильный старый пароль!";
  String errorNonEqual = "Ошибка! Введенные пароли отличаются!";
  String ok = "Пароль успешно изменен.";

  User user;
  OperatorProfile op;
  ApplicationContext aContext;
  Dispatcher dispatcher;
  JPanel jPanel1 = new JPanel();
  VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  JPanel jPanel2 = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
  JPanel jPanel3 = new JPanel();
  JPanel jPanel4 = new JPanel();
  XYLayout xYLayout3 = new XYLayout();
  JPanel jPanel5 = new JPanel();
  VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
  FlowLayout flowLayout1 = new FlowLayout();
  XYLayout xYLayout1 = new XYLayout();
  private TitledBorder titledBorder1;
  JLabel jLabel1 = new JLabel();
  JPasswordField jOldPasswordField = new JPasswordField();
  TitledBorder titledBorder2;
  TitledBorder titledBorder3;
  TitledBorder titledBorder4;


  public WhoAmIPasswordPanel()
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
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    titledBorder3 = new TitledBorder("");
    titledBorder4 = new TitledBorder("");
    jLabelError.setForeground(Color.red);
    jLabelOK.setForeground(Color.blue);
    jLabelError.setText("");
    jLabelOK.setText("");
    this.setName("Пароль");
    this.setLayout(xYLayout1);
    this.setPreferredSize(new Dimension(220, 160));
    jLabel2.setPreferredSize(new Dimension(110, 17));
    jLabel2.setText("Пароль");
    jLabel3.setPreferredSize(new Dimension(110, 17));
    jLabel3.setText("Пароль (повторно)");
    ApplyButton.setText("Применить");
//    RepeatButton.setText("Повторить");
    ApplyButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ApplyButton_actionPerformed(e);
      }
    });
//    RepeatButton.addActionListener(new java.awt.event.ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//  RepeatButton_actionPerformed(e);
//      }
//    });

    jPanel1.setPreferredSize(new Dimension(125, 500));
    jPanel1.setLayout(verticalFlowLayout1);
    jPanel2.setLayout(xYLayout2);
    jPanel3.setLayout(flowLayout1);
    jPanel4.setPreferredSize(new Dimension(169, 20));
    jPanel4.setLayout(xYLayout3);
    jPanel5.setLayout(verticalFlowLayout2);
    jPanel5.setPreferredSize(new Dimension(120, 500));
    jPanel3.setMinimumSize(new Dimension(114, 20));
    jPanel3.setPreferredSize(new Dimension(169, 25));
    jPasswordField1.setBorder(BorderFactory.createLoweredBevelBorder());
    jPasswordField1.setPreferredSize(new Dimension(4, 17));
    jPasswordField2.setBorder(BorderFactory.createLoweredBevelBorder());
    jPasswordField2.setPreferredSize(new Dimension(4, 17));
    jLabel1.setText("Старый пароль");
    jOldPasswordField.setBorder(BorderFactory.createLoweredBevelBorder());
    jOldPasswordField.setPreferredSize(new Dimension(4, 17));
    jPanel1.add(jOldPasswordField, null);
    jPanel1.add(jPasswordField1, null);
    jPanel1.add(jPasswordField2, null);
    jPanel1.add(jPanel3, null);
    jPanel3.add(jPanel2, null);
//    jPanel2.add(RepeatButton,    new XYConstraints(2, 1, 100, 17));
    jPanel2.add(ApplyButton,   new XYConstraints(2, 1, 100, 17));
    this.add(jPanel4,             new XYConstraints(1, 94, 283, -1));
    jPanel4.add(jLabelError,      new XYConstraints(1, 1, -1, 15));
    jPanel4.add(jLabelOK,    new XYConstraints(1, 1, -1, 15));
    this.add(jPanel5,       new XYConstraints(0, 0, -1, 81));
    jPanel5.add(jLabel1, null);
    jPanel5.add(jLabel2, null);
    jPanel5.add(jLabel3, null);
    this.add(jPanel1,                    new XYConstraints(117, 0, 135, 96));

    this.ApplyButton.setVisible(true);
//    this.RepeatButton.show(false);
    repaint();
  }

  public void setData(User user, OperatorProfile op)
  {
    if(user == null || op == null)
      return;
    this.op = op;
    this.user = user;

    String ok = "Пароль для " + op.login + " успешно изменен.";
  }

  void ApplyButton_actionPerformed(ActionEvent e)
  {
    String p1 = this.jPasswordField1.getText();
    String p2 = this.jPasswordField2.getText();

    if(p1.equals(p2) && p1.length() >=3 && jOldPasswordField.getText().equals(op.password))
    {
      this.PASSWORD = p1;
      modify();

      this.jLabelOK.setText(ok);
      this.jLabelError.setText("");
      repaint();
    }
    else if(!p1.equals(p2))
    {
      this.jLabelError.setText(errorNonEqual);
      this.jLabelOK.setText("");
      repaint();
    }
    else if(p1.length()<3)
    {
      this.jLabelError.setText(errorShort);
      this.jLabelOK.setText("");
      repaint();
    }
    else if(!jOldPasswordField.getText().equals(op.password))
    {
      this.jLabelError.setText(errorWrongOld);
      this.jLabelOK.setText("");
      repaint();
    }
    else
    {
      this.jLabelError.setText(error);
      this.jLabelOK.setText("");
      repaint();
    }
//    this.jPasswordField1.enable(false);
//    this.jPasswordField2.enable(false);
//    this.ApplyButton.show(false);
//    this.RepeatButton.show(true);

  }

//  void RepeatButton_actionPerformed(ActionEvent e)
//  {
//    this.jPasswordField1.setText("");
//    this.jPasswordField2.setText("");
//    this.jPasswordField1.enable(true);
//    this.jPasswordField2.enable(true);
//    this.ApplyButton.show(true);
//    this.RepeatButton.show(false);
//    this.jLabelError.setText("");
//    this.jLabelOK.setText("");
//    this.repaint();
//  }


  private void modify()
  {
    Date d = new Date();
    this.op.password = this.PASSWORD;
    this.op.modified_by = user.id;
    this.op.modified = d.getTime();
    Pool.put(op.typ, op.id, op);
    this.aContext.getDataSourceInterface().SaveOperatorProfile(op.id);
    dispatcher.notify(new OperationEvent(this, 0, OperatorProfile.typ+"updated"));
  }



  public void setAContext(ApplicationContext aContext)
  {
    this.aContext = aContext;
    this.dispatcher = this.aContext.getDispatcher();
  }
}

/*
public class WhoAmIPasswordPanel extends JPanel
{
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JPasswordField jPasswordField1 = new JPasswordField();
  JPasswordField jPasswordField2 = new JPasswordField();
  JButton ApplyButton = new JButton();
  JButton RepeatButton = new JButton();
  JLabel jLabelError = new JLabel();
  JLabel jLabelOK = new JLabel();
  String PASSWORD;

  String error = "Ошибка! Пароль не был изменен.";
  String ok = "Пароль успешно изменен.";

  User user;
  OperatorProfile op;
  ApplicationContext aContext;
  Dispatcher dispatcher;
  JPanel jPanel1 = new JPanel();
  VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
  JPanel jPanel2 = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
  JPanel jPanel3 = new JPanel();
  JPanel jPanel4 = new JPanel();
  XYLayout xYLayout3 = new XYLayout();
  JPanel jPanel5 = new JPanel();
  VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
  FlowLayout flowLayout1 = new FlowLayout();
  XYLayout xYLayout1 = new XYLayout();
  private TitledBorder titledBorder1;
  JLabel jLabel1 = new JLabel();
  JPasswordField jOldPasswordField = new JPasswordField();
  TitledBorder titledBorder2;
  TitledBorder titledBorder3;
  TitledBorder titledBorder4;


  public WhoAmIPasswordPanel()
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
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    titledBorder3 = new TitledBorder("");
    titledBorder4 = new TitledBorder("");
    jLabelError.setForeground(Color.red);
    jLabelOK.setForeground(Color.blue);
    jLabelError.setText("");
    jLabelOK.setText("");
    this.setName("Пароль");
    this.setLayout(xYLayout1);
    this.setPreferredSize(new Dimension(220, 160));
    jLabel2.setPreferredSize(new Dimension(110, 17));
    jLabel2.setText("Пароль");
    jLabel3.setPreferredSize(new Dimension(110, 17));
    jLabel3.setText("Пароль (повторно)");
    ApplyButton.setText("Применить");
    RepeatButton.setText("Повторить");
    ApplyButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ApplyButton_actionPerformed(e);
      }
    });
    RepeatButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
	RepeatButton_actionPerformed(e);
      }
    });

    jPanel1.setPreferredSize(new Dimension(125, 500));
    jPanel1.setLayout(verticalFlowLayout1);
    jPanel2.setLayout(xYLayout2);
    jPanel3.setLayout(flowLayout1);
    jPanel4.setPreferredSize(new Dimension(169, 20));
    jPanel4.setLayout(xYLayout3);
    jPanel5.setLayout(verticalFlowLayout2);
    jPanel5.setPreferredSize(new Dimension(120, 500));
    jPanel3.setMinimumSize(new Dimension(114, 20));
    jPanel3.setPreferredSize(new Dimension(169, 25));
    jPasswordField1.setBorder(BorderFactory.createLoweredBevelBorder());
    jPasswordField1.setPreferredSize(new Dimension(4, 17));
    jPasswordField2.setBorder(BorderFactory.createLoweredBevelBorder());
    jPasswordField2.setPreferredSize(new Dimension(4, 17));
    jLabel1.setText("Старый пароль");
    jOldPasswordField.setBorder(BorderFactory.createLoweredBevelBorder());
    jOldPasswordField.setPreferredSize(new Dimension(4, 17));
    jPanel1.add(jOldPasswordField, null);
    jPanel1.add(jPasswordField1, null);
    jPanel1.add(jPasswordField2, null);
    jPanel1.add(jPanel3, null);
    jPanel3.add(jPanel2, null);
    jPanel2.add(RepeatButton,    new XYConstraints(2, 1, 100, 17));
    jPanel2.add(ApplyButton,   new XYConstraints(2, 1, 100, 17));
    this.add(jPanel4,             new XYConstraints(1, 94, 283, -1));
    jPanel4.add(jLabelError,      new XYConstraints(1, 1, -1, 15));
    jPanel4.add(jLabelOK,    new XYConstraints(1, 1, -1, 15));
    this.add(jPanel5,       new XYConstraints(0, 0, -1, 81));
    jPanel5.add(jLabel1, null);
    jPanel5.add(jLabel2, null);
    jPanel5.add(jLabel3, null);
    this.add(jPanel1,                    new XYConstraints(117, 0, 135, 96));

    this.ApplyButton.show(true);
    this.RepeatButton.show(false);
    repaint();
  }

  public void setData(User user, OperatorProfile op)
  {
    if(user == null || op == null)
      return;
    this.op = op;
    this.user = user;

    String ok = "Пароль для " + op.login + " успешно изменен.";
  }

  void ApplyButton_actionPerformed(ActionEvent e)
  {
    String p1 = this.jPasswordField1.getText();
    String p2 = this.jPasswordField2.getText();

    if(p1.equals(p2) && p1.length() >=3 && jOldPasswordField.getText().equals(op.password))
    {
      this.PASSWORD = p1;
      modify();

      this.jLabelOK.setText(ok);
      this.jLabelError.setText("");
      repaint();
    }
    else
    {
      this.jLabelError.setText(error);
      this.jLabelOK.setText("");
      repaint();
    }
    this.jPasswordField1.enable(false);
    this.jPasswordField2.enable(false);
    this.ApplyButton.show(false);
    this.RepeatButton.show(true);

  }

  void RepeatButton_actionPerformed(ActionEvent e)
  {
    this.jPasswordField1.setText("");
    this.jPasswordField2.setText("");
    this.jPasswordField1.enable(true);
    this.jPasswordField2.enable(true);
    this.ApplyButton.show(true);
    this.RepeatButton.show(false);
    this.jLabelError.setText("");
    this.jLabelOK.setText("");
    this.repaint();
  }


  private void modify()
  {
    Date d = new Date();
    this.op.password = this.PASSWORD;
    this.op.modified_by = user.id;
    this.op.modified = d.getTime();
    Pool.put(op.typ, op.id, op);
    this.aContext.getDataSourceInterface().SaveOperatorProfile(op.id);
    dispatcher.notify(new OperationEvent(this, 0, OperatorProfile.typ+"updated"));
  }



  public void setAContext(ApplicationContext aContext)
  {
    this.aContext = aContext;
    this.dispatcher = this.aContext.getDispatcher();
  }
}*/