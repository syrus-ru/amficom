package com.syrus.AMFICOM.Client.Schematics.UI;

import java.awt.*;
import com.syrus.AMFICOM.Client.Configure.UI.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.Equipment;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

public class SchemeElementPane extends PropertiesPanel
{
	SchemeElement se;
	boolean is_kis;
	EquipmentPane eqPane = new EquipmentPane();
	KISPane kisPane = new KISPane();

	public SchemeElementPane()
	{
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public SchemeElementPane(SchemeElement se)
	{
		this();
		setObjectResource(se);
	}

	private void jbInit() throws Exception
	{
		this.setLayout(new BorderLayout());
	}

	public ObjectResource getObjectResource()
	{
		return se;
	}

	public void setObjectResource(ObjectResource or)
	{
		this.se = (SchemeElement )or;

		Equipment eq = (Equipment)Pool.get("kisequipment", se.equipment_id);
		if (eq == null)
			return;
		is_kis = eq.is_kis;

		if (is_kis)
		{
			removeAll();
			add(kisPane, BorderLayout.CENTER);
			kisPane.setObjectResource(eq);
		}
		else
		{
			removeAll();
			add(eqPane, BorderLayout.CENTER);
			eqPane.setObjectResource(eq);
		}
	}

	public void setContext(ApplicationContext aContext)
	{
		kisPane.setContext(aContext);
		eqPane.setContext(aContext);
	}

	public boolean modify()
	{
		if (is_kis)
			return kisPane.modify();
		else
			return eqPane.modify();
	}

	public boolean create()
	{
		if (is_kis)
			return kisPane.create();
		else
			return eqPane.create();
	}

	public boolean delete()
	{
		if (is_kis)
			return kisPane.delete();
		else
			return eqPane.delete();
	}

	public boolean open()
	{
		if (is_kis)
			return kisPane.open();
		else
			return eqPane.open();
	}

	public boolean save()
	{
		if (is_kis)
			return kisPane.save();
		else
			return eqPane.save();
	}

	public boolean cancel()
	{
		if (is_kis)
			return kisPane.cancel();
		else
			return eqPane.cancel();
	}
}

