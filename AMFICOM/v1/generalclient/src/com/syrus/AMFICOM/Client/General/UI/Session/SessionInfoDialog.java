/*
 * $Id: SessionInfoDialog.java,v 1.6 2004/09/25 19:42:21 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.UI.Session;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.Domain;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import oracle.jdeveloper.layout.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2004/09/25 19:42:21 $
 * @module generalclient_v1
 */
public class SessionInfoDialog extends JDialog
{
	SessionInterface si;

	JPanel jPanel1 = new JPanel();
	XYLayout xYLayout1 = new XYLayout();
	JButton buttonOk = new JButton();
	JLabel jLabel1 = new JLabel();
	JLabel jLabel3 = new JLabel();
	JLabel jLabel5 = new JLabel();
	JLabel jLabel7 = new JLabel();
	JLabel jLabel9 = new JLabel();
	JLabel jLabel11 = new JLabel();
	JLabel jLabel13 = new JLabel();
	JLabel labelServer = new JLabel();
	JLabel labelUser = new JLabel();
	JLabel labelCategory = new JLabel();
	JLabel labelSessionStart = new JLabel();
	JLabel labelSessionTotal = new JLabel();
	JLabel labelConnectPeriod = new JLabel();
	JLabel labelConnectLast = new JLabel();

	JSeparator jSplitPane1 = new JSeparator();
	JSeparator jSplitPane2 = new JSeparator();
	JSeparator jSplitPane3 = new JSeparator();
	private JLabel jLabel2 = new JLabel();

	protected SessionInfoDialog(Frame parent, String title, boolean modal)
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

	public SessionInfoDialog()
	{
		this(Environment.getActiveWindow(), LangModel.getString("SessionInfoTitle"), false);
	}

	public SessionInfoDialog(SessionInterface si)
	{
		this();

		SimpleDateFormat sdf =
				new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		SimpleDateFormat sdf2 =
				new java.text.SimpleDateFormat(":mm:ss");

		this.si = si;

		labelServer.setText(ConnectionInterface.getInstance().getServerName());
		labelUser.setText(si.getUser());
		labelCategory.setText("");
		long lot = si.getLogonTime();
		labelSessionStart.setText(sdf.format(new Date(lot)));
		long cm = System.currentTimeMillis();
		long st = cm - lot;
		long st2 = st / 3600000;//hours
		String s1 = String.valueOf(st2) + sdf2.format(new Date(st));
		labelSessionTotal.setText(s1);
		labelConnectPeriod.setText(((ObjectResource)Pool.get(Domain.typ, si.getDomainId())).getName());
		labelConnectLast.setText("");
	}

	private void jbInit() throws Exception
	{
		this.setResizable(false);
		jPanel1.setLayout(xYLayout1);
		xYLayout1.setHeight(224);
		xYLayout1.setWidth(400);
		buttonOk.setText(LangModel.getString("buttonClose"));
		buttonOk.addActionListener(new SessionInfoDialog_buttonOk_actionAdapter(this));
		jLabel1.setText(LangModel.getString("labelServer"));
		jLabel3.setText(LangModel.getString("labelUser"));
		jLabel5.setText(LangModel.getString("labelCategory"));
		jLabel7.setText(LangModel.getString("labelSessionStart"));
		jLabel9.setText(LangModel.getString("labelSessionTotal"));
		jLabel11.setText(LangModel.getString("labelActiveDomain"));
		jLabel13.setText(LangModel.getString("labelServerConnectLast"));
		jLabel2.setText(LangModel.getString("labelServerConnectLast2"));

		jSplitPane1.setOrientation(SwingConstants.VERTICAL);
		jSplitPane2.setOrientation(SwingConstants.HORIZONTAL);
		jSplitPane3.setOrientation(SwingConstants.VERTICAL);
		getContentPane().add(jPanel1);

		jPanel1.add(jLabel1, new XYConstraints(8, 10, 116, -1));
		jPanel1.add(jLabel3, new XYConstraints(8, 35, 116, -1));
		jPanel1.add(jLabel5, new XYConstraints(8, 60, 116, -1));
		jPanel1.add(jLabel7, new XYConstraints(8, 85, 116, -1));
		jPanel1.add(jLabel9, new XYConstraints(8, 109, 137, -1));
		jPanel1.add(jLabel11, new XYConstraints(8, 134, 191, -1));
		jPanel1.add(jLabel13, new XYConstraints(8, 159, 256, -1));
		jPanel1.add(jLabel2, new XYConstraints(8, 169, 256, -1));

		jPanel1.add(labelServer, new XYConstraints(153, 10, 237, -1));
		jPanel1.add(labelUser, new XYConstraints(153, 35, 237, -1));
		jPanel1.add(labelCategory, new XYConstraints(153, 60, 237, -1));
		jPanel1.add(labelSessionStart, new XYConstraints(153, 85, 237, -1));
		jPanel1.add(labelSessionTotal, new XYConstraints(153, 107, 237, -1));
		jPanel1.add(labelConnectPeriod, new XYConstraints(268, 132, 142, -1));
		jPanel1.add(labelConnectLast, new XYConstraints(268, 159, 142, -1));

		jPanel1.add(jSplitPane1, new XYConstraints(147, 5, 1, 120));
		jPanel1.add(jSplitPane2, new XYConstraints(5, 129, 385, 1));
		jPanel1.add(jSplitPane3, new XYConstraints(262, 133, 1, 50));

		jPanel1.add(buttonOk, new XYConstraints(306, 190, -1, 27));
	}

	void buttonOk_actionPerformed(ActionEvent e)
	{
		dispose();
	}
}

class SessionInfoDialog_buttonOk_actionAdapter implements java.awt.event.ActionListener
{
	SessionInfoDialog adaptee;

	SessionInfoDialog_buttonOk_actionAdapter(SessionInfoDialog adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonOk_actionPerformed(e);
	}
}
