/*
 * $Id: TestResourcePropertyTableTest2.java,v 1.1 2004/12/30 12:35:38 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.client_.general.ui;

import com.syrus.AMFICOM.client_.resource.TestResourcePropertiesController;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.client_.general.ui_.ObjPropertyTable;
import com.syrus.AMFICOM.client_.resource.TestResource;
import com.syrus.AMFICOM.client_.resource.TestResourceController;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2004/12/30 12:35:38 $
 * @module general_v1
 */
public class TestResourcePropertyTableTest2 {

	public static void main(String[] args) {
		JFrame frame = new JFrame("TestResourceTableTest");
		TestResource tr1 = new TestResource("#1");
		tr1.setName("tr1");
		tr1.setStatus(TestStatus.TEST_STATUS_SCHEDULED);
		tr1.setTime(System.currentTimeMillis());

		TestResource tr2 = new TestResource("#2");
		tr2.setName("tr2");
		tr2.setStatus(TestStatus.TEST_STATUS_COMPLETED);
		tr2.setTime(System.currentTimeMillis() + 1000 * 60 * 60);

		TestResourceController controller = TestResourcePropertiesController.getInstance();

		List list = new LinkedList();
		list.add(tr1);
		list.add(tr2);

		ObjPropertyTable table = new ObjPropertyTable(controller, tr1);
		table.setDefaultTableCellRenderer();
		//table.setRenderer(TestResourceCellRenderer.getInstance(), TestResourceController.KEY_NAME);

		JPanel panel = new JPanel(new GridLayout(0, 1));	
		panel.add(table.getTableHeader());
		panel.add(new JScrollPane(table));
		frame.getContentPane().add(panel);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}

