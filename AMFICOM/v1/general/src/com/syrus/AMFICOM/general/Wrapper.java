/*
* $Id: Wrapper.java,v 1.3 2005/02/01 06:19:46 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.util.List;


/**
 * Wrapper provides data from Model (such as StorableObject, ObjectResource) using key set.
 * 
 * Model has various fields, accessors for them and many other things,
 * which are represented through controller to viewers using the same interface
 * of interaction.
 * 
 * All entities of the same kind use a single Wrapper, that's why
 * wrapper's constructor must be private and its instance must be obtained
 * using a static method <code>getInstance()</code>. 
 *
 * @author $Author: bob $
 * @version $Revision: 1.3 $, $Date: 2005/02/01 06:19:46 $
 * @see <a href = "http://bass.science.syrus.ru/java/Bitter%20Java.pdf">&laquo;Bitter Java&raquo; by Bruce A. Tate</a>
 * @module general_v1
 */
public interface Wrapper {

	/**
	 * Getter for entity key by index.
	 *
	 * @param index
	 * @return key, if index in the range of keys array, <code>null</code>
	 *         otherwise 
	 */
	String getKey(final int index);
	
	/**
	 * Entity keys of controller must be unique and cannot be changed.
	 * This implementation uses unmodified List.
	 * 
	 * 
	 *
	 * @return array of keys
	 */
	List getKeys();

	/**
	 * Name of the entity represented by <code>key</code>.
	 *
	 * @param key see {@link #getKeys()}
	 * @return
	 */
	String getName(final String key);

	/**
	 *
	 * @param key
	 * @return {@link java.util.Map}<code>.class</code> for limited range of
	 *         values, {@link String}<code>.class</code> otherwise.
	 * @see #getPropertyValue(String)
	 */
	Class getPropertyClass(final String key);

	/**
	 * If some entities have limited range of values represented as a set
	 * of properties, method returns a map of property values
	 * &lt; Key , Value &gt;. <code>Key</code>&apos;ll be used by the
	 * representation, <code>Value</code> is a value of Model, for the
	 * representation chosen, e. g.:<table border="1"><tbody>
	 * <tr><th>Key</th><th>Value</th></tr>
	 * <tr><td><tt>red</tt></td><td>{@link java.awt.Color#RED}</td></tr>
	 * <tr><td><tt>blue</tt></td><td>{@link java.awt.Color#BLUE}</td></tr>
	 * </tbody></table>
	 * 
	 * @param key entity key, see {@link #getKeys()}
	 * @return
	 */
	Object getPropertyValue(final String key);
	
	/**
	 * @param key
	 * @param objectKey
	 * @param objectValue
	 * @see #getPropertyValue(String)
	 */
	void setPropertyValue(final String key, final Object objectKey, final Object objectValue);

	/**
	 * Model value witch linked to entity key.
	 * 	 
	 * There is no more reason operate with StorableObject header.
	 * 
	 * @param Object object
	 * @param key entity key
	 * @return
	 */
	Object getValue(final Object object, final String key);

	/**
	 * Returns <code>true</code> if the entity represented by
	 * <code>key</code> can be modified; <code>false</code> otherwise.
	 *
	 * @param key
	 * @return <code>true</code> if this entity can be modified;
	 * <code>false</code> otherwise.
	 */
	boolean isEditable(final String key);

	/**
	 * Sets the value for Model entity.
	 * 
	 * There is no more reason operate with StorableObject header. 
	 *
	 * @param Object object
	 * @param key entity key
	 * @param value value, which will be assigned to Model
	 */
	void setValue(Object object, final String key, final Object value);

}
