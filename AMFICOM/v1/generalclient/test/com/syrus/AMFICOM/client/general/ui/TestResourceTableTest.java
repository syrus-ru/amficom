/*
 * TestResourceTableTest.java
 * Created on 20.08.2004 11:39:04
 * 
 */

package com.syrus.AMFICOM.client.general.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.syrus.AMFICOM.CORBA.General.TestStatus;
import com.syrus.AMFICOM.client.general.ui_.ObjectResourceTable;
import com.syrus.AMFICOM.client.resource.TestResource;
import com.syrus.AMFICOM.client.resource.TestResourceController;

/**
 * @author Vladimir Dolzhenko
 */
public class TestResourceTableTest {

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

		TestResourceController controller = TestResourceController.getInstance();

		List list = new LinkedList();
		list.add(tr1);
		list.add(tr2);

		ObjectResourceTable table = new ObjectResourceTable(controller, list);
		table.setDefaultTableCellRenderer();
		table.setRenderer(TestResourceCellRenderer.getInstance(), TestResourceController.KEY_NAME);

		JPanel panel = new JPanel(new GridLayout(0, 1));	
		panel.add(table.getTableHeader());
		panel.add(new JScrollPane(table));
		frame.getContentPane().add(panel);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}

