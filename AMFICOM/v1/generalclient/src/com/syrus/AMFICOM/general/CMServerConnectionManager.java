/*
 * $Id: CMServerConnectionManager.java,v 1.3 2005/04/22 21:14:06 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.Collections;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.Verifiable;
import com.syrus.util.Log;

/**
 * @deprecated
 * @version $Revision: 1.3 $, $Date: 2005/04/22 21:14:06 $
 * @author $Author: arseniy $
 * @module generalclient_v1
 */
public final class CMServerConnectionManager extends VerifiedConnectionManager implements IGSConnectionManager {
	private String cmServerServantName;

	public CMServerConnectionManager(CORBAServer corbaServer, String cmServerServantName) {
		super(corbaServer, Collections.singleton(cmServerServantName));

		this.cmServerServantName = cmServerServantName;
	}

	public IdentifierGeneratorServer getVerifiedIGSReference() throws CommunicationException {
		Verifiable reference = null;
		try {
			reference = super.getVerifiableReference(this.cmServerServantName);
		}
		catch (IllegalDataException ide) {
			// Never
			Log.errorException(ide);
		}
		return (IdentifierGeneratorServer) reference;
	}

	public CMServer getVerifiedCMServerReference() throws CommunicationException {
		Verifiable reference = null;
		try {
			reference = super.getVerifiableReference(this.cmServerServantName);
		}
		catch (IllegalDataException ide) {
			// Never
			Log.errorException(ide);
		}
		return (CMServer) reference;
	}

	protected void onLoseConnection(String servantName) {
		Log.debugMessage("CMServerConnectionManager.onLoseConnection | Connection with '" + servantName + "' lost", Log.DEBUGLEVEL08);
	}

	protected void onRestoreConnection(String servantName) {
		Log.debugMessage("CMServerConnectionManager.onRestoreConnection | Connection with '" + servantName + "' restored",
				Log.DEBUGLEVEL08);
	}

}
