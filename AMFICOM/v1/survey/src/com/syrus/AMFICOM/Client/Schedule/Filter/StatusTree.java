package com.syrus.AMFICOM.Client.Schedule.Filter;

import com.syrus.AMFICOM.CORBA.General.TestStatus;
import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import javax.swing.*;

public class StatusTree extends FilterTree {

	ApplicationContext		aContext;
	private FilterTreeNode	root;

	public void setTree(ApplicationContext aContext) {
		this.aContext = aContext;
		if (this.root == null) {
			this.root = new FilterTreeNode(LangModelSchedule.getString("Root"), "ROOT");
			this.root.add(new FilterTreeNode(LangModelSchedule.getString("Done"), Integer
					.toString(TestStatus._TEST_STATUS_COMPLETED)));
			this.root.add(new FilterTreeNode(LangModelSchedule.getString("Running"), Integer
					.toString(TestStatus._TEST_STATUS_PROCESSING)));
			this.root.add(new FilterTreeNode(LangModelSchedule.getString("Scheduled"), Integer
					.toString(TestStatus._TEST_STATUS_SCHEDULED)));
			this.root.add(new FilterTreeNode(LangModelSchedule.getString("Aborted"), Integer
					.toString(TestStatus._TEST_STATUS_ABORTED)));

			TreeModelClone myModel = new TreeModelClone(this.root);
			this.tree = new JTree(myModel);
		}
	}
}

