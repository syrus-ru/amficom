package com.syrus.AMFICOM.Client.Configure.UI;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.administration.*;
import com.syrus.AMFICOM.client_.general.ui_.ObjListModel;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;

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
			Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
					getAccessIdentifier().domain_id);
			Domain domain = (Domain)AdministrationStorableObjectPool.getStorableObject(
					domain_id, true);
			DomainCondition condition = new DomainCondition(domain, ObjectEntities.CABLELINKTYPE_ENTITY_CODE);
			typeBox.setModel(new ObjListModel(
					CableLinkTypeController.getInstance(),
					ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true),
					StorableObjectWrapper.COLUMN_NAME));
			typeBox.setSelectedItem(((SchemeCableLink)link).cableLinkType());
		}
		catch (ApplicationException ex) {
		}
	}

	public boolean modify()
	{
		if (super.modify())
		{
			((SchemeCableLink)link).cableLinkTypeImpl((CableLinkType)typeBox.getSelectedItem());
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
