package com.syrus.AMFICOM.Client.Resource.NetworkDirectory;

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

public class CableLinkTypeDisplayModel extends StubDisplayModel
{
  ApplicationContext aContext = new ApplicationContext();

  public CableLinkTypeDisplayModel()
  {
	 this(new ApplicationContext());
  }

  public CableLinkTypeDisplayModel(ApplicationContext aContext)
  {
	 super();
	 this.aContext = aContext;
  }

  public Vector getColumns()
  {
	 Vector cols = new Vector();
//		cols.add("id");
	 cols.add("name");
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
	 CableLinkType lT = (CableLinkType)or;
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

