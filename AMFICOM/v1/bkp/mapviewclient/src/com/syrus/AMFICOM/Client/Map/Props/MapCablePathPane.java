package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MapCablePathPane
		extends JPanel 
		implements ObjectResourcePropertiesPane, MapPropertiesPane
{
	public ApplicationContext aContext;
	
	MapCablePathGeneralPanel gPanel = new MapCablePathGeneralPanel();
	MapCablePathBindPanel bPanel = new MapCablePathBindPanel();

	MapCablePathElement path;

	private LogicalNetLayer lnl;

	public JTabbedPane tabbedPane = new JTabbedPane();

	private static MapCablePathPane instance = new MapCablePathPane();

	private MapCablePathPane()
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

	public MapCablePathPane(MapCablePathElement path)
	{
		this();
		setObjectResource(path);
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
		return path;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.path = (MapCablePathElement)or;

		gPanel.setObjectResource(path);
		bPanel.setObjectResource(path);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		gPanel.setContext(aContext);
		bPanel.setContext(aContext);
	}

	public void showBindPanel()
	{
		tabbedPane.setSelectedComponent(bPanel);
	}

	public boolean modify()
	{
		if(gPanel.modify()
			&& bPanel.modify())
		{
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
