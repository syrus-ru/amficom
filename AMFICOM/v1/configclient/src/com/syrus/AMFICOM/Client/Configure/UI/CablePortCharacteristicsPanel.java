package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Network.CablePort;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

public class CablePortCharacteristicsPanel extends GeneralPanel
{
	CablePort port;

	CharacteristicsPanel charPane = new CharacteristicsPanel();

	public CablePortCharacteristicsPanel()
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

	public CablePortCharacteristicsPanel(CablePort p)
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
		setName(LangModelConfig.String("label_chars"));

		this.setLayout(new BorderLayout());
		this.add(charPane, BorderLayout.CENTER);
	}

	public ObjectResource getObjectResource()
	{
		return port;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.port = (CablePort )or;

		if(port == null)
			return true;

		charPane.setCharHash(port);
		return true;
	}

	public boolean modify()
	{
		return true;
	}
}