package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.LinkType;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

public class LinkTypeCharacteristicsPanel extends GeneralPanel
{
	LinkType linkType;
	CharacteristicsPanel charPane = new CharacteristicsPanel();

	public LinkTypeCharacteristicsPanel()
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

	public LinkTypeCharacteristicsPanel(LinkType lT)
	{
		this();
		setObjectResource(lT);
	}

	public void setContext(ApplicationContext aContext)
	{
		super.setContext(aContext);
		charPane.setContext(aContext);
	}

	private void jbInit() throws Exception
	{
		setName(LangModelConfig.String("label_chars"));

		this.setLayout(new BorderLayout());
		this.add(charPane, BorderLayout.CENTER);
	}

	public ObjectResource getObjectResource()
	{
		return linkType;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.linkType = (LinkType)or;
		if(linkType == null)
			return true;

		charPane.setCharHash(linkType);
		return true;
	}

	public boolean modify()
	{
		return true;
	}
}