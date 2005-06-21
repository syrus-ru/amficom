/*-
 * $Id: SchemeLinkCharacteristicsPanel.java,v 1.6 2005/06/21 12:52:14 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.scheme.SchemeLink;

/**
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/06/21 12:52:14 $
 * @module schemeclient_v1
 */

public class SchemeLinkCharacteristicsPanel extends CharacteristicsPanel {
	protected SchemeLink schemeLink;

	protected SchemeLinkCharacteristicsPanel() {
		super();
	}

	protected SchemeLinkCharacteristicsPanel(SchemeLink l) {
		this();
		setObject(l);
	}

	public Object getObject() {
		return schemeLink;
	}

	public void setObject(Object or) {
		this.schemeLink = (SchemeLink)or;
		super.clear();

		if (schemeLink != null) {
			super.setTypeSortMapping(CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
					schemeLink,
					schemeLink.getId(), true);
			super.addCharacteristics(schemeLink.getCharacteristics(), schemeLink.getId());
			
			Link link = schemeLink.getLink();
			if (link != null) {
				for (int i = 0; i < sorts.length; i++)
					super.setTypeSortMapping(sorts[i],
							link,
							link.getId(), true);
				super.addCharacteristics(link.getCharacteristics(), link.getId());
			} else {
				LinkType linkType = schemeLink.getLinkType();
				if (linkType != null) {
					for (int i = 0; i < sorts.length; i++)
						super.setTypeSortMapping(sorts[i],
								linkType,
								linkType.getId(), false);
					super.addCharacteristics(linkType.getCharacteristics(), linkType.getId());
				}
			}
		} 
		else
			showNoSelection();
	}
}
