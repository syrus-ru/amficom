package com.syrus.AMFICOM.Client.General.Event;

public class TestEvent extends OperationEvent 
{
	public boolean TEST_SELECTED = false;
	public boolean RESULT_SELECTED = false;

	public static final long TEST_SELECTED_EVENT = 0x00000001;
	public static final long RESULT_SELECTED_EVENT = 0x00000002;

	static final public String type = "testevent";

    public String test_id;
    public String result_id;
	
	public TestEvent(Object source, long typ, String test_id, String result_id)
	{
		super(source, 0, type);
        test_id = test_id;
        result_id = result_id;

		if((typ & TEST_SELECTED_EVENT) != 0)
			TEST_SELECTED = true;
		if((typ & RESULT_SELECTED_EVENT) != 0)
			RESULT_SELECTED = true;
	}
}