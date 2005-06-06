/**
 * $Id: MapCoordinatesConverter.java,v 1.7 2005/06/06 12:20:29 krupenn Exp $
 * Syrus Systems ������-����������� ����� ������: ������� ������������������
 * ������������������� ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.map;

import com.syrus.AMFICOM.map.DoublePoint;

import java.awt.Point;

/**
 * ��������� �������������� � �������� ��������� ��� ����������� ��������� �����
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2005/06/06 12:20:29 $
 * @module maviewclient_v1
 */
public interface MapCoordinatesConverter
{
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
	 * ��������� �������� ��������� � ��������������.
	 * 
	 * @param screenDistance �������� ���������
	 * @return �������������� ���������
	 */
	double convertScreenToMap(double screenDistance)
		throws MapConnectionException, MapDataException;

	/**
	 * ��������� �������������� ��������� � ��������.
	 * 
	 * @param topologicalDistance �������������� ���������
	 * @return �������� ���������
	 */
	double convertMapToScreen(double topologicalDistance)
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
			double dist) 
		throws MapConnectionException, MapDataException;

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
