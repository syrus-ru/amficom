/*
 * $Id: OperatorProfilePasswordPanel.java,v 1.2 2004/08/06 12:57:38 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.Resource.Object.OperatorProfile;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class actually belongs to <tt>admin_v1</tt> module. It was
 * moved to <tt>generalclient_v1</tt> to resolve cross-module
 * dependencies between <tt>generalclient_v1</tt> and <tt>admin_1</tt>.
 *
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/08/06 12:57:38 $
 * @module generalclient_v1
 */
final class OperatorProfilePasswordPanel extends GeneralPanel
{
	private OperatorProfile profile;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JButton jButtonSave = new JButton();
	private JLabel jLabel1 = new JLabel();
	private JLabel jLabelLogin = new JLabel();
	private JLabel jLabelPass = new JLabel();
	private JLabel jLabelPassRepeat = new JLabel();
	JLabel jLabelStatus = new JLabel();
	JPasswordField jPasswordField = new JPasswordField();
	JPasswordField jPasswordFieldRepeat = new JPasswordField();
	JPasswordField oldPasswordField = new JPasswordField();
	private JTextField jTextFieldLogin = new JTextField();

	private static final String ERROR_SHORT = "Ошибка: в пароле менее трёх символов.";
	private static final String ERROR_NON_EQUAL = "Ошибка: пароли не совпадают.";
	private static final String ERROR_NOT_OWNER = "Ошибка: неправильный старый пароль.";
	private static final String OK = "Пароль успешно изменён.";

	String password = "";

	OperatorProfilePasswordPanel()
	{
		jbInit();
	}

	public ObjectResource getObjectResource()
	{
		return this.profile;
	}

	/**
	 * Should return void.
	 * @param or
	 * @return
	 *
	 * @todo Consider refactoring.
	 */
	public boolean setObjectResource(ObjectResource or)
	{
		this.profile = (OperatorProfile) or;
		this.jTextFieldLogin.setText(this.profile.login);
		this.password = this.profile.password;
		if (this.password == null)
			this.password = "";
		this.jPasswordField.setText("");
		this.jPasswordFieldRepeat.setText("");
		this.jLabelStatus.setText("");
		this.jButtonSave.setVisible(true);
		return true;
	}

	public boolean modify()
	{
		this.profile.password = this.password;
		return true;
	}

	private void jbInit()
	{
		this.jLabelLogin.setText(LangModel.getString("labelName"));
		this.jLabelLogin.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		this.jLabelStatus.setText("");
		this.jLabelStatus.setPreferredSize(new Dimension(2 * DEF_WIDTH, DEF_HEIGHT));
		setName("Установка пароля");
		this.setLayout(this.gridBagLayout1);

		this.jButtonSave.setText("Сохранить");
		this.jButtonSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String pass = new String(OperatorProfilePasswordPanel.this.jPasswordField.getPassword());
				String passRepeat = new String(OperatorProfilePasswordPanel.this.jPasswordFieldRepeat.getPassword());
				if (!OperatorProfilePasswordPanel.this.password.equals(new String(OperatorProfilePasswordPanel.this.oldPasswordField.getPassword())))
				{
					OperatorProfilePasswordPanel.this.jLabelStatus.setText(OperatorProfilePasswordPanel.ERROR_NOT_OWNER);
					OperatorProfilePasswordPanel.this.jLabelStatus.setForeground(Color.RED);
				}
				else if (!pass.equals(passRepeat))
				{
					OperatorProfilePasswordPanel.this.jLabelStatus.setText(OperatorProfilePasswordPanel.ERROR_NON_EQUAL);
					OperatorProfilePasswordPanel.this.jLabelStatus.setForeground(Color.RED);
				}
				else if (pass.length() < 3)
				{
					OperatorProfilePasswordPanel.this.jLabelStatus.setText(OperatorProfilePasswordPanel.ERROR_SHORT);
					OperatorProfilePasswordPanel.this.jLabelStatus.setForeground(Color.RED);
				}
				else
				{
					OperatorProfilePasswordPanel.this.password = new String(OperatorProfilePasswordPanel.this.jPasswordField.getPassword());
					OperatorProfilePasswordPanel.this.jLabelStatus.setText(OperatorProfilePasswordPanel.OK);
					OperatorProfilePasswordPanel.this.jLabelStatus.setForeground(Color.BLUE);
				}
				OperatorProfilePasswordPanel.this.repaint();
			}
		});
		this.jLabelPassRepeat.setText(LangModel.getString("labelNewPassword2"));
		this.jLabelPassRepeat.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		this.jLabelPass.setText(LangModel.getString("labelNewPassword"));
		this.jLabelPass.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		this.oldPasswordField.setEditable(true);
		this.jLabel1.setText(LangModel.getString("labelOldPassword"));
		this.jLabel1.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));

		this.add(this.jLabelLogin, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.jLabel1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.jLabelPass, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.jLabelPassRepeat, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.jLabelStatus, new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.add(this.jTextFieldLogin, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.oldPasswordField, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.jPasswordField, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.jPasswordFieldRepeat, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.jButtonSave, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.add(Box.createVerticalGlue(), new GridBagConstraints(0, 6, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		this.jTextFieldLogin.setEnabled(false);
		this.jPasswordField.setEditable(true);
		this.jPasswordFieldRepeat.setEditable(true);
	}

	void setLogin(String login)
	{
		this.jTextFieldLogin.setText(login);
	}
}
