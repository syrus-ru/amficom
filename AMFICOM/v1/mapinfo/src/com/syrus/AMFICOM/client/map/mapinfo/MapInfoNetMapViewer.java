package com.syrus.AMFICOM.Client.Map.Mapinfo;

import com.mapinfo.beans.vmapj.VisualMapJ;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapConnection;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.NetMapViewer;

import com.syrus.AMFICOM.Client.Map.UI.MapDropTargetListener;
import com.syrus.AMFICOM.Client.Map.UI.MapKeyAdapter;
import com.syrus.AMFICOM.Client.Map.UI.MapMouseListener;
import com.syrus.AMFICOM.Client.Map.UI.MapMouseMotionListener;
import com.syrus.AMFICOM.Client.Map.UI.MapScrollPane;
import com.syrus.AMFICOM.Client.Map.UI.MapToolTippedPanel;
import java.awt.Component;

import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.List;

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

	protected MapImagePanel mapImagePanel = null;
  protected String mapperServletURL = null;
	protected MapScrollPane scrollPane = null;

	public void init()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"init()");
		
		super.init();
		
		if(lnl != null)
		{
			mapImagePanel.removeMouseListener(mttp.ls);
			mapImagePanel.removeMouseMotionListener(mttp.ls);
//			dropTarget.setActive(false);
			dropTarget.removeDropTargetListener(dtl);
			ttm.unregisterComponent(mttp);
		}
		try
		{
			lnl = new MapInfoLogicalNetLayer(this);

//			lnl.getMapState().setActionMode(MapState.DRAW_ACTION_MODE);

			dtl = new MapDropTargetListener(lnl);
			dropTarget = mapImagePanel.getDropTarget();
			if(dropTarget == null)
			{
				dropTarget = new DropTarget(mapImagePanel, dtl);
				mapImagePanel.setDropTarget(dropTarget);
			}
			else
				dropTarget.addDropTargetListener(dtl);
        
			dropTarget.setActive(true);

			mttp = new MapToolTippedPanel(this);
			mapImagePanel.addMouseListener(mttp.ls);
			mapImagePanel.addMouseMotionListener(mttp.ls);

			mka = new MapKeyAdapter(lnl);
			getVisualComponent().addKeyListener(mka);
			getVisualComponent().grabFocus();

			ttm = ToolTipManager.sharedInstance();
			ttm.registerComponent(mttp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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
	 * отобразить указанный вид кортографии
	 * 
	 */
//	public void setMap(String dataBasePath, String dataBaseView)
//	{
//		try
//		{
//			visualMapJ.getMapJ().loadGeoset(
//					dataBasePath + "\\" + dataBaseView, 
//					dataBasePath, 
//					null);
//
//			mapImagePanel.add(lnl.logicalLayerMapTool);          
///*      VisualMapJ visualMapJ;
//			visualMapJ.add(lnl.logicalLayerMapTool);
//			lnl.logicalLayerMapTool.setSelected(true);*/
//	}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public void setMap(String servletURL,String nullString)
	{
    //this.mapperServletURL = mapperServletURL;
    this.mapperServletURL = "http://amficom:8081/samples47/servlet/cmapper";
	}

	
	/**
	 * Закрывает сессию и соединение с картой 
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
	 * Установить соединение с хранилищем топографической информации
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
			setMap(conn.getPath(), conn.getView());
	}
	
	/**
	 * Получить соединение с хранилищем топографической информации
	 */
	public MapConnection getConnection()
	{
		return this.mapConnection;
	}
	
	/**
	 * Получить графический компонент, в котором отображается картография
	 */
	public JComponent getVisualComponent()
	{
		if(mapImagePanel == null)
			mapImagePanel = new MapImagePanel();
		if(scrollPane == null)
			scrollPane = new MapScrollPane(this);
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
		throw new UnsupportedOperationException();
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
		List returnList = new LinkedList();

		return returnList;
	}
	
}