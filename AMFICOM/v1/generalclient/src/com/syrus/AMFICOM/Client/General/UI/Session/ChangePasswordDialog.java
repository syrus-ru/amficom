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
// * Название: Диалоговое окно изменения пароля активного пользователя    * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 1.0                                                          * //
// * От: 22 jun 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Main\ChangePasswordDialog.java                         * //
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
// *        модуль вызывается для изменения пароля текущего пользователя  * //
// *        для этого модулю передается структура, описывающая текущую    * //
// *        сессию. При Нажатии кнопки "Изменить" проверяется соответствие* //
// *        введенного логина и старого пароля открытой сессии и равенство* //
// *        введенного и повторенного нового пароля                       * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General.UI.Session;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

public class ChangePasswordDialog extends JDialog
{
	public ApplicationContext aContext;

	JPanel jPanel1 = new JPanel();

	JButton buttonOk = new JButton();
	JButton buttonHelp = new JButton();
	JButton buttonCancel = new JButton();

	public int retCode = 0;
	public final int RET_OK = 1;
	public final int RET_CANCEL = 2;
	XYLayout xYLayout1 = new XYLayout();
	JLabel jLabel1 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JLabel jLabel3 = new JLabel();
	JLabel jLabel4 = new JLabel();
	public JTextField fieldUser = new JTextField();
	public JPasswordField fieldOldPassword = new JPasswordField();
	public JPasswordField fieldNewPassword = new JPasswordField();
	public JPasswordField fieldNewPassword2 = new JPasswordField();

	protected ChangePasswordDialog(Frame parent, String title, boolean modal)
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

	public ChangePasswordDialog(ApplicationContext aContext)
	{
		this(Environment.getActiveWindow(), LangModel.String("ChangePasswordTitle"), false);
		this.aContext = aContext;
	}

	private void jbInit() throws Exception
	{
		this.setResizable(false);
		jPanel1.setLayout(xYLayout1);
		xYLayout1.setWidth(343);
		xYLayout1.setHeight(158);

		jLabel1.setText(LangModel.String("labelName"));
		jLabel2.setText(LangModel.String("labelOldPassword"));
		jLabel3.setText(LangModel.String("labelNewPassword"));
		jLabel4.setText(LangModel.String("labelNewPassword2"));

		getContentPane().add(jPanel1);

		buttonOk.setText(LangModel.String("buttonChange"));
		buttonOk.addActionListener(
				new ChangePasswordDialog_buttonOk_actionAdapter(this));
		buttonHelp.setText(LangModel.String("buttonHelp"));
		buttonHelp.addActionListener(
				new ChangePasswordDialog_buttonHelp_actionAdapter(this));
		buttonCancel.setText(LangModel.String("buttonCancel"));
		buttonCancel.addActionListener(
				new ChangePasswordDialog_buttonCancel_actionAdapter(this));

		jPanel1.add(jLabel1, new XYConstraints(11, 12, -1, -1));
		jPanel1.add(jLabel2, new XYConstraints(11, 39, -1, -1));
		jPanel1.add(jLabel3, new XYConstraints(11, 67, -1, -1));
		jPanel1.add(jLabel4, new XYConstraints(11, 94, -1, -1));

		jPanel1.add(fieldUser, new XYConstraints(133, 10, 201, -1));
		jPanel1.add(fieldOldPassword, new XYConstraints(133, 37, 201, -1));
		jPanel1.add(fieldNewPassword, new XYConstraints(133, 65, 201, -1));
		jPanel1.add(fieldNewPassword2, new XYConstraints(133, 92, 201, -1));

		jPanel1.add(buttonOk, new XYConstraints(11, 120, -1, 27));
		jPanel1.add(buttonHelp, new XYConstraints(251, 120, -1, 27));
		jPanel1.add(buttonCancel, new XYConstraints(131, 120, -1, 27));

		fieldUser.requestFocus();
	}

	void buttonOk_actionPerformed(ActionEvent e)
	{
		String puser = fieldUser.getText();
		String pold = fieldOldPassword.getText();
		String pnew = fieldNewPassword.getText();
		String pnew2 = fieldNewPassword2.getText();

		SessionInterface si = aContext.getSessionInterface();

		if(!puser.equals(si.getUser()))
		{
			fieldUser.setText("");
			fieldOldPassword.setText("");
			fieldNewPassword.setText("");
			fieldNewPassword2.setText("");
			JOptionPane.showMessageDialog(
					this,
					LangModel.String("errorWrongName"),
					LangModel.String("errorTitleChangePassword"),
					JOptionPane.ERROR_MESSAGE,
					null);
			return;
		}

		if(!pold.equals(si.getPassword()))
		{
			fieldOldPassword.setText("");
			fieldNewPassword.setText("");
			fieldNewPassword2.setText("");
			JOptionPane.showMessageDialog(
					this,
					LangModel.String("errorWrongPassword"),
					LangModel.String("errorTitleChangePassword"),
					JOptionPane.ERROR_MESSAGE,
					null);
			return;
		}

		if(pnew.length() < 3)
		{
			fieldNewPassword.setText("");
			fieldNewPassword2.setText("");
			JOptionPane.showMessageDialog(
					this,
					LangModel.String("errorPasswordTooShort"),
					LangModel.String("errorTitleChangePassword"),
					JOptionPane.ERROR_MESSAGE,
					null);
			return;
		}
		if(!pnew.equals(pnew2))
		{
			fieldNewPassword.setText("");
			fieldNewPassword2.setText("");
			JOptionPane.showMessageDialog(
					this,
					LangModel.String("errorWrongPassword2"),
					LangModel.String("errorTitleChangePassword"),
					JOptionPane.ERROR_MESSAGE,
					null);
			return;
		}

		if(!aContext.getDataSourceInterface().ChangePassword(pold, pnew))
		{
			JOptionPane.showMessageDialog(
					this,
					"Ошибка изменения пароля",
					LangModel.String("errorTitleChangePassword"),
					JOptionPane.ERROR_MESSAGE,
					null);
			return;
		}
		si.setPassword(pnew);

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
				"Help system not installed",
				"Help",
				JOptionPane.INFORMATION_MESSAGE,
				null);
	}
}

class ChangePasswordDialog_buttonOk_actionAdapter
		implements java.awt.event.ActionListener{
	ChangePasswordDialog adaptee;

	ChangePasswordDialog_buttonOk_actionAdapter(ChangePasswordDialog adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonOk_actionPerformed(e);
	}
}

class ChangePasswordDialog_buttonCancel_actionAdapter
		implements java.awt.event.ActionListener{
	ChangePasswordDialog adaptee;

	ChangePasswordDialog_buttonCancel_actionAdapter(ChangePasswordDialog adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonCancel_actionPerformed(e);
	}
}

class ChangePasswordDialog_buttonHelp_actionAdapter
		implements java.awt.event.ActionListener{
	ChangePasswordDialog adaptee;

	ChangePasswordDialog_buttonHelp_actionAdapter(ChangePasswordDialog adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonHelp_actionPerformed(e);
	}
}
