/*-
 * $Id: SchemeCableLinkGeneralPanel.java,v 1.1 2005/04/18 10:46:57 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.LinkSort;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/18 10:46:57 $
 * @module schemeclient_v1
 */

public class SchemeCableLinkGeneralPanel extends AbstractSchemeLinkGeneralPanel {
	
	protected SchemeCableLinkGeneralPanel(SchemeCableLink schemeLink) {
		super(schemeLink);
	}
	
	protected SchemeCableLinkGeneralPanel () {
		super();
	}

	public void setObject(Object or) {
		this.schemeLink = (SchemeLink)or;
		
		typeCombo.removeAllItems();
		if (schemeLink != null) {
			EquivalentCondition condition = new EquivalentCondition(ObjectEntities.CABLELINKTYPE_ENTITY_CODE);
			try {
				typeCombo.addElements(ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true));
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
						link = SchemeObjectsFactory.createLink(LinkSort.LINKSORT_CABLELINK);
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
