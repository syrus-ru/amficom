/*
 * $Id: StorableObjectWrapper.java,v 1.2 2005/02/03 08:52:48 bob Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/02/03 08:52:48 $
 * @see <a href =
 *      "http://bass.science.syrus.ru/java/Bitter%20Java.pdf">&laquo;Bitter
 *      Java&raquo; by Bruce A. Tate </a>
 * @module general_v1
 */
public interface StorableObjectWrapper extends Wrapper {

	String	COLUMN_CREATED					= "created";
	String	COLUMN_CREATOR_ID				= "creator_id";
	String	COLUMN_ID						= "id";
	String	COLUMN_MODIFIED					= "modified";
	String	COLUMN_MODIFIER_ID				= "modifier_id";

	String	COLUMN_CODENAME					= "codename";
	String	COLUMN_DESCRIPTION				= "description";
	String	COLUMN_NAME						= "name";

	String	COLUMN_TYPE_ID					= "type_id";

	String	COLUMN_CHARACTERISTICS			= "characteristics";

	String	LINK_COLUMN_PARAMETER_MODE		= "parameter_mode";
	String	LINK_COLUMN_PARAMETER_TYPE_ID	= "parameter_type_id";

}
