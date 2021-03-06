/**
 * $Id: OfxImageLoader.java,v 1.7 2005/09/07 15:53:36 arseniy Exp $
 *
 * Syrus Systems
 * ??????-??????????? ?????
 * ??????: ???????
 *
 * ?????????: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.objectfx;

import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import com.ofx.geometry.SxRectangle;
import com.ofx.query.SxQueryResultInterface;
import com.ofx.repository.SxSpatialObject;
import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.client.map.SpatialLayer;
import com.syrus.AMFICOM.client.map.SpatialObject;
import com.syrus.AMFICOM.map.TopologicalImageQuery;

/**
 * ?????????? ?????? ??????????? ??????????? ???? ?? ????? ??????????
 * ?????? SpatialFX. ???? ??????????????? ????? ???????????? ? ???????
 * ??????? ???? SxMapViewer
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2005/09/07 15:53:36 $
 * @author $Author: arseniy $
 * @module spatialfx_v1
 */
public class OfxImageLoader implements MapImageLoader {
	
	private final OfxConnection mapConnection;

	public OfxImageLoader(OfxConnection mapConnection) {
		this.mapConnection = mapConnection;
	}
/*
	public List findSpatialObjects(String searchText) {
		List found = new LinkedList();

		Vector vector;
		String spatialClassName;
		
		String searchTextLowCase = searchText.toLowerCase();
		
		try {
			vector = this.mapConnection.getSxMapViewer().getForegroundClasses();
			for(Iterator it = vector.iterator(); it.hasNext();) {
				spatialClassName = (String )it.next();
				findInLayer(spatialClassName, searchTextLowCase, found);
			}

			vector = this.mapConnection.getSxMapViewer().getBackgroundClasses();
			for(Iterator it = vector.iterator(); it.hasNext();) {
				spatialClassName = (String )it.next();
				findInLayer(spatialClassName, searchTextLowCase, found);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return found;
	}
*/
	/**
	 * @param layerName
	 * @param searchText
	 * @param found
	 */
	private void findInLayer(String layerName, String searchText, List<SpatialObject> found) {
		String sampleLowCase;
		SxQueryResultInterface objects = this.mapConnection.getSxMapViewer().getQuery().getObjects(layerName);
		for(Enumeration en = objects.elements(); en.hasMoreElements();) {
			SxSpatialObject obj = (SxSpatialObject)en.nextElement();
			sampleLowCase = obj.getLabel().toLowerCase();
			if(sampleLowCase.indexOf(searchText) != -1) {
//				System.out.println("Label " + obj.getLabel());
				found.add(new OfxSpatialObject(obj, obj.getLabel()));
			}
		}
	}

	private void findInLayer(String layerName, SxRectangle bounds, List<SpatialObject> found) {
		SxQueryResultInterface objects = this.mapConnection.getSxMapViewer().getQuery().getObjects(layerName);
		for(Enumeration en = objects.elements(); en.hasMoreElements();) {
			SxSpatialObject obj = (SxSpatialObject)en.nextElement();
			if(obj.getBounds().intersects(bounds)) {
//				System.out.println("Label " + obj.getLabel());
				found.add(new OfxSpatialObject(obj, obj.getLabel()));
			}
		}
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

	public List<SpatialObject> findSpatialObjects(SpatialLayer layer, String searchText) throws MapConnectionException, MapDataException {
		List<SpatialObject> found = new LinkedList<SpatialObject>();
		OfxSpatialLayer ofxSpatialLayer = (OfxSpatialLayer)layer;
		findInLayer(ofxSpatialLayer.className, searchText.toLowerCase(), found);
		return found;
	}

	public List<SpatialObject> findSpatialObjects(SpatialLayer layer, Rectangle2D.Double bounds) throws MapConnectionException, MapDataException {
		List<SpatialObject> found = new LinkedList<SpatialObject>();
		OfxSpatialLayer ofxSpatialLayer = (OfxSpatialLayer)layer;
		findInLayer(
				ofxSpatialLayer.className, 
				new SxRectangle(
						bounds.getX(),
						bounds.getY(),
						bounds.getWidth(),
						bounds.getHeight()), 
				found);
		return found;
	}


}

