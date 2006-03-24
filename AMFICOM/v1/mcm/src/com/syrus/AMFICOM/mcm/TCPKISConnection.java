/*-
 * $Id: TCPKISConnection.java,v 1.27.2.4 2006/03/24 11:19:03 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
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
	 * Особое значение для {@link #kisTCPSocket}, соответствующее разорванному
	 * соединению с КИС.
	 * 
	 * @see #isEstablished()
	 */
	private static final int KIS_TCP_SOCKET_DISCONNECTED = -1;

	/**
	 * Идентификатор КИС, с которым должно устанавливаться данное соединение.
	 */
	private Identifier kisId;

	/**
	 * Имя машины КИС.
	 */
	private String kisHostName;

	/**
	 * Номер TCP-порта КИС.
	 */
	private short kisTCPPort;

	/**
	 * Описатель соединения с КИС. Если соединение не установлено, должен быть
	 * равен {@link #KIS_TCP_SOCKET_DISCONNECTED}.
	 */
	private int kisTCPSocket;

	/**
	 * Данные, полученные с КИС после завершения измерения. Это поле выставляет
	 * метод {@link #receiveKISReportFromSocket(long)}.
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
	 * Реализация {@link com.syrus.util.LRUMap.Retainable#retain()}. Нужна для
	 * хранения объектов соединения в {@link com.syrus.util.LRUMap}.
	 * 
	 * @see com.syrus.util.LRUMap.Retainable#retain()
	 * @return <code>true</code>, если соединение установлено,
	 *         <code>false</code> - в противном случае.
	 */
	public boolean retain() {
		return this.isEstablished();
	}

	/**
	 * Установить соединение с КИС. Этот метод возвращает файловый описатель
	 * соединения и, следовательно, отвечает за правильную работу метода
	 * {@link #isEstablished()}. В случае невозмжности установить соединение,
	 * он должен вернуть {@link #KIS_TCP_SOCKET_DISCONNECTED}.
	 * 
	 * @return Файловый описатель соединения при успешном выполнении, либо
	 *         {@link #KIS_TCP_SOCKET_DISCONNECTED} при ошибке.
	 */
	public native int establishSocketConnection();

	/**
	 * Сбросить соединение. Этот метод должен выставить поле
	 * {@link #kisTCPSocket} в {@link #KIS_TCP_SOCKET_DISCONNECTED},
	 * обеспечивая тем самым правильную работу метода {@link #isEstablished()}.
	 */
	public native void dropSocketConnection();

	/**
	 * Передать данные на КИС через установленное соединение.
	 * 
	 * @param measurementId
	 *        Идентификатор измерения.
	 * @param measurementTypeCodename
	 *        Кодовое имя типа измерения.
	 * @param localAddress
	 *        Строка локальной адресации КИС.
	 * @param parameterTypeCodenames
	 *        Кодовые имена типов параметров измерения.
	 * @param parameterValues
	 *        Величины параметров измерения.
	 * @param timewait
	 *        Время ожидания при записи.
	 * @return
	 */
	public native boolean transmitMeasurementBySocket(String measurementId,
			String measurementTypeCodename,
			String localAddress,
			String[] parameterTypeCodenames,
			byte[][] parameterValues,
			long timewait);

	/**
	 * Получить данные с КИС. В случае успеха выставляет поле {@link #kisReport}
	 * в значение, полученное с КИС. Если никаких данных не получено, выставляет
	 * {@link #kisReport} в <code>null</code>.
	 * 
	 * @param timewait
	 *        Время ожидания данных.
	 * @return В случае успешного приёма - <code>true</code>, при ошибке -
	 *         <code>false</code>.
	 */
	public native boolean receiveKISReportFromSocket(long timewait);
}
