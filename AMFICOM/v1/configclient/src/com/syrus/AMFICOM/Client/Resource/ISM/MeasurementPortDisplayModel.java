package com.syrus.AMFICOM.Client.Resource.ISM;

import java.util.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class MeasurementPortDisplayModel extends StubDisplayModel
{
	ApplicationContext aContext = new ApplicationContext();

	static List cols = new ArrayList();

	public MeasurementPortDisplayModel()
	{
		this(new ApplicationContext());
	}

	public MeasurementPortDisplayModel(ApplicationContext aContext)
	{
		super();
		this.aContext = aContext;

		//		cols.add("id");
		cols.add("name");
		cols.add("type_id");
		cols.add("equipment_id");
	}

	public List getColumns()
	{
		return cols;
	}

	public String getColumnName(String col_id)
	{
		if(col_id.equals("id"))
			return LangModelConfig.getString("label_id");
		if(col_id.equals("name"))
			return LangModelConfig.getString("label_name");
		if(col_id.equals("type_id"))
			return LangModelConfig.getString("label_type");
		if(col_id.equals("equipment_id"))
			return LangModelConfig.getString("menuMapSaveText");
		return "";
	}

	public int getColumnSize(String col_id)
	{
		if(col_id.equals("id"))
			return 100;
		if(col_id.equals("name"))
			return 100;
		if(col_id.equals("type_id"))
			return 100;
		if(col_id.equals("equipment_id"))
			return 100;
		return 100;
	}

	public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
	{
		MeasurementPort p = (MeasurementPort)or;
		if(col_id.equals("id"))
			return new TextFieldEditor(p.getId());
		if(col_id.equals("name"))
			return new TextFieldEditor(p.getName());
		if(col_id.equals("type_id"))
			return new TextFieldEditor(Pool.getName("accessporttype", p.typeId));
		if(col_id.equals("equipment_id"))
			return new TextFieldEditor(Pool.getName("kis", p.kisId));
		return null;
	}

	public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
	{
		return (PropertyEditor )getColumnRenderer(or, col_id);
	}
}
