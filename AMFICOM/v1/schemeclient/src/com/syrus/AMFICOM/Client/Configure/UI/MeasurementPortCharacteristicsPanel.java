package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.GeneralPanel;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.corba.*;

public class MeasurementPortCharacteristicsPanel extends GeneralPanel
{
	MeasurementPort port;
	CharacteristicsPanel charPane = new CharacteristicsPanel();
	private static CharacteristicTypeSort[] sorts = new CharacteristicTypeSort[] {
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_ELECTRICAL,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_INTERFACE,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL,
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL
		};

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
		setObject(p);
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

	public Object getObject()
	{
		return port;
	}

	public void setObject(Object or)
	{
		this.port = (MeasurementPort)or;

		for (int i = 0; i < sorts.length; i++)
			charPane.setTypeSortMapping(
					sorts[i],
					CharacteristicSort.CHARACTERISTIC_SORT_LINKTYPE,
					port,
					port.getId(),
					false);
		charPane.addCharacteristics(port.getCharacteristics(), port.getId());
	}

	public boolean modify()
	{
		return true;
	}
}