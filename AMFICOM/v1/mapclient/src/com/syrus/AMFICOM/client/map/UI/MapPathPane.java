package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.Map.MapTransmissionPathElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.BorderLayout;

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.swing.JTabbedPane;

public class MapPathPane extends PropertiesPanel
{
	public ApplicationContext aContext;
	
	MapPathGeneralPanel gPanel = new MapPathGeneralPanel();
	MapPathAttributesPanel aPanel = new MapPathAttributesPanel();

	MapTransmissionPathElement mappath;

	public JTabbedPane tabbedPane = new JTabbedPane();

	public MapPathPane()
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

	public MapPathPane(MapTransmissionPathElement mappath)
	{
		this();
		setObjectResource(mappath);
	}
	
	private void jbInit() throws Exception
	{
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(JTabbedPane.TOP);

		tabbedPane.add(gPanel.getName(), gPanel);
		tabbedPane.add(aPanel.getName(), aPanel);
	}

	public ObjectResource getObjectResource()
	{
		return mappath;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.mappath = (MapTransmissionPathElement )or;

		gPanel.setObjectResource(mappath);
		aPanel.setObjectResource(mappath);
		return true;
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		gPanel.setContext(aContext);
		aPanel.setContext(aContext);
	}

	public boolean modify()
	{
		gPanel.modify();
		aPanel.modify();
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