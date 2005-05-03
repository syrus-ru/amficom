/*
 * $Id: OpenModuleEvent.java,v 1.1.1.1 2004/05/27 11:24:21 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.Event;

import com.syrus.AMFICOM.corba.portable.alarm.Message;
import java.util.*;

/**
 * Events of this class are fired when there's a need to notify the listener
 * that some other module has to be opened.
 *
 * @version $Revision: 1.1.1.1 $, $Date: 2004/05/27 11:24:21 $
 * @author $Author: bass $
 */
public final class OpenModuleEvent extends OperationEvent implements List {
	/*
	 * Module names.
	 **************************************************************************/

	/**
	 * Value: {@value}.
	 */
	public static final String MODULE_NAME_ADMINISTRATE = "Administrate";

	/**
	 * Value: {@value}.
	 */
	public static final String MODULE_NAME_ANALYSIS = "Analysis";

	/**
	 * Value: {@value}.
	 */
	public static final String MODULE_NAME_ANALYSIS_EXT = "AnalysisExt";

	/**
	 * Value: {@value}.
	 */
	public static final String MODULE_NAME_COMPONENTS = "Components";

	/**
	 * Value: {@value}.
	 */
	public static final String MODULE_NAME_CONFIGURE = "Configure";

	/**
	 * Value: {@value}.
	 */
	public static final String MODULE_NAME_EVALUATION = "Evaluation";

	/**
	 * Value: {@value}.
	 */
	public static final String MODULE_NAME_MAINTENANCE = "Maintenance";

	/**
	 * Value: {@value}.
	 */
	public static final String MODULE_NAME_MAP = "Map";

	/**
	 * Value: {@value}.
	 */
	public static final String MODULE_NAME_MODEL = "Model";

	/**
	 * Value: {@value}.
	 */
	public static final String MODULE_NAME_OPTIMIZE = "Optimize";

	/**
	 * Value: {@value}.
	 */
	public static final String MODULE_NAME_PROGNOSIS = "Prognosis";

	/**
	 * Value: {@value}.
	 */
	public static final String MODULE_NAME_SCHEDULE = "Schedule";

	/**
	 * Value: {@value}.
	 */
	public static final String MODULE_NAME_SCHEMATICS = "Schematics";

	/**
	 * Value: {@value}.
	 */
	public static final String MODULE_NAME_SURVEY = "Survey";

	/*
	 * Operation types.
	 **************************************************************************/

	/**
	 * Value: {@value}
	 */
	public static final String OPERATION_TYPE_PROCESS_ALERTING_MESSAGE = "processAlertingMessage";

	/**
	 * This field seems mandatory for all classes that extend
	 * <code>OperationEvent</code>.
	 *
	 * Value: {@value}.
	 */
	public static final String type = "openmodule";

	/**
	 * The sequence of messages that caused the system to fire this event.
	 *
	 * Underlying implementation must be a RandomAccess, namely an ArrayList,
	 * because JTable uses random access during contents display.
	 */
	private List delegate = Collections.synchronizedList(new ArrayList());

	/** 
	 * The name of the module to be visited.
	 *
	 * @serial include
	 */
	private String moduleName;

	/**
	 * The type of the operation to be performed on the module selected.
	 *
	 * @serial include
	 */
	private String operationType;

	private static final long serialVersionUID = 2070101993064144096L;

	/**
	 * Constructs an empty <code>OpenModuleEvent</code> object.
	 */
	public OpenModuleEvent() {
		this(new LinkedList(), MODULE_NAME_SURVEY, OPERATION_TYPE_PROCESS_ALERTING_MESSAGE);
	}

	/**
	 * Constructs an <code>OpenModuleEvent</code> object.
	 *
	 * @param messageSeq sequence of messages that caused this event to be
	 *        fired.
	 * @param moduleName the name of the module to be visited.
	 * @param operationType the type of the operation to be performed on the
	 *        module selected.
	 */	
	public OpenModuleEvent(Collection messageSeq, String moduleName, String operationType) {
		super(messageSeq, 0, type);
		addAll(messageSeq);
		setModuleName(moduleName);
		setOperationType(operationType);
	}

	/**
	 * Constructs an <code>OpenModuleEvent</code> object from a single message.
	 *
	 * @param message the message that caused this event to be fired.
	 * @param moduleName the name of the module to be visited.
	 * @param operationType the type of the operation to be performed on the
	 *        module selected.
	 */	
	public OpenModuleEvent(Message message, String moduleName, String operationType) {
		super(message, 0, type);
		add(message);
		setModuleName(moduleName);
		setOperationType(operationType);
	}

	/**
	 * Checks for validity the module name suplied.
	 *
	 * @param moduleName The module name to be checked for validity.
	 * @throws java.lang.IllegalArgumentException if module name is invalid.
	 */	
	private static void checkValidModuleName(String moduleName) {
		try {
			if (moduleName.length() == 0)
				throw new IllegalArgumentException("Module name cannot be empty.");
			if (!((moduleName.equals(MODULE_NAME_ADMINISTRATE))
					|| (moduleName.equals(MODULE_NAME_ANALYSIS))
					|| (moduleName.equals(MODULE_NAME_ANALYSIS_EXT))
					|| (moduleName.equals(MODULE_NAME_COMPONENTS))
					|| (moduleName.equals(MODULE_NAME_CONFIGURE))
					|| (moduleName.equals(MODULE_NAME_EVALUATION))
					|| (moduleName.equals(MODULE_NAME_MAINTENANCE))
					|| (moduleName.equals(MODULE_NAME_MAP))
					|| (moduleName.equals(MODULE_NAME_MODEL))
					|| (moduleName.equals(MODULE_NAME_OPTIMIZE))
					|| (moduleName.equals(MODULE_NAME_PROGNOSIS))
					|| (moduleName.equals(MODULE_NAME_SCHEDULE))
					|| (moduleName.equals(MODULE_NAME_SCHEMATICS))
					|| (moduleName.equals(MODULE_NAME_SURVEY))))
				throw new IllegalArgumentException("Module name is invalid.");
		} catch (NullPointerException npe) {
			throw new IllegalArgumentException("Module name cannot be null.");
		}
	}

	/**
	 * Checks for validity the operation type suplied.
	 *
	 * @param operationType The operation type to be checked for validity.
	 * @throws java.lang.IllegalArgumentException if operation type is invalid.
	 */	
	private static void checkValidOperationType(String operationType) {
		try {
			if (operationType.length() == 0)
				throw new IllegalArgumentException("Operation type cannot be empty.");
		} catch (NullPointerException npe) {
			throw new IllegalArgumentException("Operation type cannot be null.");
		}
	}

	/**
	 * Getter for property moduleName.
	 *
	 * @return Value of property moduleName.
	 */
	public synchronized String getModuleName() {
		return moduleName;
	}

	/**
	 * Setter for property moduleName.
	 *
	 * @param moduleName New value of property moduleName.
	 */
	public synchronized void setModuleName(String moduleName) {
		checkValidModuleName(moduleName);
		this.moduleName = moduleName;
	}

	/** 
	 * Getter for property operationType.
	 *
	 * @return Value of property operationType.
	 */
	public synchronized String getOperationType() {
		return operationType;
	}
	
	/**
	 * Setter for property operationType.
	 *
	 * @param operationType New value of property operationType.
	 */
	public synchronized void setOperationType(String operationType) {
		checkValidOperationType(operationType);
		this.operationType = operationType;
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
