package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import com.syrus.AMFICOM.Client.Map.Props.MapCablePathBindPanel;
import com.syrus.AMFICOM.Client.Map.Props.MapCablePathGeneralPanel;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MapCablePathPane extends JPanel implements ObjectResourcePropertiesPane
{
	public ApplicationContext aContext;
	
	MapCablePathGeneralPanel gPanel = new MapCablePathGeneralPanel();
	MapCablePathBindPanel bPanel = new MapCablePathBindPanel();

	MapCablePathElement path;

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
