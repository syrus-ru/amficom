package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public final class MapSitePane
		extends JPanel 
		implements ObjectResourcePropertiesPane, MapPropertiesPane
{
	private JTabbedPane tabbedPane = new JTabbedPane();
	private MapSiteGeneralPanel gPanel = new MapSiteGeneralPanel();
	private MapSiteBindPanel bPanel = new MapSiteBindPanel();

	private MapSiteNodeElement site;

	private LogicalNetLayer lnl;

	private ApplicationContext aContext;

	private static MapSitePane instance = new MapSitePane();

	private MapSitePane()
	{
		super();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static ObjectResourcePropertiesPane getInstance()
	{
		return instance;
	}

	public void setLogicalNetLayer(LogicalNetLayer lnl)
	{
		this.lnl = lnl;
		gPanel.setLogicalNetLayer(lnl);
		bPanel.setLogicalNetLayer(lnl);
	}

	public LogicalNetLayer getLogicalNetLayer()
	{
		return lnl;
	}

	private void jbInit()
	{
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(JTabbedPane.TOP);

		tabbedPane.add(gPanel.getName(), gPanel);
		tabbedPane.add(bPanel.getName(), bPanel);
	}

	public ObjectResource getObjectResource()
	{
		return site;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.site = (MapSiteNodeElement )or;

		gPanel.setObjectResource(site);
		bPanel.setObjectResource(site);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		gPanel.setContext(aContext);
		bPanel.setContext(aContext);
	}

	public boolean modify()
	{
		if(gPanel.modify()
			&& bPanel.modify())
		{
//			Dispatcher disp  = aContext.getDispatcher();
//			if(disp != null)
//				for(Iterator it = bPanel.getUnboundElements().iterator(); it.hasNext();)
//				{
//					SchemeElement se = (SchemeElement )it.next();
//					disp.notify(new MapEvent(se, MapEvent.MAP_ELEMENT_CHANGED));
//				}
			return true;
		}
		return false;
	}

	public boolean cancel()
	{
		gPanel.cancel();
		bPanel.cancel();
		return true;
	}

	public boolean save()
	{
		return false;
	}

	public boolean open()
	{
		return false;
	}

	public boolean delete()
	{
		return false;
	}

	public boolean create()
	{
		return false;
	}
}