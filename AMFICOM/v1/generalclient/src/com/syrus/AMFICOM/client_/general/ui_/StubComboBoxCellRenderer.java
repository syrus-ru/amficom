package com.syrus.AMFICOM.client_.general.ui_;

import javax.swing.JTable;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * Simple stub of AbstractComboBoxCellRenderer.
 * see {@link com.syrus.AMFICOM.client.general.ui_.AbstractComboBoxCellRenderer}
 * 
 * @version $Revision: 1.2 $, $Date: 2004/08/26 10:26:40 $
 * @author $Author: krupenn $
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
	
	protected void customRendering(	JTable table,
					ObjectResource objectResource,
					ObjectResourceController objectResourceController,
					String key) {
		// nothing

	}
}