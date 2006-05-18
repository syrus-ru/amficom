/*-
 * $$Id: MapCoordinatesConverter.java,v 1.13 2005/09/30 16:08:36 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map;

import java.awt.Point;

import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * ��������� �������������� � �������� ��������� ��� ����������� ��������� �����
 * 
 * @version $Revision: 1.13 $, $Date: 2005/09/30 16:08:36 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public interface MapCoordinatesConverter {
	public abstract MapConnection getMapConnection()
			throws MapConnectionException;

	/**
	 * ��������� �������������� ���������� � ��������.
	 * 
	 * @param point �������������� ����������
	 * @return �������� ����������
	 */
	Point convertMapToScreen(DoublePoint point)
			throws MapConnectionException, MapDataException;

	/**
	 * ��������� �������� ���������� � ��������������.
	 * 
	 * @param point �������� ����������
	 * @return �������������� ����������
	 */
	DoublePoint convertScreenToMap(Point point)
			throws MapConnectionException, MapDataException;

	/**
	 * �������� ����� �� ����� (startPoint, endPoint), ��������� �� �����
	 * startPoint �� ��������� dist.
	 * 
	 * @param startPoint �������������� ���������� ��������� �����
	 * @param endPoint �������������� ���������� �������� �����
	 * @param dist �������������� ���������
	 * @return �������������� ���������� ���������� �����
	 */
	DoublePoint pointAtDistance(
			DoublePoint startPoint,
			DoublePoint endPoint,
			double dist) throws MapConnectionException, MapDataException;

	/**
	 * ����� ���������� ����� ����� ������� � �������������� ������� ���������.
	 * 
	 * @param from �������������� ���������� ��������� �����
	 * @param to �������������� ���������� �������� �����
	 * @return ���������� ����� �������
	 */
	double distance(DoublePoint from, DoublePoint to)
			throws MapConnectionException, MapDataException;

}
