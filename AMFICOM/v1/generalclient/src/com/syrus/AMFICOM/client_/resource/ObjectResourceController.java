
package com.syrus.AMFICOM.client_.resource;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Controller provides data from Model (ObjectResource) using key set.
 * 
 * Model has various fields, accessors for them and many other things,
 * which are represented through controller to viewers using the same interface
 * of interaction.
 * 
 * All entities of the same kind use a single Contoller, that's why
 * controller's constructor must be private and its instance must be obtained
 * using a static method <code>getInstance()</code>. 
 *
 * @author $Author: bob $
 * @version $Revision: 1.4 $, $Date: 2004/11/17 17:33:06 $
 * @see <a href = "http://bass.science.syrus.ru/java/Bitter%20Java.pdf">&laquo;Bitter Java&raquo; by Bruce A. Tate</a>
 * @module generalclient_v1
 */

public interface ObjectResourceController {

	final String		COLUMN_ALARM_TYPE_NAME	= "alarm_type_name";
	final String		COLUMN_GENERATED	= "generated";
	final String		COLUMN_KIS_ID		= "kis_id";
	final String		COLUMN_LOCAL_ID		= "local_id";
	final String		COLUMN_ME_ID		= "monitored_element_id";
	final String		COLUMN_PORT_ID		= "port_id";
	final String		COLUMN_SOURCE_NAME	= "source_name";
	final String		COLUMN_START_TIME	= "start_time";
	final String		COLUMN_STATUS		= "status";
	final String		COLUMN_TEMPORAL_TYPE	= "temporal_type";
	final String		COLUMN_TEST_TYPE_ID	= "test_type_id";

	final String		COLUMN_TYPE_LIST	= "list";
	final String		COLUMN_TYPE_LONG	= "long";
	final String		COLUMN_TYPE_NUMERIC	= "numeric";
	final String		COLUMN_TYPE_RANGE	= "range";
	final String		COLUMN_TYPE_STRING	= "string";
	final String		COLUMN_TYPE_TIME	= "time";

	final String		DATE_FORMAT		= "dd.MM.yy HH:mm:ss";
	final SimpleDateFormat	SIMPLE_DATE_FORMAT	= new SimpleDateFormat(DATE_FORMAT);

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
	 * @param objectResource Model
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
	 * @param objectResource Model
	 * @param key entity key
	 * @param value value, which will be assigned to Model
	 */
	void setValue(Object object, final String key, final Object value);
	
}
