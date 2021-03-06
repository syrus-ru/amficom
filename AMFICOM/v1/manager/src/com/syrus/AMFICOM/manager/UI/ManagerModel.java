/*-
* $Id: ManagerModel.java,v 1.8 2005/11/28 14:47:04 bob Exp $
*
* Copyright ? 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;


/**
 * @version $Revision: 1.8 $, $Date: 2005/11/28 14:47:04 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ManagerModel extends ApplicationModel {

	Dispatcher					dispatcher;
	
	public static final String	FLUSH_COMMAND			= "FlushCommand";
	
	public ManagerModel(final ApplicationContext aContext) {
		this.dispatcher = aContext.getDispatcher();

		this.add(MENU_SESSION);
		this.add(MENU_SESSION_NEW);
		this.add(MENU_SESSION_CLOSE);
		this.add(MENU_SESSION_OPTIONS);
		this.add(MENU_SESSION_CHANGE_PASSWORD);
		this.add(MENU_EXIT);

		this.add(MENU_VIEW);
		this.add(ManagerMainFrame.GRAPH_FRAME);
		this.add(ManagerMainFrame.TREE_FRAME);
		this.add(ManagerMainFrame.PROPERTIES_FRAME);
		this.add(MENU_VIEW_ARRANGE);

		this.add(FLUSH_COMMAND);
		this.setEnabled(FLUSH_COMMAND, true);
		
		this.add(MENU_HELP);
		this.add(MENU_HELP_ABOUT);

		this.setVisible(MENU_HELP, true);
		this.setVisible(MENU_HELP_ABOUT, true);
		
		this.setVisible(MENU_VIEW, true);
		this.setVisible(MENU_VIEW_ARRANGE, true);
		
		this.setEnabled(MENU_VIEW, true);
		this.setEnabled(MENU_VIEW_ARRANGE, true);

	}
	
	public final Dispatcher getDispatcher() {
		return this.dispatcher;
	}
}

