/*-
 * $Id: DatabaseIdentifierFactory.java,v 1.1.2.1 2006/06/27 15:51:17 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.Set;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/06/27 15:51:17 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public final class DatabaseIdentifierFactory implements IdentifierFactory {

	public Identifier getGeneratedIdentifier(final short entityCode)
			throws CommunicationException,
				IdentifierGenerationException,
				IllegalObjectEntityException {
		return IdentifierGenerator.generateIdentifier(entityCode);
	}

	public Set<Identifier> getGeneratedIdentifierRange(final short entityCode, final int size)
			throws CommunicationException,
				IdentifierGenerationException,
				IllegalObjectEntityException {
		return IdentifierGenerator.generateIdentifierRange(entityCode, size);
	}

}