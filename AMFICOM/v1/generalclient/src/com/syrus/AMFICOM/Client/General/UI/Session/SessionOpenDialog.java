//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: Диалоговое окно открытия новой сессии работы пользователя  * //
// *         с системой                                                   * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 1.0                                                          * //
// * От: 1 jul 2002                                                       * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Main\SessionOpenDialog.java                            * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General.UI.Session;

import com.syrus.AMFICOM.Client.General.ConnectionInterface;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.General.Model.Environment;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.*;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

public class SessionOpenDialog extends JDialog
{
	public SessionInterface si;
	public ConnectionInterface ci;

	JPanel jPanel1 = new JPanel();
	XYLayout xYLayout1 = new XYLayout();
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JLabel jLabel3 = new JLabel();

	public JTextField fieldUser = new JTextField();
	public JPasswordField fieldPassword = new JPasswordField();
	public JComboBox fieldCategory = new JComboBox();

	JButton buttonOk = new JButton();
	JButton buttonHelp = new JButton();
	JButton buttonCancel = new JButton();

	public int retCode = 0;
	public final int RET_OK = 1;
	public final int RET_CANCEL = 2;

	protected SessionOpenDialog(Frame parent, String title, boolean modal)
	{
		super(parent, title, modal);
		try
		{
			jbInit();
			pack();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public SessionOpenDialog()
	{
		this(Environment.getActiveWindow(), LangModel.getString("SessionOpenTitle"), false);
	}

	public SessionOpenDialog(String lastuser)
	{
		this();

		fieldUser.setText(lastuser);
	}

	private void jbInit() throws Exception
	{
		this.setResizable(false);
		jPanel1.setLayout(xYLayout1);
		jPanel1.addKeyListener(new SessionOpenDialog_jPanel1_keyAdapter(this));
		xYLayout1.setWidth(460);
		xYLayout1.setHeight(255);

		jLabel1.setText(LangModel.getString("labelName"));
		jLabel3.setText(LangModel.getString("labelCategory"));
		fieldPassword.addKeyListener(new SessionOpenDialog_fieldPassword_keyAdapter(this));
		fieldUser.addKeyListener(new SessionOpenDialog_fieldUser_keyAdapter(this));
		jLabel2.setText(LangModel.getString("labelPassword"));

		buttonOk.setText(LangModel.getString("buttonEnter"));
		buttonOk.addActionListener(
				new SessionOpenDialog_buttonOk_actionAdapter(this));
		buttonHelp.setText(LangModel.getString("buttonHelp"));
		buttonHelp.addActionListener(
				new SessionOpenDialog_buttonHelp_actionAdapter(this));
		buttonCancel.setText(LangModel.getString("buttonCancel"));
		buttonCancel.addActionListener(
				new SessionOpenDialog_buttonCancel_actionAdapter(this));

		getContentPane().add(jPanel1, BorderLayout.CENTER);

		ImageIcon image = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/logo2.jpg"));
		jPanel1.add(new JLabel(image), new XYConstraints(0, 0, -1, -1));

		jPanel1.add(jLabel1, new XYConstraints(50, 148, -1, -1));
		jPanel1.add(jLabel2, new XYConstraints(50, 178, -1, -1));
//		jPanel1.add(jLabel3, new XYConstraints(10, 67, -1, -1));

		jPanel1.add(fieldUser, new XYConstraints(140, 145, 270, 24));
		jPanel1.add(fieldPassword, new XYConstraints(140, 175, 270, 24));
//		jPanel1.add(fieldCategory, new XYConstraints(87, 65, 246, -1));

		jPanel1.add(buttonOk, new XYConstraints(50, 205, 140, 27));
		jPanel1.add(buttonCancel, new XYConstraints(210, 205, 100, 27));
		jPanel1.add(buttonHelp, new XYConstraints(320, 205, 90, 27));

		fieldUser.requestFocus();
		fieldCategory.setEnabled(false);
	}

	void buttonOk_actionPerformed(ActionEvent e)
	{
		si.setUser(fieldUser.getText());
		si.setPassword(new String (fieldPassword.getPassword()));
		SessionInterface ssi = si.OpenSession();

		if(ssi == null)
		{
			fieldPassword.setText("");
			JOptionPane.showMessageDialog(
					this,
					LangModel.getString("errorWrongLogin"),
					LangModel.getString("errorTitleOpenSession"),
					JOptionPane.ERROR_MESSAGE,
					null);
			return;
		}
		retCode = RET_OK;
		dispose();
	}

	void buttonCancel_actionPerformed(ActionEvent e)
	{
		retCode = RET_CANCEL;
		dispose();
	}

	void buttonHelp_actionPerformed(ActionEvent e)
	{
		JOptionPane.showMessageDialog(
				this,
				"Введите имя пользователя и пароль",
				"Вход в систему",
				JOptionPane.INFORMATION_MESSAGE,
				null);
	}

	void fieldUser_keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			buttonOk.doClick();
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			buttonCancel.doClick();
	}

	void fieldPassword_keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			buttonOk.doClick();
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			buttonCancel.doClick();
	}

	void jPanel1_keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			buttonCancel.doClick();
	}

}

class SessionOpenDialog_buttonOk_actionAdapter
		implements java.awt.event.ActionListener{
	SessionOpenDialog adaptee;

	SessionOpenDialog_buttonOk_actionAdapter(SessionOpenDialog adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonOk_actionPerformed(e);
	}
}

class SessionOpenDialog_buttonCancel_actionAdapter
		implements java.awt.event.ActionListener{
	SessionOpenDialog adaptee;

	SessionOpenDialog_buttonCancel_actionAdapter(SessionOpenDialog adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonCancel_actionPerformed(e);
	}
}

class SessionOpenDialog_buttonHelp_actionAdapter
		implements java.awt.event.ActionListener{
	SessionOpenDialog adaptee;

	SessionOpenDialog_buttonHelp_actionAdapter(SessionOpenDialog adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonHelp_actionPerformed(e);
	}
}



class SessionOpenDialog_fieldUser_keyAdapter extends java.awt.event.KeyAdapter
{
	SessionOpenDialog adaptee;

	SessionOpenDialog_fieldUser_keyAdapter(SessionOpenDialog adaptee)
	{
		this.adaptee = adaptee;
	}

	public void keyPressed(KeyEvent e)
	{
		adaptee.fieldUser_keyPressed(e);
	}
}

class SessionOpenDialog_fieldPassword_keyAdapter extends java.awt.event.KeyAdapter
{
	SessionOpenDialog adaptee;

	SessionOpenDialog_fieldPassword_keyAdapter(SessionOpenDialog adaptee)
	{
		this.adaptee = adaptee;
	}

	public void keyPressed(KeyEvent e)
	{
		adaptee.fieldPassword_keyPressed(e);
	}
}

class SessionOpenDialog_jPanel1_keyAdapter extends java.awt.event.KeyAdapter
{
	SessionOpenDialog adaptee;

	SessionOpenDialog_jPanel1_keyAdapter(SessionOpenDialog adaptee)
	{
		this.adaptee = adaptee;
	}

	public void keyPressed(KeyEvent e)
	{
		adaptee.jPanel1_keyPressed(e);
	}
}
