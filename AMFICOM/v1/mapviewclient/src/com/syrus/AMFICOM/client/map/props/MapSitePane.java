package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MapSitePane extends JPanel implements ObjectResourcePropertiesPane
{
	public ApplicationContext aContext;
	
	MapSiteGeneralPanel gPanel = new MapSiteGeneralPanel();
	MapSiteBindPanel bPanel = new MapSiteBindPanel();

	MapSiteNodeElement site;

	public JTabbedPane tabbedPane = new JTabbedPane();

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

	private void jbInit() throws Exception
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
			return true;
		return false;
	}

	public boolean cancel()
	{
		return false;
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