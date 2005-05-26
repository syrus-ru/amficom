/*-
 * $Id: SchemeCableLinkCharacteristicsPanel.java,v 1.2 2005/05/26 07:40:52 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.scheme.SchemeCableLink;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/05/26 07:40:52 $
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
