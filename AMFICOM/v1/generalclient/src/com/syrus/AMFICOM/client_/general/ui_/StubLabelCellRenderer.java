package com.syrus.AMFICOM.client_.general.ui_;

import javax.swing.JTable;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * Simple stub of AbstractLabelCellRenderer. see
 * {@link com.syrus.AMFICOM.client_.general.ui_.AbstractLabelCellRenderer}
 *
 * @version $Revision: 1.7 $, $Date: 2005/05/18 14:01:19 $
 * @author $Author: bass $
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
