package com.syrus.AMFICOM.Client.Configure.UI;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

import java.awt.BorderLayout;

public class EquipmentTypeCharacteristicsPanel extends GeneralPanel
{
	EquipmentType equipmentType = null;
	CharacteristicsPanel charPane = new CharacteristicsPanel();

	public EquipmentTypeCharacteristicsPanel()
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

	public EquipmentTypeCharacteristicsPanel(EquipmentType eq)
	{
		this();
		setObjectResource(eq);
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
		return equipmentType;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.equipmentType = (EquipmentType) or;

		if(equipmentType != null)
			charPane.setCharHash(equipmentType);
	}

	public boolean modify()
	{
		return true;
	}
}
