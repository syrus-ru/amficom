package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Network.CableLink;

import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

public class CableLinkCharacteristicsPanel extends GeneralPanel
{
	CableLink link;
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

	public CableLinkCharacteristicsPanel(CableLink l)
	{
		this();
		setObjectResource(l);
	}

	private void jbInit() throws Exception
	{
		setName(LangModelConfig.String("label_chars"));

		this.setLayout(new BorderLayout());
		this.add(charPane, BorderLayout.CENTER);
	}

	public ObjectResource getObjectResource()
	{
		return link;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.link = (CableLink )or;

		if(link == null)
			return true;

		charPane.setCharHash(link);
		return true;
	}

	public boolean modify()
	{
		return true;
	}
}