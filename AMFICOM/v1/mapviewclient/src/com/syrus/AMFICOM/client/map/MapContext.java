/**
 * $Id: MapContext.java,v 1.5 2005/06/20 10:09:13 peskovsky Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.client.map;

import java.util.List;

import com.syrus.AMFICOM.map.DoublePoint;

public interface MapContext {
	public static final double ZOOM_FACTOR = 2D;

	public abstract MapConnection getMapConnection()
		throws MapConnectionException;

	/**
	 * ���������� ����������� ����� ���� �����.
	 * @param center �������������� ���������� ������
	 */
	public abstract void setCenter(DoublePoint center)
			throws MapConnectionException, MapDataException;

	/**
	 * �������� ����������� ����� ���� �����.
	 * @return �������������� ���������� ������
	 */
	public abstract DoublePoint getCenter()
			throws MapConnectionException, MapDataException;

	/**
	 * �������� ������� ������� ���� �����.
	 * @return �������
	 */
	public abstract double getScale()
			throws MapConnectionException, MapDataException;

	/**
	 * ���������� �������� ������� ���� �����.
	 * @param scale �������
	 */
	public abstract void setScale(double scale)
			throws MapConnectionException, MapDataException;

	/**
	 * �������� ������� ���� ����� � �������� ����� ���.
	 * @param scaleCoef ����������� ���������������
	 */
	public abstract void scaleTo(double scaleCoef)
			throws MapConnectionException, MapDataException;

	/**
	 * ���������� ��� ����� � ����������� ����� ���.
	 */
	public abstract void zoomIn()
			throws MapConnectionException, MapDataException;

	/**
	 * �������� ��� ����� � ����������� ����� ���.
	 */
	public abstract void zoomOut()
			throws MapConnectionException, MapDataException;

	/**
	 * ���������� ��� ����������� ������� ����� (� ����������� �����)
	 * �� ����������� ������� �����.
	 * @param from �������������� ����������
	 * @param to �������������� ����������
	 */
	public abstract void zoomToBox(DoublePoint from, DoublePoint to)
			throws MapConnectionException, MapDataException;

}
