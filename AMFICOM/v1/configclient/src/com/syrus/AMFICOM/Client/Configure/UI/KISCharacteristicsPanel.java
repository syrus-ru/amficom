package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ISM.KIS;
import com.syrus.AMFICOM.Client.General.UI.GeneralPanel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

public class KISCharacteristicsPanel extends GeneralPanel
{
	KIS equipment;

	CharacteristicsPanel charPane = new CharacteristicsPanel();

	public KISCharacteristicsPanel()
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

	public KISCharacteristicsPanel(KIS mapequipment)
	{
		this();
		setObjectResource(mapequipment);
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
		return equipment;
	}

	public boolean setObjectResource(ObjectResource or)
	{
		this.equipment = (KIS )or;

		if(equipment == null)
			return true;

		charPane.setCharHash(equipment);
		return true;
	}

	public boolean modify()
	{
		return true;
	}
}