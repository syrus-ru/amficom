/*
 * $Id: LinkedIdsCondition.java,v 1.7 2005/01/14 18:04:32 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.LinkedIdsCondition_Transferable;
import com.syrus.util.Log;

/**
 * If one needs to write a implementation for <code>LinkedIdsCondition</code>
 * for a certain module (administration, configuration, etc.), the resulting
 * class must meet the following conditions:
 * <ul>
 * <li>It must be a &lt;default&gt; (i. e. package visible) final class, named
 * <code>LinkedIdsConditionImpl</code> and residing in the appropriate package (e.
 * g.: <code>com.syrus.AMFICOM.administration</code> for administration
 * module). Note that package/module name must be in sync with the appropriate
 * group name from {@link ObjectGroupEntities}, i. e. if group name is
 * &quot;AdministrationGroup&quot;, package name can&apos;t be
 * <code>com.syrus.AMFICOM.administrate</code> or
 * <code>com.syrus.AMFICOM.admin</code>.</li>
 * <li>It must inherit from this class (i. e.
 * {@link LinkedIdsCondition com.syrus.AMFICOM.general.LinkedIdsCondition})
 * </li>
 * <li>It must declare <em>the only</em> <code>private</code> constructor
 * with the following code:
 * 
 * <pre>
 * 
 * private LinkedIdsConditionImpl(final Identifier identifier, final Short entityCode) {
 * 	super(); // First line must invoke superconstructor w/o parameters.
 * 	this.identifier = identifier;
 * 	this.entityCode = entityCode;
 * 	this.linkedIds = null;
 * }
 * 
 * private LinkedIdsConditionImpl(final List linkedIds, final Short entityCode) {
 * 	super(); // First line must invoke superconstructor w/o parameters.
 * 	this.identifier = null;
 * 	this.entityCode = entityCode;
 * 	this.linkedIds = linkedIds;
 * }
 * 
 * 
 * </pre>
 * 
 * </li>
 * <li>It must override {@link #isConditionTrue(Object)},
 * {@link #isNeedMore(List)}and {@link #setEntityCode(Short)}.</li>
 * </ul>
 * 
 * @author $Author: arseniy $
 * @version $Revision: 1.7 $, $Date: 2005/01/14 18:04:32 $
 * @module general_v1
 */
public class LinkedIdsCondition implements StorableObjectCondition {

	private static final String	CREATING_A_DUMMY_CONDITION = "; creating a dummy condition..."; //$NON-NLS-1$

	private static final String	INVALID_UNDERLYING_IMPLEMENTATION = "Invalid underlying implementation: "; //$NON-NLS-1$

	private static final String	LINKED_IDS_CONDITION_INIT = "LinkedIdsCondition.<init>() | "; //$NON-NLS-1$

	private static final String	LINKED_IDS_CONDITION_INNER_ONE_IS_CONDITION_TRUE = "LinkedIdsCondition$1.isConditionTrue() | "; //$NON-NLS-1$

	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected Short entityCode;

	/**
	 * Field is used by descendants only, and never directly.
	 */

	protected List linkedIds;

	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected Identifier identifier;

	private LinkedIdsCondition delegate;

	public LinkedIdsCondition(final Identifier identifier, final short entityCode) {
		this(identifier, new Short(entityCode));
	}

	public LinkedIdsCondition(final Identifier identifier, final Short entityCode) {
		this(identifier, null, entityCode);
	}

	public LinkedIdsCondition(final List linkedIds, final short entityCode) {
		this(linkedIds, new Short(entityCode));
	}

	public LinkedIdsCondition(final List linkedIds, final Short entityCode) {
		this(null, linkedIds, entityCode);
	}

	public LinkedIdsCondition(final LinkedIdsCondition_Transferable transferable) {
		Short code = new Short(transferable.entity_code);
		Identifier id = null;
		List linkIds = null;
		
		if (transferable.linked_ids.length == 1) {
			id = new Identifier(transferable.linked_ids[0]);
		}
		else {
			linkIds = new ArrayList(transferable.linked_ids.length);
			for (int i = 0; i < transferable.linked_ids.length; i++) {
				linkIds.add(new Identifier(transferable.linked_ids[i]));
			}
		}
		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(code.shortValue()).toLowerCase().replaceAll("group$", "") + ".LinkedIdsConditionImpl"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		try {
			Constructor ctor;
			if (id == null){
				ctor = Class.forName(className).getDeclaredConstructor(new Class[] { List.class, Short.class});
				ctor.setAccessible(true);
				this.delegate = (LinkedIdsCondition) ctor.newInstance(new Object[] { linkIds, code});
			}
			else {
				ctor = Class.forName(className).getDeclaredConstructor(new Class[] { Identifier.class, Short.class});
				ctor.setAccessible(true);
				this.delegate = (LinkedIdsCondition) ctor.newInstance(new Object[] { id, code});
			}			
		}
		catch (ClassNotFoundException cnfe) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Class " + className //$NON-NLS-1$
					+ " not found on the classpath" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		}
		catch (ClassCastException cce) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " doesn't inherit from " //$NON-NLS-1$
					+ LinkedIdsCondition.class.getName() + CREATING_A_DUMMY_CONDITION, Log.WARNING);
		}
		catch (NoSuchMethodException nsme) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " doesn't have the constructor expected" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		}
		catch (InstantiationException ie) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " is abstract" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		}
		catch (InvocationTargetException ite) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
					+ "constructor throws an exception in class " //$NON-NLS-1$
					+ className + CREATING_A_DUMMY_CONDITION, Log.WARNING);
		}
		catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalAccessException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		}
		catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalArgumentException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		}
		catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Log.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught a SecurityException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		}
		finally {
			if (this.delegate == null) {
				this.delegate = new LinkedIdsCondition() {

					public boolean isConditionTrue(final Object object) throws ApplicationException {
						Log.debugMessage(LINKED_IDS_CONDITION_INNER_ONE_IS_CONDITION_TRUE
								+ "This is a dummy condition; evaluation result is always false...", //$NON-NLS-1$
							Log.WARNING);
						return false;
					}
				};
				this.delegate.identifier = id;
				this.delegate.entityCode = code;
				this.delegate.linkedIds = linkIds;
			}
		}
	}

	/**
	 * Empty constructor used by descendants only.
	 */
	protected LinkedIdsCondition() {
		// Empty constructor used by descendants only.
	}

	private LinkedIdsCondition(final Identifier identifier, final List linkedIds, final Short entityCode) {
		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(entityCode.shortValue()).toLowerCase().replaceAll("group$", "") + ".LinkedIdsConditionImpl"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		try {
			Constructor ctor;
			if (identifier == null){
				ctor = Class.forName(className).getDeclaredConstructor(new Class[] { List.class, Short.class});
				ctor.setAccessible(true);
				this.delegate = (LinkedIdsCondition) ctor.newInstance(new Object[] { linkedIds, entityCode});
			}
			else {
				ctor = Class.forName(className).getDeclaredConstructor(new Class[] { Identifier.class, Short.class});
				ctor.setAccessible(true);
				this.delegate = (LinkedIdsCondition) ctor.newInstance(new Object[] { identifier, entityCode});
			}
		}
		catch (ClassNotFoundException cnfe) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Class " + className //$NON-NLS-1$
					+ " not found on the classpath" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		}
		catch (ClassCastException cce) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " doesn't inherit from " //$NON-NLS-1$
					+ LinkedIdsCondition.class.getName() + CREATING_A_DUMMY_CONDITION, Log.WARNING);
		}
		catch (NoSuchMethodException nsme) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " doesn't have the constructor expected" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		}
		catch (InstantiationException ie) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " is abstract" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		}
		catch (InvocationTargetException ite) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
					+ "constructor throws an exception in class " //$NON-NLS-1$
					+ className + CREATING_A_DUMMY_CONDITION, Log.WARNING);
		}
		catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalAccessException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		}
		catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalArgumentException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		}
		catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Log.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught a SecurityException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		}
		finally {
			if (this.delegate == null) {
				this.delegate = new LinkedIdsCondition() {

					public boolean isConditionTrue(final Object object) throws ApplicationException {
						Log.debugMessage(LINKED_IDS_CONDITION_INNER_ONE_IS_CONDITION_TRUE
								+ "This is a dummy condition; evaluation result is always false...", //$NON-NLS-1$
							Log.WARNING);
						return false;
					}
				};
				this.delegate.identifier = identifier;
				this.delegate.entityCode = entityCode;
				this.delegate.linkedIds = linkedIds;
			}
		}
	}

	/**
	 * @see StorableObjectCondition#getEntityCode()
	 */
	public final Short getEntityCode() {
		return this.delegate.entityCode;
	}

	/**
	 * Creates a new object on every invocation.
	 * 
	 * @see StorableObjectCondition#getTransferable()
	 */
	public final Object getTransferable() {
		LinkedIdsCondition_Transferable transferable = new LinkedIdsCondition_Transferable();
		Identifier_Transferable[] linkedIdTransferable;
		if (this.delegate.linkedIds != null) {
			linkedIdTransferable = new Identifier_Transferable[this.delegate.linkedIds.size()];
			int i = 0;

			for (Iterator it = this.delegate.linkedIds.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				linkedIdTransferable[i] = (Identifier_Transferable) id.getTransferable();
			}
		} else {
			linkedIdTransferable = new Identifier_Transferable[1];
			linkedIdTransferable[0] = (Identifier_Transferable) this.delegate.identifier.getTransferable();
		}

		transferable.linked_ids = linkedIdTransferable;
		transferable.entity_code = this.delegate.entityCode.shortValue();

		return transferable;
	}

	/**
	 * Must be overridden by descendants, or a {@link NullPointerException}will
	 * occur.
	 * 
	 * @param object
	 * @throws ApplicationException
	 * @see StorableObjectCondition#isConditionTrue(Object)
	 */
	public boolean isConditionTrue(final Object object) throws ApplicationException {
		return this.delegate.isConditionTrue(object);
	}

	/**
	 * @param list
	 * @throws ApplicationException
	 * @see StorableObjectCondition#isNeedMore(List)
	 */
	public boolean isNeedMore(final List list) throws ApplicationException {
		return this.delegate.isNeedMore(list);
	}

	public final void setEntityCode(final short entityCode) {
		this.setEntityCode(new Short(entityCode));
	}

	/**
	 * @see StorableObjectCondition#setEntityCode(Short)
	 */
	public void setEntityCode(final Short entityCode) {
		this.delegate.setEntityCode(entityCode);
	}

	public void setLinkedIds(List linkedIds) {
		this.delegate.linkedIds = linkedIds;
		this.delegate.identifier = null;
	}

	public List getLinkedIds() {
		return this.delegate.linkedIds;
	}

	public Identifier getIdentifier() {
		return this.delegate.identifier;
	}

	public void setIdentifier(Identifier identifier) {
		this.delegate.identifier = identifier;
		this.delegate.linkedIds = null;
	}

}
