/*-
 * $Id: LinkedIdsCondition.java,v 1.66 2006/06/28 10:38:25 arseniy Exp $
 *
 * Copyright ? 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.omg.CORBA.ORB;

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
 * @author $Author: arseniy $
 * @version $Revision: 1.66 $, $Date: 2006/06/28 10:38:25 $
 * @module general
 */
public class LinkedIdsCondition implements StorableObjectCondition {
	private static final String CREATING_A_DUMMY_CONDITION = "; creating a dummy condition...";
	private static final String INVALID_UNDERLYING_IMPLEMENTATION = "Invalid underlying implementation: ";
	private static final String LINKED_IDS_CONDITION_INIT = "LinkedIdsCondition.<init>() | ";
	private static final String LINKED_IDS_CONDITION_INNER_ONE_IS_CONDITION_TRUE = "LinkedIdsCondition$1.isConditionTrue() | ";
	private static final String LINKED_IDS_CONDITION_INNER_ONE_IS_NEED_MORE = "LinkedIdsCondition$1.isNeedMore() | ";
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

	protected Set<? extends Identifiable> linkedIdentifiables;

	private LinkedIdsCondition delegate;

	public LinkedIdsCondition(final Identifiable identifiable, final short entityCode) {
		this(identifiable, new Short(entityCode));
	}

	public LinkedIdsCondition(final Identifiable identifiable, final Short entityCode) {
		this(Collections.singleton(identifiable), entityCode);
	}

	public LinkedIdsCondition(final Set<? extends Identifiable> linkedIdentifiables, final short entityCode) {
		this(linkedIdentifiables, new Short(entityCode));
	}

	/**
	 * Empty constructor used by descendants only.
	 */
	protected LinkedIdsCondition() {
		// Empty constructor used by descendants only.
	}

	private LinkedIdsCondition(final Set<? extends Identifiable> linkedIdentifiables, final Short entityCode) {
		final short linkedCode = StorableObject.getEntityCodeOfIdentifiables(linkedIdentifiables);

		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(entityCode.shortValue()).toLowerCase().replaceAll("group$", "") + ".LinkedIdsConditionImpl";
		try {
			final Constructor<?> ctor = Class.forName(className).getDeclaredConstructor(new Class[] {Set.class, Short.class, Short.class});
			ctor.setAccessible(true);
			this.delegate = (LinkedIdsCondition) ctor.newInstance(new Object[] {linkedIdentifiables, new Short(linkedCode), entityCode});
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Class " + className
					+ " not found on the classpath" + CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " + className
					+ " doesn't inherit from " + LinkedIdsCondition.class.getName() + CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " + className
					+ " doesn't have the constructor expected" + CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " + className
					+ " is abstract" + CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null) {
					assert false;
				} else {
					assert false : message;
				}
			} else {
				Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
						+ "constructor throws an exception in class " + className + CREATING_A_DUMMY_CONDITION, Level.WARNING);
			}
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugMessage(iae, Level.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalAccessException" + CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugMessage(iae, Level.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalArgumentException" + CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugMessage(se, Level.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught a SecurityException" + CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} finally {
			if (this.delegate == null) {
				this.delegate = createDummyCondition();
				this.delegate.entityCode = entityCode;
				this.delegate.linkedEntityCode = linkedCode;
				this.delegate.linkedIdentifiables = linkedIdentifiables;
			}
		}
	}

	public LinkedIdsCondition(final IdlLinkedIdsCondition transferable) {
		final Short code = new Short(transferable.entityCode);
		short linkedCode = transferable.linkedEntityCode;

		final Set<Identifiable> lnkIdentifiables = new HashSet<Identifiable>(transferable.linkedIds.length);
		for (int i = 0; i < transferable.linkedIds.length; i++) {
			lnkIdentifiables.add(Identifier.valueOf(transferable.linkedIds[i]));
		}

		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(code.shortValue()).toLowerCase().replaceAll("group$", "") + ".LinkedIdsConditionImpl";
		try {
			final Constructor<?> ctor = Class.forName(className).getDeclaredConstructor(new Class[] {Set.class, Short.class, Short.class});
			ctor.setAccessible(true);
			this.delegate = (LinkedIdsCondition) ctor.newInstance(new Object[] {lnkIdentifiables, new Short(linkedCode), code});
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Class " + className
					+ " not found on the classpath" + CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " + className
					+ " doesn't inherit from " + LinkedIdsCondition.class.getName() + CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " + className
					+ " doesn't have the constructor expected" + CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " + className
					+ " is abstract" + CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null) {
					assert false;
				} else {
					assert false : message;
				}
			} else
				Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
						+ "constructor throws an exception in class " + className + CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugMessage(iae, Level.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalAccessException" + CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugMessage(iae, Level.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalArgumentException" + CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugMessage(se, Level.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught a SecurityException" + CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} finally {
			if (this.delegate == null) {
				this.delegate = createDummyCondition();
				this.delegate.entityCode = code;
				this.delegate.linkedEntityCode = linkedCode;
				this.delegate.linkedIdentifiables = lnkIdentifiables;
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
	 * @see StorableObjectCondition#getIdlTransferable(ORB)
	 */
	public final IdlStorableObjectCondition getIdlTransferable(final ORB orb) {
		return this.getIdlTransferable();
	}

	public final IdlStorableObjectCondition getIdlTransferable() {
		final IdlIdentifier[] linkedIdTransferable = new IdlIdentifier[this.delegate.linkedIdentifiables.size()];
		int i = 0;
		for (final Identifiable linkedIdentifiable : this.delegate.linkedIdentifiables) {
			linkedIdTransferable[i++] = linkedIdentifiable.getId().getIdlTransferable();
		}

		final IdlLinkedIdsCondition transferable = new IdlLinkedIdsCondition(this.delegate.entityCode.shortValue(),
				this.delegate.linkedEntityCode,
				linkedIdTransferable);

		final IdlStorableObjectCondition condition = new IdlStorableObjectCondition();
		condition.linkedIdsCondition(transferable);
		return condition;
	}

	private static LinkedIdsCondition createDummyCondition() {
		return new LinkedIdsCondition() {
			@Override
			public boolean isConditionTrue(final StorableObject storableObject) {
				Log.debugMessage(LINKED_IDS_CONDITION_INNER_ONE_IS_CONDITION_TRUE
						+ "Object: " + storableObject.toString() + "; "
						+ "This is a dummy condition; evaluation result is always false...",
						Level.WARNING);
				return false;
			}

			@Override
			public boolean isNeedMore(final Set<? extends Identifiable> identifiables) {
				Log.debugMessage(LINKED_IDS_CONDITION_INNER_ONE_IS_NEED_MORE
						+ "Objects: " + identifiables + "; "
						+ "This is a dummy condition; evaluation result is always false...",
						Level.WARNING);
				return false;
			}
		};
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
	 * @param identifiables
	 * @see StorableObjectCondition#isNeedMore(Set)
	 */
	public boolean isNeedMore(final Set<? extends Identifiable> identifiables) {
		return this.delegate.isNeedMore(identifiables);
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

	public final void setLinkedIdentifiables(final Set<? extends Identifiable> linkedIdentifiables) {
		this.delegate.linkedEntityCode = StorableObject.getEntityCodeOfIdentifiables(linkedIdentifiables);
		this.delegate.linkedIdentifiables = linkedIdentifiables;
	}

	public final Set<? extends Identifiable> getLinkedIdentifiables() {
		return this.delegate.linkedIdentifiables;
	}

	public final void setLinkedIdentifiable(final Identifiable linkedIdentifiable) {
		this.setLinkedIdentifiables(Collections.singleton(linkedIdentifiable));
	}

	protected final boolean conditionTest(Set<? extends Identifiable> identifiables) {
		assert identifiables != null : ErrorMessages.NON_NULL_EXPECTED;
		for (final Identifiable identifiable : identifiables) {
			final Identifier id = identifiable.getId();
			for (final Identifiable linkedIdentifiable : this.linkedIdentifiables) {
				final Identifier linkedId = linkedIdentifiable.getId();
				if (id.equals(linkedId)) {
					return true;
				}
			}
		}
		return false;
	}

	protected final boolean conditionTest(final Identifiable identifiable) {
		if (identifiable != null) {
			final Identifier id = identifiable.getId();
			for (final Identifiable linkedIdentifiable : this.linkedIdentifiables) {
				final Identifier linkedId = linkedIdentifiable.getId();
				if (id.equals(linkedId)) {
					return true;
				}
			}
		}
		return false;
	}

	protected IllegalObjectEntityException newExceptionEntityIllegal() {
		return new IllegalObjectEntityException(ENTITY_CODE_NOT_REGISTERED
				+ ObjectEntities.asString(this.entityCode) + "; linked entity" + ObjectEntities.asString(this.linkedEntityCode),
				IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

	protected IllegalObjectEntityException newExceptionLinkedEntityIllegal() {
		return new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED
				+ ObjectEntities.asString(this.linkedEntityCode) + "for entity" + ObjectEntities.asString(this.entityCode),
				IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

	protected IllegalObjectEntityException newExceptionEntityCodeToSetIsIllegal(final Short entityCode1) {
		return new IllegalObjectEntityException(ENTITY_CODE_NOT_REGISTERED + ObjectEntities.asString(entityCode1),
				IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer(LinkedIdsCondition.class.getSimpleName());
		buffer.append(", all ");
		buffer.append(ObjectEntities.codeToString(this.delegate.entityCode));
		buffer.append(" for linked ids ");
		synchronized (this.delegate.linkedIdentifiables) {
			buffer.append(Identifier.toString(this.delegate.linkedIdentifiables));
		}
		return buffer.toString();
	}
}
