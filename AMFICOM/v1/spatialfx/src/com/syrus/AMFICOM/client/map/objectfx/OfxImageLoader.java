/**
 * $Id: OfxImageLoader.java,v 1.2 2005/06/20 15:38:43 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.objectfx;

import java.awt.Image;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.ofx.query.SxQueryResultInterface;
import com.ofx.repository.SxSpatialObject;
import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.map.TopologicalImageQuery;

/**
 * Реализация уровня логического отображения сети на карте средствами
 * пакета SpatialFX. Слой топографической схемы отображается с помощью
 * объекта типа SxMapViewer
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2005/06/20 15:38:43 $
 * @author $Author: krupenn $
 * @module spatialfx_v1
 */
public class OfxImageLoader implements MapImageLoader 
{
	
	private final OfxConnection mapConnection;

	public OfxImageLoader(OfxConnection mapConnection) {
		this.mapConnection = mapConnection;
	}

	public List findSpatialObjects(String searchText)
	{
		List found = new LinkedList();

		Vector vector;
		SxQueryResultInterface objects;
		String spatialClassName;
		
		String searchTextLowCase = searchText.toLowerCase();
		String sampleLowCase;
		
		try
		{
			vector = this.mapConnection.getSxMapViewer().getForegroundClasses();
			for(Iterator it = vector.iterator(); it.hasNext();)
			{
				spatialClassName = (String )it.next();

				objects = this.mapConnection.getSxMapViewer().getQuery().getObjects(spatialClassName);
				for(Enumeration en = objects.elements(); en.hasMoreElements();)
				{
					SxSpatialObject obj = (SxSpatialObject )en.nextElement();
					sampleLowCase = obj.getLabel().toLowerCase();
					if(sampleLowCase.indexOf(searchTextLowCase) != -1)
					{
						System.out.println("Label " + obj.getLabel());
						found.add(new OfxSpatialObject(obj, obj.getLabel()));
					}
/*
					else
					if(obj.getGeometry().toString().indexOf(searchText) != -1)
					{
						System.out.println("Geometry " + obj.getGeometry().toString());
						vec.add(new MySpatialObject(obj, obj.getGeometry().toString()));
					}
*/
				}
			}

			vector = this.mapConnection.getSxMapViewer().getBackgroundClasses();
			for(Iterator it = vector.iterator(); it.hasNext();)
			{
				spatialClassName = (String )it.next();
				objects = this.mapConnection.getSxMapViewer().getQuery().getObjects(spatialClassName);
				for(Enumeration en = objects.elements(); en.hasMoreElements();)
				{
					SxSpatialObject obj = (SxSpatialObject)en.nextElement();
					sampleLowCase = obj.getLabel().toLowerCase();
					if(sampleLowCase.indexOf(searchTextLowCase) != -1)
					{
						System.out.println("Label " + obj.getLabel());
						found.add(new OfxSpatialObject(obj, obj.getLabel()));
					}
/*
					else
					if(obj.getGeometry().toString().indexOf(searchText) != -1)
					{
						System.out.println("Geometry " + obj.getGeometry().toString());
						vec.add(new MySpatialObject(obj, obj.getGeometry().toString()));
					}
*/
				}
			}
		}
		catch(Exception ex)
		{
				ex.printStackTrace();
		}

		return found;
	}

	public Image renderMapImage(TopologicalImageQuery request) throws MapConnectionException, MapDataException {
		// TODO Auto-generated method stub
		return null;
	}

	public void stopRendering() throws MapConnectionException, MapDataException {
		// nothing
	}
	
	public MapConnection getMapConnection() throws MapConnectionException {
		return this.mapConnection;
	}


}

