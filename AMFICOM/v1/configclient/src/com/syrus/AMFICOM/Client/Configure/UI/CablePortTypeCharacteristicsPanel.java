package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.CablePortType;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

public class CablePortTypeCharacteristicsPanel extends GeneralPanel
{
	CablePortType portType;

	CharacteristicsPanel charPane = new CharacteristicsPanel();

	public CablePortTypeCharacteristicsPanel()
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

	public CablePortTypeCharacteristicsPanel(CablePortType pT)
	{
		this();
		setObjectResource(pT);
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
		this.portType = (CablePortType)or;

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