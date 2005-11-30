/*-
 * $Id: OpenSchemeViewCommand.java,v 1.2 2005/11/30 14:06:54 stas Exp $
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
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/11/30 14:06:54 $
 * @module surveyclient_v1
 */

public class OpenSchemeViewCommand extends AbstractCommand {
	ApplicationContext aContext;
	
	public OpenSchemeViewCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	@Override
	public void execute() {
//		AlarmReceiver.getInstance().receiveMessage(DefaultReflectogramMismatchEvent.valueOf(
//				new ReflectogramMismatchImpl(), new Identifier("Result_985")));
		
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
