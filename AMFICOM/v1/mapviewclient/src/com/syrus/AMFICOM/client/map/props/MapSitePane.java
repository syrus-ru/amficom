package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.map.SiteNode;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

public final class MapSitePane
		extends JPanel 
		implements ObjectResourcePropertiesPane, MapPropertiesPane
{
	private JTabbedPane tabbedPane = new JTabbedPane();
	private MapSiteGeneralPanel gPanel = new MapSiteGeneralPanel();
	private MapSiteBindPanel bPanel = new MapSiteBindPanel();

	private SiteNode site;

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
		this.gPanel.setLogicalNetLayer(lnl);
		this.bPanel.setLogicalNetLayer(lnl);
	}

	public LogicalNetLayer getLogicalNetLayer()
	{
		return this.lnl;
	}

	private void jbInit()
	{
		this.setLayout(new BorderLayout());
		this.add(this.tabbedPane, BorderLayout.CENTER);

		this.tabbedPane.setTabPlacement(SwingConstants.TOP);

		this.tabbedPane.add(this.gPanel.getName(), this.gPanel);
		this.tabbedPane.add(this.bPanel.getName(), this.bPanel);
	}

	public Object getObject()
	{
		return this.site;
	}

	public void setObject(Object or)
	{
		this.site = (SiteNode)or;

		this.gPanel.setObject(this.site);
		this.bPanel.setObject(this.site);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		this.gPanel.setContext(aContext);
		this.bPanel.setContext(aContext);
	}

	public boolean modify()
	{
		if(this.gPanel.modify()
			&& this.bPanel.modify())
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
		this.gPanel.cancel();
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
