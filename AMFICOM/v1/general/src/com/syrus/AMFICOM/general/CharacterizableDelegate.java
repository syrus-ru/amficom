/*-
 * $Id: CharacterizableDelegate.java,v 1.1 2005/08/08 14:21:01 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.HashSet;
import java.util.Set;

/**
 * @version $Revision: 1.1 $, $Date: 2005/08/08 14:21:01 $
 * @author $Author: arseniy $
 * @module general
 */
public final class CharacterizableDelegate {
	private Identifier characterizableId;
	private StorableObjectCondition condition;
	private Set<Characteristic> characteristics;

	public CharacterizableDelegate(final Identifier characterizableId) {
		this.characterizableId = characterizableId;
		this.condition = new LinkedIdsCondition(this.characterizableId, ObjectEntities.CHARACTERISTIC_CODE);
	}

	public Set<Characteristic> getCharacteristics(final boolean usePool) throws ApplicationException {
		if (this.characteristics == null || usePool) {
			final Set<Characteristic> chs = StorableObjectPool.getStorableObjectsByCondition(this.condition, true);
			this.characteristics = new HashSet<Characteristic>(chs);
		}
		return this.characteristics;
	}
}
