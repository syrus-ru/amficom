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

public class CableLinkDisplayModel extends StubDisplayModel
{
	ApplicationContext aContext = new ApplicationContext();

	public CableLinkDisplayModel()
	{
		this(new ApplicationContext());
	}

	public CableLinkDisplayModel(ApplicationContext aContext)
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
		cols.add("start_equipment_id");
		cols.add("end_equipment_id");
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
		if(col_id.equals("start_equipment_id"))
			return LangModelConfig.getString("link_start_equipment_id");
		if(col_id.equals("end_equipment_id"))
			return LangModelConfig.getString("link_end_equipment_id");
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
		if(col_id.equals("start_equipment_id"))
			return 100;
		if(col_id.equals("end_equipment_id"))
			return 100;
		return 100;
	}

	public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
	{
		CableLink l = (CableLink )or;
		if(col_id.equals("id"))
		  return new TextFieldEditor(l.getId());
		if(col_id.equals("name"))
		  return new TextFieldEditor(l.getName());
		if(col_id.equals("type_id"))
		  return new TextFieldEditor(Pool.getName("cablelinktype", l.type_id));
		if(col_id.equals("start_equipment_id"))
		  return new TextFieldEditor(Pool.getName("kisequipment", l.start_equipment_id));
		if(col_id.equals("end_equipment_id"))
		  return new TextFieldEditor(Pool.getName("kisequipment", l.end_equipment_id));
		return null;
	}

	public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
	{
		return (PropertyEditor )getColumnRenderer(or, col_id);
	}
}
