package com.syrus.AMFICOM.client.UI;

import javax.swing.JTable;

import com.syrus.util.Wrapper;

/**
 * Simple stub of AbstractComboBoxCellRenderer.
 * see {@link AbstractComboBoxCellRenderer}
 * 
 * @version $Revision: 1.2 $, $Date: 2005/08/11 18:51:08 $
 * @author $Author: arseniy $
 * @module commonclient
 */
public class StubComboBoxCellRenderer extends AbstractComboBoxCellRenderer {

	private static final long serialVersionUID = 4277733257122003024L;
	private static StubComboBoxCellRenderer instance;

	private StubComboBoxCellRenderer() {
		// empty
	}

	public static StubComboBoxCellRenderer getInstance() {
		if (instance == null) {
			instance = new StubComboBoxCellRenderer();
		}
		return instance;
	}

	@Override
	protected void customRendering(JTable table, Object object, Wrapper objectResourceController, String key) {
		// nothing

	}
}
