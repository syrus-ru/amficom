package com.syrus.AMFICOM.Client.Map.Mapinfo;

import com.mapinfo.beans.vmapj.VisualMapJ;
import com.mapinfo.mapj.AbstractLayer;
import com.mapinfo.mapj.FeatureLayer;
import com.mapinfo.mapj.Layer;
import com.mapinfo.mapj.LayerType;
import com.mapinfo.mapj.MapJ;
//import com.mapinfo.util.DoublePoint;
import com.mapinfo.unit.LinearUnit;
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
import com.syrus.AMFICOM.map.DoublePoint;
import java.awt.Component;

import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import java.util.TooManyListenersException;
import javax.swing.JComponent;
import javax.swing.ToolTipManager;

public class MapInfoNetMapViewer extends NetMapViewer
{
	protected MapInfoLogicalNetLayer lnl = null;
	
	protected MapToolTippedPanel mttp = null;
	protected ToolTipManager ttm = null;
	
	protected DropTarget dropTarget = null;
	
	protected MouseListener ml = null;
	protected MouseMotionListener mml = null;
	protected MapKeyAdapter mka = null;
	protected DropTargetListener dtl = null;

	protected static MapInfoNetMapViewer mapViewer = null;
	protected static boolean dbset = false;

	protected MapScrollPane scrollPane = null;
	protected MapImagePanel mapImagePanel = null;
  
	protected MapJ localMapJ = null;  
	protected String mapperServletURL = null;
	protected String mapDefinitionFile = null;
  
	public void init()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"init()");
		
		super.init();

//		if(lnl != null)
//		{
//			mapImagePanel.removeMouseListener(mttp.ls);
//			mapImagePanel.removeMouseMotionListener(mttp.ls);
//			dropTarget.removeDropTargetListener(dtl);
//			ttm.unregisterComponent(mttp);
//		}
		try
		{
			lnl = new MapInfoLogicalNetLayer(this);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void saveConfig()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"saveConfig()");
		
		MapPropertiesManager.setCenter(lnl.getCenter());
		MapPropertiesManager.setZoom(lnl.getScale());
		MapPropertiesManager.saveIniFile();
	}

	/**
	 * ���������� ��������� ��� �����������
	 * 
	 */
	public void setMap(String mapperServletURL,String mapDefinitionFile)
	{
    this.mapDefinitionFile = mapDefinitionFile;
    this.mapperServletURL = mapperServletURL;    
    
    this.localMapJ = this.initMapJ(this.mapDefinitionFile);
    this.lnl.setCenter(MapPropertiesManager.getCenter());
    this.lnl.setScale(MapPropertiesManager.getZoom());
  }

/**
 * �������������� ������ MapJ ��� ��������� �������������� ���������
 */
  private MapJ initMapJ(String mapDefinitionFile)
  {
		MapJ myMap = new MapJ(); // this MapJ object

		// Query for image locations and load the geoset
		try
		{
      System.out.println("MapImagePanel - Loading geoset...");
      myMap.loadMapDefinition(mapDefinitionFile);
      System.out.println("MapImagePanel - Geoset " + mapDefinitionFile + " has been loaded.");        
		}
		catch (IOException e)
		{
			System.out.println("MapImagePanel - Can't load geoset: " + mapDefinitionFile);
			e.printStackTrace();
		}

		System.out.println("Units " + myMap.getDistanceUnits().toString());
		myMap.setDistanceUnits(LinearUnit.meter);
   
    return myMap;
  }

	
	/**
	 * ��������� ������ � ���������� � ������ 
	 */
	public void closeMap()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"closeMap()");
		
//		throw new UnsupportedOperationException();
	}
	
	/**
	 * ���������� ���������� � ���������� ��������������� ����������
	 */
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
			setMap(conn.getURL(), conn.getPath() + conn.getView());
	}
	
	/**
	 * �������� ���������� � ���������� ��������������� ����������
	 */
	public MapConnection getConnection()
	{
		return this.mapConnection;
	}
	
	/**
	 * �������� ����������� ���������, � ������� ������������ �����������
	 */
	public JComponent getVisualComponent()
	{
		if(mapImagePanel == null)
		{
			mapImagePanel = new MapImagePanel();

			if(scrollPane == null)
				scrollPane = new MapScrollPane(this);

		    mapImagePanel.setLogicalLayer(lnl);

			dtl = new MapDropTargetListener(lnl);
			dropTarget = mapImagePanel.getDropTarget();
			if(dropTarget == null)
			{
				dropTarget = new DropTarget(mapImagePanel, dtl);
				mapImagePanel.setDropTarget(dropTarget);
			}
			else
				try
				{
					dropTarget.addDropTargetListener(dtl);
				}
				catch (TooManyListenersException e)
				{
					e.printStackTrace();
				}
        
			dropTarget.setActive(true);

			mttp = new MapToolTippedPanel(this);
			mapImagePanel.addMouseListener(mttp.toolTippedPanelListener);
			mapImagePanel.addMouseMotionListener(mttp.toolTippedPanelListener);

			mka = new MapKeyAdapter(lnl);
			scrollPane.addKeyListener(mka);
			scrollPane.grabFocus();

			ttm = ToolTipManager.sharedInstance();
			ttm.registerComponent(mttp);

			if(ml == null)
			{
				ml = new MapMouseListener(lnl);
				mapImagePanel.addMouseListener(ml);
			}
			if(mml == null)
			{
				mml = new MapMouseMotionListener(lnl);
				mapImagePanel.addMouseMotionListener(mml);
			}
		}
		return scrollPane;
	}

	public LogicalNetLayer getLogicalNetLayer()
	{
		return this.lnl;
	}

	public JComponent getJComponent()
	{
		return null;
	}
	
	public Component getComponent()
	{
		return mapImagePanel;
	}
	
	public List getAvailableViews()
	{
    List listToReturn = new ArrayList();
    listToReturn.add(this.mapDefinitionFile);
    
    return listToReturn;
	}
	
	public void setView(String dataBaseView)
	{
		if (!getConnection().getView().equals(dataBaseView)) 
		{
			setMap(getConnection().getPath(), dataBaseView);
		}
	}

	public List getLayers()
	{
		List returnList = new ArrayList();

    Iterator layersIt =  this.localMapJ.getLayers().iterator(LayerType.FEATURE);
    for (;layersIt.hasNext();)
    {
      FeatureLayer currLayer = (FeatureLayer)layersIt.next();
      SpatialLayer spL = new MapInfoSpatialLayer(currLayer,this.lnl);
      returnList.add(spL);
    }

		return returnList;
	}
	
}