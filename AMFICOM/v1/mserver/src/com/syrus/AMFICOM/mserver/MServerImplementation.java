/*
 * $Id: MServerImplementation.java,v 1.2 2004/07/28 16:32:28 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.mserver.corba._MServerImplBase;

/**
 * @version $Revision: 1.2 $, $Date: 2004/07/28 16:32:28 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */

public class MServerImplementation extends _MServerImplBase {

	public MServerImplementation() {
		
	}

	public void transmitTests(Test_Transferable[] tests) throws AMFICOMRemoteException {
		System.out.println("length: " + tests.length);
		for (int i = 0; i < tests.length; i++) {
			
		}
	}
	
	public void ping(int i) throws AMFICOMRemoteException {
		System.out.println("i == " + i);
	}
}
