/*-
 * $Id: SchemeCableLinkCharacteristicsPanel.java,v 1.9 2005/07/19 06:54:18 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.AbstractLink;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.9 $, $Date: 2005/07/19 06:54:18 $
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
			try {
				super.setTypeSortMapping(CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
						schemeCableLink,
						schemeCableLink.getId(), true);
				super.addCharacteristics(schemeCableLink.getCharacteristics(), schemeCableLink.getId());
				AbstractLink link = schemeCableLink.getAbstractLink();
				if (link != null) {
					for (int i = 0; i < sorts.length; i++)
						super.setTypeSortMapping(sorts[i],
								link,
								link.getId(), true);
					super.addCharacteristics(link.getCharacteristics(), link.getId());
				} else {
					CableLinkType linkType = schemeCableLink.getAbstractLinkType();
					if (linkType != null) {
						for (int i = 0; i < sorts.length; i++)
							super.setTypeSortMapping(sorts[i],
									linkType,
									linkType.getId(), false);
						super.addCharacteristics(linkType.getCharacteristics(), linkType.getId());
					}
				}
			} catch (ApplicationException e) {
				Log.errorException(e);
				showNoSelection();
			}
		} 
		else
			showNoSelection();
	}
}
