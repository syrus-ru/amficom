/*
 * $Id: OperatorProfilePasswordPanel.java,v 1.2 2004/08/05 12:56:35 bass Exp $
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
 * @version $Revision: 1.2 $, $Date: 2004/08/05 12:56:35 $
 * @author $Author: bass $
 * @module admin_v1
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
	private JLabel jLabelStatus = new JLabel();
	private JPasswordField jPasswordField = new JPasswordField();
	private JPasswordField jPasswordFieldRepeat = new JPasswordField();
	private JPasswordField oldPasswordField = new JPasswordField();
	private JTextField jTextFieldLogin = new JTextField();

	private static final String ERROR_SHORT = "Ошибка: в пароле менее трёх символов.";
	private static final String ERROR_NON_EQUAL = "Ошибка: пароли не совпадают.";
	private static final String ERROR_NOT_OWNER = "Ошибка: неправильный старый пароль.";
	private static final String OK = "Пароль успешно изменён.";

	private String password = "";

	OperatorProfilePasswordPanel()
	{
		jbInit();
	}

	public ObjectResource getObjectResource()
	{
		return profile;
	}

	/**
	 * Should return void.
	 *
	 * @todo Consider refactoring.
	 */
	public boolean setObjectResource(ObjectResource or)
	{
		this.profile = (OperatorProfile) or;
		this.jTextFieldLogin.setText(profile.login);
		this.password = profile.password;
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
		profile.password = this.password;
		return true;
	}

	private void jbInit()
	{
		jLabelLogin.setText("Логин");
		jLabelLogin.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		jLabelStatus.setText("");
		jLabelStatus.setPreferredSize(new Dimension(2 * DEF_WIDTH, DEF_HEIGHT));
		setName("Установка пароля");
		this.setLayout(gridBagLayout1);

		jButtonSave.setText("Сохранить");
		jButtonSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String pass = new String(OperatorProfilePasswordPanel.this.jPasswordField.getPassword());
				String passRepeat = new String(OperatorProfilePasswordPanel.this.jPasswordFieldRepeat.getPassword());
				if (!password.equals(new String(oldPasswordField.getPassword())))
				{
					OperatorProfilePasswordPanel.this.jLabelStatus.setText(OperatorProfilePasswordPanel.ERROR_NOT_OWNER);
					jLabelStatus.setForeground(Color.RED);
				}
				else if (!pass.equals(passRepeat))
				{
					OperatorProfilePasswordPanel.this.jLabelStatus.setText(OperatorProfilePasswordPanel.ERROR_NON_EQUAL);
					jLabelStatus.setForeground(Color.RED);
				}
				else if (pass.length() < 3)
				{
					OperatorProfilePasswordPanel.this.jLabelStatus.setText(OperatorProfilePasswordPanel.ERROR_SHORT);
					jLabelStatus.setForeground(Color.RED);
				}
				else
				{
					OperatorProfilePasswordPanel.this.password = new String(OperatorProfilePasswordPanel.this.jPasswordField.getPassword());
					OperatorProfilePasswordPanel.this.jLabelStatus.setText(OperatorProfilePasswordPanel.OK);
					jLabelStatus.setForeground(Color.BLUE);
				}
				OperatorProfilePasswordPanel.this.repaint();
			}
		});
		jLabelPassRepeat.setText(LangModel.getString("labelNewPassword2"));
		jLabelPassRepeat.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		jLabelPass.setText(LangModel.getString("labelNewPassword"));
		jLabelPass.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
		oldPasswordField.setEditable(true);
		jLabel1.setText(LangModel.getString("labelOldPassword"));
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
		this.jPasswordField.setEditable(true);
		this.jPasswordFieldRepeat.setEditable(true);
	}

	private boolean incorrectSymbols(String pass)
	{
		for (int i = 0; i < pass.length(); i++)
			if (pass.charAt(i) == ' ')
				return true;
		return false;
	}

	void setLogin(String login) {
		this.jTextFieldLogin.setText(login);
	}
}
