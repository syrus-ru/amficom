package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeLink;

public class LinkCharacteristicsPanel extends GeneralPanel
{
	SchemeLink link;

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

	public LinkCharacteristicsPanel(SchemeLink l)
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

	public void setObjectResource(ObjectResource or)
	{
		link = (SchemeLink)or;

		if(link.link != null)
			charPane.setCharHash(link.link);
	}

	public boolean modify()
	{
		return true;
	}
}