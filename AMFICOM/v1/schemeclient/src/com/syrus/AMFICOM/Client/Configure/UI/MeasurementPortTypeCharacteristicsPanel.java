package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.MeasurementPortType;

public class MeasurementPortTypeCharacteristicsPanel extends GeneralPanel
{
	MeasurementPortType portType;
	CharacteristicsPanel charPane = new CharacteristicsPanel();

	public MeasurementPortTypeCharacteristicsPanel()
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

	public MeasurementPortTypeCharacteristicsPanel(MeasurementPortType pType)
	{
		this();
		setObjectResource(pType);
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

	public void setObjectResource(ObjectResource or)
	{
		this.portType = (MeasurementPortType)or;

		if(portType != null)
			charPane.setCharHash(portType);
	}

	public boolean modify()
	{
		return true;
	}
}