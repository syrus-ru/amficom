/*
 * $Id: TestResourceListTest.java,v 1.4 2004/12/23 12:52:33 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.client_.general.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.client_.general.ui_.ObjList;
import com.syrus.AMFICOM.client_.general.ui_.ObjListModel;
import com.syrus.AMFICOM.client_.resource.TestResource;
import com.syrus.AMFICOM.client_.resource.TestResourceController;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2004/12/23 12:52:33 $
 * @module general_v1
 */
public class TestResourceListTest {

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

		ObjListModel listModel = new ObjListModel(controller, list, TestResourceController.KEY_STATUS);
		ObjList objList = new ObjList(listModel); 

		JPanel panel = new JPanel(new GridLayout(0, 1));
		Dimension d = new Dimension(100,100);
		objList.setPreferredSize(d);
		panel.add(objList);

		
		objList.setSelectedValue(tr2, true);
		//objList.setSelectedItem(tr2);		
		//comboBox.setSelectedIndex(1);

		frame.getContentPane().add(panel);		

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}

