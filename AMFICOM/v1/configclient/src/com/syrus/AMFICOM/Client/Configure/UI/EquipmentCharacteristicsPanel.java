package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Network.Equipment;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

public class EquipmentCharacteristicsPanel extends GeneralPanel
{
	Equipment equipment;

	CharacteristicsPanel charPane = new CharacteristicsPanel();

	public EquipmentCharacteristicsPanel()
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

	public EquipmentCharacteristicsPanel(Equipment mapequipment)
	{
		this();
		setObjectResource(mapequipment);
	}

	public void setContext(ApplicationContext aContext)
	{
		super.setContext(aContext);
		charPane.setContext(aContext);
	}

	private void jbInit() throws Exception
	{
		setName(LangModelConfig.getString("label_chars"));

		this.setLayout(new BorderLayout());
		this.add(charPane, BorderLayout.CENTER);
	}

	public ObjectResource getObjectResource()
	{
		return equipment;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.equipment = (Equipment )or;

		if(equipment == null)
			return true;

		charPane.setCharHash(equipment);
		return true;
	}

	public boolean modify()
	{
		return true;
	}

}
/*
public class EquipmentCharacteristicsPanel extends GeneralPanel
{
	Equipment equipment;

	ObjectResourcePropertyTablePane charPane = new ObjectResourcePropertyTablePane();

	public EquipmentCharacteristicsPanel()
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
		charPane.initialize(new String[] {"Характеристика", "Значение"}, null);
	}

	public EquipmentCharacteristicsPanel(Equipment mapequipment)
	{
		this();
		setObjectResource(mapequipment);
	}

	private void jbInit() throws Exception
	{
		setName("Характеристики");

		this.setLayout(new BorderLayout());
//		this.setPreferredSize(new Dimension(510, 410));
//		this.setMaximumSize(new Dimension(510, 410));
//		this.setMinimumSize(new Dimension(510, 410));
		this.add(charPane, BorderLayout.CENTER);
//		this.add(charPane, new XYConstraints(10, 20, 395, 430));
	}

	public ObjectResource getObjectResource()
	{
		return equipment;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.equipment = (Equipment )or;

//		System.out.println("set prop pane to " + mapequipment.name);

		if(equipment == null)
			return true;

//		System.out.println("set Char pane to " + equipment.name);
		charPane.setSelected(equipment);
		return true;
	}

	public boolean modify()
	{
		return true;
	}
}
*/