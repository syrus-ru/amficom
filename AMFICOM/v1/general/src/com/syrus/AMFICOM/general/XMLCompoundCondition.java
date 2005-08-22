/*-
* $Id: XMLCompoundCondition.java,v 1.2 2005/08/22 12:08:50 bob Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/08/22 12:08:50 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module general
 */
public class XMLCompoundCondition extends XMLStorableObjectCondition<CompoundCondition> {
	
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
		Class< ? extends StorableObjectCondition> clazz1 = soCondition.getClass();
		final String className = clazz1.isAnonymousClass() ?
				clazz1.getSuperclass().getName() : 
				clazz1.getName();		
		final int lastPoint = className.lastIndexOf('.');
		final String xmlClassName = className.substring(0, lastPoint + 1) 
			+ "XML" 
			+ className.substring(lastPoint + 1);
		try {
			Log.debugMessage("XMLCompoundCondition.reflectXMLCondition | try reflect " + xmlClassName,
				Level.FINEST);
			final Class clazz = Class.forName(xmlClassName);
			final Constructor constructor = 
				clazz.getDeclaredConstructor(
					new Class[] {
							soCondition.getClass(),
							StorableObjectXMLDriver.class});
			constructor.setAccessible(true);
			xmlStorableObjectCondition = 
				(XMLStorableObjectCondition) constructor.newInstance(
					new Object[] {soCondition, this.driver});
		} catch (ClassNotFoundException e) {
			Log.errorException(e);
		} catch (SecurityException e) {
			Log.errorException(e);
		} catch (NoSuchMethodException e) {
			Log.errorException(e);
		} catch (IllegalArgumentException e) {
			Log.errorException(e);
		} catch (InstantiationException e) {
			Log.errorException(e);
		} catch (IllegalAccessException e) {
			Log.errorException(e);
		} catch (InvocationTargetException e) {
			final Throwable cause = e.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			} else {
				Log.errorException(e);
			}
		}
		return xmlStorableObjectCondition;
	}
}

