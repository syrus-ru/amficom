package com.syrus.AMFICOM.Client.Resource.SchemeDirectory;

import java.util.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;


public class ProtoElementDisplayModel extends StubDisplayModel
{
	ApplicationContext aContext = new ApplicationContext();
	List cols;

	public ProtoElementDisplayModel()
	{
	 this(new ApplicationContext());
	}

 public ProtoElementDisplayModel(ApplicationContext aContext)
 {
	 super();
	 this.aContext = aContext;

	 cols = new ArrayList(2);
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
	 ProtoElement proto = (ProtoElement)or  ;
	 EquipmentType eqType = (EquipmentType)Pool.get(EquipmentType.typ, proto.equipment_type_id);
	 if(col_id.equals("id"))
		return new TextFieldEditor(proto.getId());
	 if(col_id.equals("name"))
		return new TextFieldEditor(proto.getName());
	 if(col_id.equals("eq_class_id"))
		return new TextFieldEditor(eqType.eq_class);
	 return null;
	}

	public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
	{
	 return (PropertyEditor )getColumnRenderer(or, col_id);
	}
}
