package com.syrus.AMFICOM.client_.general.ui_;

import javax.swing.JTable;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * Simple stub of AbstractComboBoxCellRenderer.
 * see {@link com.syrus.AMFICOM.client.general.ui_.AbstractComboBoxCellRenderer}
 *
 * @version $Revision: 1.6 $, $Date: 2005/05/18 14:01:19 $
 * @author $Author: bass $
 * @module generalclient_v1
 */
public class StubComboBoxCellRenderer extends AbstractComboBoxCellRenderer {

	private static final long	serialVersionUID	= 4277733257122003024L;
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
					Object object,
					ObjectResourceController objectResourceController,
					String key) {
		// nothing

	}
}
