package com.syrus.AMFICOM.Client.Configure.UI;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.client_.general.ui_.ObjListModel;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.corba.SchemeLink;

public class LinkGeneralPanel extends AbstractLinkGeneralPanel
{
	protected LinkGeneralPanel()
	{
	}

	protected LinkGeneralPanel(SchemeLink link)
	{
		super(link);
	}

	public void setObject(Object or)
	{
		super.setObject(or);

		try {
			EquivalentCondition condition = new EquivalentCondition(ObjectEntities.LINKTYPE_ENTITY_CODE);
			List lTypes = new ArrayList(ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true));
			typeBox.setModel(new ObjListModel(
					LinkTypeController.getInstance(),
					lTypes,
					StorableObjectWrapper.COLUMN_NAME));
			typeBox.setSelectedItem(((SchemeLink)link).linkType());
		}
		catch (ApplicationException ex) {
		}
	}

	public boolean modify()
	{
		if (super.modify())
		{
			((SchemeLink)link).linkTypeImpl((LinkType)typeBox.getSelectedItem());
			return true;
		}
		return false;
	}

	protected void typeBox_actionPerformed()
	{
		LinkType type = (LinkType)typeBox.getSelectedItem();
		manufacturerField.setText(type.getManufacturer());
		manufacturerCodeField.setText(type.getManufacturerCode());
	}

}