package com.syrus.AMFICOM.Client.Configure.UI;

import java.util.*;

import java.awt.BorderLayout;
import javax.swing.JTabbedPane;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;

public class EquipmentPane extends PropertiesPanel
{
	public ApplicationContext aContext;

	EquipmentGeneralPanel gPanel = new EquipmentGeneralPanel();
	EquipmentGeneralPanelAdd gaPanel = new EquipmentGeneralPanelAdd();
	EquipmentPortsPanel pPanel = new EquipmentPortsPanel();
	EquipmentCablePortsPanel cpPanel = new EquipmentCablePortsPanel();
	EquipmentCharacteristicsPanel chPanel = new EquipmentCharacteristicsPanel();

	Equipment equipment;

	public JTabbedPane tabbedPane = new JTabbedPane();

	public EquipmentPane()
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

	public EquipmentPane(Equipment equipment)
	{
		this();
		setObjectResource(equipment);
	}

	private void jbInit() throws Exception
	{
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(JTabbedPane.TOP);

		tabbedPane.add(gPanel.getName(), gPanel);
		tabbedPane.add(gaPanel.getName(), gaPanel);
		tabbedPane.add(pPanel.getName(), pPanel);
		tabbedPane.add(cpPanel.getName(), cpPanel);
		tabbedPane.add(chPanel.getName(), chPanel);
	}

	public ObjectResource getObjectResource()
	{
		return equipment;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.equipment = (Equipment)or;

		gPanel.setObjectResource(equipment);
		gaPanel.setObjectResource(equipment);
		pPanel.setObjectResource(equipment);
		cpPanel.setObjectResource(equipment);
		chPanel.setObjectResource(equipment);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		gPanel.setContext(aContext);
		gaPanel.setContext(aContext);
		pPanel.setContext(aContext);
		cpPanel.setContext(aContext);
		chPanel.setContext(aContext);
	}

	public boolean modify()
	{
		if(	gPanel.modify() &&
				gaPanel.modify() &&
				pPanel.modify() &&
				cpPanel.modify() &&
				chPanel.modify())
			return true;
		return false;
	}

	public boolean save()
	{
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogTCediting))
		{
			return false;
		}

		if(modify())
		{
			DataSourceInterface dataSource = aContext.getDataSourceInterface();
			dataSource.SaveEquipment(equipment.getId());
			return true;
		}
		else
		{
			new MessageBox(LangModelConfig.getString("err_incorrect_data_input")).show();
		}
		return false;
	}

	public boolean open()
	{
		return false;
	}

	public boolean delete()
	{
		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogTCediting))
			return false;

		int i = 0;

		if(equipment instanceof KIS)
		{
			KIS kis = (KIS) equipment;
			String []sa = new String[kis.access_ports.size()];
			for(Iterator it = kis.access_ports.iterator(); it.hasNext();)
			{
				AccessPort port = (AccessPort)it.next();
				sa[i++] = port.getId();
			}
			aContext.getDataSourceInterface().RemoveAccessPorts(sa);
		}

		i = 0;
		String []sp = new String[equipment.ports.size()];
		for(Iterator it = equipment.ports.iterator(); it.hasNext();)
		{
			Port port = (Port)it.next();
			sp[i++] = port.getId();
		}
		aContext.getDataSourceInterface().RemovePorts(sp);

		i = 0;
		String []sc = new String[equipment.cports.size()];
		for(Iterator it = equipment.cports.iterator(); it.hasNext();)
		{
			CablePort port = (CablePort)it.next();
			sc[i++] = port.getId();
		}
		aContext.getDataSourceInterface().RemoveCablePorts(sc);

		String []se = new String[1];
		se[0] = equipment.id;
		aContext.getDataSourceInterface().RemoveEquipments(se);

		return true;
	}

	public boolean create()
	{
		return false;
	}
}