package com.syrus.AMFICOM.Client.Map.UI;

import java.awt.datatransfer.DataFlavor;

public class MapDataFlavor extends DataFlavor
{
    public MapDataFlavor(Class representationClass, String humanPresentableName)
	{
		super(representationClass, humanPresentableName);
	}
	
    public boolean isFlavorSerializedObjectType() 
	{
        return false;
    }

}
