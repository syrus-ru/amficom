package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.measurement.Measurement;

public interface KISConnection {

	boolean isEstablished();

	void establish(final long kisConnectionTimeout) throws CommunicationException;

	void establish(final long kisConnectionTimeout, final boolean dropIfAlreadyEstablished) throws CommunicationException;

	void drop();

	void transmitMeasurement(final Measurement measurement, final long timewait) throws CommunicationException;

	KISReport receiveKISReport(final long timewait) throws CommunicationException;

	Identifier getKISId();
}
