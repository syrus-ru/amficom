package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.GeneralPanel;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;

public class EquipmentTypeCharacteristicsPanel extends GeneralPanel
{
	protected EquipmentType type;
	private static CharacteristicTypeSort[] sorts = new CharacteristicTypeSort[] {
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_ELECTRICAL,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_INTERFACE,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL
	};

	private CharacteristicsPanel charPane = new CharacteristicsPanel();

	protected EquipmentTypeCharacteristicsPanel()
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

	protected EquipmentTypeCharacteristicsPanel(EquipmentType eq)
	{
		this();
		setObject(eq);
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

	public Object getObject()
	{
		return type;
	}

	public void setObject(Object or)
	{
		this.type = (EquipmentType) or;
		charPane.clear();
		
		for (int i = 0; i < sorts.length; i++)
			charPane.setTypeSortMapping(
					sorts[i],
					CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENTTYPE,
					type,
					type.getId(),
					true);
		charPane.addCharacteristics(type.getCharacteristics(), type.getId());

	}

	public boolean modify() {
		return charPane.modify();
	}
	
	public boolean save() {
		return charPane.save();
	}
}
