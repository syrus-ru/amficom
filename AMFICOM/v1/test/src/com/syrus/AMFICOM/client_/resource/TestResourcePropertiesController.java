/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.client_.resource;

import com.syrus.AMFICOM.client_.general.ui.MLabel;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * @author Andrei Kroupennikov
 */
public final class TestResourcePropertiesController extends TestResourceController {

	protected TestResourcePropertiesController() {
		// empty private constructor
//		String[] keysArray = new String[] { KEY_ID, KEY_NAME, KEY_STATUS_COLOR, KEY_STATUS, KEY_TIME, KEY_CHANGED,
//				KEY_VALUES};	
		String[] keysArray = new String[] { KEY_VALUES };
	
		super.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));

		super.statusMap = new HashMap();
		this.statusMap.put(new MLabel("Aborted", TestStatus._TEST_STATUS_ABORTED),
					TestStatus.TEST_STATUS_ABORTED);
		this.statusMap.put(new MLabel("Completed", TestStatus._TEST_STATUS_COMPLETED),
					TestStatus.TEST_STATUS_COMPLETED);
		this.statusMap.put(new MLabel("Processing", TestStatus._TEST_STATUS_PROCESSING),
					TestStatus.TEST_STATUS_PROCESSING);
		this.statusMap.put(new MLabel("Scheduled", TestStatus._TEST_STATUS_SCHEDULED),
					TestStatus.TEST_STATUS_SCHEDULED);

		this.values = new HashMap();

		this.values.put("add", "add");
		this.values.put("...", "...");

	}

	public static TestResourceController getInstance() {
		if (instance == null)
			instance = new TestResourcePropertiesController();
		return instance;
	}

}