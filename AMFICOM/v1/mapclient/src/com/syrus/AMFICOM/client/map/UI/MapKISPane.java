package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.Map.MapKISNodeElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.BorderLayout;

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.swing.JTabbedPane;

public class MapKISPane extends PropertiesPanel
{
	public ApplicationContext aContext;
	
	MapKISGeneralPanel gPanel = new MapKISGeneralPanel();
	MapKISAttributesPanel aPanel = new MapKISAttributesPanel();
//	MapKISPortsPanel pPanel = new MapKISPortsPanel();
//	MapKISCatalogPanel caPanel = new MapKISCatalogPanel();
//	MapKISCharacteristicsPanel chPanel = new MapKISCharacteristicsPanel();

	MapKISNodeElement mapkis;

	public JTabbedPane tabbedPane = new JTabbedPane();

	public MapKISPane()
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

	public MapKISPane(MapKISNodeElement mapkis)
	{
		this();
		setObjectResource(mapkis);
	}
	
	private void jbInit() throws Exception
	{
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(JTabbedPane.TOP);

		tabbedPane.add(gPanel.getName(), gPanel);
		tabbedPane.add(aPanel.getName(), aPanel);
//		tabbedPane.add(caPanel.getName(), caPanel);
//		tabbedPane.add(pPanel.getName(), pPanel);
//		tabbedPane.add(chPanel.getName(), chPanel);
	}

	public ObjectResource getObjectResource()
	{
		return mapkis;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.mapkis = (MapKISNodeElement )or;

//		System.out.println("set prop pane to " + mapkis.name);

		gPanel.setObjectResource(mapkis);
		aPanel.setObjectResource(mapkis);
//		pPanel.setObjectResource(mapkis);
//		caPanel.setObjectResource(mapkis);
//		chPanel.setObjectResource(mapkis);
		return true;
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		gPanel.setContext(aContext);
		aPanel.setContext(aContext);
//		pPanel.setContext(aContext);
//		caPanel.setContext(aContext);
//		chPanel.setContext(aContext);
	}

	public boolean modify()
	{
		gPanel.modify();
		aPanel.modify();
//		pPanel.modify();
//		caPanel.modify();
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