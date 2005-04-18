package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.util.Log;

public class SchemeNewCommand extends VoidCommand {
	private static int counter = 1;
	ApplicationContext aContext;

	public SchemeNewCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public Object clone() {
		return new SchemeNewCommand(aContext);
	}

	public void execute() {
		final Identifier userId = new Identifier(((RISDSessionInfo) aContext
				.getSessionInterface()).getAccessIdentifier().user_id);
		final Identifier domainId = new Identifier(((RISDSessionInfo) aContext
				.getSessionInterface()).getAccessIdentifier().domain_id);
		
		try {
			Scheme scheme = Scheme.createInstance(userId, domainId, "Новая схема" + (counter == 1 ? "" : "(" + counter + ")"), "");
			counter++;
			aContext.getDispatcher().notify(new SchemeEvent(this, scheme,
					SchemeEvent.OPEN_SCHEME));
			result = RESULT_OK;
		} 
		catch (CreateObjectException e) {
			Log.errorException(e);
		}
	}
}
