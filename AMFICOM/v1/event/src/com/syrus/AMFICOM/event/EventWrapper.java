/*
 * $Id: EventWrapper.java,v 1.3 2005/02/03 15:51:00 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.event.corba.EventStatus;
import com.syrus.AMFICOM.event.corba.EventParameter_TransferablePackage.EventParameterSort;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2005/02/03 15:51:00 $
 * @author $Author: arseniy $
 * @module event_v1
 */
public class EventWrapper implements StorableObjectWrapper {

	public static final String COLUMN_STATUS = "status";

	public static final String LINK_FIELD_EVENT_PARAMETERS = "event_parameters";
	public static final String LINK_COLUMN_EVENT_PARAMETER_SORT	= "sort";
	public static final String LINK_COLUMN_EVENT_PARAMETER_VALUE_NUMBER	= "value_number";
	public static final String LINK_COLUMN_EVENT_PARAMETER_VALUE_STRING	= "value_string";
	public static final String LINK_COLUMN_EVENT_PARAMETER_VALUE_RAW	= "value_raw";

	public static final String LINK_FIELD_EVENT_SOURCES = "event_sources";
	public static final String LINK_FIELD_SOURCE_ENTITY_ID	= "entity_id";

	private static EventWrapper instance;

	private List keys;

	private EventWrapper() {
		//private constructor
		String[] keysArray = new String[] {COLUMN_TYPE_ID,
				COLUMN_STATUS,
				COLUMN_DESCRIPTION,
				LINK_FIELD_EVENT_PARAMETERS,
				LINK_FIELD_EVENT_SOURCES};
		this.keys = Collections.unmodifiableList(new ArrayList(Arrays.asList(keysArray)));
	}

	public static EventWrapper getInstance() {
		if (instance == null)
			instance = new EventWrapper();
		return instance;
	}

	public List getKeys() {
		return this.keys;
	}

	public String getName(final String key) {
		/* there is no reason to rename it */
		return key;
	}

	public Object getValue(final Object object, final String key) {
		if (object instanceof Event) {
			Event event = (Event) object;
			if (key.equals(COLUMN_TYPE_ID))
				return event.getType();
			if (key.equals(COLUMN_STATUS))
				return new Integer(event.getStatus().value());
			if (key.equals(COLUMN_DESCRIPTION))
				return event.getDescription();
			if (key.equals(LINK_FIELD_EVENT_PARAMETERS)) {
				EventParameter[] eventParameters = event.getParameters();
				Map values = new HashMap(eventParameters.length * 4);
				int eventParameterSort;
				for (int i = 0; i < eventParameters.length; i++) {
					values.put(COLUMN_ID + i, eventParameters[i].getId());
					values.put(COLUMN_TYPE_ID + i, eventParameters[i].getType());
					eventParameterSort = eventParameters[i].getSort().value();
					values.put(LINK_COLUMN_EVENT_PARAMETER_SORT + i, new Integer(eventParameterSort));
					switch (eventParameterSort) {
						case EventParameterSort._PARAMETER_SORT_NUMBER:
							values.put(LINK_COLUMN_EVENT_PARAMETER_VALUE_NUMBER + i, new Integer(eventParameters[i].getValueNumber()));
							break;
						case EventParameterSort._PARAMETER_SORT_STRING:
							values.put(LINK_COLUMN_EVENT_PARAMETER_VALUE_STRING + i, eventParameters[i].getValueString());
							break;
						case EventParameterSort._PARAMETER_SORT_RAW:
							values.put(LINK_COLUMN_EVENT_PARAMETER_VALUE_RAW + i, eventParameters[i].getValueRaw());
							break;
						default:
							Log.errorMessage("EventWrapper.getValue | Unknown sort: " + eventParameterSort + " of parameter '" + eventParameters[i].getId() + "', event '" + event.getId() + "'");
					}
				}
				return values;
			}
			if (key.equals(LINK_FIELD_EVENT_SOURCES)) {
				List eventSources = event.getEventSources();
				Map values = new HashMap(eventSources.size() * 2);
				EventSource eventSource;
				int i = 0;
				for (Iterator it = eventSources.iterator(); it.hasNext(); i++) {
					eventSource = (EventSource) it.next();
					values.put(COLUMN_ID + i, eventSource.getId());
					values.put(LINK_FIELD_SOURCE_ENTITY_ID + i, eventSource.getEntityId());
				}
				return values;
			}
		}
		return null;
	}

	public void setValue(Object object, String key, Object value) {
		if (object instanceof Event) {
			Event event = (Event) object;
			if (key.equals(COLUMN_TYPE_ID))
				event.setType((EventType) value);
			else
				if (key.equals(COLUMN_STATUS))
					event.setStatus(EventStatus.from_int(((Integer) value).intValue()));
				else
					if (key.equals(COLUMN_DESCRIPTION))
						event.setDescription((String) value);
					else
						if (key.equals(LINK_FIELD_EVENT_PARAMETERS)) {
							Map eventParametersMap = (Map) value;
							/* there are 4*N keys for N EventParameter */
							EventParameter[] parameters = new EventParameter[eventParametersMap.size() / 4];
							Identifier parameterId;
							ParameterType parameterType;
							int eventParameterSort;
							for (int i = 0; i < parameters.length; i++) {
								parameterId = (Identifier) eventParametersMap.get(COLUMN_ID + i);
								parameterType = (ParameterType) eventParametersMap.get(COLUMN_TYPE_ID + i);
								eventParameterSort = ((Integer) eventParametersMap.get(LINK_COLUMN_EVENT_PARAMETER_SORT + i)).intValue();
								switch (eventParameterSort) {
									case EventParameterSort._PARAMETER_SORT_NUMBER:
										parameters[i] = new EventParameter(parameterId,
												parameterType,
												((Integer) eventParametersMap.get(LINK_COLUMN_EVENT_PARAMETER_VALUE_NUMBER + i)).intValue());
										break;
									case EventParameterSort._PARAMETER_SORT_STRING:
										parameters[i] = new EventParameter(parameterId,
												parameterType,
												(String) eventParametersMap.get(LINK_COLUMN_EVENT_PARAMETER_VALUE_STRING + i));
										break;
									case EventParameterSort._PARAMETER_SORT_RAW:
										parameters[i] = new EventParameter(parameterId,
												parameterType,
												(byte[]) eventParametersMap.get(LINK_COLUMN_EVENT_PARAMETER_VALUE_RAW + i));
										break;
									default:
										Log.errorMessage("EventWrapper.setValue | Unknown sort: " + eventParameterSort + " of parameter '" + parameterId + "', event '" + event.getId() + "'");
								}
							}
							event.setEventParameters(parameters);
						}
						else
							if (key.equals(LINK_FIELD_EVENT_SOURCES)) {
								Map eventSourcesMap = (Map) value;
								/* there are 2*N keys for N EventSources */
								int n = eventSourcesMap.size() / 2;
								List eventSources = new ArrayList(n);
								for (int i = 0; i < n; i++)
									eventSources.add(new EventSource((Identifier) eventSourcesMap.get(COLUMN_ID + i),
											(Identifier) eventSourcesMap.get(LINK_FIELD_SOURCE_ENTITY_ID + i) ));
								event.setEventSources(eventSources);
							}
		}
	}

	public boolean isEditable(String key) {
		return false;
	}

	public String getKey(final int index) {
		return (String) this.keys.get(index);
	}

	public Object getPropertyValue(String key) {
		/* there is no properties */
		return null;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		/* there is no properties */
	}

	public Class getPropertyClass(String key) {
		if (key.equals(COLUMN_TYPE_ID))
			return EventType.class;
		if (key.equals(COLUMN_STATUS))
			return Integer.class;
		if (key.equals(LINK_FIELD_EVENT_PARAMETERS))
			return Map.class;
		if (key.equals(LINK_FIELD_EVENT_SOURCES))
			return Map.class;
		return String.class;
	}

}
