/*-
* $Id: XMLEquivalentCondition.java,v 1.1 2005/08/19 14:02:46 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.util.Set;


/**
 * @version $Revision: 1.1 $, $Date: 2005/08/19 14:02:46 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module general
 */
public class XMLEquivalentCondition extends XMLStorableObjectCondition<EquivalentCondition> {

	@SuppressWarnings("unused")
	private XMLEquivalentCondition(final EquivalentCondition condition,
	                            final StorableObjectXMLDriver driver) {
		super(condition, driver);
	}

	@Override 
	public Set<Identifier> getIdsByCondition() throws IllegalDataException {
		return super.getIdsByCondition(super.getBaseQuery(), false);
	}
}

