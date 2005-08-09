/*
 * $Id: CableLinkTypeCharacteristicsPanel.java,v 1.10 2005/08/09 06:52:40 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.util.Log;


/**
 * @author $Author: stas $
 * @version $Revision: 1.10 $, $Date: 2005/08/09 06:52:40 $
 * @module schemeclient
 */

public class CableLinkTypeCharacteristicsPanel extends CharacteristicsPanel {
	protected CableLinkType type;

	protected CableLinkTypeCharacteristicsPanel() {
		super();
	}

	protected CableLinkTypeCharacteristicsPanel(CableLinkType l) {
		this();
		setObject(l);
	}

	public Object getObject() {
		return this.type;
	}

	public void setObject(Object or) {
		this.type = (CableLinkType) or;
		super.clear();
		
		if (this.type != null) {
			try {
				for (int i = 0; i < sorts.length; i++)
					super.setTypeSortMapping(sorts[i],
							this.type,
							this.type.getId(), true);
				super.addCharacteristics(this.type.getCharacteristics(true), this.type.getId());
			} catch (ApplicationException e) {
				Log.errorException(e);
				showNoSelection();
			}
		} else
			showNoSelection();
	}
}
