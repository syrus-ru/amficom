/*
 * $Id: MCMImplementation.java,v 1.7 2004/08/04 17:29:26 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.mcm.corba.MCMPOA;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;

/**
 * @version $Revision: 1.7 $, $Date: 2004/08/04 17:29:26 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class MCMImplementation extends MCMPOA {

	public MCMImplementation() {
	}

	public void transmitTests(Test_Transferable[] tts) throws AMFICOMRemoteException {
		System.out.println("length: " + tts.length);
		for (int i = 0; i < tts.length; i++) {
			
		}
	}

	public void ping(int i) {
		System.out.println("i == " + i);
	}
}
