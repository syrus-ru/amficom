package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;

public class CableLinkCharacteristicsPanel extends GeneralPanel
{
	SchemeCableLink link;

	CharacteristicsPanel charPane = new CharacteristicsPanel();

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

	public CableLinkCharacteristicsPanel(SchemeCableLink link)
	{
		this();
		setObjectResource(link);
	}

	private void jbInit() throws Exception
	{
		setName(LangModelConfig.getString("label_chars"));

		this.setLayout(new BorderLayout());
		this.add(charPane, BorderLayout.CENTER);
	}

	public ObjectResource getObjectResource()
	{
		return link;
	}

	public void setObjectResource(ObjectResource or)
	{
		link = (SchemeCableLink)or;

		if(link.cableLink != null)
			charPane.setCharHash(link.cableLink);
	}

	public boolean modify()
	{
		return true;
	}
}