package com.syrus.AMFICOM.client_.general.ui_;

import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

public interface ObjectResourceCatalogController
{
	ObjectResourcePropertiesPane getPropertiesPane(Class clazz);
	ObjectResourceController getController(Class clazz);
	ObjectResourceFilter getFilter(Class clazz);
}