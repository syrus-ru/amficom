/*
 * $Id: CharacteristicSeqContainer.java,v 1.1 2004/12/21 15:35:01 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.Characteristic;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/12/21 15:35:01 $
 * @module scheme_v1
 */
public final class CharacteristicSeqContainer {
	private List value = new LinkedList();

	public CharacteristicSeqContainer(final Characteristic value[]) {
		this(Arrays.asList(value));
	}

	public CharacteristicSeqContainer(final List value) {
		if (value != null)
			this.value.addAll(value);
	}

	/**
	 * @return this container's value masked by an unmodifiable list.
	 */
	public List getValue() {
		return Collections.unmodifiableList(this.value);
	}

	/**
	 * @return a newly created array on every invocation.
	 */
	public Characteristic[] getValueAsArray() {
		return (Characteristic[]) this.value.toArray(new Characteristic[this.value.size()]);
	}
}
