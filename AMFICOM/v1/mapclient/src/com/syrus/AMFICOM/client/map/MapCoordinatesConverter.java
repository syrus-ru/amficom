/**
 * $Id: MapCoordinatesConverter.java,v 1.2 2004/10/15 14:09:00 krupenn Exp $
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
 * @version $Revision: 1.2 $, $Date: 2004/10/15 14:09:00 $
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
	 * ����� ���������� ����� ����� ������� � �������������� ������� ���������
	 * @param from
	 * @param to
	 * @return 
	 */
	double distance(Point2D.Double from, Point2D.Double to);
}
