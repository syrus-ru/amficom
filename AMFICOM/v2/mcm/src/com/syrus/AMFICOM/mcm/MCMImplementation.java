package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.util.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.mcm.corba.MCMPOA;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.measurement.Test;

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