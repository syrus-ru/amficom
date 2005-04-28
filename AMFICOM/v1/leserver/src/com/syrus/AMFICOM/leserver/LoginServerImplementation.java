/*
 * $Id: LoginServerImplementation.java,v 1.2 2005/04/28 10:35:43 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.SecurityKey;
import com.syrus.AMFICOM.leserver.corba.LoginServerPOA;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/28 10:35:43 $
 * @author $Author: arseniy $
 * @module loginserver_v1
 */
public final class LoginServerImplementation extends LoginServerPOA {
	private static final long serialVersionUID = -7190112124735462314L;

	public SecurityKey login(String login, String password, Identifier_TransferableHolder user_id) throws AMFICOMRemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public void logout(SecurityKey security_key) throws AMFICOMRemoteException {
		// TODO Auto-generated method stub
		
	}

	public Domain_Transferable[] transmitAvailableDomains(SecurityKey security_key) throws AMFICOMRemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public void selectDomain(Identifier_Transferable domain_id) throws AMFICOMRemoteException {
		// TODO Auto-generated method stub
		
	}

	public void verify(byte i) {
		Log.debugMessage("Verify value: " + i, Log.DEBUGLEVEL10);
	}

}
