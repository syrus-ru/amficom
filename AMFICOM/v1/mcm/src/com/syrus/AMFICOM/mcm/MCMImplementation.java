package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.mcm.corba._MCMImplBase;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;

public class MCMImplementation extends _MCMImplBase {

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