package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.*;

public class EquipmentTypePane extends PropertiesPanel
{
	public ApplicationContext aContext;

	EquipmentTypeGeneralPanel gPanel = new EquipmentTypeGeneralPanel();
	EquipmentTypeCharacteristicsPanel chPanel = new EquipmentTypeCharacteristicsPanel();

	EquipmentType eq;

	public JTabbedPane tabbedPane = new JTabbedPane();

	public EquipmentTypePane()
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

	public EquipmentTypePane(ProtoElement pe)
	{
		this();
		setObjectResource(pe);
	}

	private void jbInit() throws Exception
	{
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(JTabbedPane.TOP);

		tabbedPane.add(gPanel.getName(), gPanel);
		tabbedPane.add(chPanel.getName(), chPanel);
	}

	public ObjectResource getObjectResource()
	{
		return eq;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		if (or instanceof EquipmentType)
			this.eq = (EquipmentType)or;
		else if (or instanceof ProtoElement)
			this.eq = (EquipmentType)Pool.get(EquipmentType.typ, ((ProtoElement)or).equipment_type_id);

		gPanel.setObjectResource(eq);
		chPanel.setObjectResource(eq);
		return true;
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		gPanel.setContext(aContext);
		chPanel.setContext(aContext);
	}

	public boolean modify()
	{
		if (gPanel.modify() &&
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
		String[] s = new String[1];
		s[0] = eq.getId();
			dataSource.SaveEquipmentTypes(s);
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
/*		if(!Checker.checkCommandByUserId(
				aContext.getSessionInterface().getUserId(),
				Checker.catalogTCediting))
			return false;

		int i = 0;

		if(equipmentType instanceof KIS)
		{
			KIS kis = (KIS )equipment;
			String []sa = new String[kis.access_ports.size()];
			for(Enumeration enum1 = kis.access_ports.elements(); enum1.hasMoreElements();)
			{
				AccessPort port = (AccessPort )enum1.nextElement();
				sa[i++] = port.getId();
			}
			aContext.getDataSourceInterface().RemoveAccessPorts(sa);
		}

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
		aContext.getDataSourceInterface().RemoveEquipments(se);
*/
		return true;
	}

	public boolean create()
	{
		return false;
	}
}