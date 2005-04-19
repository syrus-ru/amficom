package com.syrus.AMFICOM.Client.Map.Props;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.map.SiteNode;

public final class MapSitePane
		extends JPanel 
		implements ObjectResourcePropertiesPane, MapPropertiesPane
{
	private MapSiteBindPanel bPanel = new MapSiteBindPanel();

	private SiteNode site;

	private LogicalNetLayer lnl;

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
		this.bPanel.setLogicalNetLayer(lnl);
	}

	public LogicalNetLayer getLogicalNetLayer()
	{
		return this.lnl;
	}

	private void jbInit()
	{
		this.setLayout(new BorderLayout());
		this.add(this.bPanel, BorderLayout.CENTER);
	}

	public Object getObject()
	{
		return this.site;
	}

	public void setObject(Object or)
	{
		this.site = (SiteNode)or;

		this.bPanel.setObject(this.site);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.bPanel.setContext(aContext);
	}

	public boolean modify()
	{
		if(this.bPanel.modify())
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
		this.bPanel.cancel();
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
