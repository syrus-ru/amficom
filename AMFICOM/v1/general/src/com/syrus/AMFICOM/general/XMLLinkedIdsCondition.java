/*-
* $Id: XMLLinkedIdsCondition.java,v 1.6.2.1 2006/04/18 17:06:09 arseniy Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;

import com.syrus.util.Log;


/**
 * @version $Revision: 1.6.2.1 $, $Date: 2006/04/18 17:06:09 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module general
 */
public class XMLLinkedIdsCondition 
extends XMLStorableObjectCondition<LinkedIdsCondition> {
	
	private XMLLinkedIdsCondition delegate;
	
	private static final String	INVALID_UNDERLYING_IMPLEMENTATION	= 
		"Invalid underlying implementation: ";	
	private static final String	LINKED_IDS_CONDITION_INIT			= 
		"XMLLinkedIdsCondition.<init>() | ";
	
	@SuppressWarnings("unused")
	private XMLLinkedIdsCondition(final LinkedIdsCondition condition,
	                            final StorableObjectXMLDriver driver) {
		super(condition, driver);
		final String className = "com.syrus.AMFICOM."
			+ ObjectGroupEntities.getGroupName(
				condition.getEntityCode().shortValue()).toLowerCase().replaceAll("group$", "")
			+ ".XMLLinkedIdsConditionImpl";
		try {
			Constructor<?> ctor;
			ctor = Class.forName(className).getDeclaredConstructor(
				new Class[] { LinkedIdsCondition.class, 
						StorableObjectXMLDriver.class});
			ctor.setAccessible(true);
			this.delegate = 
				(XMLLinkedIdsCondition) ctor.newInstance(
					new Object[] { condition, 
							driver});
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Class " + className
					+ " not found on the classpath"
			, Level.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't inherit from "
					+ LinkedIdsCondition.class.getName(), Level.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't have the constructor expected"
			, Level.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " is abstract"
			, Level.WARNING);
		} catch (InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false: message;
			} else		
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
					+ "constructor throws an exception in class "
					+ className, Level.WARNING);
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugMessage(iae, Level.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalAccessException"
			, Level.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugMessage(iae, Level.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalArgumentException"
			, Level.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugMessage(se, Level.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught a SecurityException"
			, Level.SEVERE);
		}
	}

	/**
	 * Empty constructor used by descendants only.
	 */
	protected XMLLinkedIdsCondition() {
		// Empty constructor used by descendants only.
		super(null, null);
	}
	
	@Override
	public Set<Identifier> getIdsByCondition() throws IllegalDataException {
		return this.delegate.getIdsByCondition();
	}
	
	protected Set<Identifier> getIdsByCondition(final String field) throws IllegalDataException {
		final String baseQuery = super.getBaseQuery();
		Set<Identifier> identifiers = null;
		for (final Identifiable identifiable : super.condition.getLinkedIdentifiables()) {
			final Identifier id = identifiable.getId();
			final Set<Identifier> ids = super.getIdsByCondition(baseQuery 
					+ '/' 
					+ field 
					+ "[text()='" 
					+ super.getValue(id) 
					+ "']", 
				true);
			if (identifiers == null) {
				if (!ids.isEmpty()) {
					identifiers = ids;
				}
			} else {
				identifiers.addAll(ids);
			}
		}
		if (identifiers == null) {
			identifiers = Collections.emptySet();
		}
		return identifiers;
	}
	
	protected IllegalDataException newExceptionEntityIllegal() {
		return new IllegalDataException("Unsupported entity '"
				+ ObjectEntities.codeToString(super.condition.getEntityCode()) + "'/" + super.condition.getEntityCode()
				+ "; linked entity '"+ ObjectEntities.codeToString(super.condition.getLinkedEntityCode()) + "'/" + super.condition.getLinkedEntityCode());
	}
	
	protected IllegalDataException newExceptionLinkedEntityIllegal() {
		return new IllegalDataException("Unsupported linked entity '"
				+ ObjectEntities.codeToString(super.condition.getLinkedEntityCode()) + "'/" + super.condition.getLinkedEntityCode()
				+ " for entity '" + ObjectEntities.codeToString(super.condition.getEntityCode()) + "'/" + super.condition.getEntityCode());
	}
}

