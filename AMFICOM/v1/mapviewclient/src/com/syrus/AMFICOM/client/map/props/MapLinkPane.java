package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import com.syrus.AMFICOM.Client.Map.Props.MapLinkBindPanel;
import com.syrus.AMFICOM.Client.Map.Props.MapLinkGeneralPanel;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;

import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import java.awt.BorderLayout;

import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public final class MapLinkPane extends JPanel implements ObjectResourcePropertiesPane
{
	public ApplicationContext aContext;
	
	MapLinkGeneralPanel gPanel = new MapLinkGeneralPanel();
	MapLinkBindPanel bPanel = new MapLinkBindPanel();

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
	}

	public ObjectResource getObjectResource()
	{
		return maplink;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.maplink = (MapPhysicalLinkElement )or;

		gPanel.setObjectResource(maplink);
		bPanel.setObjectResource(maplink);
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
