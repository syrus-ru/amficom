package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;

public interface ObjectResourcePropertiesPane 
{
	static final int DEF_HEIGHT = 24;
	static final int DEF_WIDTH = 150;

	ObjectResource getObjectResource();

	void setObjectResource(ObjectResource objectResource);

	void setContext(ApplicationContext aContext);

	boolean modify();
	boolean create();
	boolean delete();
	boolean open();
	boolean save();
	boolean cancel();
}