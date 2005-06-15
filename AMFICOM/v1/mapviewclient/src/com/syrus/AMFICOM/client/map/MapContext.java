/**
 * $Id: MapContext.java,v 1.1 2005/06/15 07:42:28 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.client.map;

import java.util.List;

import com.syrus.AMFICOM.map.DoublePoint;

public interface MapContext {

	/**
	 * ���������� ����������� ����� ���� �����.
	 * @param center �������������� ���������� ������
	 * @deprecated use netMapViewer
	 */
	public abstract void setCenter(DoublePoint center)
			throws MapConnectionException, MapDataException;

	/**
	 * �������� ����������� ����� ���� �����.
	 * @return �������������� ���������� ������
	 * @deprecated use netMapViewer
	 */
	public abstract DoublePoint getCenter()
			throws MapConnectionException, MapDataException;

	/**
	 * �������� ������� ������� ���� �����.
	 * @return �������
	 * @deprecated use netMapViewer
	 */
	public abstract double getScale()
			throws MapConnectionException, MapDataException;

	/**
	 * ���������� �������� ������� ���� �����.
	 * @param scale �������
	 * @deprecated use netMapViewer
	 */
	public abstract void setScale(double scale)
			throws MapConnectionException, MapDataException;

	/**
	 * ���������� ������� ���� ����� � �������� �������������.
	 * @param scale�oef ����������� ���������������
	 * @deprecated use netMapViewer
	 */
	public abstract void scaleTo(double scaleCoef)
			throws MapConnectionException, MapDataException;

	/**
	 * ���������� ��� ����� �� ����������� �������������.
	 * @deprecated use netMapViewer
	 */
	public abstract void zoomIn()
			throws MapConnectionException, MapDataException;

	/**
	 * �������� ��� ����� �� ����������� �������������.
	 * @deprecated use netMapViewer
	 */
	public abstract void zoomOut()
			throws MapConnectionException, MapDataException;

	/**
	 * ���������� ��� ����������� ������� ����� (� ����������� �����)
	 * �� ����������� ������� �����.
	 * @param from �������������� ����������
	 * @param to �������������� ����������
	 * @deprecated use netMapViewer
	 */
	public abstract void zoomToBox(DoublePoint from, DoublePoint to)
			throws MapConnectionException, MapDataException;


	/**
	 * �������� ������ �������������� �����.
	 * @return ������ ����� &lt;{@link SpatialLayer}&gt;
	 */
	public abstract List getLayers()
		throws MapDataException;
	
}
