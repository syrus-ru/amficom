/*
 * $Id: ChangePasswordDialog.java,v 1.6 2004/09/25 19:34:28 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.UI.Session;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import java.awt.event.*;
import javax.swing.*;
import oracle.jdeveloper.layout.*;

/**
 * Диалоговое окно изменения пароля активного пользователя. Модуль вызывается
 * для изменения пароля текущего пользователя. Для этого модулю передается
 * структура, описывающая текущую сессию. При нажатии кнопки
 * &quot;Изменить&quot; проверяется соответствие введенного логина и старого
 * пароля открытой сессии и равенство введённого и повторённого нового пароля.
 *
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2004/09/25 19:34:28 $
 * @module generalclient_v1
 */
public final class ChangePasswordDialog extends JDialog
{
	ApplicationContext aContext;
	int returnCode = 0;

	private JPanel jPanel1 = new JPanel();
	private JButton buttonOk = new JButton();
	private JButton buttonHelp = new JButton();
	private JButton buttonCancel = new JButton();
	private XYLayout xYLayout1 = new XYLayout();
	private JLabel jLabel1 = new JLabel();
	private JLabel jLabel2 = new JLabel();
	private JLabel jLabel3 = new JLabel();
	private JLabel jLabel4 = new JLabel();
	JTextField fieldUser = new JTextField();
	JPasswordField fieldOldPassword = new JPasswordField();
	JPasswordField fieldNewPassword = new JPasswordField();
	JPasswordField fieldNewPassword2 = new JPasswordField();

	public static final int RET_OK = 1;
	public static final int RET_CANCEL = 2;

	public ChangePasswordDialog(ApplicationContext aContext)
	{
		super(Environment.getActiveWindow(), LangModel.getString("ChangePasswordTitle"), false);
		jbInit();
		pack();
		this.aContext = aContext;
	}

	private void jbInit()
	{
		this.setResizable(false);
		this.jPanel1.setLayout(this.xYLayout1);
		this.xYLayout1.setWidth(343);
		this.xYLayout1.setHeight(158);

		this.jLabel1.setText(LangModel.getString("labelName"));
		this.jLabel2.setText(LangModel.getString("labelOldPassword"));
		this.jLabel3.setText(LangModel.getString("labelNewPassword"));
		this.jLabel4.setText(LangModel.getString("labelNewPassword2"));

		getContentPane().add(this.jPanel1);

		this.buttonOk.setText(LangModel.getString("buttonChange"));
		this.buttonOk.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String puser = ChangePasswordDialog.this.fieldUser.getText();
				String pold = new String(ChangePasswordDialog.this.fieldOldPassword.getPassword());
				String pnew = new String (ChangePasswordDialog.this.fieldNewPassword.getPassword());
				String pnew2 = new String(ChangePasswordDialog.this.fieldNewPassword2.getPassword());
				
				SessionInterface si = ChangePasswordDialog.this.aContext.getSessionInterface();
				
				if (!puser.equals(si.getUser()))
				{
					ChangePasswordDialog.this.fieldUser.setText("");
					ChangePasswordDialog.this.fieldOldPassword.setText("");
					ChangePasswordDialog.this.fieldNewPassword.setText("");
					ChangePasswordDialog.this.fieldNewPassword2.setText("");
					JOptionPane.showMessageDialog(
							ChangePasswordDialog.this,
							LangModel.getString("errorWrongName"),
							LangModel.getString("errorTitleChangePassword"),
							JOptionPane.ERROR_MESSAGE,
							null);
					return;
				}
				
				if (!pold.equals(si.getPassword()))
				{
					ChangePasswordDialog.this.fieldOldPassword.setText("");
					ChangePasswordDialog.this.fieldNewPassword.setText("");
					ChangePasswordDialog.this.fieldNewPassword2.setText("");
					JOptionPane.showMessageDialog(
							ChangePasswordDialog.this,
							LangModel.getString("errorWrongPassword"),
							LangModel.getString("errorTitleChangePassword"),
							JOptionPane.ERROR_MESSAGE,
							null);
					return;
				}
				
				if (pnew.length() < 3)
				{
					ChangePasswordDialog.this.fieldNewPassword.setText("");
					ChangePasswordDialog.this.fieldNewPassword2.setText("");
					JOptionPane.showMessageDialog(
							ChangePasswordDialog.this,
							LangModel.getString("errorPasswordTooShort"),
							LangModel.getString("errorTitleChangePassword"),
							JOptionPane.ERROR_MESSAGE,
							null);
					return;
				}
				if (!pnew.equals(pnew2))
				{
					ChangePasswordDialog.this.fieldNewPassword.setText("");
					ChangePasswordDialog.this.fieldNewPassword2.setText("");
					JOptionPane.showMessageDialog(
							ChangePasswordDialog.this,
							LangModel.getString("errorWrongPassword2"),
							LangModel.getString("errorTitleChangePassword"),
							JOptionPane.ERROR_MESSAGE,
							null);
					return;
				}

				if (!ChangePasswordDialog.this.aContext.getDataSource().ChangePassword(pold, pnew))
				{
					JOptionPane.showMessageDialog(
							ChangePasswordDialog.this,
							"Ошибка изменения пароля",
							LangModel.getString("errorTitleChangePassword"),
							JOptionPane.ERROR_MESSAGE,
							null);
					return;
				}
				si.setPassword(pnew);
				
				ChangePasswordDialog.this.returnCode = RET_OK;
				dispose();
			}
		});
		this.buttonHelp.setText(LangModel.getString("buttonHelp"));
		this.buttonHelp.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(
						ChangePasswordDialog.this,
						"Help system not installed",
						"Help",
						JOptionPane.INFORMATION_MESSAGE,
						null);
			}
		});
		this.buttonCancel.setText(LangModel.getString("buttonCancel"));
		this.buttonCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ChangePasswordDialog.this.returnCode = RET_CANCEL;
				dispose();
			}
		});

		this.jPanel1.add(this.jLabel1, new XYConstraints(11, 12, -1, -1));
		this.jPanel1.add(this.jLabel2, new XYConstraints(11, 39, -1, -1));
		this.jPanel1.add(this.jLabel3, new XYConstraints(11, 67, -1, -1));
		this.jPanel1.add(this.jLabel4, new XYConstraints(11, 94, -1, -1));

		this.jPanel1.add(this.fieldUser, new XYConstraints(133, 10, 201, -1));
		this.jPanel1.add(this.fieldOldPassword, new XYConstraints(133, 37, 201, -1));
		this.jPanel1.add(this.fieldNewPassword, new XYConstraints(133, 65, 201, -1));
		this.jPanel1.add(this.fieldNewPassword2, new XYConstraints(133, 92, 201, -1));

		this.jPanel1.add(this.buttonOk, new XYConstraints(11, 120, -1, 27));
		this.jPanel1.add(this.buttonHelp, new XYConstraints(251, 120, -1, 27));
		this.jPanel1.add(this.buttonCancel, new XYConstraints(131, 120, -1, 27));

		this.fieldUser.requestFocus();
	}

	public int getReturnCode()
	{
		return this.returnCode;
	}
}
