package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import oracle.jdeveloper.layout.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import oracle.jdeveloper.layout.XYConstraints;
import java.awt.Panel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;


public class OperatorProfilePasswordPanel extends GeneralPanel
{

  OperatorProfile profile;
  JLabel jLabelLogin = new JLabel();
  JPasswordField jPasswordField = new JPasswordField();
  JButton jButtonSave = new JButton();
  JLabel jLabelPassRepeat = new JLabel();
//  JButton jButtonRepeat = new JButton();
  JPasswordField jPasswordFieldRepeat = new JPasswordField();
  JLabel jLabelPass = new JLabel();
  JTextField jTextFieldLogin = new JTextField();
  String PASSWORD = "";
  JLabel jLabelStatus = new JLabel();
  JPanel panel1 = new JPanel();
  XYLayout xYLayout2 = new XYLayout();

//  private String errorEmpty =    "Ошибка: применен пустой символ.";
  private String errorShort =    "Ошибка: в пароле менее трех символов.";
  private String errorNonEqual =    "Ошибка: пароли не совпадают.";
  private String error =         "Ошибка: пароль останется прежним.";
  private String errorNotOwner = "Ошибка: неправильный старый пароль.";

  private String Ok =      "Пароль успешно изменен.";
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  TitledBorder titledBorder3;
  JPasswordField oldPasswordField = new JPasswordField();
  JLabel jLabel1 = new JLabel();
	private Panel panel2 = new Panel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

  public OperatorProfilePasswordPanel()
  {
    super();
    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public OperatorProfilePasswordPanel(OperatorProfile profile)
  {
    this();
    setObjectResource(profile);
  }

  private void jbInit() throws Exception
  {
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    titledBorder3 = new TitledBorder("");
    jLabelLogin.setText("Логин");
	jLabelLogin.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
	jLabelStatus.setText("");
	jLabelStatus.setPreferredSize(new Dimension(2 * DEF_WIDTH, DEF_HEIGHT));
    setName("Установка пароля");
    this.setLayout(gridBagLayout1);

    jButtonSave.setText("Сохранить");
    jButtonSave.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        jButtonSave_actionPerformed(e);
      }
    });
    jLabelPassRepeat.setText("Подтверждение");
	jLabelPassRepeat.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    jLabelPass.setText("Новый пароль");
	jLabelPass.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
    oldPasswordField.setEditable(true);
    jLabel1.setText("Старый пароль");
	jLabel1.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

    this.add(jLabelLogin, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabel1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelPass, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelPassRepeat, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jLabelStatus, new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

	this.add(jTextFieldLogin, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	this.add(oldPasswordField, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	this.add(jPasswordField, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	this.add(jPasswordFieldRepeat, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	this.add(jButtonSave, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    this.add(Box.createVerticalGlue(), new GridBagConstraints(0, 6, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    this.jTextFieldLogin.setEnabled(false);
//    this.jTextFieldLogin.setBackground(Color.white);
    this.jPasswordField.setEditable(true);
    this.jPasswordFieldRepeat.setEditable(true);
//    this.jButtonRepeat.show(false);
  }

  public ObjectResource getObjectResource()
  {
    return profile;
  }

  public boolean setObjectResource(ObjectResource or)
  {
    this.profile = (OperatorProfile )or;
    this.jTextFieldLogin.setText(profile.login);
    this.PASSWORD = profile.password;
    if(this.PASSWORD == null)
      this.PASSWORD = "";
    this.jPasswordField.setText("");
    this.jPasswordFieldRepeat.setText("");
    this.jLabelStatus.setText("");
//    this.jButtonRepeat.show(false);
    this.jButtonSave.setVisible(true);
    return true;
  }

  public boolean modify()
  {
    profile.password = this.PASSWORD;
    return true;
  }
///////////////////////////////////////////////////////////////////////////////

  boolean incorrectSymbols(String pass)
  {
    for(int i=0; i<pass.length(); i++)
    {
      if(pass.charAt(i) == ' ')
        return true;
    }
    return false;
  }

  void jButtonSave_actionPerformed(ActionEvent e)
  {
    String pass = new String(this.jPasswordField.getPassword());
    String passRepeat = new String(this.jPasswordFieldRepeat.getPassword());
    if(!PASSWORD.equals(new String(oldPasswordField.getPassword())))
    {
      this.jLabelStatus.setText(this.errorNotOwner);
		jLabelStatus.setForeground(Color.red);
    }
    else if(!pass.equals(passRepeat))
    {
      this.jLabelStatus.setText(this.errorNonEqual);
		jLabelStatus.setForeground(Color.red);
    }
    else if(pass.length()<3)
    {
      this.jLabelStatus.setText(this.errorShort);
		jLabelStatus.setForeground(Color.red);
    }
    else
    {
      this.PASSWORD = new String(this.jPasswordField.getPassword());
      this.jLabelStatus.setText(this.Ok);
		jLabelStatus.setForeground(Color.blue);
    }
    this.repaint();
  }

  void jButtonRepeat_actionPerformed(ActionEvent e)
  {
    this.jPasswordField.setText("");
    this.jPasswordFieldRepeat.setText("");
    this.jLabelStatus.setText("");
    this.jLabelLogin.setText("");
//    this.jButtonRepeat.show(false);
    this.jButtonSave.setVisible(true);
    this.repaint();
  }

}