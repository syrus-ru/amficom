package com.syrus.AMFICOM.Client.Resource.ISM;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;

public class MonitoredElementModel extends ObjectResourceModel
{
	private MonitoredElement me;
	public MonitoredElementModel(MonitoredElement me)
	{
		super(me);
		this.me = me;
	}

	public String getColumnValue(String col_id)
	{
		if(col_id.equals("id"))
			return me.id;
		if(col_id.equals("name"))
			return me.getName();
		if(col_id.equals("type_id"))
			return LangModel.getString("node" + me.elementType);
		return "";
	}
}

