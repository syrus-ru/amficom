package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;

public class EquipmentCharacteristicsPanel extends GeneralPanel
{
	SchemeElement element;
	Identifier elementId;

	CharacteristicsPanel charPane = new CharacteristicsPanel();

	private static CharacteristicTypeSort[] sorts = new CharacteristicTypeSort[] {
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_ELECTRICAL,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_INTERFACE,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL
	};
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

	public EquipmentCharacteristicsPanel(SchemeElement element)
	{
		this();
		setObject(element);
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
		return element;
	}

	public void setObject(Object or)
	{
		element = (SchemeElement)or;
		elementId = new Identifier(element.id().transferable());

		for (int i = 0; i < sorts.length; i++)
			charPane.setTypeSortMapping(
					sorts[i],
					CharacteristicSort.CHARACTERISTIC_SORT_SCHEMEELEMENT,
					element,
					elementId,
					false);
		charPane.addCharacteristics(element.characteristicsImpl().getValue(), elementId);

		if (element.equipment() != null)
		{
			charPane.setTypeSortMapping(
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
					CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENT,
					element.equipmentImpl(),
					element.equipmentImpl().getId(),
					true);
			charPane.addCharacteristics(element.equipmentImpl().getCharacteristics(), element.equipmentImpl().getId());
		}
	}

	public boolean modify()
	{
		return true;
	}
}
