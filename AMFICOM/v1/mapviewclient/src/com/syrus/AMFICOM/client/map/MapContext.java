/*-
 * $$Id: MapContext.java,v 1.9 2005/09/30 16:08:36 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map;

import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * @version $Revision: 1.9 $, $Date: 2005/09/30 16:08:36 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
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
