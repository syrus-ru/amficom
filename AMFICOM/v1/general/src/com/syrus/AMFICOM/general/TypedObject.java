/*
 * $Id: TypedObject.java,v 1.9 2005/09/14 18:51:56 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.9 $, $Date: 2005/09/14 18:51:56 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */

public interface TypedObject<T extends StorableObjectType> {
	String ID_CODENAME = "codename" + TransferableObject.KEY_VALUE_SEPERATOR;
	String ID_DESCRIPTION = "description"  + TransferableObject.KEY_VALUE_SEPERATOR ;

	T getType();
}
