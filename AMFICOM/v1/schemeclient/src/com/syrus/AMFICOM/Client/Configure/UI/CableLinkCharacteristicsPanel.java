package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;
import java.util.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.GeneralPanel;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.*;

public class CableLinkCharacteristicsPanel extends GeneralPanel
{
	SchemeCableLink link;
	Identifier linkId;

	CharacteristicsPanel charPane;

	private static CharacteristicTypeSort[] sorts = new CharacteristicTypeSort[] {
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_ELECTRICAL,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_INTERFACE,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL
	};

	public CableLinkCharacteristicsPanel()
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

	public CableLinkCharacteristicsPanel(SchemeCableLink l)
	{
		this();
		setObject(l);
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
		return link;
	}

	public void setObject(Object or)
	{
		link = (SchemeCableLink)or;
		linkId = new Identifier(link.id().transferable());

		for (int i = 0; i < sorts.length; i++)
			charPane.setTypeSortMapping(
					sorts[i],
					CharacteristicSort.CHARACTERISTIC_SORT_SCHEMECABLELINK,
					linkId,
					false);
		charPane.addCharacteristics(link.characteristicsImpl().getValue(), linkId);

		if (link.link() != null)
		{
			charPane.setTypeSortMapping(
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
					CharacteristicSort.CHARACTERISTIC_SORT_CABLELINK,
					link.linkImpl().getId(),
					true);
			charPane.addCharacteristics(link.linkImpl().getCharacteristics(), link.linkImpl().getId());
		}
	}

	public boolean modify()
	{
		return true;
	}
}
