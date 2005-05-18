/*-
 * $Id: SchemeLinkGeneralPanel.java,v 1.3 2005/05/18 14:59:44 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.corba.LinkSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/05/18 14:59:44 $
 * @module schemeclient_v1
 */

public class SchemeLinkGeneralPanel extends AbstractSchemeLinkGeneralPanel {
	
	protected SchemeLinkGeneralPanel(SchemeLink schemeLink) {
		super(schemeLink);
	}
	
	protected SchemeLinkGeneralPanel () {
		super();
	}

	public void setObject(Object or) {
		this.schemeLink = (SchemeLink)or;
		
		typeCombo.removeAllItems();
		if (schemeLink != null) {
			EquivalentCondition condition = new EquivalentCondition(ObjectEntities.LINKTYPE_ENTITY_CODE);
			try {
				typeCombo.addElements(StorableObjectPool.getStorableObjectsByCondition(condition, true));
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
		}
		super.setObject(or);
	}

	public void commitChanges() {
		if (schemeLink != null && MiscUtil.validName(nameText.getText())) {
			Link link = schemeLink.getLink();
			if (linkBox.isSelected()) {
				if (link == null) {
					try {
						Identifier userId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
								getAccessIdentifier().user_id);
						Identifier domainId = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
								getAccessIdentifier().domain_id);
						link = Link.createInstance(userId, domainId, schemeLink.getName(), schemeLink.getDescription(), schemeLink.getAbstractLinkType(), "", "", "", LinkSort.LINKSORT_LINK, 0, "");
						schemeLink.setLink(link);
					} catch (CreateObjectException e) {
						Log.errorException(e);
					}
				}
			} else if (link != null) {
				ConfigurationStorableObjectPool.delete(link.getId());
				schemeLink.setLink(null);
			}
			super.commitChanges();
		}
	}
}
