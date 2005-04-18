/*-
 * $Id: LinkTypeGeneralPanel.java,v 1.1 2005/04/18 10:45:17 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/18 10:45:17 $
 * @module schemeclient_v1
 */

public class LinkTypeGeneralPanel extends AbstractLinkTypeGeneralPanel {
	protected LinkTypeGeneralPanel(LinkType linkType) {
		super(linkType);
	}
	
	protected LinkTypeGeneralPanel() {
		super();
	}

	public void commitChanges() {
		if (MiscUtil.validName(tfNameText.getText())) {
			if (linkType == null) {
				try {
					linkType = SchemeObjectsFactory.createLinkType(tfNameText.getText());
					aContext.getDispatcher().notify(new SchemeEvent(this, linkType, SchemeEvent.CREATE_OBJECT));
				} 
				catch (CreateObjectException e) {
					Log.errorException(e);
					return;
				}
			}
			super.commitChanges();
		}
	}
}
