package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.util.Log;

public class SchemeNewCommand extends AbstractCommand {
	ApplicationContext aContext;

	public SchemeNewCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		try {
			Scheme scheme = SchemeObjectsFactory.createScheme();
			
			this.aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, scheme.getId(), SchemeEvent.OPEN_SCHEME));
			this.result = RESULT_OK;
		} 
		catch (CreateObjectException e) {
			Log.errorMessage(e);
		}
	}
}
