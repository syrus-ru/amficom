package com.syrus.AMFICOM.Client.Resource.Network;

import java.util.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class EquipmentDisplayModel extends StubDisplayModel
{
	ApplicationContext aContext = new ApplicationContext();
	List cols = new ArrayList();

	public EquipmentDisplayModel()
	{
		this(new ApplicationContext());
	}

	public EquipmentDisplayModel(ApplicationContext aContext)
	{
		super();
		this.aContext = aContext;

		cols.add("name");
		cols.add("type_id");
		cols.add("longitude");
		cols.add("latitude");
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
		if(col_id.equals("longitude"))
			return LangModelConfig.getString("equip_longitude");
		if(col_id.equals("latitude"))
			return LangModelConfig.getString("equip_latitude");
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
		if(col_id.equals("longitude"))
			return 100;
		if(col_id.equals("latitude"))
			return 100;
		return 100;
	}

	public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
	{
		Equipment eq = (Equipment )or;
		if(col_id.equals("id"))
			return new TextFieldEditor(eq.getId());
		if(col_id.equals("name"))
			return new TextFieldEditor(eq.getName());
		if(col_id.equals("type_id"))
			return new TextFieldEditor(Pool.getName("equipmenttype", eq.typeId));
		if(col_id.equals("longitude"))
			return new TextFieldEditor( String.valueOf(eq.longitude) );
		if(col_id.equals("latitude"))
			return new TextFieldEditor( String.valueOf(eq.latitude) );
		return null;
	}

	public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
	{
		return (PropertyEditor )getColumnRenderer(or, col_id);
	}
}
