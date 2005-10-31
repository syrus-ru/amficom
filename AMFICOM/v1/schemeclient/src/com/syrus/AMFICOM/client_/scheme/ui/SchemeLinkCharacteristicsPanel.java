/*-
 * $Id: SchemeLinkCharacteristicsPanel.java,v 1.15 2005/10/31 12:30:28 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.Link;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.15 $, $Date: 2005/10/31 12:30:28 $
 * @module schemeclient
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
		return this.schemeLink;
	}

	public void setObject(Object or) {
		this.schemeLink = (SchemeLink)or;
		super.clear();

		if (this.schemeLink != null) {
			try {
				super.setTypeSortMapping(CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
						this.schemeLink,
						this.schemeLink.getId(), true);
				super.addCharacteristics(this.schemeLink.getCharacteristics(true), this.schemeLink.getId());
				Link link = this.schemeLink.getAbstractLink();
				if (link != null) {
					for (int i = 0; i < sorts.length; i++)
						super.setTypeSortMapping(sorts[i],
								link,
								link.getId(), true);
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
