package com.syrus.AMFICOM.Client.Resource.NetworkDirectory;

import java.util.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

public class LinkTypeDisplayModel extends StubDisplayModel
{
	ApplicationContext aContext = new ApplicationContext();
	List cols = new ArrayList();

	public LinkTypeDisplayModel()
	{
	 this(new ApplicationContext());
	}

	public LinkTypeDisplayModel(ApplicationContext aContext)
	{
	 super();
	 this.aContext = aContext;

		cols.add("name");
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
	 return "";
	}

	public int getColumnSize(String col_id)
	{
	 if(col_id.equals("id"))
		return 100;
	 if(col_id.equals("name"))
		return 100;
	 return 100;
	}

	public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
	{
	 LinkType lT = (LinkType)or;
	 if(col_id.equals("id"))
		return new TextFieldEditor(lT.getId());
	 if(col_id.equals("name"))
		return new TextFieldEditor(lT.getName());
	 return null;
	}

	public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
	{
	 return (PropertyEditor )getColumnRenderer(or, col_id);
	}
}

