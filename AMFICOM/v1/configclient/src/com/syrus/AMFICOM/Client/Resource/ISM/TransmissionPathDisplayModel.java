package com.syrus.AMFICOM.Client.Resource.ISM;

import java.util.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

public class TransmissionPathDisplayModel extends StubDisplayModel
{
	ApplicationContext aContext = new ApplicationContext();
	List cols = new ArrayList();

	public TransmissionPathDisplayModel()
	{
		this(new ApplicationContext());
	}

	public TransmissionPathDisplayModel(ApplicationContext aContext)
	{
		super();
		this.aContext = aContext;

		cols.add("name");
//		cols.add("type_id");
		cols.add("KIS_id");
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
		if(col_id.equals("KIS_id"))
			return LangModelConfig.getString("label_KIS");
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
		if(col_id.equals("KIS_id"))
			return 100;
		return 100;
	}

	public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
	{
		TransmissionPath tp = (TransmissionPath )or;
		if(col_id.equals("id"))
			return new TextFieldEditor(tp.getId());
		if(col_id.equals("name"))
			return new TextFieldEditor(tp.getName());
//		if(col_id.equals("type_id"))
//		  return new TextFieldEditor(Pool.getName("pathtype", tp.type_id));
//		if(col_id.equals("KIS_id"))
//			return new TextFieldEditor(Pool.getName("kis", tp.KIS_id));
//		if(col_id.equals("end_equipment_id"))
//		  return new TextFieldEditor(Pool.getName("equipment", tp.end_equipment_id));
		return null;
	}

	public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
	{
		return (PropertyEditor )getColumnRenderer(or, col_id);
	}
}
