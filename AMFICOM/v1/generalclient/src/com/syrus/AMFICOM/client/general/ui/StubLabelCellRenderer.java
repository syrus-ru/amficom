package com.syrus.AMFICOM.client.general.ui;

import javax.swing.JTable;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/24 06:54:50 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class StubLabelCellRenderer extends AbstractLabelCellRenderer {

	private static StubLabelCellRenderer	instance;

	private StubLabelCellRenderer() {
		// empty
	}

	public static StubLabelCellRenderer getInstance() {
		if (instance == null)
			instance = new StubLabelCellRenderer();
		return instance;
	}

	protected void customRendering(JTable table, ObjectResource objectResource, String colId) {
		// nothing
	}
}