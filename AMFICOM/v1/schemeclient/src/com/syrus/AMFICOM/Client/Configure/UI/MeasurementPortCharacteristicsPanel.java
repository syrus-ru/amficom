package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ISM.MeasurementPort;

public class MeasurementPortCharacteristicsPanel extends GeneralPanel
{
	MeasurementPort port;

	CharacteristicsPanel charPane = new CharacteristicsPanel();

	public MeasurementPortCharacteristicsPanel()
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

	public MeasurementPortCharacteristicsPanel(MeasurementPort p)
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
		this.port = (MeasurementPort)or;

		if(port != null)
			charPane.setCharHash(port);
	}

	public boolean modify()
	{
		return true;
	}
}