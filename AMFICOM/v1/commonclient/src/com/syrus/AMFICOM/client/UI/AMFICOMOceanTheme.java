/*-
 * $Id: AMFICOMOceanTheme.java,v 1.1 2006/05/29 09:35:59 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.awt.Font;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.OceanTheme;

import com.syrus.AMFICOM.client.model.AbstractApplication;
import com.syrus.util.ApplicationProperties;


public class AMFICOMOceanTheme extends OceanTheme {
	// Contants identifying the various Fonts that are Theme can support
	static final int CONTROL_TEXT_FONT = 0;
	static final int SYSTEM_TEXT_FONT = 1;
	static final int USER_TEXT_FONT = 2;
	static final int MENU_TEXT_FONT = 3;
	static final int WINDOW_TITLE_FONT = 4;
	public static final int SUB_TEXT_FONT = 5;

	private static final String[] defaultNames = {
		"swing.plaf.metal.controlFont",
		"swing.plaf.metal.systemFont",
		"swing.plaf.metal.userFont",
		"swing.plaf.metal.menuFont",
		"swing.plaf.metal.titleFont",
		"swing.plaf.metal.smallFont"
	};
	/**
	* Names of the fonts to use.
	*/
	private static final String[] fontNames = {
		"Dialog", "Dialog", "Dialog", "Serif", "Serif", "Dialog"
	};
	/**
	* Styles for the fonts.
	*/
	private static final int[] fontStyles = {
		Font.PLAIN, Font.PLAIN, Font.PLAIN, Font.BOLD, Font.BOLD, Font.PLAIN
	};
	/**
	* Sizes for the fonts.
	*/
	private static int[] fontSizes;

	private FontDelegate fontDelegate;
	/**
	 * Returns the ideal font name for the font identified by key.
	 */
	static String getDefaultFontName(int key) {
		return fontNames[key];
	}

	/**
	 * Returns the ideal font size for the font identified by key.
	 */
	static int getDefaultFontSize(int key) {
		return fontSizes[key];
	}

	/**
	 * Returns the ideal font style for the font identified by key.
	 */
	static int getDefaultFontStyle(int key) {
		return fontStyles[key];
	}

	/**
	 * Returns the default used to look up the specified font.
	 */
	static String getDefaultPropertyName(int key) {
		return defaultNames[key];
	}

	public AMFICOMOceanTheme() {
		this.install();
		UIDefaults def = UIManager.getDefaults();
		def.put("ComboBox.disabledBackground", def.get("ComboBox.background"));
	}

	void install() {
		int controlFontSize = Integer.parseInt(ApplicationProperties.getString(
				AbstractApplication.CONTROL_FONT_SIZE,	"12"));
		int systemFontSize = Integer.parseInt(ApplicationProperties.getString(
				AbstractApplication.SYSTEM_FONT_SIZE,	"11"));
		int subFontSize = Integer.parseInt(ApplicationProperties.getString(
				AbstractApplication.SUB_FONT_SIZE,	"10"));
		fontSizes = new int[] {
				controlFontSize, systemFontSize, systemFontSize, 
				controlFontSize, controlFontSize, subFontSize
			};
		
		this.fontDelegate = new FontDelegate();
	}

	@Override
	public FontUIResource getControlTextFont() {
		return this.getFont(CONTROL_TEXT_FONT);
	}

	@Override
	public FontUIResource getSystemTextFont() {
		return this.getFont(SYSTEM_TEXT_FONT);
	}

	@Override
	public FontUIResource getUserTextFont() {
		return this.getFont(USER_TEXT_FONT);
	}

	@Override
	public FontUIResource getMenuTextFont() {
		return this.getFont(MENU_TEXT_FONT);
	}

	@Override
	public FontUIResource getWindowTitleFont() {
		return this.getFont(WINDOW_TITLE_FONT);
	}

	@Override
	public FontUIResource getSubTextFont() {
		return this.getFont(SUB_TEXT_FONT);
	}

	private FontUIResource getFont(final int key) {
		return this.fontDelegate.getFont(key);
	}

	/**
	 * FontDelegates add an extra level of indirection to obtaining fonts.
	 */
	private static class FontDelegate {
		private static int[] defaultMapping = { CONTROL_TEXT_FONT,
				SYSTEM_TEXT_FONT,
				USER_TEXT_FONT,
				MENU_TEXT_FONT,
				WINDOW_TITLE_FONT,
				SUB_TEXT_FONT };
		FontUIResource fonts[];

		// menu and window are mapped to controlFont
		public FontDelegate() {
			this.fonts = new FontUIResource[6];
		}

		public FontUIResource getFont(int type) {
			type = defaultMapping[type];
			if (this.fonts[type] == null) {
				Font f = getPrivilegedFont(type);

				if (f == null) {
					f = new Font(getDefaultFontName(type), getDefaultFontStyle(type), getDefaultFontSize(type));
				}
				this.fonts[type] = new FontUIResource(f);
			}
			return this.fonts[type];
		}

		/**
		 * This is the same as invoking <code>Font.getFont(key)</code>, with the
		 * exception that it is wrapped inside a <code>doPrivileged</code> call.
		 */
		@SuppressWarnings("unchecked")
		protected Font getPrivilegedFont(final int key) {
			return (Font) AccessController.doPrivileged(new PrivilegedAction() {
				public Object run() {
					return Font.getFont(getDefaultPropertyName(key));
				}
			});
		}
	}
}
