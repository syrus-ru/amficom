
package com.syrus.AMFICOM.client_.general.ui;

import java.awt.Color;

import javax.swing.JTable;

import com.syrus.AMFICOM.CORBA.General.TestStatus;
import com.syrus.AMFICOM.client_.general.ui_.AbstractLabelCellRenderer;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.client_.resource.TestResource;
import com.syrus.AMFICOM.client_.resource.TestResourceController;

/**
 * @author Vladimir Dolzhenko
 */
public class TestResourceCellRenderer extends AbstractLabelCellRenderer {

	private static final long	serialVersionUID	= 5175325260719712484L;
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
					Object object,
					ObjectResourceController controller,
					String key) {
		if (object instanceof TestResource) {
			TestResource tr = (TestResource) object;
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