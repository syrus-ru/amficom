package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.Identifier;

//import com.syrus.AMFICOM.general.SleepButWorkThread;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.util.Log;
import com.syrus.util.ApplicationProperties;

public class KISConnectionManager/* extends SleepButWorkThread*/ {

	private KISConnectionLRUMap kisConnections;	//KISConnectionLRUMap <Identifier kisId, KISConnection kisConnection>

	public KISConnectionManager() {
//		super(ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_TICK_TIME, MeasurementControlModule.KIS_TICK_TIME) * 1000,
//				ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_MAX_FALLS, MeasurementControlModule.KIS_MAX_FALLS));

		this.kisConnections = new KISConnectionLRUMap(ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_MAX_OPENED_CONNECTIONS, MeasurementControlModule.KIS_MAX_OPENED_CONNECTIONS));
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

	public void dropConnection(Identifier kisId) {
		KISConnection kisConnection = (KISConnection)this.kisConnections.get(kisId);
		if (kisConnection != null) {
			kisConnection.drop();
			this.kisConnections.remove(kisId);
		}
		else
			Log.errorMessage("KISConnectionManager.dropConnection | Connection for KIS '" + kisId + "' not found");
	}

	private KISConnection establishNewConnection(KIS kis) throws CommunicationException {
		KISConnection kisConnection = new TCPKISConnection(kis);
		long kisConnectionTimeout = ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_CONNECTION_TIMEOUT, MeasurementControlModule.KIS_CONNECTION_TIMEOUT)*1000;
		kisConnection.establish(kisConnectionTimeout, false);
		return kisConnection;
	}

//	protected void processFall() {
//		switch (super.fallCode) {
//			case FALL_CODE_NO_ERROR:
//				break;
//		default:
//				Log.errorMessage("processError | Unknown error code: " + super.fallCode);
//		}
//	}
}
