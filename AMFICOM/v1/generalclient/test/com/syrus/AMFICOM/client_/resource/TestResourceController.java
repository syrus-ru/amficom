/*
 * TestResourceController.java
 * Created on 20.08.2004 10:38:55
 * 
 */

package com.syrus.AMFICOM.client_.resource;

import java.awt.Color;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.CORBA.General.TestStatus;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.client.general.ui.MLabel;

/**
 * @author Vladimir Dolzhenko
 */
public final class TestResourceController implements ObjectResourceController {

	public static final String		KEY_ID			= "id";
	public static final String		KEY_NAME		= "name";
	public static final String		KEY_VALUES		= "values";
	public static final String		KEY_STATUS_COLOR	= "statuscolor";
	public static final String		KEY_STATUS		= "status";
	public static final String		KEY_TIME		= "time";
	public static final String		KEY_CHANGED		= "changed";

	private static TestResourceController	instance;

	private List			keys;

	private Map				statusMap;
	private Map				values;

	private TestResourceController() {
		// empty private constructor
		String[] keysArray = new String[] { KEY_ID, KEY_NAME, KEY_STATUS_COLOR, KEY_STATUS, KEY_TIME, KEY_CHANGED,
				KEY_VALUES};	
	
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));

		this.statusMap = new HashMap();
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
			instance = new TestResourceController();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		String name = null;
		if (key.equals(KEY_ID))
			name = "id";
		else if (key.equals(KEY_NAME))
			name = "Name";
		else if (key.equals(KEY_STATUS_COLOR))
			name = "Color of Status";
		else if (key.equals(KEY_STATUS))
			name = "Status";
		else if (key.equals(KEY_TIME))
			name = "Time";
		else if (key.equals(KEY_CHANGED))
			name = "Changed";
		else if (key.equals(KEY_VALUES))
			name = "Values";
		return name;
	}

	public Object getValue(final ObjectResource objectResource, final String key) {
		Object result = null;
		if (objectResource instanceof TestResource) {
			TestResource testResource = (TestResource) objectResource;
			if (key.equals(KEY_ID))
				result = testResource.getId();
			else if (key.equals(KEY_NAME))
				result = testResource.getName();
			else if (key.equals(KEY_STATUS))
				result = testResource.getStatus();
			else if (key.equals(KEY_STATUS_COLOR)) {
				switch (testResource.getStatus().value()) {
					case TestStatus._TEST_STATUS_ABORTED:
						result = Color.RED;
						break;
					case TestStatus._TEST_STATUS_COMPLETED:
						result = Color.GREEN;
						break;
					case TestStatus._TEST_STATUS_PROCESSING:
						result = Color.CYAN;
						break;
					case TestStatus._TEST_STATUS_SCHEDULED:
						result = Color.GRAY;
						break;
					default:
						result = Color.WHITE;
				}
			} else if (key.equals(KEY_TIME))
				result = SIMPLE_DATE_FORMAT.format(new Date(testResource.getTime()));
			else if (key.equals(KEY_CHANGED))
				result = Boolean.valueOf(testResource.isChanged());
			else if (key.equals(KEY_VALUES)) {
				result = testResource.getValue();
				if (!this.values.containsKey(result)) {
					this.values.put(result, result);
				}
			}
		}
		return result;
	}

	public boolean isEditable(final String key) {
		boolean editable = false;
		if (key.equals(KEY_NAME))
			editable = true;
		else if (key.equals(KEY_STATUS))
			editable = true;
		else if (key.equals(KEY_TIME))
			editable = true;
		else if (key.equals(KEY_CHANGED))
			editable = true;
		else if (key.equals(KEY_VALUES))
			editable = true;
		return editable;
	}

	public void setValue(ObjectResource objectResource, final String key, final Object value) {
		if (objectResource instanceof TestResource) {
			TestResource testResource = (TestResource) objectResource;
			if (key.equals(KEY_NAME))
				testResource.setName((String) value);
			else if (key.equals(KEY_STATUS)) {
				testResource.setStatus((TestStatus) value);
			} else if (key.equals(KEY_TIME)) {
				try {
					testResource.setTime(SIMPLE_DATE_FORMAT.parse((String) value).getTime());
				} catch (ParseException pe) {
					//pe.printStackTrace();
				}
			} else if (key.equals(KEY_CHANGED)) {
				testResource.setChanged(((Boolean) value).booleanValue());
			} else if (key.equals(KEY_VALUES)) {
				if (value != null) {
					testResource.setValue((String) value);
					if (value.equals("add")) {
						String str = Integer.toString(this.values.keySet().size());
						setPropertyValue(KEY_VALUES, str, str);
					}
				}
			}
		}

	}

	public String getKey(final int index) {
		return (String)this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
		Object result = "";
		if (key.equals(KEY_STATUS))
			result = this.statusMap;
		else if (key.equals(KEY_VALUES)) {
			result = this.values;
		}
		return result;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		if (key.equals(KEY_VALUES)) {
			this.values.put(objectKey, objectValue);
		}
	}

	public Class getPropertyClass(String key) {
		Class clazz = String.class;
		if (key.equals(KEY_STATUS))
			clazz = Map.class;
		else if (key.equals(KEY_VALUES))
			clazz = Map.class;
		if (key.equals(KEY_STATUS_COLOR))
			clazz = Color.class;
		else if (key.equals(KEY_CHANGED))
			clazz = Boolean.class;
		return clazz;
	}
}