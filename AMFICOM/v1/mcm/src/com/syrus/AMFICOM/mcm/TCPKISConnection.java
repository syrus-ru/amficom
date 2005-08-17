/*-
 * $Id: TCPKISConnection.java,v 1.15 2005/08/17 11:48:45 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.15 $, $Date: 2005/08/17 11:48:45 $
 * @module mcm
 */
final class TCPKISConnection implements KISConnection {
	
	private static final int KIS_TCP_SOCKET_DISCONNECTED = -1;

	private Identifier kisId;
	private String kisHostName;
	private short kisTCPPort;
	private int kisTCPSocket;
	
	private KISReport kisReport;

	static {
		System.loadLibrary("mcmtransceiver");
	}

	public TCPKISConnection(final KIS kis) {
		this.kisId = kis.getId();
		
		this.kisHostName = kis.getHostName();
		if (this.kisHostName == null) {
			this.kisHostName = ApplicationProperties.getString(MeasurementControlModule.KEY_KIS_HOST_NAME,
					MeasurementControlModule.KIS_HOST_NAME);
		}

		this.kisTCPPort = kis.getTCPPort();
		if (this.kisTCPPort <= 0) {
			this.kisTCPPort = (short) ApplicationProperties.getInt(MeasurementControlModule.KEY_KIS_TCP_PORT,
					MeasurementControlModule.KIS_TCP_PORT);
		}

		this.kisTCPSocket = KIS_TCP_SOCKET_DISCONNECTED;
	}

	public synchronized boolean isEstablished() {
		return (this.kisTCPSocket != KIS_TCP_SOCKET_DISCONNECTED);
	}

	public synchronized void establish(final long kisConnectionTimeout) throws CommunicationException {
		this.establish(kisConnectionTimeout, true);
	}

	public synchronized void establish(final long kisConnectionTimeout, final boolean dropIfAlreadyEstablished)
			throws CommunicationException {
		Log.debugMessage("TCPKISConnection.establish | Connecting to KIS '" + this.kisId
				+ "' on host '" + this.kisHostName + "', port " + this.kisTCPPort, Log.DEBUGLEVEL07);
		if (this.isEstablished()) {
			if (dropIfAlreadyEstablished) {
				this.drop();
			}
			else {
				Log.errorMessage("TCPKISConnection.establish | Connection with KIS '" + this.kisId
						+ "' already established -- nothing to do!");
				return;
			}
		}

		final long deadtime = System.currentTimeMillis() + kisConnectionTimeout;
		while (System.currentTimeMillis() < deadtime && ! this.isEstablished()) {
			this.kisTCPSocket = this.establishSocketConnection();
			if (! this.isEstablished()) {
				Log.debugMessage("TCPKISConnection.establish | Cannot connect to KIS '" + this.kisId
						+ "' on host '" + this.kisHostName + "', port " + this.kisTCPPort, Log.DEBUGLEVEL07);
				final Object obj = new Object();
				try {
					synchronized (obj) {
						obj.wait(5*1000);
					}
				}
				catch (InterruptedException ex) {
					Log.errorException(ex);
				}
			}
		}	//while

		if (this.isEstablished()) {
			Log.debugMessage("TCPKISConnection.establish | Connected to KIS '" + this.kisId + "'", Log.DEBUGLEVEL07);
		}
		else {
			throw new CommunicationException("Cannot connect to KIS '" + this.kisId
					+ "' on host '" + this.kisHostName + "', port " + this.kisTCPPort);
		}
	}

	public synchronized void drop() {
		if (this.kisTCPSocket != KIS_TCP_SOCKET_DISCONNECTED) {
			Log.debugMessage("TCPKISConnection.drop | Closing socket: " + this.kisTCPSocket, Log.DEBUGLEVEL09);
			this.dropSocketConnection();
			this.kisTCPSocket = KIS_TCP_SOCKET_DISCONNECTED;
		}
	}

	public synchronized void transmitMeasurement(final Measurement measurement, final long timewait) throws CommunicationException {
		final Identifier measurementId  = measurement.getId();
		Log.debugMessage("TCPKISConnection.transmitMeasurement | Transmitting measurement '" + measurementId
				+ "' to KIS '" + this.kisId + "'", Log.DEBUGLEVEL07);
		if (this.transmitMeasurementBySocket(measurementId.toString(),
				measurement.getType().getCodename(),
				measurement.getLocalAddress(),
				measurement.getSetup().getParameterTypeCodenames(),
				measurement.getSetup().getParameterValues(),
				timewait))
			Log.debugMessage("TCPKISConnection.transmitMeasurement | Transmitted measurement '" + measurementId
					+ "' to KIS '" + this.kisId + "'", Log.DEBUGLEVEL07);
		else {
			throw new CommunicationException("TCPKISConnection.transmitMeasurement | Cannot transmit measurement '"
					+ measurementId + "' to KIS '" + this.kisId + "'");
		}
	}

	public synchronized KISReport receiveKISReport(final long timewait) throws CommunicationException {
		this.kisReport = null;
		if (this.receiveKISReportFromSocket(timewait)) {
			if (this.kisReport != null) {
				Log.debugMessage("TCPKISConnection.receiveKISReport | Received report for measurement '"
						+ this.kisReport.getMeasurementId() + "'", Log.DEBUGLEVEL07);
			}
			return this.kisReport;
		}
		throw new CommunicationException("TCPKISConnection.receiveKISReport | Cannot receive report");
	}

	public Identifier getKISId() {
		return this.kisId;
	}

	/**
	 * Establishes connection with remote KIS.
	 * NOTE: this method is responsible for correct work of method isEstablished() after its invocation;
	 * in this TCP implementation it returns KIS_TCP_SOCKET_DISCONNECTED on error.
	 * @see #isEstablished
	 * @return socket file descriptor.
	 */
	public native int establishSocketConnection();

	/**
	 * Drop connection. ParameterSet field kisTCPSocket to KIS_TCP_SOCKET_DISCONNECTED.
	 */
	public native void dropSocketConnection();

	/**
	 * Transmits data to socket.
	 * @param measurementId
	 * @param measurementTypeCodename
	 * @param localAddress
	 * @param parameterTypeCodenames
	 * @param parameterValues
	 * @return true on success, false on failure.
	 */
	public native boolean transmitMeasurementBySocket(String measurementId,
														String measurementTypeCodename,
														String localAddress,
														String[] parameterTypeCodenames,
														byte[][] parameterValues,
														long timewait);

	/**
	 * Receive report from socket. On success save the report into field kisReport
	 * NOTE: if nothing received, this method also returns true, but saves null into field kisReport
	 * @param timewait
	 * @return true on success, false on failure.
	 */
	public native boolean receiveKISReportFromSocket(long timewait);
}
