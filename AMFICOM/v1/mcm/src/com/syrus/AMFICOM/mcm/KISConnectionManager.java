/*-
 * $Id: KISConnectionManager.java,v 1.14.2.1 2006/03/16 11:58:45 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mcm;

import static com.syrus.AMFICOM.mcm.MeasurementControlModule.KEY_KIS_CONNECTION_TIMEOUT;
import static com.syrus.AMFICOM.mcm.MeasurementControlModule.KEY_KIS_MAX_OPENED_CONNECTIONS;
import static com.syrus.AMFICOM.mcm.MeasurementControlModule.KIS_CONNECTION_TIMEOUT;
import static com.syrus.AMFICOM.mcm.MeasurementControlModule.KIS_MAX_OPENED_CONNECTIONS;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.14.2.1 $, $Date: 2006/03/16 11:58:45 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
final class KISConnectionManager {
	private LRUMap<Identifier, KISConnection> kisConnections;

	public KISConnectionManager() {
		final int maxOpenedConnections = ApplicationProperties.getInt(KEY_KIS_MAX_OPENED_CONNECTIONS, KIS_MAX_OPENED_CONNECTIONS);
		final long connectionTimeToRetain = ApplicationProperties.getInt(KEY_KIS_CONNECTION_TIMEOUT, KIS_CONNECTION_TIMEOUT) * 1000 * 1000 * 1000;
		this.kisConnections = new LRUMap<Identifier, KISConnection>(maxOpenedConnections, connectionTimeToRetain);
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
		long kisConnectionTimeout = ApplicationProperties.getInt(KEY_KIS_CONNECTION_TIMEOUT, KIS_CONNECTION_TIMEOUT) * 1000;
		kisConnection.establish(kisConnectionTimeout, false);
		return kisConnection;
	}

}
