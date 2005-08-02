/*-
* $Id: ManagerModel.java,v 1.1 2005/08/02 14:40:51 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;


/**
 * @version $Revision: 1.1 $, $Date: 2005/08/02 14:40:51 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ManagerModel extends ApplicationModel {

	Dispatcher					dispatcher;
	
	public ManagerModel(ApplicationContext aContext) {
		// this.aContext = aContext;
		this.dispatcher = aContext.getDispatcher();


		//
		this.add("menuSession");
		this.add("menuSessionNew");
		this.add("menuSessionClose");
		this.add("menuSessionOptions");
		this.add("menuSessionConnection");
		this.add("menuSessionChangePassword");
		this.add("menuSessionSave");
		this.add("menuSessionUndo");
		this.add("menuSessionDomain");
		this.add("menuExit");

		this.add(ApplicationModel.MENU_VIEW_ARRANGE);


		this.add(ApplicationModel.MENU_HELP);
		this.add(ApplicationModel.MENU_HELP_ABOUT);

		this.setVisible("menuSessionSave", false);
		this.setVisible("menuSessionUndo", false);
		this.setVisible("menuSessionOptions", false);


		this.setVisible(ApplicationModel.MENU_HELP, true);
		this.setVisible(ApplicationModel.MENU_HELP_ABOUT, true);

	}
}

