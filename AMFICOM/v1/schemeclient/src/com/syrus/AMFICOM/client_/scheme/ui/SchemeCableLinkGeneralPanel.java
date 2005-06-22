/*-
 * $Id: SchemeCableLinkGeneralPanel.java,v 1.7 2005/06/22 15:05:19 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2005/06/22 15:05:19 $
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
		this.schemeLink = (SchemeCableLink)or;
		
		typeCombo.removeAllItems();
		if (schemeLink != null) {
			EquivalentCondition condition = new EquivalentCondition(ObjectEntities.CABLELINK_TYPE_CODE);
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
						link = SchemeObjectsFactory.createCableLink();
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
