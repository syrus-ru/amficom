package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import com.syrus.AMFICOM.Client.Map.Props.MapLinkBindPanel;
import com.syrus.AMFICOM.Client.Map.Props.MapLinkGeneralPanel;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public final class MapLinkPane extends JPanel implements ObjectResourcePropertiesPane
{
	public ApplicationContext aContext;
	
	MapLinkGeneralPanel gPanel = new MapLinkGeneralPanel();
	MapLinkBindPanel bPanel = new MapLinkBindPanel();
//	MapLinkCatalogPanel caPanel = new MapLinkCatalogPanel();
//	MapLinkPortsPanel lpPanel = new MapLinkPortsPanel();
//	MapLinkCharacteristicsPanel chPanel = new MapLinkCharacteristicsPanel();

	MapPhysicalLinkElement maplink;

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
		tabbedPane.add(bPanel.getName(), bPanel);
//		tabbedPane.add(caPanel.getName(), caPanel);
//		tabbedPane.add(lpPanel.getName(), lpPanel);
//		tabbedPane.add(chPanel.getName(), chPanel);
	}

	public ObjectResource getObjectResource()
	{
		return maplink;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.maplink = (MapPhysicalLinkElement )or;

//		System.out.println("set prop pane to " + maplink.name);

		gPanel.setObjectResource(maplink);
		bPanel.setObjectResource(maplink);
//		caPanel.setObjectResource(maplink);
//		lpPanel.setObjectResource(maplink);
//		chPanel.setObjectResource(maplink);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		gPanel.setContext(aContext);
		bPanel.setContext(aContext);
//		caPanel.setContext(aContext);
//		lpPanel.setContext(aContext);
//		chPanel.setContext(aContext);
	}

	public boolean modify()
	{
		if(gPanel.modify()
			&& bPanel.modify())
			return true;
//		caPanel.modify();
//		lpPanel.modify();
//		chPanel.modify();
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
