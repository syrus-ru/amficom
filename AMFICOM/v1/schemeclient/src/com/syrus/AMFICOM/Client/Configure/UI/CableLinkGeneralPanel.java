package com.syrus.AMFICOM.Client.Configure.UI;

import java.util.LinkedList;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.client_.general.ui_.ObjListModel;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;

public class CableLinkGeneralPanel extends AbstractLinkGeneralPanel
{
	public CableLinkGeneralPanel()
	{
	}

	public CableLinkGeneralPanel(SchemeCableLink link)
	{
		super(link);
	}

	public void setObject(Object or)
	{
		super.setObject(or);

		try {
			Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
					getAccessIdentifier().domain_id);
			Domain domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(
					domain_id, true);
			DomainCondition condition = new DomainCondition(domain, ObjectEntities.CABLE_LINKTYPE_ENTITY_CODE);
			typeBox.setModel(new ObjListModel(
					LinkTypeController.getInstance(),
					ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true),
					LinkTypeController.KEY_NAME));
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
		LinkType type = (LinkType)typeBox.getSelectedItem();
		manufacturerField.setText(type.getManufacturer());
		manufacturerCodeField.setText(type.getManufacturerCode());
	}

}
