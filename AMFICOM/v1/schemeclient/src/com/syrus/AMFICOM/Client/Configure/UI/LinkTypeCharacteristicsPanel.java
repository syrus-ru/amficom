package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.GeneralPanel;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.general.corba.*;

public class LinkTypeCharacteristicsPanel extends GeneralPanel
{
	protected LinkType type;
	private static CharacteristicTypeSort[] sorts = new CharacteristicTypeSort[] {
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_ELECTRICAL,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_INTERFACE,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL
	};

	private CharacteristicsPanel charPane;

	protected LinkTypeCharacteristicsPanel()
	{
		super();

		try {
			jbInit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected LinkTypeCharacteristicsPanel(LinkType l)
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
		this.type = (LinkType)or;

		for (int i = 0; i < sorts.length; i++)
			charPane.setTypeSortMapping(
					sorts[i],
					CharacteristicSort.CHARACTERISTIC_SORT_LINKTYPE,
					type,
					type.getId(),
					true);
		charPane.addCharacteristics(type.getCharacteristics(), type.getId());
	}

	public boolean modify()
	{
		return true;
	}
}