/*-
* $Id: XMLCompoundCondition.java,v 1.4 2005/10/30 14:49:07 bass Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.4 $, $Date: 2005/10/30 14:49:07 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module general
 */
public class XMLCompoundCondition extends XMLStorableObjectCondition<CompoundCondition> {
	
	private static final String	COMPOUND_CONDITION_INIT			= 
		"XMLCompoundCondition.reflectXMLCondition() | ";
	
	@SuppressWarnings("unused")
	private XMLCompoundCondition(final CompoundCondition condition,
	                            final StorableObjectXMLDriver driver) {
		super(condition, driver);
	}
	
	@Override
	public Set<Identifier> getIdsByCondition() throws IllegalDataException {
		Set<StorableObjectCondition> conditions = 
			super.condition.getConditions();

		final int operation = super.condition.getOperation();
		
		Set<Identifier> identifiers = null;
		for(StorableObjectCondition soCondition: conditions) {
			XMLStorableObjectCondition<?> xmlCondition = 
				this.reflectXMLCondition(soCondition);			
			Set<Identifier> idsByCondition = xmlCondition.getIdsByCondition();
			switch(super.condition.getOperation()) {
				case CompoundConditionSort._AND:					
					if (identifiers == null) {
						identifiers = idsByCondition;
					} else {
						for(Iterator<Identifier> it = identifiers.iterator(); it.hasNext();) {
							Identifier id = it.next();
							if(!idsByCondition.contains(id)) {
								it.remove();
							}
						}
					}
					break;
				case CompoundConditionSort._OR:
					if (identifiers == null) {
						identifiers = idsByCondition;
					} else {
						identifiers.addAll(idsByCondition);
					}
					break;
				default:
					throw new IllegalArgumentException("XMLCompoundCondition.getIdsByCondition Unsupported operation " + operation);					
			}
		}
		if (identifiers == null) {
			identifiers = Collections.emptySet();
		}
		return identifiers;
	}
	
	private XMLStorableObjectCondition reflectXMLCondition(final StorableObjectCondition soCondition) {
		XMLStorableObjectCondition xmlStorableObjectCondition = null;
		Class soConditionClazz = soCondition.getClass();
		if (soConditionClazz.isAnonymousClass()) {
			soConditionClazz = soConditionClazz.getSuperclass();
		}
		final String className = soConditionClazz.getName();		
		final int lastPoint = className.lastIndexOf('.');
		final String xmlClassName = className.substring(0, lastPoint + 1) 
			+ "XML" 
			+ className.substring(lastPoint + 1);
		try {
			final Class clazz = Class.forName(xmlClassName);
			final Constructor constructor = 
				clazz.getDeclaredConstructor(
					new Class[] {
							soConditionClazz,
							StorableObjectXMLDriver.class});
			constructor.setAccessible(true);
			xmlStorableObjectCondition = 
				(XMLStorableObjectCondition) constructor.newInstance(
					new Object[] {soCondition, this.driver});
		} catch (ClassNotFoundException e) {
			Log.debugMessage(COMPOUND_CONDITION_INIT + "Class " + className
				+ " not found on the classpath"
			, Level.WARNING);
			Log.errorMessage(e);
		} catch (SecurityException e) {
			Log.errorMessage(e);
		} catch (NoSuchMethodException e) {
			Log.errorMessage(e);
			Log.debugMessage(COMPOUND_CONDITION_INIT + "class "
				+ className + " doesn't have the constructor expected"
			, Level.WARNING);
		} catch (IllegalArgumentException e) {
			Log.errorMessage(e);
		} catch (InstantiationException e) {
			Log.errorMessage(e);
		} catch (IllegalAccessException e) {
			Log.errorMessage(e);
		} catch (InvocationTargetException e) {
			final Throwable cause = e.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			} else {
				Log.errorMessage(e);
			}
		}
		return xmlStorableObjectCondition;
	}
}

