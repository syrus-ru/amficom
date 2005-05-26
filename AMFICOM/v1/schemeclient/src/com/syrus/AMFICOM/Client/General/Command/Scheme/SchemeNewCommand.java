package com.syrus.AMFICOM.Client.General.Command.Scheme;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.model.*;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.resource.Constants;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.corba.Scheme_TransferablePackage.Kind;
import com.syrus.util.Log;

public class SchemeNewCommand extends AbstractCommand {
	private static int counter = 1;
	ApplicationContext aContext;

	public SchemeNewCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public Object clone() {
		return new SchemeNewCommand(aContext);
	}

	public void execute() {
		final Identifier userId = LoginManager.getUserId();
		final Identifier domainId = LoginManager.getDomainId();
		
		try {
			Scheme scheme = Scheme.createInstance(userId, LangModelScheme.getString(Constants.NEW_SCHEME)
					+ (counter == 1 ? "" : "(" + counter + ")"), Kind.NETWORK, domainId); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			counter++;
			aContext.getDispatcher().firePropertyChange(new SchemeEvent(this, scheme, SchemeEvent.OPEN_SCHEME));
			result = RESULT_OK;
		} 
		catch (CreateObjectException e) {
			Log.errorException(e);
		}
	}
}
