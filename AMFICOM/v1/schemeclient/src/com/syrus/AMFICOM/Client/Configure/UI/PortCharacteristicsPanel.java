package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePort;

public class PortCharacteristicsPanel extends GeneralPanel
{
	SchemePort port;

	CharacteristicsPanel charPane = new CharacteristicsPanel();

	public PortCharacteristicsPanel()
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

	public PortCharacteristicsPanel(SchemePort port)
	{
		this();
		setObjectResource(port);
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
		this.port = (SchemePort)or;

		if(port.port != null)
			charPane.setCharHash(port.port);
	}

	public boolean modify()
	{
		return true;
	}

}