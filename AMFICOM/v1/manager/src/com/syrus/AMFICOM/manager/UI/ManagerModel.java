/*-
* $Id: ManagerModel.java,v 1.2 2005/08/17 15:59:40 bob Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/08/17 15:59:40 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ManagerModel extends ApplicationModel {

	Dispatcher					dispatcher;
	
	public static final String DOMAINS_COMMAND = "DomainsCommand";
	public static final String FLUSH_COMMAND = "FlushCommand";
	
	public ManagerModel(ApplicationContext aContext) {
		this.dispatcher = aContext.getDispatcher();

		this.add(MENU_SESSION);
		this.add(MENU_SESSION_NEW);
		this.add(MENU_SESSION_CLOSE);
		this.add(MENU_SESSION_OPTIONS);
		this.add(MENU_SESSION_CHANGE_PASSWORD);
		this.add(MENU_SESSION_DOMAIN);
		this.add(MENU_EXIT);

		this.add(ApplicationModel.MENU_VIEW_ARRANGE);

		this.add(DOMAINS_COMMAND);
		this.setEnabled(DOMAINS_COMMAND, true);

		this.add(FLUSH_COMMAND);
		this.setEnabled(FLUSH_COMMAND, true);

		
		this.add(ApplicationModel.MENU_HELP);
		this.add(ApplicationModel.MENU_HELP_ABOUT);

		this.setVisible(ApplicationModel.MENU_HELP, true);
		this.setVisible(ApplicationModel.MENU_HELP_ABOUT, true);

	}
}

