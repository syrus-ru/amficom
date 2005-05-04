/*
 * $Id: LoginRestorer.java,v 1.1 2005/05/04 11:12:07 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/04 11:12:07 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public interface LoginRestorer {

	boolean restoreLogin();

	String getLogin();

	String getPassword();
}
