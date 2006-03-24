/*-
 * $Id: KISConnection.java,v 1.9.2.2 2006/03/24 11:19:03 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
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
	 * ���������, ����������� �� ������ ����������.
	 * 
	 * @return <code>true</code>, ���� ���������� �����������,
	 *         <code>false</code>, ���� ���.
	 */
	boolean isEstablished();

	/**
	 * ���������� ���������� � ���. ���� ���������� ��� ����������� - ��������
	 * ��� � ���������� ������.
	 * 
	 * @param kisConnectionTimeout
	 *        �����, ����ģ���� �� ������� ���������� ����������.
	 * @throws CommunicationException
	 */
	void establish(final long kisConnectionTimeout) throws CommunicationException;

	/**
	 * ���������� ���������� � ���.
	 * 
	 * @param kisConnectionTimeout
	 *        �����, ����ģ���� �� ������� ���������� ����������.
	 * @param dropIfAlreadyEstablished
	 *        ���� ���������� ��� ����������� - ���������� ��� � ���������
	 *        ������, ��� �� ������������ ������������?
	 * @throws CommunicationException
	 */
	void establish(final long kisConnectionTimeout, final boolean dropIfAlreadyEstablished) throws CommunicationException;

	/**
	 * �������� ����������.
	 */
	void drop();

	/**
	 * �������� ������ �� ��� ����� ������������� ����������.
	 * 
	 * @param measurement
	 *        ������������� ���������.
	 * @param timewait
	 *        ����� �������� ��� ������.
	 * @throws ApplicationException
	 */
	void transmitMeasurement(final Measurement measurement, final long timewait) throws ApplicationException;

	/**
	 * �������� ������ � ���.
	 * 
	 * @param timewait
	 *        ����� �������� ������.
	 * @return ������, ���������� � ���. ���� ������� ������ �� �������� - ��
	 *         <code>null</code>.
	 * @throws CommunicationException
	 */
	KISReport receiveKISReport(final long timewait) throws CommunicationException;

	/**
	 * �������� ������������� ���, � ������� ������ ��������������� ������
	 * ����������.
	 */
	Identifier getKISId();
}
