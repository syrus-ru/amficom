/*
 * $Id: UserPane.java,v 1.4 2004/09/27 12:54:41 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Administrate.Object.UI;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.User;
import java.awt.*;
import javax.swing.JPanel;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2004/09/27 12:54:41 $
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
		new RejectDialog(" Ошибка. Пользователь создается только \n посредством создания соответствующего профиля.");
		return false;
	}

	public boolean open() {
		return false;
	}

	public boolean delete() {
		new RejectDialog(" Ошибка. Пользователь может быть удален только \n посредством удаления соответствующего профиля.");
		return false;
	}

	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
		String userID = this.aContext.getSessionInterface().getUserId();
		this.dispatcher = this.aContext.getDispatcher();
		this.loggedUser = (User)Pool.get(User.typ, userID);
		this.dispatcher.register(this, User.typ+"updated");
	}

	public void setObjectResource(ObjectResource or) {
		ObjectResourceCatalogFrame f = (ObjectResourceCatalogFrame)
		Pool.get("ObjectFrame", "AdministrateObjectFrame");
		if (f != null)
			f.setTitle("Пользователи");
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

	public ObjectResource getObjectResource() {
		return user;
	}

	private void showTheWindow(boolean key) {
		this.up.setVisible(key);
		this.repaint();
	}

	public void operationPerformed(OperationEvent oe) {
		if (oe.getActionCommand().equals(User.typ + "updated") && user != null) {
			user = (User)Pool.get(User.typ, user.id);
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
