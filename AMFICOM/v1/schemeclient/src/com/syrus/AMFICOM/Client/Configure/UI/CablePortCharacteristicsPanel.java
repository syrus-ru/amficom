package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.GeneralPanel;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.corba.CharacteristicTypeSort;
import com.syrus.AMFICOM.scheme.corba.SchemeCablePort;

public class CablePortCharacteristicsPanel extends GeneralPanel
{
	protected SchemeCablePort port;
	protected Identifier portId;
	private static CharacteristicTypeSort[] sorts = new CharacteristicTypeSort[] {
		CharacteristicTypeSort.CHARACTERISTICTYPESORT_ELECTRICAL,
		CharacteristicTypeSort.CHARACTERISTICTYPESORT_INTERFACE,
		CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL,
		CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL
	};

	private CharacteristicsPanel charPane;

	protected CablePortCharacteristicsPanel()
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

	protected CablePortCharacteristicsPanel(SchemeCablePort p)
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
		port = (SchemeCablePort)or;
		portId = port.getId();
		charPane.clear();

		for (int i = 0; i < sorts.length; i++)
			charPane.setTypeSortMapping(
					sorts[i],
					CharacteristicSort.CHARACTERISTIC_SORT_SCHEMECABLEPORT,
					port,
					portId,
					true);
		
		charPane.addCharacteristics(port.characteristicsImpl().getValue(), portId);

		if (port.port() != null)
		{
			charPane.setTypeSortMapping(
					CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
					CharacteristicSort.CHARACTERISTIC_SORT_CABLEPORT,
					port.portImpl(),
					port.portImpl().getId(),
					true);
			charPane.addCharacteristics(port.portImpl().getCharacteristics(), port.portImpl().getId());
		}
	}

	public boolean modify() {
		return charPane.modify();
	}
	
	public boolean save() {
		return charPane.save();
	}
}
