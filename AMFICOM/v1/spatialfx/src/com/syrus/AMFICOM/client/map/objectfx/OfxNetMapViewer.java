/**
 * $Id: OfxNetMapViewer.java,v 1.7 2005/06/06 13:04:56 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.objectfx;

import java.awt.BorderLayout;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;

import com.ofx.component.swing.JMapViewer;
import com.ofx.mapViewer.SxMapLayerInterface;
import com.ofx.mapViewer.SxMapViewer;
import com.ofx.mapViewer.SxMarkerLayer;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.SpatialLayer;
import com.syrus.AMFICOM.client.map.ui.MapDropTargetListener;
import com.syrus.AMFICOM.client.map.ui.MapKeyAdapter;
import com.syrus.AMFICOM.client.map.ui.MapMouseListener;
import com.syrus.AMFICOM.client.map.ui.MapMouseMotionListener;
import com.syrus.AMFICOM.client.map.ui.MapToolTippedPanel;
import com.syrus.AMFICOM.client.model.Environment;

/**
 * 
 * @version $Revision: 1.7 $, $Date: 2005/06/06 13:04:56 $
 * @author $Author: krupenn $
 * @module spatialfx_v1
 */
public class OfxNetMapViewer extends NetMapViewer
{
	protected OfxLogicalNetLayer logicalNetLayer = null;

	protected OfxConnection mapConnection = null;
	
	protected DropTarget dropTarget = null;
	protected DropTargetListener dtl = null;
	protected MapToolTippedPanel mttp = null;
	protected ToolTipManager ttm = null;
	protected MouseListener ml = null;
	protected MouseMotionListener mml = null;
	protected MapKeyAdapter mka = null;

	protected JPanel visualComponent = null;

	public JMapViewer getJMapViewer()
	{
		return this.mapConnection.getJMapViewer();
	}
	
	public JComponent getVisualComponent()
	{
		return this.visualComponent;
	}

	public void init() throws MapDataException
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"init()");
		
		super.init();
		
		if(this.mapConnection == null)
			throw new MapDataException("Нет соединения.");

		JMapViewer jMapViewer = this.mapConnection.getJMapViewer();
		
		if(jMapViewer == null)
			throw new MapDataException("Соединение не инициализировано.");

		this.visualComponent = new JPanel();
		this.visualComponent.setLayout(new BorderLayout());
		this.visualComponent.add(jMapViewer);
		
		if(this.logicalNetLayer != null)
		{
			jMapViewer.removeMouseListener(this.mttp.toolTippedPanelListener);
			jMapViewer.removeMouseMotionListener(this.mttp.toolTippedPanelListener);
			this.dropTarget.setActive(false);
			this.ttm.unregisterComponent(this.mttp);
		}
		try
		{
			this.logicalNetLayer = new OfxLogicalNetLayer(this);

			this.logicalNetLayer.setCenter(MapPropertiesManager.getCenter());
			this.logicalNetLayer.setScale(MapPropertiesManager.getZoom());

			this.dtl = new MapDropTargetListener(this.logicalNetLayer);
			this.dropTarget = new DropTarget( jMapViewer.getMapCanvas(), this.dtl);
			this.dropTarget.setActive(true);

			this.mttp = new MapToolTippedPanel(this);
			jMapViewer.addMouseListener(this.mttp.toolTippedPanelListener);
			jMapViewer.addMouseMotionListener(this.mttp.toolTippedPanelListener);

			this.mka = new MapKeyAdapter(this.logicalNetLayer);
			this.visualComponent.addKeyListener(this.mka);
			this.visualComponent.grabFocus();

			this.ttm = ToolTipManager.sharedInstance();
			this.ttm.registerComponent(this.mttp);

			SxMapViewer anSxMapViewer = jMapViewer.getSxMapViewer();
	
			anSxMapViewer.addLayer( "Network layer", this.logicalNetLayer.spatialLayer);

			try 
			{
				SxMarkerLayer markerLayer = (SxMarkerLayer) 
						anSxMapViewer.getLayer(SxMapLayerInterface.MARKER);
				markerLayer.listenForMapEvents( false );
				markerLayer.setEnabled(false);
			} 
			catch (Exception ex) 
			{
					ex.printStackTrace();
			} 
			
			anSxMapViewer.removeNamedLayer("OFX LOGO");
			anSxMapViewer.removeNamedLayer("OFX COPYRIGHT");

			this.logicalNetLayer.repaint(true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new MapDataException("abracadabra");
		}
		if(this.ml == null)
		{
			this.ml = new MapMouseListener(this.logicalNetLayer);
			jMapViewer.addMouseListener(this.ml);
		}
		if(this.mml == null)
		{
			this.mml = new MapMouseMotionListener(this.logicalNetLayer);
			jMapViewer.addMouseMotionListener(this.mml);
		}
	}

	public void saveConfig()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"saveConfig()");
		
		MapPropertiesManager.setCenter(this.logicalNetLayer.getCenter());
		MapPropertiesManager.setZoom(this.logicalNetLayer.getScale());
		MapPropertiesManager.saveIniFile();
	}

	public MapConnection getConnection()
	{
		return this.mapConnection;
	}

	public void setConnection(MapConnection conn)
		throws MapDataException
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setConnection(" + conn + ")");
		
		try
		{
			this.mapConnection = (OfxConnection )conn;
			
		}
		catch (ClassCastException e)
		{
			throw new MapDataException(e);
		}
	}
	
	public LogicalNetLayer getLogicalNetLayer()
	{
		return this.logicalNetLayer;
	}

	public List getLayers()
	{
		List returnList = new LinkedList();

		int sortOrder = 301;// as used in Ofx.JMapLegend
		
		SxMapViewer sxMapViewer = this.mapConnection.getJMapViewer().getSxMapViewer();
		
		{
		Vector vector = sxMapViewer.getForegroundClasses(sortOrder);
		for(Iterator it = vector.iterator(); it.hasNext();)
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

		{
		Vector vector = sxMapViewer.getBackgroundClasses();
		for(Iterator it = vector.iterator(); it.hasNext();)
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
}
