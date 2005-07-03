/*-
 * $Id: SchemeLinkGeneralPanel.java,v 1.6 2005/06/22 15:05:19 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/06/22 15:05:19 $
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
			EquivalentCondition condition = new EquivalentCondition(ObjectEntities.LINK_TYPE_CODE);
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
			AbstractLink link = schemeLink.getAbstractLink();
			if (linkBox.isSelected()) {
				if (link == null) {
					try {
						Identifier userId = LoginManager.getUserId();
						Identifier domainId = LoginManager.getDomainId();
						final AbstractLinkType type = this.schemeLink.getAbstractLinkType();
						if (this.schemeLink instanceof SchemeLink) {
							link = Link.createInstance(userId, domainId, this.schemeLink.getName(), this.schemeLink.getDescription(), ((SchemeLink) this.schemeLink).getAbstractLinkType(), "", "", "", 0, "");							
						} else if (this.schemeLink instanceof SchemeCableLink) {
							link = CableLink.createInstance(userId, domainId, this.schemeLink.getName(), this.schemeLink.getDescription(), ((SchemeCableLink) this.schemeLink).getAbstractLinkType(), "", "", "", 0, "");							
						} else {
							assert false;
						}
						schemeLink.setAbstractLink(link);
					} catch (CreateObjectException e) {
						Log.errorException(e);
					}
				}
			} else if (link != null) {
				StorableObjectPool.delete(link.getId());
				schemeLink.setAbstractLink(null);
			}
			super.commitChanges();
		}
	}
}
