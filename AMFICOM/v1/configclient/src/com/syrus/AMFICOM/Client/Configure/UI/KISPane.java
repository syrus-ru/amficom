package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import java.util.Enumeration;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JButton;

import oracle.jdeveloper.layout.XYConstraints;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.General.UI.MessageBox;
import com.syrus.AMFICOM.Client.General.Checker;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

import com.syrus.AMFICOM.Client.Resource.Network.Equipment;
import com.syrus.AMFICOM.Client.Resource.Network.Port;
import com.syrus.AMFICOM.Client.Resource.Network.CablePort;
import com.syrus.AMFICOM.Client.Resource.ISM.KIS;
import com.syrus.AMFICOM.Client.Resource.ISM.AccessPort;


public class KISPane extends PropertiesPanel
{
	public ApplicationContext aContext;

	KISGeneralPanel gPanel = new KISGeneralPanel();
	KISGeneralPanelAdd gaPanel = new KISGeneralPanelAdd();
	KISPortsPanel pPanel = new KISPortsPanel();
	KISCablePortsPanel cpPanel = new KISCablePortsPanel();
	KISCharacteristicsPanel chPanel = new KISCharacteristicsPanel();

	KIS equipment;

	public JTabbedPane tabbedPane = new JTabbedPane();

	public KISPane()
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

	public KISPane(KIS equipment)
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

	public boolean setObjectResource(ObjectResource or)
	{
		this.equipment = (KIS )or;

		gPanel.setObjectResource(equipment);
		gaPanel.setObjectResource(equipment);
		pPanel.setObjectResource(equipment);
		cpPanel.setObjectResource(equipment);
		chPanel.setObjectResource(equipment);
		return true;
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
				Checker.catalogCMediting))
		{
			return false;
		}

		if(modify())
		{
			DataSourceInterface dataSource = aContext.getDataSourceInterface();
			dataSource.SaveKIS(equipment.getId());
			return true;
		}
		else
		{
			new MessageBox(LangModelConfig.String("err_incorrect_data_input")).show();
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
				Checker.catalogCMediting))
			return false;

		int i = 0;
		String []sa = new String[equipment.access_ports.size()];
		for(Enumeration enum1 = equipment.access_ports.elements(); enum1.hasMoreElements();)
		{
			AccessPort port = (AccessPort )enum1.nextElement();
			sa[i++] = port.getId();
		}
		aContext.getDataSourceInterface().RemoveAccessPorts(sa);

		i = 0;
		String []sp = new String[equipment.ports.size()];
		for(Enumeration enum1 = equipment.ports.elements(); enum1.hasMoreElements();)
		{
			Port port = (Port )enum1.nextElement();
			sp[i++] = port.getId();
		}
		aContext.getDataSourceInterface().RemovePorts(sp);

		i = 0;
		String []sc = new String[equipment.cports.size()];
		for(Enumeration enum1 = equipment.cports.elements(); enum1.hasMoreElements();)
		{
			CablePort port = (CablePort )enum1.nextElement();
			sc[i++] = port.getId();
		}
		aContext.getDataSourceInterface().RemoveCablePorts(sc);

		String []se = new String[1];
		se[0] = equipment.id;
		aContext.getDataSourceInterface().RemoveKISs(se);

		return true;
	}

	public boolean create()
	{
		return false;
	}
}