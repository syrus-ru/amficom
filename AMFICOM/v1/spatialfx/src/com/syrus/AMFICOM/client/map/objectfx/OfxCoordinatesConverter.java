/**
 * $Id: OfxCoordinatesConverter.java,v 1.3 2005/08/12 12:29:18 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.objectfx;

import java.awt.Point;

import com.ofx.geometry.SxDoublePoint;
import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.general.DoublePoint;

/**
 * ���������� ������ ����������� ����������� ���� �� ����� ����������
 * ������ SpatialFX. ���� ��������������� ����� ������������ � �������
 * ������� ���� SxMapViewer
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2005/08/12 12:29:18 $
 * @author $Author: krupenn $
 * @module spatialfx_v1
 */
public class OfxCoordinatesConverter implements MapCoordinatesConverter {
	OfxConnection mapConnection;

	public OfxCoordinatesConverter(OfxConnection mapConnection) {
		this.mapConnection = mapConnection;
	}

	/**
	 * �������� �������� ���������� �� �������������� �����������
	 */
	public Point convertMapToScreen(DoublePoint point) {
		SxDoublePoint sdp = new SxDoublePoint(point.getX(), point.getY());
		return this.mapConnection.getSpatialLayer().convertLongLatToScreen(sdp);
	}

	/**
	 * �������� �������������� ���������� �� ��������
	 */
	public DoublePoint convertScreenToMap(Point point) {
		SxDoublePoint sdp = this.mapConnection.getSpatialLayer()
				.convertScreenToLongLat(point);
		return new DoublePoint(sdp.getX(), sdp.getY());
	}

	public DoublePoint pointAtDistance(
			DoublePoint startPoint,
			DoublePoint endPoint,
			double dist) {
		double x = startPoint.getX();
		double y = startPoint.getY();
		double len = distance(startPoint, endPoint);
		x += (endPoint.getX() - startPoint.getX()) / len * dist;
		y += (endPoint.getY() - startPoint.getY()) / len * dist;
		return new DoublePoint(x, y);
	}

	/**
	 * �������� ��������� ����� ����� ������� � �������������� �����������
	 * �������� ���������� ��������� �������� ��������, ��� ��� �� ���
	 * ��������������� ��� �������������
	 */
	public double distance(DoublePoint from, DoublePoint to) {
		return this.mapConnection.getSxMapViewer().distance(
				from.getX(),
				from.getY(),
				to.getX(),
				to.getY());
	}

	public MapConnection getMapConnection() throws MapConnectionException {
		return this.mapConnection;
	}

}

