package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.GeneralPanel;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.scheme.corba.SchemePort;

public class PortCharacteristicsPanel extends GeneralPanel
{
	SchemePort port;
	Identifier portId;

	CharacteristicsPanel charPane;

	private static CharacteristicTypeSort[] sorts = new CharacteristicTypeSort[] {
		CharacteristicTypeSort.CHARACTERISTICTYPESORT_ELECTRICAL,
		CharacteristicTypeSort.CHARACTERISTICTYPESORT_INTERFACE,
		CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL,
		CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL
	};

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

	public PortCharacteristicsPanel(SchemePort p)
	{
		this();
		setObject(p);
	}

	public void setContext(ApplicationContext aContext)
	{
		super.setContext(aContext);
		charPane.setContext(aContext);
	}

	private void jbInit() throws Exception
	{
		charPane = new CharacteristicsPanel();
		setName(LangModelConfig.getString("label_chars"));

		this.setLayout(new BorderLayout());
		this.add(charPane, BorderLayout.CENTER);
	}

	public Object getObject()
	{
		return port;
	}

	public void setObject(Object or)
	{
		port = (SchemePort)or;
		portId = new Identifier(port.id().transferable());

		for (int i = 0; i < sorts.length; i++)
			charPane.setTypeSortMapping(
					sorts[i],
					CharacteristicSort.CHARACTERISTIC_SORT_SCHEMEPORT,
					portId,
					false);
		charPane.addCharacteristics(port.characteristicsImpl().getValue(), portId);

		if (port.port() != null)
		{
			charPane.setTypeSortMapping(
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
					CharacteristicSort.CHARACTERISTIC_SORT_PORT,
					port.portImpl().getId(),
					true);
			charPane.addCharacteristics(port.portImpl().getCharacteristics(), port.portImpl().getId());
		}
	}

	public boolean modify()
	{
		return true;
	}
}