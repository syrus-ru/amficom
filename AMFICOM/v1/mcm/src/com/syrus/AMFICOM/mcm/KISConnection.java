/*-
 * $Id: KISConnection.java,v 1.9.2.1 2006/03/01 15:50:14 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.util.LRUMap.Retainable;

/**
 * @version $Revision: 1.9.2.1 $, $Date: 2006/03/01 15:50:14 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
public interface KISConnection extends Retainable {

	boolean isEstablished();

	void establish(final long kisConnectionTimeout) throws CommunicationException;

	void establish(final long kisConnectionTimeout, final boolean dropIfAlreadyEstablished) throws CommunicationException;

	void drop();

	void transmitMeasurement(final Measurement measurement, final long timewait) throws ApplicationException;

	KISReport receiveKISReport(final long timewait) throws CommunicationException;

	Identifier getKISId();
}
