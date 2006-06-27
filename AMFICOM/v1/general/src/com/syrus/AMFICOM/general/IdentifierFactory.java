/*-
 * $Id: IdentifierFactory.java,v 1.1.2.1 2006/06/27 15:30:08 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.Set;

/**
 * Интерфейс для всех источников новых идентификаторов. Нужен для
 * {@link IdentifierPool}.
 * 
 * @version $Revision: 1.1.2.1 $, $Date: 2006/06/27 15:30:08 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public interface IdentifierFactory {

	Identifier getGeneratedIdentifier(final short entityCode)
			throws CommunicationException,
				IdentifierGenerationException,
				IllegalObjectEntityException;

	Set<Identifier> getGeneratedIdentifierRange(final short entityCode, final int size)
			throws CommunicationException,
				IdentifierGenerationException,
				IllegalObjectEntityException;
}
