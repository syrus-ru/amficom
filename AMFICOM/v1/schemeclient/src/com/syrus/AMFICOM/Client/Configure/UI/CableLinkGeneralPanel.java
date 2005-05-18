package com.syrus.AMFICOM.Client.Configure.UI;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.client_.general.ui_.ObjListModel;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.SchemeCableLink;

public class CableLinkGeneralPanel extends AbstractLinkGeneralPanel
{
	protected CableLinkGeneralPanel()
	{
		super();
		typeBox.setModel(new ObjListModel(CableLinkTypeController.getInstance(),
				StorableObjectWrapper.COLUMN_NAME));
	}

	protected CableLinkGeneralPanel(SchemeCableLink link)
	{
		super(link);
		typeBox.setModel(new ObjListModel(CableLinkTypeController.getInstance(),
				StorableObjectWrapper.COLUMN_NAME));
	}

	public void setObject(Object or)
	{
		super.setObject(or);

		try {
			EquivalentCondition condition = new EquivalentCondition(ObjectEntities.CABLELINKTYPE_ENTITY_CODE);
			List clTypes = new ArrayList(StorableObjectPool.getStorableObjectsByCondition(condition, true));
			typeBox.setModel(new ObjListModel(
					CableLinkTypeController.getInstance(),
					clTypes,
					StorableObjectWrapper.COLUMN_NAME));
			typeBox.setSelectedItem(((SchemeCableLink)link).getCableLinkType().getTransferable());
		}
		catch (ApplicationException ex) {
		}
	}

	public boolean modify()
	{
		if (super.modify())
		{
			((SchemeCableLink)link).setCableLinkType((CableLinkType)typeBox.getSelectedItem());
			return true;
		}
		return false;
	}

	protected void typeBox_actionPerformed()
	{
		CableLinkType type = (CableLinkType)typeBox.getSelectedItem();
		manufacturerField.setText(type.getManufacturer());
		manufacturerCodeField.setText(type.getManufacturerCode());
	}

}
