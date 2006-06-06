/*-
 * $Id: SchemeCableLinkCharacteristicsPanel.java,v 1.18 2006/06/06 12:41:55 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.client_.scheme.SchemePermissionManager;
import com.syrus.AMFICOM.configuration.AbstractLink;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.IdlCharacteristicTypeSort;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.18 $, $Date: 2006/06/06 12:41:55 $
 * @module schemeclient
 */

public class SchemeCableLinkCharacteristicsPanel extends CharacteristicsPanel<SchemeCableLink> {
	protected SchemeCableLink schemeCableLink;

	protected SchemeCableLinkCharacteristicsPanel() {
		super();
	}

	protected SchemeCableLinkCharacteristicsPanel(SchemeCableLink l) {
		this();
		setObject(l);
	}

	public SchemeCableLink getObject() {
		return this.schemeCableLink;
	}
	
	@Override
	protected boolean isEditable() {
		return SchemePermissionManager.isPermitted(SchemePermissionManager.Operation.EDIT);
	}

	public void setObject(SchemeCableLink or) {
		this.schemeCableLink = or;
		super.clear();

		if (this.schemeCableLink != null) {
			try {
				super.setTypeSortMapping(IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
						this.schemeCableLink,
						this.schemeCableLink.getId(), isEditable());
				super.addCharacteristics(this.schemeCableLink.getCharacteristics(true), this.schemeCableLink.getId());
				AbstractLink link = this.schemeCableLink.getAbstractLink();
				if (link != null) {
					for (int i = 0; i < sorts.length; i++)
						super.setTypeSortMapping(sorts[i],
								link,
								link.getId(), isEditable());
					super.addCharacteristics(link.getCharacteristics(true), link.getId());
				} else {
					CableLinkType linkType = this.schemeCableLink.getAbstractLinkType();
					if (linkType != null) {
						for (int i = 0; i < sorts.length; i++)
							super.setTypeSortMapping(sorts[i],
									linkType,
									linkType.getId(), false);
						super.addCharacteristics(linkType.getCharacteristics(true), linkType.getId());
					}
				}
			} catch (ApplicationException e) {
				Log.errorMessage(e);
				showNoSelection();
			}
		} 
		else
			showNoSelection();
	}
}
