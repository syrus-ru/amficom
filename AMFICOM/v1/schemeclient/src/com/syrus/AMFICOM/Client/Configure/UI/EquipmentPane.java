package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;

public class EquipmentPane extends JPanel implements ObjectResourcePropertiesPane
{
	public ApplicationContext aContext;

	EquipmentGeneralPanel gPanel = new EquipmentGeneralPanel();
	EquipmentGeneralPanelAdd gaPanel = new EquipmentGeneralPanelAdd();
	EquipmentPortsPanel pPanel = new EquipmentPortsPanel();
	EquipmentCablePortsPanel cpPanel = new EquipmentCablePortsPanel();
	EquipmentCharacteristicsPanel chPanel = new EquipmentCharacteristicsPanel();

	SchemeElement element;

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

	public EquipmentPane(SchemeElement element)
	{
		this();
		setObject(element);
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

	public Object getObject()
	{
		return element;
	}

	public void setObject(Object or)
	{
		element = (SchemeElement)or;

		gPanel.setObject(element);
		gaPanel.setObject(element);
		pPanel.setObject(element);
		cpPanel.setObject(element);
		chPanel.setObject(element);
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
		if(modify())
		{
			if (element.equipment() != null)
			{
				try {
					ConfigurationStorableObjectPool.putStorableObject(element.equipmentImpl());
					return true;
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}

			}
		}
		else
		{
			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					LangModelConfig.getString("err_incorrect_data_input"));
		}
		return false;
	}

	public boolean open()
	{
		return false;
	}

	public boolean cancel()
	{
		return false;
	}

	public boolean delete()
	{
		int i = 0;
/*
		if(kis != null)
		{
			Collection measurementPorts = element.getMeasurementPorts();
			if (measurementPorts.size() != 0)
			{
				String[] sa = new String[measurementPorts.size()];
				Iterator it = measurementPorts.iterator();
				for (int j = 0; j < measurementPorts.size(); j++)
					sa[i] = ((MeasurementPort)it.next()).getId();
				aContext.getDataSourceInterface().RemoveAccessPorts(sa);
			}
		}

		i = 0;
		String []sp = new String[element.getPorts().size()];
		for(Iterator it = element.getPorts().iterator(); it.hasNext();)
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
*/
		return true;
	}

	public boolean create()
	{
		return false;
	}
}