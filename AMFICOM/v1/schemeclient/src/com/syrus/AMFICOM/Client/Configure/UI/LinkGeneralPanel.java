package com.syrus.AMFICOM.Client.Configure.UI;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.administration.*;
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
			Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
					getAccessIdentifier().domain_id);
			Domain domain = (Domain)AdministrationStorableObjectPool.getStorableObject(
					domain_id, true);
			DomainCondition condition = new DomainCondition(domain, ObjectEntities.LINKTYPE_ENTITY_CODE);
			typeBox.setModel(new ObjListModel(
					LinkTypeController.getInstance(),
					ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true),
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