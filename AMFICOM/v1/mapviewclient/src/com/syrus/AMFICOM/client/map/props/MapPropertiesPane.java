package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;

public interface MapPropertiesPane 
{
	LogicalNetLayer getLogicalNetLayer();
	void setLogicalNetLayer(LogicalNetLayer lnl);
}