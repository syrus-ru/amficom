package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.GeneralPanel;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.scheme.corba.SchemeLink;

public class LinkCharacteristicsPanel extends GeneralPanel
{
	protected SchemeLink link;
	protected Identifier linkId;
	private static CharacteristicTypeSort[] sorts = new CharacteristicTypeSort[] {
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_ELECTRICAL,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_INTERFACE,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL
	};

	private CharacteristicsPanel charPane;

	protected LinkCharacteristicsPanel()
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

	protected LinkCharacteristicsPanel(SchemeLink l)
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
		link = (SchemeLink)or;
		linkId = new Identifier(link.getId().getTransferable());
		charPane.clear();
		
		for (int i = 0; i < sorts.length; i++)
			charPane.setTypeSortMapping(
					sorts[i],
					CharacteristicSort.CHARACTERISTIC_SORT_SCHEMELINK,
					link,
					linkId,
					true);
		charPane.addCharacteristics(link.characteristicsImpl().getValue(), linkId);

		if (link.link() != null)
		{
			charPane.setTypeSortMapping(
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
					CharacteristicSort.CHARACTERISTIC_SORT_LINK,
					link.linkImpl(),
					link.linkImpl().getId(),
					true);
			charPane.addCharacteristics(link.linkImpl().getCharacteristics(), link.linkImpl().getId());
		}
	}

	public boolean modify() {
		return charPane.modify();
	}
	
	public boolean save() {
		return charPane.save();
	}
}