package com.syrus.AMFICOM.Client.General.Event;

import com.syrus.AMFICOM.Client.Resource.Result.Test;

public class TestUpdateEvent extends OperationEvent {
	public boolean TEST_SELECTED = false;
	public boolean TEST_DESELECTED = false;
	public boolean TEST_CREATED = false;

	public static final long TEST_SELECTED_EVENT = 0x00000001;
	public static final long TEST_DESELECTED_EVENT = 0x00000002;
	public static final long TEST_CREATED_EVENT = 0x00000004;

	public long type;
	public static final String TYPE = "testupdate";
	public Test test;

	public TestUpdateEvent(Object source, Test test, long type) {
		super(source, 0, TYPE);
		this.type = type;
		this.test = test;

		if ((type & TEST_SELECTED_EVENT) != 0)
			this.TEST_SELECTED = true;
		if ((type & TEST_DESELECTED_EVENT) != 0)
			this.TEST_DESELECTED = true;
		if ((type & TEST_CREATED_EVENT) != 0)
			this.TEST_CREATED = true;

	}
}
