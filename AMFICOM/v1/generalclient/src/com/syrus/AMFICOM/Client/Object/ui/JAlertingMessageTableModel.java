/*
 * $Id: JAlertingMessageTableModel.java,v 1.1 2004/05/27 11:24:21 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Object.ui;

import com.syrus.AMFICOM.Client.General.Event.OpenModuleEvent;
import com.syrus.AMFICOM.corba.portable.alarm.Message;
import com.syrus.AMFICOM.corba.portable.common.Identifier;
import java.util.*;
import javax.swing.event.*;
import javax.swing.table.TableModel;

/**
 * @version $Revision: 1.1 $, $Date: 2004/05/27 11:24:21 $
 * @author $Author: bass $
 */
final class JAlertingMessageTableModel implements TableModel, List {
	private OpenModuleEvent delegate = new OpenModuleEvent();

	private EventListenerList listenerList = new EventListenerList();

	private static final ResourceBundle MESSAGE_MODEL_RESOURCE_BUNDLE
		= ResourceBundle.getBundle("com.syrus.AMFICOM.Client.Resource.Alarm.util.MessageModelPropertyResourceBundle");

	private static final String ALERTING_ID_TITLE
		= MESSAGE_MODEL_RESOURCE_BUNDLE.getString("Alerting_Id");

	private static final String MESSAGE_TYPE_TITLE
		= MESSAGE_MODEL_RESOURCE_BUNDLE.getString("Message_Type");

	private static final String EVENT_ID_TITLE
		= MESSAGE_MODEL_RESOURCE_BUNDLE.getString("Event_Id");

	private static final String EVENT_DATE_TITLE
		= MESSAGE_MODEL_RESOURCE_BUNDLE.getString("Event_Date");

	private static final String EVENT_SOURCE_NAME_TITLE
		= MESSAGE_MODEL_RESOURCE_BUNDLE.getString("Event_Source_Name");

	private static final String EVENT_SOURCE_DESCRIPTION_TITLE
		= MESSAGE_MODEL_RESOURCE_BUNDLE.getString("Event_Source_Description");

	private static final String TRANSMISSION_PATH_NAME_TITLE
		= MESSAGE_MODEL_RESOURCE_BUNDLE.getString("Transmission_Path_Name");

	private static final String TRANSMISSION_PATH_DESCRIPTION_TITLE
		= MESSAGE_MODEL_RESOURCE_BUNDLE.getString("Transmission_Path_Description");

	private static final String TEXT_TITLE
		= MESSAGE_MODEL_RESOURCE_BUNDLE.getString("Text");

	JAlertingMessageTableModel() {
	}

	OpenModuleEvent getDelegate() {
		return delegate;
	}

	void setDelegate(OpenModuleEvent delegate) {
		this.delegate = delegate;
	}

	/*
	 * Methods below are required by the TableModel interface.
	 **************************************************************************/

	public void addTableModelListener(TableModelListener l) {
		listenerList.add(TableModelListener.class, l);
	}

	/**
	 * In the future, the second column will contain MessageType objects instead
	 * of Identifier.
	 */
	public Class getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case 0:
				/*
				 * Fall through.
				 */
			case 1:
				/*
				 * Fall through.
				 */
			case 2:
				return Identifier.class;
			case 3:
				return Date.class;
			case 4:
				/*
				 * Fall through.
				 */
			case 5:
				/*
				 * Fall through.
				 */
			case 6:
				/*
				 * Fall through.
				 */
			case 7:
				/*
				 * Fall through.
				 */
			case 8:
				return String.class;
			default:
				throw new IllegalArgumentException();
		}
	}

	public int getColumnCount() {
		/*
		 * Just the number of fields in Message.
		 */
		return 9;
	}

	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
			case 0:
				return ALERTING_ID_TITLE;
			case 1:
				return MESSAGE_TYPE_TITLE;
			case 2:
				return EVENT_ID_TITLE;
			case 3:
				return EVENT_DATE_TITLE;
			case 4:
				return EVENT_SOURCE_NAME_TITLE;
			case 5:
				return EVENT_SOURCE_DESCRIPTION_TITLE;
			case 6:
				return TRANSMISSION_PATH_NAME_TITLE;
			case 7:
				return TRANSMISSION_PATH_DESCRIPTION_TITLE;
			case 8:
				return TEXT_TITLE;
			default:
				throw new IllegalArgumentException();
		}
	}

	public int getRowCount() {
		return size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Message message = (Message) get(rowIndex);
		switch (columnIndex) {
			case 0:
				return message.getAlertingId();
			case 1:
				return message.getMessageTypeId();
			case 2:
				return message.getEventId();
			case 3:
				return new Date(message.getEventDate());
			case 4:
				return message.getEventSourceName();
			case 5:
				return message.getEventSourceDescription();
			case 6:
				return message.getTransmissionPathName();
			case 7:
				return message.getTransmissionPathDescription();
			case 8:
				return message.getText();
			default:
				throw new IllegalArgumentException();
		}
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public void removeTableModelListener(TableModelListener l) {
		listenerList.remove(TableModelListener.class, l);
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	/*
	 * All the methods below are required by the List interface.
	 **************************************************************************/

	public boolean add(Object o) {
		return delegate.add(o);
	}

	public void add(int index, Object element) {
		delegate.add(index, element);
	}

	public boolean addAll(Collection c) {
		return delegate.addAll(c);
	}

	public boolean addAll(int index, Collection c) {
		return delegate.addAll(index, c);
	}

	public void clear() {
		delegate.clear();
	}

	public boolean contains(Object o) {
		return delegate.contains(o);
	}

	public boolean containsAll(Collection c) {
		return delegate.containsAll(c);
	}

	public Object get(int index) {
		return delegate.get(index);
	}

	public int indexOf(Object o) {
		return delegate.indexOf(o);
	}

	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	public Iterator iterator() {
		return delegate.iterator();
	}

	public int lastIndexOf(Object o) {
		return delegate.lastIndexOf(o);
	}

	public ListIterator listIterator() {
		return delegate.listIterator();
	}

	public ListIterator listIterator(int index) {
		return delegate.listIterator(index);
	}

	public boolean remove(Object o) {
		return delegate.remove(o);
	}

	public Object remove(int index) {
		return delegate.remove(index);
	}

	public boolean removeAll(Collection c) {
		return delegate.removeAll(c);
	}

	public boolean retainAll(Collection c) {
		return delegate.retainAll(c);
	}

	public Object set(int index, Object element) {
		return delegate.set(index, element);
	}

	public int size() {
		return delegate.size();
	}

	public List subList(int fromIndex, int toIndex) {
		return delegate.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return delegate.toArray();
	}

	public Object[] toArray(Object[] a) {
		return delegate.toArray(a);
	}
}
