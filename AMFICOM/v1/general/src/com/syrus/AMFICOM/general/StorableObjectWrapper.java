/*
 * $Id: StorableObjectWrapper.java,v 1.1 2005/02/03 08:37:26 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.util.Wrapper;

/**
 * StorableObjectWrapper provides data from Model (such as StorableObject,
 * ObjectResource) using key set.
 * 
 * Model has various fields, accessors for them and many other things, which are
 * represented through controller to viewers using the same interface of
 * interaction.
 * 
 * All entities of the same kind use a single StorableObjectWrapper, that's why
 * wrapper's constructor must be private and its instance must be obtained using
 * a static method <code>getInstance()</code>.
 * 
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2005/02/03 08:37:26 $
 * @see <a href =
 *      "http://bass.science.syrus.ru/java/Bitter%20Java.pdf">&laquo;Bitter
 *      Java&raquo; by Bruce A. Tate </a>
 * @module general_v1
 */
public interface StorableObjectWrapper extends Wrapper {

	final String	COLUMN_CREATED					= "created";
	final String	COLUMN_CREATOR_ID				= "creator_id";
	final String	COLUMN_ID						= "id";
	final String	COLUMN_MODIFIED					= "modified";
	final String	COLUMN_MODIFIER_ID				= "modifier_id";

	final String	COLUMN_CODENAME					= "codename";
	final String	COLUMN_DESCRIPTION				= "description";
	final String	COLUMN_NAME						= "name";

	final String	COLUMN_TYPE_ID					= "type_id";

	final String	COLUMN_CHARACTERISTICS			= "characteristics";

	final String	LINK_COLUMN_PARAMETER_MODE		= "parameter_mode";
	final String	LINK_COLUMN_PARAMETER_TYPE_ID	= "parameter_type_id";

}
