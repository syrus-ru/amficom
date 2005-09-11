/*-
 * $Id: OpenSchemeViewCommand.java,v 1.1 2005/09/11 17:39:24 krupenn Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.observer.command;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ObserverApplicationModel;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/09/11 17:39:24 $
 * @module surveyclient_v1
 */

public class OpenSchemeViewCommand extends AbstractCommand {
	ApplicationContext aContext;
	
	public OpenSchemeViewCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public void execute() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		
		aModel.getCommand(ObserverApplicationModel.MENU_VIEW_RESULTS).execute();
		aModel.getCommand(ObserverApplicationModel.MENU_VIEW_ALARMS).execute();
		aModel.getCommand(ObserverApplicationModel.MENU_VIEW_NAVIGATOR).execute();

		aModel.getCommand(ObserverApplicationModel.MENU_VIEW_SCHEME).execute();
		aModel.getCommand(ObserverApplicationModel.MENU_VIEW_GENERAL_PROPERTIES).execute();
		aModel.getCommand(ObserverApplicationModel.MENU_VIEW_ADDITIONAL_PROPERTIES).execute();
		aModel.getCommand(ObserverApplicationModel.MENU_VIEW_CHARACTERISTICS).execute();

		aModel.getCommand(ApplicationModel.MENU_VIEW_ARRANGE).execute();
		
	}
}
