/**
 * $Id: MapCoordinatesConverter.java,v 1.4 2004/11/11 18:08:57 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * ��������� �������������� � �������� ��������� ��� ����������� ��������� ����� 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/11/11 18:08:57 $
 * @module
 * @author $Author: krupenn $
 * @see
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
	Point convertMapToScreen(Point2D.Double point);
	
	/**
	 * ��������� �������� ���������� � ��������������
	 * @param point
	 * @return 
	 */
	Point2D.Double convertScreenToMap(Point point);

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
	Point2D.Double pointAtDistance(
			Point2D.Double startPoint, 
			Point2D.Double endPoint, 
			double dist);

	/**
	 * ����� ���������� ����� ����� ������� � �������������� ������� ���������
	 * @param from
	 * @param to
	 * @return 
	 */
	double distance(Point2D.Double from, Point2D.Double to);
}
