package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

public class TCPKISConnection implements KISConnection {
	private static final String KEY_KIS_HOST_NAME = "KISHostName";
	private static final String KEY_KIS_TCP_PORT = "KISTCPPort";
	
	private static final String KIS_HOST_NAME = "127.0.0.1";
	private static final short KIS_TCP_PORT = 7501;

	private static final int KIS_TCP_SOCKET_DISCONNECTED = -1;

	private Identifier kisId;
	private String kisHostName;
	private short kisTCPPort;
	private int kisTCPSocket;

	public TCPKISConnection(KIS kis) {
		this.kisId = kis.getId();
		
		this.kisHostName = kis.getHostName();
		if (this.kisHostName == null)
			this.kisHostName = ApplicationProperties.getString(KEY_KIS_HOST_NAME, KIS_HOST_NAME);

		this.kisTCPPort = kis.getTCPPort();
		if (this.kisTCPPort <= 0)
			this.kisTCPPort = (short)ApplicationProperties.getInt(KEY_KIS_TCP_PORT, (int)KIS_TCP_PORT);

		this.kisTCPSocket = KIS_TCP_SOCKET_DISCONNECTED;
	}
	
	public boolean isEstablished() {
		return (this.kisTCPSocket > 0);
	}

	public void establish(long kisConnectionTimeout) throws CommunicationException {
		Log.debugMessage("TCPKISConnection.establish | Connecting to KIS '" + this.kisId + "' on host '" + this.kisHostName + "', port " + this.kisTCPPort, Log.DEBUGLEVEL07);
		if (this.isEstablished()) {
			Log.errorMessage("TCPKISConnection.establish | Connection with KIS '" + this.kisId + "' already established -- nothing to do!");
			return;
		}

		long deadtime = System.currentTimeMillis() + kisConnectionTimeout;
		while (System.currentTimeMillis() < deadtime && ! this.isEstablished()) {
			if (! this.establishSocketConnection()) {
				Log.debugMessage("TCPKISConnection.establish | Cannot connect to KIS '" + this.kisId + "' on host '" + this.kisHostName + "', port " + this.kisTCPPort, Log.DEBUGLEVEL07);
				Object obj = new Object();
				try {
					synchronized (obj) {
						obj.wait(5*1000);
					}
				}
				catch (InterruptedException ex) {
					Log.errorException(ex);
				}
			}

		}
		if (this.isEstablished())
			Log.debugMessage("TCPKISConnection.establish | Connected to KIS '" + this.kisId + "'", Log.DEBUGLEVEL07);
		else
			throw new CommunicationException("Cannot connect to KIS '" + this.kisId + "' on host '" + this.kisHostName + "', port " + this.kisTCPPort);
	}

	public void drop() {
		this.dropSocketConnection();
	}

	public void transmitMeasurement(Measurement measurement) throws CommunicationException {
		Identifier measurementId  = measurement.getId();
		Log.debugMessage("TCPKISConnection.transmitMeasurement | Transmitting measurement '" + measurementId + "' to KIS '" + this.kisId + "'", Log.DEBUGLEVEL07);
		if (this.transmitMeasurementBySocket(measurementId.toString(),
								measurement.getType().getCodename(),
								measurement.getLocalAddress(),
								measurement.getSetup().getParameterTypeCodenames(),
								measurement.getSetup().getParameterValues()))
			Log.debugMessage("TCPKISConnection.transmitMeasurement | Transmitted measurement '" + measurementId + "' to KIS '" + this.kisId + "'", Log.DEBUGLEVEL07);
		else
			throw new CommunicationException("TCPKISConnection.transmitMeasurement | Cannot transmit measurement '" + measurementId + "' to KIS '" + this.kisId + "'");
	}

	public native boolean establishSocketConnection();

	public native void dropSocketConnection();

	public native boolean transmitMeasurementBySocket(String measurementId,
														String measurementTypeCodename,
														String localAddress,
														String[] parameterTypeCodenames,
														byte[][] parameterValues);
}
