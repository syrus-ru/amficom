/*-
 * $Id: LinkedIdsCondition.java,v 1.38 2005/06/21 12:43:48 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlLinkedIdsCondition;
import com.syrus.util.Log;

/**
 * If one needs to write a implementation for <code>LinkedIdsCondition</code>
 * for a certain module (administration, configuration, etc.), the resulting
 * class must meet the following conditions:
 * <ul>
 * <li>It must be a &lt;default&gt; (i. e. package visible) final class, named
 * <code>LinkedIdsConditionImpl</code> and residing in the appropriate package
 * (e. g.: <code>com.syrus.AMFICOM.administration</code> for administration
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
 * <li>It must override {@link #isConditionTrue(StorableObject)},
 * {@link #isNeedMore(Set)}and {@link #setEntityCode(Short)}.</li>
 * </ul>
 *
 * @author $Author: bass $
 * @version $Revision: 1.38 $, $Date: 2005/06/21 12:43:48 $
 * @module general_v1
 */
public class LinkedIdsCondition implements StorableObjectCondition {

	private static final String CREATING_A_DUMMY_CONDITION = "; creating a dummy condition...";
	private static final String INVALID_UNDERLYING_IMPLEMENTATION = "Invalid underlying implementation: ";
	private static final String LINKED_IDS_CONDITION_INIT = "LinkedIdsCondition.<init>() | ";
	private static final String LINKED_IDS_CONDITION_INNER_ONE_IS_CONDITION_TRUE = "LinkedIdsCondition$1.isConditionTrue() | ";
	protected static final String ENTITY_CODE_NOT_REGISTERED = "Entity not registered for this condition -- ";
	protected static final String LINKED_ENTITY_CODE_NOT_REGISTERED = "Linked entity not registered for this condition -- ";

	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected Short entityCode;

	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected short linkedEntityCode;
	/**
	 * Field is used by descendants only, and never directly.
	 */

	protected Set<Identifier> linkedIds;

	private LinkedIdsCondition delegate;

	public LinkedIdsCondition(final Identifier identifier, final short entityCode) {
		this(identifier, new Short(entityCode));
	}

	public LinkedIdsCondition(final Identifier identifier, final Short entityCode) {
		this(Collections.singleton(identifier), entityCode);
	}

	public LinkedIdsCondition(final Set<Identifier> linkedIds, final short entityCode) {
		this(linkedIds, new Short(entityCode));
	}

	public LinkedIdsCondition(final IdlLinkedIdsCondition transferable) {
		final Short code = new Short(transferable.entityCode);
		short linkedCode = transferable.linkedEntityCode;

		final Set<Identifier> linkIds = new HashSet<Identifier>(transferable.linkedIds.length);
		for (int i = 0; i < transferable.linkedIds.length; i++) {
			linkIds.add(new Identifier(transferable.linkedIds[i]));
		}

		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(code.shortValue()).toLowerCase().replaceAll("group$", "") + ".LinkedIdsConditionImpl";
		try {
			Constructor ctor;
			ctor = Class.forName(className).getDeclaredConstructor(new Class[] {Set.class, Short.class, Short.class});
			ctor.setAccessible(true);
			this.delegate = (LinkedIdsCondition) ctor.newInstance(new Object[] {linkIds, new Short(linkedCode), code});
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Class " + className
					+ " not found on the classpath"
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't inherit from "
					+ LinkedIdsCondition.class.getName() + CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't have the constructor expected"
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " is abstract"
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			} else
				Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
						+ "constructor throws an exception in class "
						+ className + CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalAccessException"
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalArgumentException"
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Log.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught a SecurityException"
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} finally {
			if (this.delegate == null) {
				this.delegate = new LinkedIdsCondition() {

					@Override
					public boolean isConditionTrue(final StorableObject storableObject) {
						Log.debugMessage(LINKED_IDS_CONDITION_INNER_ONE_IS_CONDITION_TRUE
								+ "Object: " + storableObject.toString() + "; "
								+ "This is a dummy condition; evaluation result is always false...",
								Log.WARNING);
						return false;
					}
				};
				this.delegate.entityCode = code;
				this.delegate.linkedEntityCode = linkedCode;
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

	private LinkedIdsCondition(final Set<Identifier> linkedIds, final Short entityCode) {
		short linkedCode;
		try {
			linkedCode = StorableObject.getEntityCodeOfIdentifiables(linkedIds);
		} catch (final AssertionError ae) {
			linkedCode = ObjectEntities.UNKNOWN_CODE;
			Log.errorException(ae);
		}

		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(entityCode.shortValue()).toLowerCase().replaceAll("group$", "") + ".LinkedIdsConditionImpl";
		try {
			Constructor ctor;
			ctor = Class.forName(className).getDeclaredConstructor(new Class[] {Set.class, Short.class, Short.class});
			ctor.setAccessible(true);
			this.delegate = (LinkedIdsCondition) ctor.newInstance(new Object[] {linkedIds, new Short(linkedCode), entityCode});
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Class " + className
					+ " not found on the classpath"
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't inherit from "
					+ LinkedIdsCondition.class.getName() + CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't have the constructor expected"
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " is abstract"
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			} else
				Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
						+ "constructor throws an exception in class "
						+ className
						+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalAccessException"
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalArgumentException"
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Log.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught a SecurityException"
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} finally {
			if (this.delegate == null) {
				this.delegate = new LinkedIdsCondition() {

					@Override
					public boolean isConditionTrue(final StorableObject storableObject) {
						Log.debugMessage(LINKED_IDS_CONDITION_INNER_ONE_IS_CONDITION_TRUE
								+ "Object: " + storableObject.toString() + "; "
								+ "This is a dummy condition; evaluation result is always false...",
								Log.WARNING);
						return false;
					}
				};
				this.delegate.entityCode = entityCode;
				this.delegate.linkedEntityCode = linkedCode;
				this.delegate.linkedIds = linkedIds;
			}
		}
	}

	public final short getLinkedEntityCode() {
		return this.delegate.linkedEntityCode;
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
	public final IdlStorableObjectCondition getTransferable() {
		IdlIdentifier[] linkedIdTransferable = new IdlIdentifier[this.delegate.linkedIds.size()];
		int i = 0;
		for (Iterator<Identifier> it = this.delegate.linkedIds.iterator(); it.hasNext(); i++)
			linkedIdTransferable[i] = it.next().getTransferable();

		final IdlLinkedIdsCondition transferable = new IdlLinkedIdsCondition(this.delegate.entityCode.shortValue(),
				this.delegate.linkedEntityCode,
				linkedIdTransferable);

		final IdlStorableObjectCondition condition = new IdlStorableObjectCondition();
		condition.linkedIdsCondition(transferable);
		return condition;
	}

	/**
	 * Must be overridden by descendants, or a {@link NullPointerException}will
	 * occur.
	 *
	 * @param storableObject
	 * @throws IllegalObjectEntityException
	 * @see StorableObjectCondition#isConditionTrue(StorableObject)
	 */
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		return this.delegate.isConditionTrue(storableObject);
	}

	/**
	 * @param storableObjects
	 * @see StorableObjectCondition#isNeedMore(Set)
	 */
	public boolean isNeedMore(final Set<StorableObject> storableObjects) {
		return this.delegate.isNeedMore(storableObjects);
	}

	public final void setEntityCode(final short entityCode) throws IllegalObjectEntityException {
		this.setEntityCode(new Short(entityCode));
	}

	/**
	 * @throws IllegalObjectEntityException
	 * @see StorableObjectCondition#setEntityCode(Short)
	 */
	public void setEntityCode(final Short entityCode) throws IllegalObjectEntityException {
		this.delegate.setEntityCode(entityCode);
	}

	public final void setLinkedIds(Set<Identifier> linkedIds) {
		try {
			this.delegate.linkedEntityCode = StorableObject.getEntityCodeOfIdentifiables(linkedIds);
		} catch (final AssertionError ae) {
			this.delegate.linkedEntityCode = ObjectEntities.UNKNOWN_CODE;
			Log.errorException(ae);
		}
		this.delegate.linkedIds = linkedIds;
	}

	public final Set<Identifier> getLinkedIds() {
		return this.delegate.linkedIds;
	}

	public final void setLinkedId(final Identifier linkedId) {
		this.setLinkedIds(Collections.singleton(linkedId));
	}

	protected final Map sort(Set<Identifier> linkIds) {
		final Map<Short, Set<Identifier>> codeIdsMap = new HashMap<Short, Set<Identifier>>();
		for (Iterator<Identifier> it = linkIds.iterator(); it.hasNext();) {
			final Identifier id = it.next();
			final short code = id.getMajor();
			Set<Identifier> ids = codeIdsMap.get(new Short(code));
			if (ids == null) {
				ids = new HashSet<Identifier>();
				codeIdsMap.put(new Short(code), ids);
			}
			ids.add(id);
		}
		return codeIdsMap;
	}

	protected final boolean conditionTest(Set<Identifiable> identifiables) {
		assert identifiables != null : ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator<Identifiable> it = identifiables.iterator(); it.hasNext();) {
			final Identifiable identifiable = it.next();
			final Identifier id = identifiable.getId();
			for (final Iterator<Identifier> linkedIdsIt = this.linkedIds.iterator(); linkedIdsIt.hasNext();) {
				final Identifier linkedId = linkedIdsIt.next();
				if (id.equals(linkedId)) {
					return true;
				}
			}
		}
		return false;
	}

	protected final boolean conditionTest(final Identifier id) {
		if (id != null && !id.isVoid()) {
			for (final Iterator<Identifier> linkedIdsIt = this.linkedIds.iterator(); linkedIdsIt.hasNext();) {
				final Identifier linkedId = linkedIdsIt.next();
				if (id.equals(linkedId)) {
					return true;
				}
			}
		}
		return false;
	}
}
