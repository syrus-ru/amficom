/*-
 * $Id: ConditionKey.java,v 1.3 2005/05/18 12:42:50 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.newFilter;

import java.util.List;

import com.syrus.AMFICOM.general.ConditionWrapper;
import com.syrus.AMFICOM.general.ObjectEntities;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/18 12:42:50 $
 * @author $Author: bass $
 * @module filterclient_v1
 */

/**
 * The <code>ConditionKey</code> is used to create criteria for {@link com.syrus.AMFICOM.newFilter.Filter Filter}
 * @version $Revision: 1.3 $, $Date: 2005/05/18 12:42:50 $
 * @author $Author: bass $
 * @module filterclient_v1
 */
public class ConditionKey {
	
	private String 		name;
	private String 		key;
	private byte 		type;
	private short 		entityCodeOfLinkedEntity;
	private List		linkedObjects;
	private String[] 	constraintNames;
	
	/**
	 * This method creates key for {@link com.syrus.AMFICOM.general.TypicalCondition}
	 * @param key that is used in {@link com.syrus.AMFICOM.general.TypicalCondition} conctructor
	 * @param name criteria name in {@link com.syrus.AMFICOM.newFilter.Filter}
	 * @param type one from <ul><li>{@link com.syrus.AMFICOM.general.ConditionWrapper#INT}</li>
	 * 						<li>{@link com.syrus.AMFICOM.general.ConditionWrapper#FLOAT}</li>
	 * 						<li>{@link com.syrus.AMFICOM.general.ConditionWrapper#DOUBLE}</li></ul>
	 */
	public ConditionKey(String key, String name, byte type) {
		this.name = name;
		this.key = key;
		this.type = type;		
	}
	
	/**
	 * This method creates key for {@link com.syrus.AMFICOM.general.TypicalCondition}
	 * @param key that is used in {@link com.syrus.AMFICOM.general.TypicalCondition} conctructor
	 * @param name criteria name in {@link com.syrus.AMFICOM.newFilter.Filter}
	 * @param constraintNames are names which will be displayed in Filter.
	 */
	public ConditionKey(String key, String name, String[] constraintNames) {
		this(key, name, ConditionWrapper.CONSTRAINT);
		this.constraintNames = constraintNames;
	}
	/**
	 * This method creates key for {@link com.syrus.AMFICOM.general.LinkedIdsCondition}
	 * @param linkedEntityCode type of linked entities.
	 */
	public ConditionKey(short linkedEntityCode) {
		this.name = ObjectEntities.codeToString(linkedEntityCode);
		this.type = ConditionWrapper.LIST;
		this.entityCodeOfLinkedEntity = linkedEntityCode;
	}
	
	public List getLinkedObjects() {
		return this.linkedObjects;
	}
	public void setLinkedObjects(List linkedObjects) {
		this.linkedObjects = linkedObjects;
	}
	public String[] getConstraintNames() {
		return this.constraintNames;
	}
	public short getLinkedEntityCode() {
		return this.entityCodeOfLinkedEntity;
	}
	public String getKey() {
		return this.key;
	}
	public String getName() {
		return this.name;
	}
	public byte getType() {
		return this.type;
	}
}
