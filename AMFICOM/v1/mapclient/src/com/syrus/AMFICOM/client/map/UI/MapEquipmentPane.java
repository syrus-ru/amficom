package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.Map.MapEquipmentNodeElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.BorderLayout;

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.swing.JTabbedPane;

public class MapEquipmentPane extends PropertiesPanel
{
	public ApplicationContext aContext;
	
	MapEquipmentGeneralPanel gPanel = new MapEquipmentGeneralPanel();
	MapEquipmentAttributesPanel aPanel = new MapEquipmentAttributesPanel();
//	MapEquipmentLinksPanel lPanel = new MapEquipmentLinksPanel();
//	MapEquipmentCommutatePanel cmPanel = new MapEquipmentCommutatePanel();
//	MapEquipmentPortsPanel pPanel = new MapEquipmentPortsPanel();
//	MapEquipmentCatalogPanel caPanel = new MapEquipmentCatalogPanel();
//	MapEquipmentCharacteristicsPanel chPanel = new MapEquipmentCharacteristicsPanel();

	MapEquipmentNodeElement mapequipment;

	public JTabbedPane tabbedPane = new JTabbedPane();

	public MapEquipmentPane()
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

	public MapEquipmentPane(MapEquipmentNodeElement mapequipment)
	{
		this();
		setObjectResource(mapequipment);
	}
	
	private void jbInit() throws Exception
	{
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(JTabbedPane.TOP);

		tabbedPane.add(gPanel.getName(), gPanel);
		tabbedPane.add(aPanel.getName(), aPanel);
//		tabbedPane.add(pPanel.getName(), pPanel);
//		tabbedPane.add(caPanel.getName(), caPanel);
//		tabbedPane.add(lPanel.getName(), lPanel);
//		tabbedPane.add(cmPanel.getName(), cmPanel);
//		tabbedPane.add(chPanel.getName(), chPanel);
	}

	public ObjectResource getObjectResource()
	{
		return mapequipment;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.mapequipment = (MapEquipmentNodeElement )or;

//		System.out.println("set prop pane to " + mapequipment.name);

		gPanel.setObjectResource(mapequipment);
		aPanel.setObjectResource(mapequipment);
//		lPanel.setObjectResource(mapequipment);
//		cmPanel.setObjectResource(mapequipment);
//		pPanel.setObjectResource(mapequipment);
//		caPanel.setObjectResource(mapequipment);
//		chPanel.setObjectResource(mapequipment);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		gPanel.setContext(aContext);
		aPanel.setContext(aContext);
//		lPanel.setContext(aContext);
//		cmPanel.setContext(aContext);
//		pPanel.setContext(aContext);
//		caPanel.setContext(aContext);
//		chPanel.setContext(aContext);
	}

	public boolean modify()
	{
		gPanel.modify();
		aPanel.modify();
//		lPanel.modify();
//		cmPanel.modify();
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