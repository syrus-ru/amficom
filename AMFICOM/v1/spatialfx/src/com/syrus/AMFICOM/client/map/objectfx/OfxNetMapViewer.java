/**
 * $Id: OfxNetMapViewer.java,v 1.2 2005/02/10 11:37:49 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.ObjectFX;

import java.awt.Component;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.ToolTipManager;

import com.ofx.base.SxEnvironment;
import com.ofx.component.swing.JMapViewer;
import com.ofx.mapViewer.SxMapLayerInterface;
import com.ofx.mapViewer.SxMapViewer;
import com.ofx.mapViewer.SxMarkerLayer;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapConnection;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.NetMapViewer;
import com.syrus.AMFICOM.Client.Map.SpatialLayer;
import com.syrus.AMFICOM.Client.Map.UI.MapDropTargetListener;
import com.syrus.AMFICOM.Client.Map.UI.MapKeyAdapter;
import com.syrus.AMFICOM.Client.Map.UI.MapMouseListener;
import com.syrus.AMFICOM.Client.Map.UI.MapMouseMotionListener;
import com.syrus.AMFICOM.Client.Map.UI.MapScrollPane;
import com.syrus.AMFICOM.Client.Map.UI.MapToolTippedPanel;

/**
 * 
 * @version $Revision: 1.2 $, $Date: 2005/02/10 11:37:49 $
 * @author $Author: krupenn $
 * @module spatialfx_v1
 */
public class OfxNetMapViewer extends NetMapViewer
{
	protected OfxLogicalNetLayer lnl = null;
	
	protected MapToolTippedPanel mttp = null;
	protected ToolTipManager ttm = null;
	
	protected DropTarget dropTarget = null;
	
	protected MouseListener ml = null;
	protected MouseMotionListener mml = null;
	protected DropTargetListener dtl = null;
	protected MapKeyAdapter mka = null;

	protected static OfxNetMapViewer mapViewer = null;
	protected static boolean dbset = false;

	protected JMapViewer jMapViewer = null;
	protected MapScrollPane scrollPane = null;

	public JComponent getJComponent()
	{
		return null;
	}
	
	public Component getComponent()
	{
		return this.jMapViewer;
	}
	
	public JMapViewer getJMapViewer()
	{
		return this.jMapViewer;
	}
	
	public JComponent getVisualComponent()
	{
		if(this.jMapViewer == null)
			this.jMapViewer = new JMapViewer();
		if(this.scrollPane == null)
			this.scrollPane = new MapScrollPane(this);
		return this.scrollPane;
	}

	public void init()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"init()");
		
		super.init();
		
		if(this.lnl != null)
		{
//			this.removeMouseListener(ml);
//			this.removeMouseMotionListener(mml);
			this.jMapViewer.removeMouseListener(this.mttp.toolTippedPanelListener);
			this.jMapViewer.removeMouseMotionListener(this.mttp.toolTippedPanelListener);
			this.dropTarget.setActive(false);
			this.ttm.unregisterComponent(this.mttp);
		}
		try
		{
			this.lnl = new OfxLogicalNetLayer(this);

//			lnl.getMapState().setActionMode(MapState.DRAW_ACTION_MODE);

			this.dtl = new MapDropTargetListener(this.lnl);
			this.dropTarget = new DropTarget( this.jMapViewer.getMapCanvas(), this.dtl);
			this.dropTarget.setActive(true);

			this.mttp = new MapToolTippedPanel(this);
			this.jMapViewer.addMouseListener(this.mttp.toolTippedPanelListener);
			this.jMapViewer.addMouseMotionListener(this.mttp.toolTippedPanelListener);

			this.mka = new MapKeyAdapter(this.lnl);
			getVisualComponent().addKeyListener(this.mka);
			getVisualComponent().grabFocus();

			this.ttm = ToolTipManager.sharedInstance();
			this.ttm.registerComponent(this.mttp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(this.ml == null)
		{
			this.ml = new MapMouseListener(this.lnl);
			this.jMapViewer.addMouseListener(this.ml);
		}
		if(this.mml == null)
		{
			this.mml = new MapMouseMotionListener(this.lnl);
			this.jMapViewer.addMouseMotionListener(this.mml);
		}
	}

	public void saveConfig()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"saveConfig()");
		
		MapPropertiesManager.setCenter(this.lnl.getCenter());
		MapPropertiesManager.setZoom(this.lnl.getScale());
		MapPropertiesManager.saveIniFile();
	}

	public MapConnection getConnection()
	{
		return this.mapConnection;
	}

	//Установить карту
	public void setConnection(MapConnection conn)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setConnection(" + conn + ")");
		
		this.mapConnection = conn;
		
		if(conn == null)
		{
			closeMap();
		}
		else
			setMap(conn.getPath(), conn.getView());
	}
	
	public void setMap( String dataBasePath , String dataBaseView )
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setMap(" + dataBasePath  + ", " + dataBaseView + ")");
		
		try
		{
			SxMapViewer anSxMapViewer = this.jMapViewer.getSxMapViewer();

			if(!dbset)
				this.jMapViewer.setDBName( dataBasePath);
			this.jMapViewer.setMapName( dataBaseView);

			anSxMapViewer.addLayer( "Network layer", this.lnl.spatialLayer);
			
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
			anSxMapViewer.postPaintEvent();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void closeMap()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"closeMap()");
		
		SxEnvironment.singleton().getQuery().close();
		this.jMapViewer.closeSession();
		com.ofx.service.SxServiceFactory.shutdown();
	}

	/**
	 * 
	 */
	public LogicalNetLayer getLogicalNetLayer()
	{
		return this.lnl;
	}

	public List getAvailableViews()
	{
		return this.jMapViewer.getAvailableMaps();
	}
	
	public void setView(String dataBaseView)
	{
		if (!getConnection().getView().equals(dataBaseView)) 
		{
			setMap(getConnection().getPath(), dataBaseView);
//			try 
//			{
//				jMapViewer.setMapName(dataBaseView);
//				getConnection().setView(dataBaseView);
//			} 
//			catch (SxInvalidNameException ine) { }
		}
	}

	public List getLayers()
	{
		List returnList = new LinkedList();

		int sortOrder = 301;// as used in Ofx.JMapLegend
		
		SxMapViewer sxMapViewer = this.jMapViewer.getSxMapViewer();
		
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
