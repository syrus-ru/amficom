/**
 * $Id: OfxContext.java,v 1.3 2005/08/12 12:29:18 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.objectfx;

import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapContext;
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
public class OfxContext implements MapContext
{
	private final OfxConnection mapConnection;

	public OfxContext(OfxConnection mapConnection)
	{
		this.mapConnection = mapConnection;
	}

	/**
	 * ���������� ����������� ����� ���� �����
	 */
	public void setCenter(DoublePoint center)
	{
		this.mapConnection.getSxMapViewer().setCenter(center.getX(), center.getY());
	}

	/**
	 * �������� ����������� ����� ���� �����
	 */
	public DoublePoint getCenter()
	{
		DoublePoint center = new DoublePoint(
			this.mapConnection.getSxMapViewer().getCenter()[0],
			this.mapConnection.getSxMapViewer().getCenter()[1]);
		return center;
	}

	/**
	 * �������� ������� ������� ���� �����
	 */
	public double getScale()
	{
		return this.mapConnection.getSxMapViewer().getScale();
	}

	/**
	 * ���������� �������� ������� ���� �����
	 */
	public void setScale(double scale)
	{
		this.mapConnection.getSxMapViewer().setScale(scale);
	}

	/**
	 * ���������� ������� ���� ����� � �������� �������������
	 */
	public void scaleTo(double scale�oef)
	{
		this.mapConnection.getSxMapViewer().setScale(this.mapConnection.getSxMapViewer().getScale() * scale�oef);
	}

	/**
	 * ���������� ��� ����� �� ����������� �������������
	 */
	public void zoomIn()
	{
		this.mapConnection.getSxMapViewer().zoomIn();
	}

	/**
	 * �������� ��� ����� �� ����������� �������������
	 */
	public void zoomOut()
	{
		this.mapConnection.getSxMapViewer().zoomOut();
	}
	
	/**
	 * ���������� ��� ����������� ������� ����� (� ����������� �����)
	 * �� ����������� ������� �����
	 */
	public void zoomToBox(DoublePoint from, DoublePoint to)
	{
		this.mapConnection.getSxMapViewer().zoomToRect(from.getX(), from.getY(), to.getX(), to.getY());
	}

	public MapConnection getMapConnection() throws MapConnectionException {
		return this.mapConnection;
	}

}

