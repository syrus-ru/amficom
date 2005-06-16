/**
 * $Id: OfxContext.java,v 1.1 2005/06/16 14:44:28 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
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
 * Реализация уровня логического отображения сети на карте средствами
 * пакета SpatialFX. Слой топографической схемы отображается с помощью
 * объекта типа SxMapViewer
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
	 * Установить центральную точку вида карты
	 */
	public void setCenter(DoublePoint center)
	{
		this.mapConnection.getSxMapViewer().setCenter(center.getX(), center.getY());
	}

	/**
	 * Получить центральную точку вида карты
	 */
	public DoublePoint getCenter()
	{
		DoublePoint center = new DoublePoint(
			this.mapConnection.getSxMapViewer().getCenter()[0],
			this.mapConnection.getSxMapViewer().getCenter()[1]);
		return center;
	}

	/**
	 * Получить текущий масштаб вида карты
	 */
	public double getScale()
	{
		return this.mapConnection.getSxMapViewer().getScale();
	}

	/**
	 * Установить заданный масштаб вида карты
	 */
	public void setScale(double scale)
	{
		this.mapConnection.getSxMapViewer().setScale(scale);
	}

	/**
	 * Установить масштаб вида карты с заданным коэффициентом
	 */
	public void scaleTo(double scaleСoef)
	{
		this.mapConnection.getSxMapViewer().setScale(this.mapConnection.getSxMapViewer().getScale() * scaleСoef);
	}

	/**
	 * Приблизить вид карты со стандартным коэффициентом
	 */
	public void zoomIn()
	{
		this.mapConnection.getSxMapViewer().zoomIn();
	}

	/**
	 * Отдалить вид карты со стандартным коэффициентом
	 */
	public void zoomOut()
	{
		this.mapConnection.getSxMapViewer().zoomOut();
	}
	
	/**
	 * Приблизить вид выделенного участка карты (в координатах карты)
	 * по координатам угловых точек
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

