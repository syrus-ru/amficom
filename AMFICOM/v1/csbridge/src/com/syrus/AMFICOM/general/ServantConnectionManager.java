/*
 * $Id: ServantConnectionManager.java,v 1.2 2005/04/22 21:09:33 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.mshserver.corba.MSHServer;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/22 21:09:33 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public class ServantConnectionManager  implements IGSConnectionManager, CMServerConnectionManager {
//	/** @deprecated*/
//	private static final String KEY_SERVANT_NAME_AMFICOM = "AMFICOMServantName";

	private static final String KEY_SERVANT_NAME_CMSERVER = "CMServerServantName";
	private static final String KEY_SERVANT_NAME_MSHSERVER = "MSHServerServantName";

//	/** @deprecated*/
//	private static final String SERVANT_NAME_AMFICOM = "AMFICOM";

	private static final String SERVANT_NAME_CMSERVER = "CMServer";
	private static final String SERVANT_NAME_MSHSERVER = "MSHServer";

//	/** @deprecated*/
//	private String amficomServantName;

	private String cmServerServantName;
	private String mshServerServantName;

	private VerifiedConnectionManager verifiedConnectionManager;

	public ServantConnectionManager(final String serverHostName) throws CommunicationException {
//		this.amficomServantName = ApplicationProperties.getString(KEY_SERVANT_NAME_AMFICOM, SERVANT_NAME_AMFICOM);
		this.cmServerServantName = ApplicationProperties.getString(KEY_SERVANT_NAME_CMSERVER, SERVANT_NAME_CMSERVER);
		this.mshServerServantName = ApplicationProperties.getString(KEY_SERVANT_NAME_MSHSERVER, SERVANT_NAME_MSHSERVER);

		CORBAServer corbaServer = new CORBAServer(serverHostName);
		this.verifiedConnectionManager = new VerifiedConnectionManager(corbaServer,
				new String[] {/*this.amficomServantName,*/
					this.cmServerServantName,
					this.mshServerServantName});

		//@todo Add here some code to startup client's own servant
	}

	public IdentifierGeneratorServer getVerifiedIGSReference() throws CommunicationException {
		try {
			return (IdentifierGeneratorServer) this.verifiedConnectionManager.getVerifiableReference(this.cmServerServantName);
		}
		catch (IllegalDataException ide) {
			// Never
			assert false;
			return null;
		}
	}
//
//	/**
//	 * @deprecated
//	 * @return
//	 * @throws CommunicationException
//	 */
//	public AMFICOM getAMFICOMReference() throws CommunicationException {
//		try {
//			return (AMFICOM) this.verifiedConnectionManager.getVerifiableReference(this.amficomServantName);
//		}
//		catch (IllegalDataException e) {
//			// Never
//			assert false;
//			return null;
//		}
//	}

	public CMServer getCMServerReference() throws CommunicationException {
		try {
			return (CMServer) this.verifiedConnectionManager.getVerifiableReference(this.cmServerServantName);
		}
		catch (IllegalDataException e) {
			// Never
			assert false;
			return null;
		}
	}

	public MSHServer getMSHServerReference() throws CommunicationException {
		try {
			return (MSHServer) this.verifiedConnectionManager.getVerifiableReference(this.mshServerServantName);
		}
		catch (IllegalDataException e) {
			// Never
			assert false;
			return null;
		}
	}

}
