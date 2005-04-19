package com.syrus.AMFICOM.Client.Map.Props;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.map.PhysicalLink;

public final class MapLinkPane
		extends JPanel 
		implements ObjectResourcePropertiesPane, MapPropertiesPane
{
	public ApplicationContext aContext;
	
	MapLinkBindPanel bPanel = new MapLinkBindPanel();

	PhysicalLink maplink;

	private LogicalNetLayer lnl;

	private static MapLinkPane instance = new MapLinkPane();

	private MapLinkPane()
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

	public MapLinkPane(PhysicalLink maplink)
	{
		this();
		setObject(maplink);
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
		return this.maplink;
	}

	public void setObject(Object or)
	{
		this.maplink = (PhysicalLink)or;

		this.bPanel.setObject(this.maplink);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
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
//					SchemeCableLink scl = (SchemeCableLink )it.next();
//					disp.notify(new MapEvent(scl, MapEvent.MAP_ELEMENT_CHANGED));
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
