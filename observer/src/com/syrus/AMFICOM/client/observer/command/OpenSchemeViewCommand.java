/*-
 * $Id: OpenSchemeViewCommand.java,v 1.3 2005/10/22 13:22:24 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.observer.command;

import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatchImpl;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ObserverApplicationModel;
import com.syrus.AMFICOM.client.observer.alarm.AlarmReceiver;
import com.syrus.AMFICOM.eventv2.DefaultReflectogramMismatchEvent;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/10/22 13:22:24 $
 * @module surveyclient_v1
 */

public class OpenSchemeViewCommand extends AbstractCommand {
	ApplicationContext aContext;
	
	public OpenSchemeViewCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		AlarmReceiver.getInstance().receiveMessage(DefaultReflectogramMismatchEvent.valueOf(
				new ReflectogramMismatchImpl(), new Identifier("Result_985")));
		
		ApplicationModel aModel = this.aContext.getApplicationModel();
		
		aModel.getCommand(ObserverApplicationModel.MENU_VIEW_RESULTS).execute();
		aModel.getCommand(ObserverApplicationModel.MENU_VIEW_ALARMS).execute();
		aModel.getCommand(ObserverApplicationModel.MENU_VIEW_NAVIGATOR).execute();

		aModel.getCommand(ObserverApplicationModel.MENU_VIEW_SCHEME).execute();
		
		aModel.getCommand(ObserverApplicationModel.MENU_VIEW_GENERAL_PROPERTIES).execute();
		aModel.getCommand(ObserverApplicationModel.MENU_VIEW_ADDITIONAL_PROPERTIES).execute();
		aModel.getCommand(ObserverApplicationModel.MENU_VIEW_CHARACTERISTICS).execute();

		aModel.getCommand(ApplicationModel.MENU_VIEW_ARRANGE).execute();
		new SchemeNewCommand(this.aContext).execute();		
	}
}
