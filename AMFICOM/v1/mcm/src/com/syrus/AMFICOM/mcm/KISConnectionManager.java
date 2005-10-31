/*-
 * $Id: KISConnectionManager.java,v 1.13 2005/10/31 10:47:23 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.Identifier;

import com.syrus.util.KISConnectionLRUMap;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.util.Log;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.13 $, $Date: 2005/10/31 10:47:23 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
final class KISConnectionManager/* extends SleepButWorkThread*/ {

	/**
	 * KISConnectionLRUMap <Identifier kisId, KISConnection kisConnection>
	 */
	private KISConnectionLRUMap kisConnections;

	public KISConnectionManager() {
//		super(ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_TICK_TIME, MeasurementControlModule.KIS_TICK_TIME) * 1000,
//				ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_MAX_FALLS, MeasurementControlModule.KIS_MAX_FALLS));

		this.kisConnections = new KISConnectionLRUMap(ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_MAX_OPENED_CONNECTIONS,
				MeasurementControlModule.KIS_MAX_OPENED_CONNECTIONS));
	}

	KISConnection getConnection(final KIS kis) throws CommunicationException {
		final Identifier kisId = kis.getId();
		KISConnection kisConnection;

		kisConnection = this.kisConnections.get(kisId);
		if (kisConnection != null) {
			return kisConnection;
		}

		Log.debugMessage("Connection for KIS '" + kisId
				+ "' not found in map; establishing new one", Log.DEBUGLEVEL07);
		kisConnection = this.establishNewConnection(kis);
		this.kisConnections.put(kisId, kisConnection);
		return kisConnection;
	}

	private KISConnection establishNewConnection(final KIS kis) throws CommunicationException {
		final KISConnection kisConnection = new TCPKISConnection(kis);
		long kisConnectionTimeout = ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_CONNECTION_TIMEOUT,
				MeasurementControlModule.KIS_CONNECTION_TIMEOUT) * 1000;
		kisConnection.establish(kisConnectionTimeout, false);
		return kisConnection;
	}

//	protected void processFall() {
//		switch (super.fallCode) {
//		case FALL_CODE_NO_ERROR:
//			break;
//		default:
//			Log.errorMessage("Unknown error code: " + super.fallCode);
//		}
//	}
}
