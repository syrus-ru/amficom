/*-
 * $Id: SchemeCableLinkCharacteristicsPanel.java,v 1.11 2005/08/08 11:58:07 arseniy Exp $
 *
 * Copyright � 2005 Syrus Systems.
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
 * @author $Author: arseniy $
 * @version $Revision: 1.11 $, $Date: 2005/08/08 11:58:07 $
 * @module schemeclient
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
		return this.schemeCableLink;
	}

	public void setObject(Object or) {
		this.schemeCableLink = (SchemeCableLink)or;
		super.clear();

		if (this.schemeCableLink != null) {
			try {
				super.setTypeSortMapping(CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
						this.schemeCableLink,
						this.schemeCableLink.getId(), true);
				super.addCharacteristics(this.schemeCableLink.getCharacteristics(), this.schemeCableLink.getId());
				AbstractLink link = this.schemeCableLink.getAbstractLink();
				if (link != null) {
					for (int i = 0; i < sorts.length; i++)
						super.setTypeSortMapping(sorts[i],
								link,
								link.getId(), true);
					super.addCharacteristics(link.getCharacteristics(), link.getId());
				} else {
					CableLinkType linkType = this.schemeCableLink.getAbstractLinkType();
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
