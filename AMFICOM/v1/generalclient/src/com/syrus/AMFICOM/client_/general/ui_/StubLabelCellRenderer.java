package com.syrus.AMFICOM.client_.general.ui_;

import javax.swing.JTable;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * Simple stub of AbstractLabelCellRenderer. see
 * {@link com.syrus.AMFICOM.client.general.ui_.AbstractLabelCellRenderer}
 * 
 * @version $Revision: 1.4 $, $Date: 2004/10/07 11:31:15 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class StubLabelCellRenderer extends AbstractLabelCellRenderer {

	private static final long	serialVersionUID	= 1402647243697400892L;
	private static StubLabelCellRenderer	instance;

	private StubLabelCellRenderer() {
		// empty
	}

	public static StubLabelCellRenderer getInstance() {
		if (instance == null)
			instance = new StubLabelCellRenderer();
		return instance;
	}

	protected void customRendering(	JTable table,
					Object objec,
					ObjectResourceController controller,
					String key) {
		//	 nothing

	}

}