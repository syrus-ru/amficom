/*
 * $Id: LinkTypeCharacteristicsPanel.java,v 1.6 2005/07/19 06:49:44 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.client.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2005/07/19 06:49:44 $
 * @module schemeclient_v1
 */

public class LinkTypeCharacteristicsPanel extends CharacteristicsPanel {
	protected LinkType type;

	protected LinkTypeCharacteristicsPanel() {
		super();
	}

	protected LinkTypeCharacteristicsPanel(LinkType l) {
		this();
		setObject(l);
	}

	public Object getObject() {
		return type;
	}

	public void setObject(Object or) {
		this.type = (LinkType) or;
		super.clear();
		
		if (type != null) {
			try {
				for (int i = 0; i < sorts.length; i++)
					super.setTypeSortMapping(sorts[i],
							type,
							type.getId(), true);
				super.addCharacteristics(type.getCharacteristics(), type.getId());
			} catch (ApplicationException e) {
				Log.errorException(e);
				showNoSelection();
			}
		} else
			showNoSelection();
	}
}
