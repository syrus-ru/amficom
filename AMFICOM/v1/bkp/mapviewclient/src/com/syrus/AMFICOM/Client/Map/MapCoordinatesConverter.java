/**
 * $Id: MapCoordinatesConverter.java,v 1.6 2005/04/28 13:17:45 krupenn Exp $
 * Syrus Systems ������-����������� ����� ������: ������� ������������������
 * ������������������� ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.Client.Map;

import com.syrus.AMFICOM.map.DoublePoint;

import java.awt.Point;

/**
 * ��������� �������������� � �������� ��������� ��� ����������� ��������� �����
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.6 $, $Date: 2005/04/28 13:17:45 $
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
