/*-
 * $Id: TCPKISConnection.java,v 1.27.2.4 2006/03/24 11:19:03 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mcm;

import static com.syrus.AMFICOM.mcm.MeasurementControlModule.KEY_KIS_HOST_NAME;
import static com.syrus.AMFICOM.mcm.MeasurementControlModule.KEY_KIS_TCP_PORT;
import static com.syrus.AMFICOM.mcm.MeasurementControlModule.KIS_HOST_NAME;
import static com.syrus.AMFICOM.mcm.MeasurementControlModule.KIS_TCP_PORT;

import java.util.Map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.27.2.4 $, $Date: 2006/03/24 11:19:03 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
final class TCPKISConnection implements KISConnection {

	/**
	 * ������ �������� ��� {@link #kisTCPSocket}, ��������������� ������������
	 * ���������� � ���.
	 * 
	 * @see #isEstablished()
	 */
	private static final int KIS_TCP_SOCKET_DISCONNECTED = -1;

	/**
	 * ������������� ���, � ������� ������ ��������������� ������ ����������.
	 */
	private Identifier kisId;

	/**
	 * ��� ������ ���.
	 */
	private String kisHostName;

	/**
	 * ����� TCP-����� ���.
	 */
	private short kisTCPPort;

	/**
	 * ��������� ���������� � ���. ���� ���������� �� �����������, ������ ����
	 * ����� {@link #KIS_TCP_SOCKET_DISCONNECTED}.
	 */
	private int kisTCPSocket;

	/**
	 * ������, ���������� � ��� ����� ���������� ���������. ��� ���� ����������
	 * ����� {@link #receiveKISReportFromSocket(long)}.
	 */
	private KISReport kisReport;

	static {
		System.loadLibrary("mcmtransceiver");
	}

	public TCPKISConnection(final KIS kis) {
		this.kisId = kis.getId();

		this.kisHostName = kis.getHostName();
		if (this.kisHostName == null) {
			this.kisHostName = ApplicationProperties.getString(KEY_KIS_HOST_NAME, KIS_HOST_NAME);
		}

		this.kisTCPPort = kis.getTCPPort();
		if (this.kisTCPPort <= 0) {
			this.kisTCPPort = (short) ApplicationProperties.getInt(KEY_KIS_TCP_PORT, KIS_TCP_PORT);
		}

		this.kisTCPSocket = KIS_TCP_SOCKET_DISCONNECTED;
	}

	/**
	 * @inheritDoc
	 */
	public boolean isEstablished() {
		return (this.kisTCPSocket != KIS_TCP_SOCKET_DISCONNECTED);
	}

	/**
	 * @inheritDoc
	 */
	public synchronized void establish(final long kisConnectionTimeout) throws CommunicationException {
		this.establish(kisConnectionTimeout, true);
	}

	/**
	 * @inheritDoc
	 */
	public synchronized void establish(final long kisConnectionTimeout, final boolean dropIfAlreadyEstablished)
			throws CommunicationException {
		Log.debugMessage("Connecting to KIS '" + this.kisId + "' on host '" + this.kisHostName + "', port " + this.kisTCPPort,
				Log.DEBUGLEVEL07);
		if (this.isEstablished()) {
			if (dropIfAlreadyEstablished) {
				this.drop();
			} else {
				Log.errorMessage("Connection with KIS '" + this.kisId + "' already established -- nothing to do!");
				return;
			}
		}

		final long deadtime = System.currentTimeMillis() + kisConnectionTimeout;
		while (System.currentTimeMillis() < deadtime && ! this.isEstablished()) {
			this.kisTCPSocket = this.establishSocketConnection();
			if (!this.isEstablished()) {
				Log.debugMessage("Cannot connect to KIS '" + this.kisId + "' on host '" + this.kisHostName + "', port " + this.kisTCPPort,
						Log.DEBUGLEVEL07);
				final Object obj = new Object();
				try {
					synchronized (obj) {
						obj.wait(5 * 1000);
					}
				} catch (InterruptedException ex) {
					Log.errorMessage(ex);
				}
			}
		}	//while

		if (this.isEstablished()) {
			Log.debugMessage("Connected to KIS '" + this.kisId + "'", Log.DEBUGLEVEL07);
		} else {
			throw new CommunicationException("Cannot connect to KIS '" + this.kisId
					+ "' on host '" + this.kisHostName
					+ "', port " + this.kisTCPPort);
		}
	}

	/**
	 * @inheritDoc
	 */
	public synchronized void drop() {
		if (this.kisTCPSocket != KIS_TCP_SOCKET_DISCONNECTED) {
			Log.debugMessage("Closing socket: " + this.kisTCPSocket, Log.DEBUGLEVEL09);
			this.dropSocketConnection();
			this.kisTCPSocket = KIS_TCP_SOCKET_DISCONNECTED;
		}
	}

	/**
	 * @inheritDoc
	 */
	public synchronized void transmitMeasurement(final Measurement measurement, final long timewait) throws ApplicationException {
		final Identifier measurementId  = measurement.getId();

		final Map<String, byte[]> codenameValueMap = measurement.getActionTemplate().getParameterTypeCodenameValueMap();
		final String[] codenames = new String[codenameValueMap.size()];
		final byte[][] values = new byte[codenameValueMap.size()][];
		int i = 0;
		for (final String codename : codenameValueMap.keySet()) {
			codenames[i] = codename;
			values[i] = codenameValueMap.get(codename);
			i++;
		}

		final String localAddress = measurement.getMonitoredElement().getLocalAddress();

		Log.debugMessage("Transmitting measurement '" + measurementId
				+ "' to KIS '" + this.kisId + "' on " + this.kisHostName + ":" + this.kisTCPPort, Log.DEBUGLEVEL07);
		if (this.transmitMeasurementBySocket(measurementId.toString(),
				measurement.getTypeCodename(),
				localAddress,
				codenames,
				values,
				timewait)) {
			Log.debugMessage("Transmitted measurement '" + measurementId + "' to KIS '" + this.kisId + "'", Log.DEBUGLEVEL07);
		} else {
			throw new CommunicationException("Cannot transmit measurement '" + measurementId + "' to KIS '" + this.kisId + "'");
		}
	}

	/**
	 * @inheritDoc
	 */
	public synchronized KISReport receiveKISReport(final long timewait) throws CommunicationException {
		this.kisReport = null;
		if (this.receiveKISReportFromSocket(timewait)) {
			if (this.kisReport != null) {
				Log.debugMessage("Received report for measurement '" + this.kisReport.getMeasurementId() + "'", Log.DEBUGLEVEL07);
			}
			return this.kisReport;
		}
		throw new CommunicationException("TCPKISConnection.receiveKISReport | Cannot receive report");
	}

	/**
	 * @inheritDoc
	 */
	public Identifier getKISId() {
		return this.kisId;
	}

	/**
	 * ���������� {@link com.syrus.util.LRUMap.Retainable#retain()}. ����� ���
	 * �������� �������� ���������� � {@link com.syrus.util.LRUMap}.
	 * 
	 * @see com.syrus.util.LRUMap.Retainable#retain()
	 * @return <code>true</code>, ���� ���������� �����������,
	 *         <code>false</code> - � ��������� ������.
	 */
	public boolean retain() {
		return this.isEstablished();
	}

	/**
	 * ���������� ���������� � ���. ���� ����� ���������� �������� ���������
	 * ���������� �, �������������, �������� �� ���������� ������ ������
	 * {@link #isEstablished()}. � ������ ������������ ���������� ����������,
	 * �� ������ ������� {@link #KIS_TCP_SOCKET_DISCONNECTED}.
	 * 
	 * @return �������� ��������� ���������� ��� �������� ����������, ����
	 *         {@link #KIS_TCP_SOCKET_DISCONNECTED} ��� ������.
	 */
	public native int establishSocketConnection();

	/**
	 * �������� ����������. ���� ����� ������ ��������� ����
	 * {@link #kisTCPSocket} � {@link #KIS_TCP_SOCKET_DISCONNECTED},
	 * ����������� ��� ����� ���������� ������ ������ {@link #isEstablished()}.
	 */
	public native void dropSocketConnection();

	/**
	 * �������� ������ �� ��� ����� ������������� ����������.
	 * 
	 * @param measurementId
	 *        ������������� ���������.
	 * @param measurementTypeCodename
	 *        ������� ��� ���� ���������.
	 * @param localAddress
	 *        ������ ��������� ��������� ���.
	 * @param parameterTypeCodenames
	 *        ������� ����� ����� ���������� ���������.
	 * @param parameterValues
	 *        �������� ���������� ���������.
	 * @param timewait
	 *        ����� �������� ��� ������.
	 * @return
	 */
	public native boolean transmitMeasurementBySocket(String measurementId,
			String measurementTypeCodename,
			String localAddress,
			String[] parameterTypeCodenames,
			byte[][] parameterValues,
			long timewait);

	/**
	 * �������� ������ � ���. � ������ ������ ���������� ���� {@link #kisReport}
	 * � ��������, ���������� � ���. ���� ������� ������ �� ��������, ����������
	 * {@link #kisReport} � <code>null</code>.
	 * 
	 * @param timewait
	 *        ����� �������� ������.
	 * @return � ������ ��������� ��ɣ�� - <code>true</code>, ��� ������ -
	 *         <code>false</code>.
	 */
	public native boolean receiveKISReportFromSocket(long timewait);
}
