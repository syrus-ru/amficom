package com.syrus.AMFICOM.client.UI;

import javax.swing.JTable;

import com.syrus.util.Wrapper;

/**
 * Simple stub of AbstractLabelCellRenderer. see
 * {@link AbstractLabelCellRenderer}
 * 
 * @version $Revision: 1.3 $, $Date: 2005/09/08 13:53:11 $
 * @author $Author: bob $
 * @module commonclient
 */
public class StubLabelCellRenderer extends AbstractLabelCellRenderer {

	private static final long	serialVersionUID	= 1402647243697400892L;
	private static StubLabelCellRenderer	instance;

	private StubLabelCellRenderer() {
		// empty
	}

	public static synchronized StubLabelCellRenderer getInstance() {
		if (instance == null) {
			instance = new StubLabelCellRenderer();
		}
		return instance;
	}

	protected void customRendering(	JTable table,
					Object objec,
					Wrapper controller,
					String key) {
		//	 nothing

	}

}
