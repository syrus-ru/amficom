package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.map.PhysicalLink;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

public final class MapLinkPane
		extends JPanel 
		implements ObjectResourcePropertiesPane, MapPropertiesPane
{
	public ApplicationContext aContext;
	
	MapLinkGeneralPanel gPanel = new MapLinkGeneralPanel();
	MapLinkBindPanel bPanel = new MapLinkBindPanel();

	PhysicalLink maplink;

	private LogicalNetLayer lnl;

	public JTabbedPane tabbedPane = new JTabbedPane();
	
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
		return this.maplink;
	}

	public void setObject(Object or)
	{
		this.maplink = (PhysicalLink)or;

		this.gPanel.setObject(this.maplink);
		this.bPanel.setObject(this.maplink);
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
//					SchemeCableLink scl = (SchemeCableLink )it.next();
//					disp.notify(new MapEvent(scl, MapEvent.MAP_ELEMENT_CHANGED));
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
