package com.syrus.AMFICOM.Client.Resource;

import java.awt.datatransfer.DataFlavor;

public class MyDataFlavor extends DataFlavor
{
    public MyDataFlavor(Class representationClass, String humanPresentableName)
	{
		super(representationClass, humanPresentableName);
	}
	
    public boolean isFlavorSerializedObjectType() 
	{
        return false;
    }

}
