package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.GeneralPanel;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.administration.*;
import com.syrus.AMFICOM.general.corba.*;


public class PortTypeCharacteristicsPanel extends GeneralPanel
{
	PortType type;

	CharacteristicsPanel charPane;

	private static CharacteristicTypeSort[] sorts = new CharacteristicTypeSort[] {
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_ELECTRICAL,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_INTERFACE,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL
	};

	public PortTypeCharacteristicsPanel()
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

	public PortTypeCharacteristicsPanel(PortType type)
	{
		this();
		setObject(type);
	}

	public void setContext(ApplicationContext aContext)
	{
		super.setContext(aContext);
		charPane.setContext(aContext);
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

	public void setObject(Object or)
	{
		this.type = (PortType)or;

		for (int i = 0; i < sorts.length; i++)
			charPane.setTypeSortMapping(
					sorts[i],
					CharacteristicSort.CHARACTERISTIC_SORT_PORTTYPE,
					type,
					type.getId(),
					false);
		charPane.addCharacteristics(type.getCharacteristics(), type.getId());
	}

	public boolean modify()
	{
		return true;
	}
}
