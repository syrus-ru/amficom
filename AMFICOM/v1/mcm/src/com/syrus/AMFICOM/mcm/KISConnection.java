/*-
 * $Id: KISConnection.java,v 1.9.2.2 2006/03/24 11:19:03 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
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
 * @version $Revision: 1.9.2.2 $, $Date: 2006/03/24 11:19:03 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
public interface KISConnection extends Retainable {

	/**
	 * Проверить, установлено ли данное соединение.
	 * 
	 * @return <code>true</code>, если соединение установлено,
	 *         <code>false</code>, если нет.
	 */
	boolean isEstablished();

	/**
	 * Установить соединение с КИС. Если соединение уже установлено - сбросить
	 * его и установить заново.
	 * 
	 * @param kisConnectionTimeout
	 *        Время, отведённое на попытки установить соединение.
	 * @throws CommunicationException
	 */
	void establish(final long kisConnectionTimeout) throws CommunicationException;

	/**
	 * Установить соединение с КИС.
	 * 
	 * @param kisConnectionTimeout
	 *        Время, отведённое на попытки установить соединение.
	 * @param dropIfAlreadyEstablished
	 *        Если соединение уже установлено - сбрасывать его и создавать
	 *        заново, или же использовать существующее?
	 * @throws CommunicationException
	 */
	void establish(final long kisConnectionTimeout, final boolean dropIfAlreadyEstablished) throws CommunicationException;

	/**
	 * Сбросить соединение.
	 */
	void drop();

	/**
	 * Передать данные на КИС через установленное соединение.
	 * 
	 * @param measurement
	 *        Идентификатор измерения.
	 * @param timewait
	 *        Время ожидания при записи.
	 * @throws ApplicationException
	 */
	void transmitMeasurement(final Measurement measurement, final long timewait) throws ApplicationException;

	/**
	 * Получить данные с КИС.
	 * 
	 * @param timewait
	 *        Время ожидания данных.
	 * @return Данные, полученные с КИС. Если никаких данных не получено - то
	 *         <code>null</code>.
	 * @throws CommunicationException
	 */
	KISReport receiveKISReport(final long timewait) throws CommunicationException;

	/**
	 * Получить идентификатор КИС, с которым должно устанавливаться данное
	 * соединение.
	 */
	Identifier getKISId();
}
