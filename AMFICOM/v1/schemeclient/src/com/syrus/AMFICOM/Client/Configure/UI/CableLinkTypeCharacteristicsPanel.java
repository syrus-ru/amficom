package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.general.corba.*;

public class CableLinkTypeCharacteristicsPanel extends CharacteristicsPanel
{
	protected CableLinkType type;
	private static CharacteristicTypeSort[] sorts = new CharacteristicTypeSort[] {
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_ELECTRICAL,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_INTERFACE,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL
	};

	private CharacteristicsPanel charPane;

	protected CableLinkTypeCharacteristicsPanel()
	{
		super();

		try {
			jbInit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected CableLinkTypeCharacteristicsPanel(CableLinkType l)
	{
		this();
		setObject(l);
	}

	private void jbInit() throws Exception
	{
		charPane = new CharacteristicsPanel();
		setName(LangModelConfig.getString("label_chars"));

		this.setLayout(new BorderLayout());
		this.add(charPane, BorderLayout.CENTER);
	}

	public Object getObject()
	{
		return type;
	}

	public void setContext(ApplicationContext aContext)
	{
		super.setContext(aContext);
		charPane.setContext(aContext);
	}

	public void setObject(Object or)
	{
		this.type = (CableLinkType)or;
		charPane.clear();
		
		for (int i = 0; i < sorts.length; i++)
			charPane.setTypeSortMapping(
					sorts[i],
					CharacteristicSort.CHARACTERISTIC_SORT_CABLELINKTYPE,
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
