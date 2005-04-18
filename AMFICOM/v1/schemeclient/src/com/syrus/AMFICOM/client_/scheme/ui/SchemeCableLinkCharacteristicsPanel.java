/*-
 * $Id: SchemeCableLinkCharacteristicsPanel.java,v 1.1 2005/04/18 10:45:17 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client_.general.ui_.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.scheme.SchemeCableLink;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/18 10:45:17 $
 * @module schemeclient_v1
 */

public class SchemeCableLinkCharacteristicsPanel extends CharacteristicsPanel {
	protected SchemeCableLink schemeCableLink;

	protected SchemeCableLinkCharacteristicsPanel() {
		super();
	}

	protected SchemeCableLinkCharacteristicsPanel(SchemeCableLink l) {
		this();
		setObject(l);
	}

	public Object getObject() {
		return schemeCableLink;
	}

	public void setObject(Object or) {
		this.schemeCableLink = (SchemeCableLink)or;
		super.clear();

		if (schemeCableLink != null) {
			super.setTypeSortMapping(CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
					CharacteristicSort.CHARACTERISTIC_SORT_SCHEMECABLELINK, schemeCableLink,
					schemeCableLink.getId(), true);
			super.addCharacteristics(schemeCableLink.getCharacteristics(), schemeCableLink.getId());
			
			Link link = schemeCableLink.getLink();
			if (link != null) {
				for (int i = 0; i < sorts.length; i++)
					super.setTypeSortMapping(sorts[i],
							CharacteristicSort.CHARACTERISTIC_SORT_CABLELINK, link,
							link.getId(), true);
				super.addCharacteristics(link.getCharacteristics(), link.getId());
			}
			else {
				CableLinkType linkType = schemeCableLink.getCableLinkType();
				if (linkType != null) {
					for (int i = 0; i < sorts.length; i++)
						super.setTypeSortMapping(sorts[i],
								CharacteristicSort.CHARACTERISTIC_SORT_CABLELINKTYPE, linkType,
								linkType.getId(), false);
					super.addCharacteristics(linkType.getCharacteristics(), linkType.getId());
				}
			}
		} 
		else
			showNoSelection();
	}
}
