/*
 * $Id: WhoAmIPasswordPanel.java,v 1.3 2004/09/27 16:21:03 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.Object.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import javax.swing.*;
import oracle.jdeveloper.layout.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/09/27 16:21:03 $
 * @module admin_v1
 */
final class WhoAmIPasswordPanel extends JPanel
{
	private BorderLayout borderLayout1 = new BorderLayout();
	private JButton applyButton = new JButton();
	private JLabel jLabel1 = new JLabel();
	private JLabel jLabel2 = new JLabel();
	private JLabel jLabel3 = new JLabel();
	JLabel jLabelError = new JLabel();
	JLabel jLabelOK = new JLabel();
	private JPanel labelsPanel = new JPanel();
	private JPanel jPanel1 = new JPanel();
	private JPanel jPanel2 = new JPanel();
	private JPanel textPanel = new JPanel();
	JPasswordField jOldPasswordField = new JPasswordField();
	JPasswordField jPasswordField1 = new JPasswordField();
	JPasswordField jPasswordField2 = new JPasswordField();
	private VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
	private VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();
	private XYLayout xYLayout1 = new XYLayout();

	private static final String ERROR_COMMON = "Ошибка! Пароль не был изменён.";
	private static final String ERROR_SHORT = "Ошибка! Пароль короче 3-х знаков!";
	private static final String ERROR_WRONG_ALL = "Ошибка! Неправильный старый пароль!";
	private static final String ERROR_NON_EQUAL = "Ошибка! Введённые пароли отличаются!";
	private static final String OK = "Пароль успешно изменён.";

	String password;
	private User user;
	OperatorProfile op;
	private ApplicationContext aContext;
	private Dispatcher dispatcher;

	WhoAmIPasswordPanel()
	{
		jbInit();
	}

	private void jbInit()
	{
		this.setLayout(this.borderLayout1);
		this.setName("Пароль");
		this.textPanel.setLayout(this.verticalFlowLayout1);
		this.jLabel1.setPreferredSize(new Dimension(89, 21));
		this.jLabel1.setText(LangModel.getString("labelOldPassword"));
		this.jLabel2.setPreferredSize(new Dimension(44, 21));
		this.jLabel2.setText(LangModel.getString("labelNewPassword"));
		this.jLabel3.setPreferredSize(new Dimension(106, 21));
		this.jLabel3.setText(LangModel.getString("labelNewPassword2"));
		this.jPanel1.setLayout(this.verticalFlowLayout2);
		this.applyButton.setText("Применить");
		this.jLabelError.setText("   ");
		this.labelsPanel.setLayout(this.xYLayout1);
		this.jLabelOK.setText("   ");
		this.add(this.labelsPanel, BorderLayout.SOUTH);
		this.add(this.textPanel, BorderLayout.WEST);
		this.add(this.jPanel1, BorderLayout.CENTER);
		this.jPanel1.add(this.jOldPasswordField, null);
		this.textPanel.add(this.jLabel1, null);
		this.textPanel.add(this.jLabel2, null);
		this.textPanel.add(this.jLabel3, null);
		this.jPanel1.add(this.jPasswordField1, null);
		this.jPanel1.add(this.jPasswordField2, null);
		this.jPanel1.add(this.jPanel2, null);
		this.jPanel2.add(this.applyButton, null);
		this.labelsPanel.add(this.jLabelError, new XYConstraints(5, 2, -1, -1));
		this.labelsPanel.add(this.jLabelOK, new XYConstraints(5, 2, -1, -1));

		this.applyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String p1 = new String(WhoAmIPasswordPanel.this.jPasswordField1.getPassword());
				String p2 = new String(WhoAmIPasswordPanel.this.jPasswordField2.getPassword());

				if (p1.equals(p2) && p1.length() >= 3 && new String(WhoAmIPasswordPanel.this.jOldPasswordField.getPassword()).equals(WhoAmIPasswordPanel.this.op.password))
				{
					WhoAmIPasswordPanel.this.password = p1;
					modify();

					WhoAmIPasswordPanel.this.jLabelOK.setText(OK);
					WhoAmIPasswordPanel.this.jLabelError.setText("");
					repaint();
				}
				else if (!new String(WhoAmIPasswordPanel.this.jOldPasswordField.getPassword()).equals(WhoAmIPasswordPanel.this.op.password))
				{
					WhoAmIPasswordPanel.this.jLabelError.setText(ERROR_WRONG_ALL);
					WhoAmIPasswordPanel.this.jLabelOK.setText("");
					repaint();
				}
				else if (!p1.equals(p2))
				{
					WhoAmIPasswordPanel.this.jLabelError.setText(ERROR_NON_EQUAL);
					WhoAmIPasswordPanel.this.jLabelOK.setText("");
					repaint();
				}
				else if (p1.length() < 3)
				{
					WhoAmIPasswordPanel.this.jLabelError.setText(ERROR_SHORT);
					WhoAmIPasswordPanel.this.jLabelOK.setText("");
					repaint();
				}
				else
				{
					WhoAmIPasswordPanel.this.jLabelError.setText(ERROR_COMMON);
					WhoAmIPasswordPanel.this.jLabelOK.setText("");
					repaint();
				}
			}
		});

		this.jLabelError.setForeground(Color.RED);
		this.jLabelOK.setForeground(Color.BLUE);
	}

	void setAContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		this.dispatcher = this.aContext.getDispatcher();
	}

	void setData(User user, OperatorProfile op)
	{
		if (user == null || op == null)
			return;
		this.op = op;
		this.user = user;
	}

	void modify()
	{
		Date d = new Date();
		this.op.password = this.password;
		this.op.modified_by = this.user.id;
		this.op.modified = d.getTime();
		Pool.put(OperatorProfile.typ, this.op.id, this.op);
		this.aContext.getDataSource().SaveOperatorProfile(this.op.id);
		this.dispatcher.notify(new OperationEvent(this, 0, OperatorProfile.typ + "updated"));
	}
}
