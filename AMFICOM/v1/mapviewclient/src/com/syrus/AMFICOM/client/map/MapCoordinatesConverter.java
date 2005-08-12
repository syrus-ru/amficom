/**
 * $Id: MapCoordinatesConverter.java,v 1.11 2005/08/12 12:17:58 krupenn Exp $
 * Syrus Systems ������-����������� ����� ������: ������� ������������������
 * ������������������� ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.map;

import java.awt.Point;

import com.syrus.AMFICOM.general.DoublePoint;

/**
 * ��������� �������������� � �������� ��������� ��� ����������� ��������� �����
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.11 $, $Date: 2005/08/12 12:17:58 $
 * @module maviewclient_v1
 */
public interface MapCoordinatesConverter
{
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

	/**
	 * ��������� �������� ��������� � ��������������.
	 * 
	 * @param screenDistance �������� ���������
	 * @return �������������� ���������
	 */
//	double convertScreenToMap(double screenDistance)
//		throws MapConnectionException, MapDataException;

	/**
	 * ��������� �������������� ��������� � ��������.
	 * 
	 * @param topologicalDistance �������������� ���������
	 * @return �������� ���������
	 */
//	double convertMapToScreen(double topologicalDistance)
//		throws MapConnectionException, MapDataException;

}
