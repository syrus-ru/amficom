package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.measurement.Measurement;

public interface KISConnection {

	boolean isEstablished();

	void establish(long kisConnectionTimeout) throws CommunicationException;

	void establish(long kisConnectionTimeout, boolean dropIfAlreadyEstablished) throws CommunicationException; 

	void drop();

	void transmitMeasurement(Measurement measurement) throws CommunicationException;
}
