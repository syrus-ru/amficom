/**
 * $Id: MapCoordinatesConverter.java,v 1.5 2004/12/07 17:02:02 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map;

import com.syrus.AMFICOM.Client.Resource.Map.DoublePoint;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * ��������� �������������� � �������� ��������� ��� ����������� ��������� ����� 
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/12/07 17:02:02 $
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
	 * @deprecated
	 */
	Point convertMapToScreen(Point2D.Double point);

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
	 * @deprecated
	 */
	Point2D.Double convertScreenToMap1(Point point);

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
	 * @deprecated
	 */
	Point2D.Double pointAtDistance(
			Point2D.Double startPoint, 
			Point2D.Double endPoint, 
			double dist);

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
	 * @deprecated
	 */
	double distance(Point2D.Double from, Point2D.Double to);

	/**
	 * ����� ���������� ����� ����� ������� � �������������� ������� ���������
	 * @param from
	 * @param to
	 * @return 
	 */
	double distance(DoublePoint from, DoublePoint to);
}
