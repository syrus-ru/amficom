package com.syrus.AMFICOM.client.general.ui;

import javax.swing.JTable;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/24 06:54:50 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class StubComboBoxCellRenderer extends AbstractComboBoxCellRenderer {

	private static StubComboBoxCellRenderer	instance;

	private StubComboBoxCellRenderer() {
		// empty
	}

	public static StubComboBoxCellRenderer getInstance() {
		if (instance == null)
			instance = new StubComboBoxCellRenderer();
		return instance;
	}

	protected void customRendering(JTable table, ObjectResource objectResource, String colId) {
		// nothing
	}
}