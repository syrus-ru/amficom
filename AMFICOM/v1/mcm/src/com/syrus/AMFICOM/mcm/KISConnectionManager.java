package com.syrus.AMFICOM.mcm;

import java.util.Map;
import java.util.Hashtable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.util.Log;
import com.syrus.util.ApplicationProperties;

public class KISConnectionManager extends SleepButWorkThread {
	private static final String KEY_MAX_OPENED_CONNECTIONS = "MaxOpenedConnections";
	private static final String KEY_KIS_CONNECTION_TIMEOUT = "KISConnectionTimeout";

	private static final int MAX_OPENED_CONNECTIONS = 1;
	private static final int KIS_CONNECTION_TIMEOUT = 120;

	private KISConnectionLRUMap kisConnections;	//Map <Identifier kisId, KISConnection kisConnection>

	public KISConnectionManager() {
		super(ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_TICK_TIME, MeasurementControlModule.KIS_TICK_TIME) * 1000,
				ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_MAX_FALLS, MeasurementControlModule.KIS_MAX_FALLS));

		this.kisConnections = new KISConnectionLRUMap(ApplicationProperties.getInt(KEY_MAX_OPENED_CONNECTIONS, MAX_OPENED_CONNECTIONS));
	}

	public KISConnection getConnection(KIS kis) throws CommunicationException {
		Identifier kisId = kis.getId();
		KISConnection kisConnection;

		kisConnection = (KISConnection)this.kisConnections.get(kisId);
		if (kisConnection != null)
			return kisConnection;

		kisConnection = this.establishNewConnection(kis);
		this.kisConnections.put(kisId, kisConnection);
		return kisConnection;
	}

	private KISConnection establishNewConnection(KIS kis) throws CommunicationException {
		KISConnection kisConnection = new TCPKISConnection(kis);
		long kisConnectionTimeout = ApplicationProperties.getInt(KEY_KIS_CONNECTION_TIMEOUT, KIS_CONNECTION_TIMEOUT)*1000;
		kisConnection.establish(kisConnectionTimeout);
		return kisConnection;
	}

	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
		default:
				Log.errorMessage("processError | Unknown error code: " + super.fallCode);
		}
	}
}
