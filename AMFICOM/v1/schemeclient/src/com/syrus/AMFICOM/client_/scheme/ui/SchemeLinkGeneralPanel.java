/*-
 * $Id: SchemeLinkGeneralPanel.java,v 1.7 2005/07/11 12:31:39 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.configuration.AbstractLink;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.7 $, $Date: 2005/07/11 12:31:39 $
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
		
		cmbTypeCombo.removeAllItems();
		if (schemeLink != null) {
			EquivalentCondition condition = new EquivalentCondition(ObjectEntities.LINK_TYPE_CODE);
			try {
				cmbTypeCombo.addElements(StorableObjectPool.getStorableObjectsByCondition(condition, true));
			} catch (ApplicationException e) {
				Log.errorException(e);
			}
			cbLinkBox.setVisible(((SchemeLink)schemeLink).getParentScheme() != null); 
		}
		super.setObject(or);
	}

	public void commitChanges() {
		if (schemeLink != null && MiscUtil.validName(tfNameText.getText())) {
			AbstractLink link = schemeLink.getAbstractLink();
			if (cbLinkBox.isSelected()) {
				if (link == null) {
					try {
						link = SchemeObjectsFactory.createLink();
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
