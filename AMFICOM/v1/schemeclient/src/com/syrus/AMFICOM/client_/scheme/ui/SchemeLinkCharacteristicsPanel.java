/*-
 * $Id: SchemeLinkCharacteristicsPanel.java,v 1.18 2006/06/06 12:41:55 stas Exp $
 *
 * Copyright ? 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.client_.scheme.SchemePermissionManager;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.IdlCharacteristicTypeSort;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.18 $, $Date: 2006/06/06 12:41:55 $
 * @module schemeclient
 */

public class SchemeLinkCharacteristicsPanel extends CharacteristicsPanel<SchemeLink> {
	protected SchemeLink schemeLink;

	protected SchemeLinkCharacteristicsPanel() {
		super();
	}

	protected SchemeLinkCharacteristicsPanel(SchemeLink l) {
		this();
		setObject(l);
	}

	public SchemeLink getObject() {
		return this.schemeLink;
	}
	
	@Override
	protected boolean isEditable() {
		return SchemePermissionManager.isPermitted(SchemePermissionManager.Operation.EDIT);
	}

	public void setObject(SchemeLink or) {
		this.schemeLink = or;
		super.clear();

		if (this.schemeLink != null) {
			try {
				super.setTypeSortMapping(IdlCharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
						this.schemeLink,
						this.schemeLink.getId(), isEditable());
				super.addCharacteristics(this.schemeLink.getCharacteristics(true), this.schemeLink.getId());
				Link link = this.schemeLink.getAbstractLink();
				if (link != null) {
					for (int i = 0; i < sorts.length; i++)
						super.setTypeSortMapping(sorts[i],
								link,
								link.getId(), isEditable());
					super.addCharacteristics(link.getCharacteristics(true), link.getId());
				} else {
					LinkType linkType = this.schemeLink.getAbstractLinkType();
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
