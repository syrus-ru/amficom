/*-
 * $Id: KISConnection.java,v 1.7 2005/09/12 16:31:53 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.measurement.Measurement;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.7 $, $Date: 2005/09/12 16:31:53 $
 * @module mcm
 */
public interface KISConnection {

	boolean isEstablished();

	void establish(final long kisConnectionTimeout) throws CommunicationException;

	void establish(final long kisConnectionTimeout, final boolean dropIfAlreadyEstablished) throws CommunicationException;

	void drop();

	void transmitMeasurement(final Measurement measurement, final long timewait) throws CommunicationException;

	KISReport receiveKISReport(final long timewait) throws CommunicationException;

	Identifier getKISId();
}
