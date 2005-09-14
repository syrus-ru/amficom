/*-
 * $Id: KISConnection.java,v 1.8 2005/09/14 18:13:47 arseniy Exp $
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
 * @version $Revision: 1.8 $, $Date: 2005/09/14 18:13:47 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
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
