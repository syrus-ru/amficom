package com.syrus.AMFICOM.client_.general.ui_;

import javax.swing.JTable;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * Simple stub of AbstractLabelCellRenderer. see
 * {@link com.syrus.AMFICOM.client.general.ui_.AbstractLabelCellRenderer}
 * 
 * @version $Revision: 1.2 $, $Date: 2004/08/26 10:26:40 $
 * @author $Author: krupenn $
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

	protected void customRendering(	JTable table,
					ObjectResource objectResource,
					ObjectResourceController controller,
					String key) {
		//	 nothing

	}

}