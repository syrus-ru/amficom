/*-
 * $Id: KISConnection.java,v 1.9 2005/12/09 10:00:02 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.util.LRUMap.Retainable;

/**
 * @version $Revision: 1.9 $, $Date: 2005/12/09 10:00:02 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
public interface KISConnection extends Retainable {

	boolean isEstablished();

	void establish(final long kisConnectionTimeout) throws CommunicationException;

	void establish(final long kisConnectionTimeout, final boolean dropIfAlreadyEstablished) throws CommunicationException;

	void drop();

	void transmitMeasurement(final Measurement measurement, final long timewait) throws CommunicationException;

	KISReport receiveKISReport(final long timewait) throws CommunicationException;

	Identifier getKISId();
}
