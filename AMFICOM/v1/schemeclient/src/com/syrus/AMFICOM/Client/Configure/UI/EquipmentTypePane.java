package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.scheme.corba.SchemeProtoElement;

public class EquipmentTypePane extends JPanel implements ObjectResourcePropertiesPane
{
	public ApplicationContext aContext;

	EquipmentTypeGeneralPanel gPanel = new EquipmentTypeGeneralPanel();
	EquipmentTypeCharacteristicsPanel chPanel = new EquipmentTypeCharacteristicsPanel();

	SchemeProtoElement proto;
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

	public EquipmentTypePane(SchemeProtoElement pe)
	{
		this();
		setObject(pe);
	}

	private void jbInit() throws Exception
	{
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(JTabbedPane.TOP);

		tabbedPane.add(gPanel.getName(), gPanel);
		tabbedPane.add(chPanel.getName(), chPanel);
	}

	public Object getObject()
	{
		return proto;
	}

	public void setObject(Object or)
	{
		proto = (SchemeProtoElement)or;
		eq = proto.equipmentTypeImpl();

		gPanel.setObject(proto);
		chPanel.setObject(proto);
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
		if(modify())
		{
			try {
				ConfigurationStorableObjectPool.putStorableObject(eq);
				return true;
			}
			catch (ApplicationException ex) {
				ex.printStackTrace();
			}
		}
		JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				LangModelConfig.getString("err_incorrect_data_input"));
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