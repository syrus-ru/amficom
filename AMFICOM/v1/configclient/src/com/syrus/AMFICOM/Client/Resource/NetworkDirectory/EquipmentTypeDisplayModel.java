package com.syrus.AMFICOM.Client.Resource.NetworkDirectory;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.General.UI.PropertyEditor;
import com.syrus.AMFICOM.Client.General.UI.PropertyRenderer;
import com.syrus.AMFICOM.Client.General.UI.StubDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.TextFieldEditor;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

import java.util.Vector;

public class EquipmentTypeDisplayModel extends StubDisplayModel
{
  ApplicationContext aContext = new ApplicationContext();

  public EquipmentTypeDisplayModel()
  {
    this(new ApplicationContext());
  }

  public EquipmentTypeDisplayModel(ApplicationContext aContext)
  {
    super();
    this.aContext = aContext;
  }

  public Vector getColumns()
  {
    Vector cols = new Vector();
//		cols.add("id");
    cols.add("name");
    cols.add("eq_class_id");
    return cols;
  }

  public String getColumnName(String col_id)
  {
    if(col_id.equals("id"))
      return LangModelConfig.String("label_id");
    if(col_id.equals("name"))
      return LangModelConfig.String("label_name");
    if(col_id.equals("eq_class_id"))
      return LangModelConfig.String("port_class");
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
      return new TextFieldEditor(eqType.eq_class);
    return null;
  }

  public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
  {
    return (PropertyEditor )getColumnRenderer(or, col_id);
  }
}