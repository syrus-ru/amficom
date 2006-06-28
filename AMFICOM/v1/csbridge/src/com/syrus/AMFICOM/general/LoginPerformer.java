/*-
 * $Id: LoginPerformer.java,v 1.1 2006/02/17 11:31:21 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.util.Set;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.security.SessionKey;

/**
 * @version $Revision: 1.1 $, $Date: 2006/02/17 11:31:21 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public interface LoginPerformer {

	public Set<Domain> getAvailableDomains() throws CommunicationException, LoginException;

	public void login(final String login, final String password, final Identifier loginDomainId)
			throws CommunicationException,
				LoginException;

	public void logout() throws CommunicationException, LoginException;

	public boolean isLoggedIn();

	public void restoreLogin(final String login, final String password) throws CommunicationException, LoginException;

	public SessionKey getSessionKey();

	public Identifier getUserId();

	public Identifier getDomainId();

	public void setPassword(final String password) throws CommunicationException, LoginException;

	public void setPassword(final Identifier systemUserId, final String password) throws CommunicationException, LoginException;
}
