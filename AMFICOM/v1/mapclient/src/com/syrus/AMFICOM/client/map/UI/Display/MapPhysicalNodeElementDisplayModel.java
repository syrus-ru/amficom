package com.syrus.AMFICOM.Client.Map.UI.Display;

import com.syrus.AMFICOM.Client.Map.Popup.PhysicalNodeElementPopupMenu;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.PropertyEditor;
import com.syrus.AMFICOM.Client.General.UI.PropertyRenderer;
import com.syrus.AMFICOM.Client.General.UI.StubDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.TextFieldEditor;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

//A0A
public class MapPhysicalNodeElementDisplayModel extends StubDisplayModel
{
	ApplicationContext aContext;

	public MapPhysicalNodeElementDisplayModel()
	{
		this(new ApplicationContext());
	}

	public MapPhysicalNodeElementDisplayModel(ApplicationContext aContext)
	{
		super();
		this.aContext = aContext;
	}
	
	List cols = new LinkedList();
	{
		cols.add("id");
		cols.add("name");
	//    cols.put("codename", "Кодовое имя");
	//    cols.put("owner_id", "Владелец");
		cols.add("link_id");
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
			return "Идентификатор";
		if(col_id.equals("name"))
			return "Название";
		if(col_id.equals("codename"))
			return "Кодовое имя";
		if(col_id.equals("owner_id"))
			return "Владелец";
		if(col_id.equals("link_id"))
			return "Линия связи";
		if(col_id.equals("longitude"))
			return "Долгота";
		if(col_id.equals("latitude"))
			return "Широта";
		return "";
	}

	public int getColumnSize(String col_id)
	{
		if(col_id.equals("id"))
			return 100;
		if(col_id.equals("name"))
			return 100;
		if(col_id.equals("codename"))
			return 100;
		if(col_id.equals("owner_id"))
			return 100;
		if(col_id.equals("typ"))
			return 100;
		if(col_id.equals("longitude"))
			return 100;
		if(col_id.equals("latitude"))
			return 100;
		return 100;
	}
/*
	public boolean isColumnEditable(String col_id)
	{
		if (aContext.getApplicationModel().isEnabled("mapActionEditProperties"))
		{
			if(col_id.equals("id"))
				return false;
			if(col_id.equals("name"))
				return true;
			if(col_id.equals("codename"))
				return true;
			if(col_id.equals("owner_id"))
				return false;
			if(col_id.equals("typ"))
				return false;
			if(col_id.equals("longitude"))
				return true;
			if(col_id.equals("latitude"))
				return true;
		}
		return false;
	}
*/
	public PropertyRenderer getColumnRenderer(ObjectResource or, String col_id)
	{
		MapPhysicalNodeElement node = (MapPhysicalNodeElement )or;
		if(col_id.equals("id"))
			return new TextFieldEditor(node.getId());
		if(col_id.equals("name"))
			return new TextFieldEditor(node.name);
		if(col_id.equals("link_id"))
			return new TextFieldEditor( Pool.getName(MapPhysicalLinkElement.typ, node.getPhysicalLinkID()));
		if(col_id.equals("typ"))
			return new TextFieldEditor( node.getTyp() );
		if(col_id.equals("longitude"))
			return new TextFieldEditor( String.valueOf(MiscUtil.fourdigits(node.getAnchor().x)) );
		if(col_id.equals("latitude"))
			return new TextFieldEditor( String.valueOf(MiscUtil.fourdigits(node.getAnchor().y)) );
		return null;
	}

	public PropertyEditor getColumnEditor(ObjectResource or, String col_id)
	{
		return (PropertyEditor )getColumnRenderer(or, col_id);
	}
}
