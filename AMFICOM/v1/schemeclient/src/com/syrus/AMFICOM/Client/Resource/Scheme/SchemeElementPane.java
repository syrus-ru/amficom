package com.syrus.AMFICOM.Client.Resource.Scheme;

import com.syrus.AMFICOM.Client.Configure.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.Equipment;

public class SchemeElementPane extends EquipmentPane
{
	SchemeElement se;

	public SchemeElementPane()
	{
	}

	public SchemeElementPane(SchemeElement se)
	{
		super();
		setObjectResource(se);
	}

	public ObjectResource getObjectResource()
	{
		return se;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.se = (SchemeElement )or;

		Equipment eq = (Equipment )Pool.get(Equipment.typ, se.equipment_id);
		super.setObjectResource(eq);

		if(eq != null)
			return true;
		return false;
	}
/*
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
			for(Enumeration enum1 = kis.access_ports.elements(); enum1.hasMoreElements();)
			{
				AccessPort port = (AccessPort) enum1.nextElement();
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

		return true;
	}

	public boolean create()
	{
		return false;
	}
*/
}

