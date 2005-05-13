/*
 * $Id: UserPane.java,v 1.6 2005/05/13 19:03:16 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceCatalogFrame;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.General.UI.RejectDialog;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.corba.portable.reflect.common.ObjectResource;
import com.syrus.AMFICOM.general.StorableObject;

/**
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/05/13 19:03:16 $
 * @module generalclient_v1
 */
public final class UserPane extends JPanel implements ObjectResourcePropertiesPane, OperationListener {
	private static UserPane instance = null;

	private User user;

	private ApplicationContext aContext = new ApplicationContext();

	private Dispatcher dispatcher;

	private User loggedUser;

	private BorderLayout borderLayout1 = new BorderLayout();

	private UserPanel up = new UserPanel();

	/**
	 * @deprecated Use {@link #getInstance()} instead.
	 */
	public UserPane() {
		jbInit();
	}

	/**
	 * @deprecated Use {@link #getInstance()} instead.
	 */
	public UserPane(User user) {
		this();
		this.setObjectResource(user);
	}

	private void jbInit() {
		this.setPreferredSize(new Dimension(500, 500));
		this.setLayout(borderLayout1);
		this.add(up);
	}

	public boolean save() {
		return false;
	}

	public boolean modify() {
		return false;
	}

	public boolean cancel() {
		return false;
	}

	public boolean create() {
		new RejectDialog(" ������. ������������ ��������� ������ \n ����������� �������� ���������������� �������.");
		return false;
	}

	public boolean open() {
		return false;
	}

	public boolean delete() {
		new RejectDialog(" ������. ������������ ����� ���� ������ ������ \n ����������� �������� ���������������� �������.");
		return false;
	}

	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
		String userID = this.aContext.getSessionInterface().getUserId();
		this.dispatcher = this.aContext.getDispatcher();
		this.loggedUser = (User)Pool.get(User.class.getName(), userID);
		this.dispatcher.register(this, User.class.getName()+"updated");
	}

	public void setObjectResource(ObjectResource or) {
		ObjectResourceCatalogFrame f = (ObjectResourceCatalogFrame)
		Pool.get("ObjectFrame", "AdministrateObjectFrame");
		if (f != null)
			f.setTitle("������������");
		if (!Checker.checkCommand(loggedUser, Checker.readUserInfo)) {
			this.showTheWindow(false);
			setData(or);
			return;
		}
		this.showTheWindow(true);
		setData(or);
	}

	private void setData(ObjectResource or) {
		this.user = (User) or;
		user.updateLocalFromTransferable();
		up.setObjectResource(or);
	}

	public StorableObject getObjectResource() {
		return user;
	}

	private void showTheWindow(boolean key) {
		this.up.setVisible(key);
		this.repaint();
	}

	public void operationPerformed(OperationEvent oe) {
		if (oe.getActionCommand().equals(User.class.getName() + "updated") && user != null) {
			user = (User)Pool.get(User.class.getName(), user.getId());
			this.setData(user);
		}
	}

	public static UserPane getInstance() {
		if (instance == null)
			synchronized (UserPane.class) {
				if (instance == null)
					instance = new UserPane();
			}
		return instance;
	}
}
