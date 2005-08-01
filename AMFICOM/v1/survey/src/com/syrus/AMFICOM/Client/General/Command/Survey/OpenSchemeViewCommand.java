/*-
 * $Id: OpenSchemeViewCommand.java,v 1.1 2005/08/01 08:37:21 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Survey;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/08/01 08:37:21 $
 * @module surveyclient_v1
 */

public class OpenSchemeViewCommand extends AbstractCommand {
	ApplicationContext aContext;
	
	public OpenSchemeViewCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public void execute() {
		ApplicationModel aModel = aContext.getApplicationModel();
		
		//XXX remove comment when frames will be compiled 
		aModel.getCommand("menuWindowResults").execute();
//		aModel.getCommand("menuWindowAlarms").execute();
		aModel.getCommand("menuWindowMeasurements").execute();
		
		aModel.getCommand("menuWindowScheme").execute();
		aModel.getCommand("menuWindowProperties").execute();
		aModel.getCommand("menuWindowCharacteristics").execute();
		
		aModel.getCommand(ApplicationModel.MENU_VIEW_ARRANGE).execute();
		
	}
}
