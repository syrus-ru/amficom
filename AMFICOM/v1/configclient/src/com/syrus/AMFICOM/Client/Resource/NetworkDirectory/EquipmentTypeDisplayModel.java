package com.syrus.AMFICOM.Client.Resource.NetworkDirectory;

import java.util.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

public class EquipmentTypeDisplayModel extends StubDisplayModel
{
	ApplicationContext aContext = new ApplicationContext();
	List cols = new ArrayList();

	public EquipmentTypeDisplayModel()
	{
		this(new ApplicationContext());
	}

	public EquipmentTypeDisplayModel(ApplicationContext aContext)
	{
		super();
		this.aContext = aContext;

		cols.add("name");
		cols.add("eq_class_id");
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
	 if(col_id.equals("eq_class_id"))
		return LangModelConfig.getString("port_class");
	 return "";
	}

	public int getColumnSize(String col_id)
	{
	 if(col_id.equals("id"))
		return 100;
	 if(col_id.equals("name"))
		return 100;
	 if(col_id.equals("eq_class_id"))
		return 100;
	 return 100;
	}

	public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
	{
	 EquipmentType eqType = (EquipmentType)or;
	 if(col_id.equals("id"))
		return new TextFieldEditor(eqType.getId());
	 if(col_id.equals("name"))
		return new TextFieldEditor(eqType.getName());
	 if(col_id.equals("eq_class_id"))
		return new TextFieldEditor(eqType.eqClass);
	 return null;
	}

	public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
	{
	 return (PropertyEditor )getColumnRenderer(or, col_id);
	}
}