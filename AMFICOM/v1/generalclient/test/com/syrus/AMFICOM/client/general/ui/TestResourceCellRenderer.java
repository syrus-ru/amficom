
package com.syrus.AMFICOM.client.general.ui;

import java.awt.Color;

import javax.swing.JTable;

import com.syrus.AMFICOM.CORBA.General.TestStatus;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.client.resource.ObjectResourceController;
import com.syrus.AMFICOM.client.resource.TestResource;
import com.syrus.AMFICOM.client.resource.TestResourceController;

/**
 * @author Vladimir Dolzhenko
 */
public class TestResourceCellRenderer extends AbstractLabelCellRenderer {

	private static TestResourceCellRenderer	instance;

	private TestResourceCellRenderer() {
		// empty
	}

	public static TestResourceCellRenderer getInstance() {
		if (instance == null)
			instance = new TestResourceCellRenderer();
		return instance;
	}

	protected void customRendering(	JTable table,
					ObjectResource objectResource,
					ObjectResourceController controller,
					String key) {
		if (objectResource instanceof TestResource) {
			TestResource tr = (TestResource) objectResource;
			if (key.equals(TestResourceController.KEY_NAME)) {
				Color color = null;
				switch (tr.getStatus().value()) {
					case TestStatus._TEST_STATUS_ABORTED:
						color = Color.RED;
						break;
					case TestStatus._TEST_STATUS_COMPLETED:
						color = Color.GREEN;
						break;
					case TestStatus._TEST_STATUS_PROCESSING:
						color = Color.CYAN;
						break;
					case TestStatus._TEST_STATUS_SCHEDULED:
						color = Color.GRAY;
						break;
					default:
						break;
				}
				if (color != null)
					super.setBackground(color);

			}

		}

	}
}