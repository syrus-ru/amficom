/*
 * $Id: AdminApplicationModel.java,v 1.3 2004/09/27 16:26:48 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.*;

/**
 * @version $Revision: 1.3 $, $Date: 2004/09/27 16:26:48 $
 * @author $Author: bass $
 * @module admin_v1
 */
public final class AdminApplicationModel extends ApplicationModel {
	public AdminApplicationModel() {
		add("menuSession");
		add("menuSessionNew");
		add("menuSessionClose");
		add("menuSessionOptions");
		add("menuSessionConnection");
		add("menuSessionChangePassword");
		add("menuExit");

		add("menuView");
		add("menuViewNavigator");
		add("menuViewOpenAll");
		add("menuViewWhoAmI");
		add("menuViewOpenObjectsWindow");

		add("menuArchitecture");
		add("menuArchitectureServer");
		add("menuArchitectureAgent");
		add("menuArchitectureClient");

		add("menuUser");
		add("menuUserCategory");
		add("menuUserGroup");
		add("menuUserProfile");

		add("menuAccess");
		add("menuAccessDomain");
		add("menuAccessModul");
		add("menuAccessMaintain");

		add("menuHelp");
		add("menuHelpContents");
		add("menuHelpFind");
		add("menuHelpTips");
		add("menuHelpStart");
		add("menuHelpCourse");
		add("menuHelpHelp");
		add("menuHelpSupport");
		add("menuHelpLicense");
		add("menuHelpAbout");
	}

	public DataSourceInterface getDataSource(final SessionInterface session) {
		if ((this.session == null) || (!this.session.equals(session)))
			synchronized (this) {
				if ((this.session == null) || (!this.session.equals(session))) {
					this.session = session;
					this.dataSource = new RISDObjectDataSource(this.session);
				}
			}
		return this.dataSource;
	}
}
