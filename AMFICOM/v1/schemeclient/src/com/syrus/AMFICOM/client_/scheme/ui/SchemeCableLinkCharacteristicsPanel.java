/*-
 * $Id: SchemeCableLinkCharacteristicsPanel.java,v 1.4 2005/06/04 16:56:20 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.general.corba.CharacteristicType_TransferablePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.scheme.SchemeCableLink;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/06/04 16:56:20 $
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
					schemeCableLink,
					schemeCableLink.getId(), true);
			super.addCharacteristics(schemeCableLink.getCharacteristics(), schemeCableLink.getId());
			
			Link link = schemeCableLink.getLink();
			if (link != null) {
				for (int i = 0; i < sorts.length; i++)
					super.setTypeSortMapping(sorts[i],
							link,
							link.getId(), true);
				super.addCharacteristics(link.getCharacteristics(), link.getId());
			}
			else {
				CableLinkType linkType = schemeCableLink.getCableLinkType();
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
