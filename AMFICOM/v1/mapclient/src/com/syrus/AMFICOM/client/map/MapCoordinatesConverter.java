/**
 * $Id: MapCoordinatesConverter.java,v 1.6 2004/12/08 16:20:01 krupenn Exp $
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
 * @version $Revision: 1.6 $, $Date: 2004/12/08 16:20:01 $
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
