/**
 * $Id: OfxContext.java,v 1.1 2005/06/16 14:44:28 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.objectfx;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.ofx.geometry.SxRectangle;
import com.ofx.mapViewer.SxMapViewer;
import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapContext;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.SpatialLayer;
import com.syrus.AMFICOM.map.DoublePoint;

/**
 * ���������� ������ ����������� ����������� ���� �� ����� ����������
 * ������ SpatialFX. ���� ��������������� ����� ������������ � �������
 * ������� ���� SxMapViewer
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2005/06/16 14:44:28 $
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

	public List getLayers() throws MapDataException {
		List returnList = new LinkedList();

		int sortOrder = 301;// as used in Ofx.JMapLegend
		
		SxMapViewer sxMapViewer = this.mapConnection.getJMapViewer().getSxMapViewer();
		
		Vector foregroundClasses = sxMapViewer.getForegroundClasses(sortOrder);
		for(Iterator it = foregroundClasses.iterator(); it.hasNext();)
		{
			String s = (String )it.next();
			SpatialLayer sl = new OfxSpatialLayer(sxMapViewer, s);
			returnList.add(sl);
			Vector vector2 = sxMapViewer.classBinNames(s);
			for(Iterator it2 = vector2.iterator(); it2.hasNext();)
			{
				String s2 = (String )it2.next();
				SpatialLayer sl2 = new OfxSpatialLayer(sxMapViewer, s2);
				returnList.add(sl2);
			}
		}

		{
		Vector backgroundClasses = sxMapViewer.getBackgroundClasses();
		for(Iterator it = backgroundClasses.iterator(); it.hasNext();)
		{
			String s = (String )it.next();
			SpatialLayer sl = new OfxSpatialLayer(sxMapViewer, s);
			returnList.add(sl);
			Vector vector2 = sxMapViewer.classBinNames(s);
			for(Iterator it2 = vector2.iterator(); it2.hasNext();)
			{
				String s2 = (String )it2.next();
				SpatialLayer sl2 = new OfxSpatialLayer(sxMapViewer, s2);
				returnList.add(sl2);
			}
		}
		}

		return returnList;
	}

	public MapConnection getMapConnection() throws MapConnectionException {
		return this.mapConnection;
	}

}

