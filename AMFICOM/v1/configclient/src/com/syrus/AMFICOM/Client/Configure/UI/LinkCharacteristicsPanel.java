package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Network.Link;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

public class LinkCharacteristicsPanel extends GeneralPanel
{
	Link link;

	CharacteristicsPanel charPane = new CharacteristicsPanel();

	public LinkCharacteristicsPanel()
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

	public LinkCharacteristicsPanel(Link l)
	{
		this();
		setObjectResource(l);
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

	public ObjectResource getObjectResource()
	{
		return link;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.link = (Link )or;

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