/**
 * $Id: MapCoordinatesConverter.java,v 1.3 2005/02/08 15:11:08 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map;

import com.syrus.AMFICOM.map.DoublePoint;

import java.awt.Point;

/**
 * ��������� �������������� � �������� ��������� ��� ����������� ��������� ����� 
 * 
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/02/08 15:11:08 $
 * @module maviewclient_v1
 * @todo � ������� ������� ������������ ���������� ���������� � ���� 
 * LogicalNetLayer, ��������� � ������ ���������� ������ ������ �� 
 * ����������� MapCoordinatesConverter - ���������� ����� �������� ����
 */
public interface MapCoordinatesConverter 
{
	/**
	 * ��������� �������������� ���������� � ��������
	 * @param point
	 * @return 
	 */
	Point convertMapToScreen(DoublePoint point);
	
	/**
	 * ��������� �������� ���������� � ��������������
	 * @param point
	 * @return 
	 */
	DoublePoint convertScreenToMap(Point point);

	/**
	 * ��������� �������� ��������� � ��������������
	 * @param screenDistance
	 * @return 
	 */
	double convertScreenToMap(double screenDistance);
	
	/**
	 * ��������� �������������� ��������� � ��������
	 * @param topologicalDistance
	 * @return 
	 */
	double convertMapToScreen(double topologicalDistance);

	/**
	 * �������� ����� �� ����� (startPoint, endPoint), ��������� ��
	 * ����� startPoint �� ��������� dist
	 * @param startPoint
	 * @param endPoint
	 * @param dist
	 * @return 
	 */
	DoublePoint pointAtDistance(
			DoublePoint startPoint, 
			DoublePoint endPoint, 
			double dist);

	/**
	 * ����� ���������� ����� ����� ������� � �������������� ������� ���������
	 * @param from
	 * @param to
	 * @return 
	 */
	double distance(DoublePoint from, DoublePoint to);
}
