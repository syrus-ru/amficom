package com.syrus.AMFICOM.Client.General.Event;

import com.syrus.AMFICOM.Client.Resource.Result.Test;

public class TestUpdateEvent extends OperationEvent {
	public boolean testSelected = false;
	public boolean testDeselected = false;

	public static final long TEST_SELECTED_EVENT = 0x00000001;
	public static final long TEST_DESELECTED_EVENT = 0x00000002;

	public long type;
	public static final String TYPE = "testupdate";
	public Test test;

	public TestUpdateEvent(Object source, Test test, long type) {
		super(source, 0, TYPE);
		this.type = type;
		this.test = test;

		if ((type & TEST_SELECTED_EVENT) != 0)
			this.testSelected = true;
		if ((type & TEST_DESELECTED_EVENT) != 0)
			this.testDeselected = true;
	}
}
