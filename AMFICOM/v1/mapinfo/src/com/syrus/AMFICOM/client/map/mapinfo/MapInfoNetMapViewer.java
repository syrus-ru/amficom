package com.syrus.AMFICOM.Client.Map.Mapinfo;

import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TooManyListenersException;

import javax.swing.JComponent;
import javax.swing.ToolTipManager;

import com.mapinfo.mapj.FeatureLayer;
import com.mapinfo.mapj.LayerType;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapConnection;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.NetMapViewer;
import com.syrus.AMFICOM.Client.Map.SpatialLayer;
import com.syrus.AMFICOM.Client.Map.UI.MapDropTargetListener;
import com.syrus.AMFICOM.Client.Map.UI.MapKeyAdapter;
import com.syrus.AMFICOM.Client.Map.UI.MapMouseListener;
import com.syrus.AMFICOM.Client.Map.UI.MapMouseMotionListener;
import com.syrus.AMFICOM.Client.Map.UI.MapToolTippedPanel;

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

	protected static boolean dbset = false;

	protected MapImagePanel mapImagePanel = null;
	
	protected MapInfoConnection mapConnection = null;
	
	/**
	 * Список слоёв. Подгружается один раз при инциализации модуля.
	 * Следует обновлять при изменении файла проекта во время работы (это опция пока нереализована)
	 */
	private List layersList = null;
	
	public void init() throws MapDataException
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass()
				.getName(), "init()");

		super.init();
		
		if(this.mapConnection == null)
			throw new MapDataException("Нет соединения.");

		try
		{
			this.lnl = new MapInfoLogicalNetLayer(this);
			
			if(this.mapImagePanel == null)
			{
				this.mapImagePanel = new MapImagePanel();

				this.mapImagePanel.setLogicalLayer(this.lnl);

				this.dtl = new MapDropTargetListener(this.lnl);
				this.dropTarget = this.mapImagePanel.getDropTarget();
				if(this.dropTarget == null)
				{
					this.dropTarget = new DropTarget(this.mapImagePanel, this.dtl);
					this.mapImagePanel.setDropTarget(this.dropTarget);
				}
				else
					try
					{
						this.dropTarget.addDropTargetListener(this.dtl);
					}
					catch(TooManyListenersException e)
					{
						e.printStackTrace();
					}

				this.dropTarget.setActive(true);

				this.mttp = new MapToolTippedPanel(this);
				this.mapImagePanel.addMouseListener(this.mttp.toolTippedPanelListener);
				this.mapImagePanel.addMouseMotionListener(this.mttp.toolTippedPanelListener);

				this.mka = new MapKeyAdapter(this.lnl);
				this.mapImagePanel.addKeyListener(this.mka);
				this.mapImagePanel.grabFocus();

				this.ttm = ToolTipManager.sharedInstance();
				this.ttm.registerComponent(this.mttp);

				if(this.ml == null)
				{
					this.ml = new MapMouseListener(this.lnl);
					this.mapImagePanel.addMouseListener(this.ml);
				}
				if(this.mml == null)
				{
					this.mml = new MapMouseMotionListener(this.lnl);
					this.mapImagePanel.addMouseMotionListener(this.mml);
				}
			}
			
			this.lnl.initializeImageCache();
			this.lnl.setCenter(MapPropertiesManager.getCenter());
			this.lnl.setScale(MapPropertiesManager.getZoom());
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}

	public void saveConfig()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass()
				.getName(), "saveConfig()");

		MapPropertiesManager.setCenter(this.lnl.getCenter());
		MapPropertiesManager.setZoom(this.lnl.getScale());
		MapPropertiesManager.saveIniFile();
	}

	/**
	 * Установить соединение с хранилищем топографической информации
	 */
	public void setConnection(MapConnection conn)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass()
				.getName(), "setConnection(" + conn + ")");

		try
		{
			this.mapConnection = (MapInfoConnection) conn;
		} catch (ClassCastException e)
		{
			// TODO Auto-generated catch block
			Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass()
					.getName(), "Only MapInfoConnection can be set for this implementation.");			
		}
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
		return this.mapImagePanel;
	}

	public LogicalNetLayer getLogicalNetLayer()
	{
		return this.lnl;
	}

	public List getLayers()
	{
		if (this.layersList == null)
		{
			this.layersList = new ArrayList();

			Iterator layersIt = this.mapConnection.getLocalMapJ().getLayers().iterator(
					LayerType.FEATURE);
			for(; layersIt.hasNext();)
			{
				FeatureLayer currLayer = (FeatureLayer) layersIt.next();
				SpatialLayer spL = new MapInfoSpatialLayer(currLayer, this.lnl);
				this.layersList.add(spL);
			}
		}

		return this.layersList;
	}

}
