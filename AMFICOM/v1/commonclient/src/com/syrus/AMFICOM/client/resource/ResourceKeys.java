/*
 * $Id: ResourceKeys.java,v 1.7 2005/06/23 14:44:52 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.resource;

/**
 * @version $Revision: 1.7 $, $Date: 2005/06/23 14:44:52 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module generalclient_v1
 */
public interface ResourceKeys {

	String	SIMPLE_DATE_FORMAT						= "simpleDateFormat";
	String	HOURS_MINUTES_SECONDS_DATE_FORMAT		= "hmsDateFormat";

	String	COLOR_GRAPHICS_BACKGROUND				= "graphicsBackgroundColor";

	/* Insets */
	String	INSETS_NULL								= "insets.null";
	String	INSETS_ICONED_BUTTON					= "insets.iconedButton";

	String	TABLE_NO_FOCUS_BORDER					= "Table.nofocusBorder";

	/* Sizes */
	String	SIZE_BUTTON								= "Button.size";
	String	SIZE_NULL								= "Null.size";

	/* Icons */
	String	ICON_OPEN_SESSION						= "com.syrus.AMFICOM.icon.general.opensession";
	String	ICON_CLOSE_SESSION						= "com.syrus.AMFICOM.icon.general.closesession";
	String	ICON_DOMAIN_SELECTION					= "com.syrus.AMFICOM.icon.general.domainselection";
	String	ICON_DELETE								= "com.syrus.AMFICOM.icon.general.delete";
	String	ICON_GENERAL							= "com.syrus.AMFICOM.icon.general";
	String	ICON_OPEN_FILE							= "com.syrus.AMFICOM.icon.general.openfile";
	String	ICON_ADD_FILE							= "com.syrus.AMFICOM.icon.general.addfile";
	String	ICON_REMOVE_FILE						= "com.syrus.AMFICOM.icon.general.removefile";
	String	ICON_REFRESH						= "com.syrus.AMFICOM.icon.general.refresh";
	String	ICON_ADD							= "com.syrus.AMFICOM.icon.general.add";
	String	ICON_COMMIT							= "com.syrus.AMFICOM.icon.general.commit";

	/* Mini Icons */
	Object	ICON_MINI_FOLDER						= "com.syrus.AMFICOM.icon.mini.general.folder";
	String	ICON_MINI_PATHMODE						= "com.syrus.AMFICOM.icon.mini.general.pathmode";
	Object	ICON_MINI_PORT							= "com.syrus.AMFICOM.icon.mini.general.port";
	Object	ICON_MINI_MEASUREMENT_SETUP				= "com.syrus.AMFICOM.icon.mini.general.measurementSetup";
	Object	ICON_MINI_RESULT						= "com.syrus.AMFICOM.icon.mini.general.result";
	Object	ICON_MINI_TESTING						= "com.syrus.AMFICOM.icon.mini.general.testing";

	/* Images */
	Object	IMAGE_LOGIN_LOGO						= "LoginLogo";

	/* Internationalization keys */
	String	I18N_ROOT								= "root";													//$NON-NLS-1$

	String	I18N_CHARACTERISTIC						= "characteristic";										//$NON-NLS-1$"
	String	I18N_CHARACTERISTICTYPESORT_OPTICAL		= "optical_characteristics";								//$NON-NLS-1$
	String	I18N_CHARACTERISTICTYPESORT_ELECTRICAL	= "electrical_characteristics";							//$NON-NLS-1$
	String	I18N_CHARACTERISTICTYPESORT_OPERATIONAL	= "operational_characteristics";							//$NON-NLS-1$
	String	I18N_CHARACTERISTICTYPESORT_INTERFACE	= "interface_characteristics";								//$NON-NLS-1$
	String	I18N_CHARACTERISTICTYPESORT_VISUAL		= "visual_attributes";										//$NON-NLS-1$

	String	I18N_ADD_CHARACTERISTIC					= "add_char";												//$NON-NLS-1$
	String	I18N_REMOVE_CHARACTERISTIC				= "remove_char";											//$NON-NLS-1$
	String	I18N_NEW_CHARACTERISTICTYPE				= "new_char_type";											//$NON-NLS-1$
	String	I18N_EXISTING_CHARACTERISTICTYPE		= "existing_char_type";									//$NON-NLS-1$

	String	I18N_ADD								= "add";													//$NON-NLS-1$
	String	I18N_CANCEL								= "cancel";												//$NON-NLS-1$

	String	I18N_NAME								= "name";													//$NON-NLS-1$
	String	I18N_DESCRIPTION						= "description";											//$NON-NLS-1$

	String	I18N_CHOOSE_COLOR						= "choose_color";											//$NON-NLS-1$
	String	I18N_CUSTOM								= "custom";												//$NON-NLS-1$

}
