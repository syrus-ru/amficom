/*
 * $Id: ResourceKeys.java,v 1.14 2005/10/21 12:36:54 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.resource;

/**
 * @version $Revision: 1.14 $, $Date: 2005/10/21 12:36:54 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public interface ResourceKeys {

	String	SIMPLE_DATE_FORMAT						= "Common.DateFormat.simpleDateFormat";
	String	HOURS_MINUTES_SECONDS_DATE_FORMAT		= "Common.DateFormat.hmsDateFormat";

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
	String	ICON_FURTHER							= "com.syrus.AMFICOM.icon.general.further";
	String	ICON_GENERAL							= "com.syrus.AMFICOM.icon.general";
	String	ICON_INTRODUCE							= "com.syrus.AMFICOM.icon.general.introduce";
	String	ICON_OPEN_FILE							= "com.syrus.AMFICOM.icon.general.openfile";
	String	ICON_ADD_FILE							= "com.syrus.AMFICOM.icon.general.addfile";
	String	ICON_REMOVE_FILE						= "com.syrus.AMFICOM.icon.general.removefile";
	String	ICON_REFRESH							= "com.syrus.AMFICOM.icon.general.refresh";
	String	ICON_ADD								= "com.syrus.AMFICOM.icon.general.add";
	String	ICON_COMMIT								= "com.syrus.AMFICOM.icon.general.commit";
	String	ICON_SYNCHRONIZE						= "com.syrus.AMFICOM.icon.general.synchronize";
	String	ICON_TIME_DATE							= "com.surus.AMFICOM.icon.general.timedate";

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

	String	I18N_CHARACTERISTIC						= "Characteristic.Text.Characteristic";										//$NON-NLS-1$"
	String	I18N_CHARACTERISTICTYPESORT_OPTICAL		= "Characteristic.Text.OpticalCharacteristics";								//$NON-NLS-1$
	String	I18N_CHARACTERISTICTYPESORT_ELECTRICAL	= "Characteristic.Text.ElectricalCharacteristics";							//$NON-NLS-1$
	String	I18N_CHARACTERISTICTYPESORT_OPERATIONAL	= "Characteristic.Text.OperationalCharacteristics";							//$NON-NLS-1$
	String	I18N_CHARACTERISTICTYPESORT_INTERFACE	= "Characteristic.Text.InterfaceCharacteristics";								//$NON-NLS-1$
	String	I18N_CHARACTERISTICTYPESORT_VISUAL		= "Characteristic.Text.VisualAttributes";										//$NON-NLS-1$

	String	I18N_ADD_CHARACTERISTIC					= "CharacteristicsPanel.Action.AddCharacteristic";												//$NON-NLS-1$
	String	I18N_REMOVE_CHARACTERISTIC				= "CharacteristicsPanel.Action.RemoveCharacteristic";											//$NON-NLS-1$
	String	I18N_NEW_CHARACTERISTICTYPE				= "CharacteristicAddDialog.Text.NewCharacteristicType";											//$NON-NLS-1$
	String	I18N_EXISTING_CHARACTERISTICTYPE		= "CharacteristicAddDialog.Text.ExistingCharacteristicType";									//$NON-NLS-1$

	String	I18N_ADD								= "Action.Text.Add";													//$NON-NLS-1$
	String	I18N_CANCEL								= "Action.Text.Cancel";												//$NON-NLS-1$

	String	I18N_NAME								= "Text.Name";													//$NON-NLS-1$
	String	I18N_DESCRIPTION						= "Text.Description";											//$NON-NLS-1$

	String	I18N_CHOOSE_COLOR						= "Common.ColorChooser.ChooseColor";											//$NON-NLS-1$
	String	I18N_CHOOSE								= "Common.ColorChooser.Choose";												//$NON-NLS-1$

	String	I18N_COMMIT		= "Action.Text.Commit";									//$NON-NLS-1$
}
