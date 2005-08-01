package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.util.Log;

public class SchemeNewCommand extends AbstractCommand {
	ApplicationContext aContext;

	public SchemeNewCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public void execute() {
		try {
			Scheme scheme = SchemeObjectsFactory.createScheme();
			
			ApplicationModel aModel = aContext.getApplicationModel(); 
			aModel.getCommand("menuWindowScheme").execute();
			aModel.getCommand("menuWindowTree").execute();
			aModel.getCommand("menuWindowUgo").execute();
			aModel.getCommand("menuWindowProps").execute();
			aModel.getCommand("menuWindowList").execute();
			
			aModel.getCommand(ApplicationModel.MENU_VIEW_ARRANGE).execute();
			
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, scheme, SchemeEvent.OPEN_SCHEME));
			result = RESULT_OK;
		} 
		catch (CreateObjectException e) {
			Log.errorException(e);
		}
	}
}
