package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.PortType;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

public class PortTypeCharacteristicsPanel extends GeneralPanel
{
	PortType portType;

	CharacteristicsPanel charPane = new CharacteristicsPanel();

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

	public PortTypeCharacteristicsPanel(PortType p)
	{
		this();
		setObjectResource(p);
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
		return portType;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.portType = (PortType)or;

		if(portType == null)
			return true;

		charPane.setCharHash(portType);
		return true;
	}

	public boolean modify()
	{
		return true;
	}
}