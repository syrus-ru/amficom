package com.syrus.AMFICOM.Client.Resource.Network;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.UI.PropertyEditor;
import com.syrus.AMFICOM.Client.General.UI.PropertyRenderer;
import com.syrus.AMFICOM.Client.General.UI.StubDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.TextFieldEditor;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.util.Vector;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

public class PortDisplayModel extends StubDisplayModel
{
	ApplicationContext aContext = new ApplicationContext();

	public PortDisplayModel()
	{
		this(new ApplicationContext());
	}

	public PortDisplayModel(ApplicationContext aContext)
	{
		super();
		this.aContext = aContext;
	}

	public Vector getColumns()
	{
		Vector cols = new Vector();
//		cols.add("id");
		cols.add("name");
		cols.add("type_id");
		cols.add("equipment_id");
		return cols;
	}

	public String getColumnName(String col_id)
	{
		if(col_id.equals("id"))
			return LangModelConfig.String("label_id");
		if(col_id.equals("name"))
			return LangModelConfig.String("label_name");
		if(col_id.equals("type_id"))
			return LangModelConfig.String("label_type");
		if(col_id.equals("equipment_id"))
			return LangModelConfig.String("menuNetCatEquipmentText");
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
		Port p = (Port )or;
		if(col_id.equals("id"))
		  return new TextFieldEditor(p.getId());
		if(col_id.equals("name"))
		  return new TextFieldEditor(p.getName());
		if(col_id.equals("type_id"))
		  return new TextFieldEditor(Pool.getName("porttype", p.type_id));
		if(col_id.equals("equipment_id"))
		  return new TextFieldEditor(Pool.getName("kisequipment", p.equipment_id));
		return null;
	}

	public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
	{
		return (PropertyEditor )getColumnRenderer(or, col_id);
	}
}
