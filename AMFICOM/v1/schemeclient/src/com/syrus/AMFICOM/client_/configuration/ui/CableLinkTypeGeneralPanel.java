/*-
 * $Id: CableLinkTypeGeneralPanel.java,v 1.2 2005/04/28 16:02:36 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.configuration.ui;

import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/04/28 16:02:36 $
 * @module schemeclient_v1
 */

public class CableLinkTypeGeneralPanel extends AbstractLinkTypeGeneralPanel {
	protected CableLinkTypeGeneralPanel(CableLinkType linkType) {
		super(linkType);
	}
	
	protected CableLinkTypeGeneralPanel() {
		super();
	}

	public void commitChanges() {
//		CableThreadType ctt; ctt.
		if (MiscUtil.validName(tfNameText.getText())) {
			if (linkType == null) {
				try {
					linkType = SchemeObjectsFactory.createCableLinkType(tfNameText.getText());
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
