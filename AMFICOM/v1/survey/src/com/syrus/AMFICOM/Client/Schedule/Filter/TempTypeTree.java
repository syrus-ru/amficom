package com.syrus.AMFICOM.Client.Schedule.Filter;

import com.syrus.AMFICOM.CORBA.General.TestTemporalType;
import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import javax.swing.*;

public class TempTypeTree extends FilterTree {

	ApplicationContext		aContext;
	private FilterTreeNode	root;

	public void setTree(ApplicationContext aContext) {
		this.aContext = aContext;
		if (this.root == null) {
			this.root = new FilterTreeNode(LangModelSchedule.getString("Root"), "ROOT");
			this.root.add(new FilterTreeNode(LangModelSchedule.getString("Onetime"), Integer
					.toString(TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME)));
			/**
			 * @TODO fix for continual temporal test type
			 */
			//root.add(new
			// FilterTreeNode(LangModelSchedule.getString("Continual"),
			// Integer.toString(TestTemporalType._TEST_TEMPORAL_TYPE_TIMETABLE));
			this.root.add(new FilterTreeNode(LangModelSchedule.getString("Periodical"), Integer
					.toString(TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL)));
			TreeModelClone myModel = new TreeModelClone(this.root);
			this.tree = new JTree(myModel);
		}
	}
}

