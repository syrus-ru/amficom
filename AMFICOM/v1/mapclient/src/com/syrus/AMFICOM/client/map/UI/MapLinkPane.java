package com.syrus.AMFICOM.Client.Configure.Map.UI;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.BorderLayout;

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.swing.JTabbedPane;

public class MapLinkPane extends PropertiesPanel
{
	public ApplicationContext aContext;
	
	MapLinkGeneralPanel gPanel = new MapLinkGeneralPanel();
	MapLinkAttributesPanel aPanel = new MapLinkAttributesPanel();
//	MapLinkCatalogPanel caPanel = new MapLinkCatalogPanel();
//	MapLinkPortsPanel lpPanel = new MapLinkPortsPanel();
//	MapLinkCharacteristicsPanel chPanel = new MapLinkCharacteristicsPanel();

	MapPhysicalLinkElement maplink;

	public JTabbedPane tabbedPane = new JTabbedPane();

	public MapLinkPane()
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

	public MapLinkPane(MapPhysicalLinkElement maplink)
	{
		this();
		setObjectResource(maplink);
	}
	
	private void jbInit() throws Exception
	{
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(JTabbedPane.TOP);

		tabbedPane.add(gPanel.getName(), gPanel);
		tabbedPane.add(aPanel.getName(), aPanel);
//		tabbedPane.add(caPanel.getName(), caPanel);
//		tabbedPane.add(lpPanel.getName(), lpPanel);
//		tabbedPane.add(chPanel.getName(), chPanel);
	}

	public ObjectResource getObjectResource()
	{
		return maplink;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.maplink = (MapPhysicalLinkElement )or;

//		System.out.println("set prop pane to " + maplink.name);

		gPanel.setObjectResource(maplink);
		aPanel.setObjectResource(maplink);
//		caPanel.setObjectResource(maplink);
//		lpPanel.setObjectResource(maplink);
//		chPanel.setObjectResource(maplink);
		return true;
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		gPanel.setContext(aContext);
		aPanel.setContext(aContext);
//		caPanel.setContext(aContext);
//		lpPanel.setContext(aContext);
//		chPanel.setContext(aContext);
	}

	public boolean modify()
	{
		gPanel.modify();
		aPanel.modify();
//		caPanel.modify();
//		lpPanel.modify();
//		chPanel.modify();
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