package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.CablePort;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class CablePortCharacteristicsPanel extends GeneralPanel
{
	SchemeCablePort port;

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

	public CablePortCharacteristicsPanel(SchemeCablePort p)
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
		return port;
	}

	public void setObjectResource(ObjectResource or)
	{
		port = (SchemeCablePort)or;

		if(port.cablePort != null)
			charPane.setCharHash(port.cablePort);
	}

	public boolean modify()
	{
		return true;
	}
}