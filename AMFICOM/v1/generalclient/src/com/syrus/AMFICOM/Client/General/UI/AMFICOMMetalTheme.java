package com.syrus.AMFICOM.Client.General.UI;

import java.awt.Font;
import javax.swing.*;

import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

public class AMFICOMMetalTheme extends DefaultMetalTheme
{
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
	private static final int[] fontSizes = {
		12, 11, 11, 12, 12, 10
	};

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

	public AMFICOMMetalTheme()
	{
		install();
		UIDefaults def = UIManager.getDefaults();
		def.put("ComboBox.disabledBackground", def.get("ComboBox.background"));
	}

	void install() {
		fontDelegate = new FontDelegate();
	}

	public FontUIResource getControlTextFont() {
		return getFont(CONTROL_TEXT_FONT);
	}

	public FontUIResource getSystemTextFont() {
		return getFont(SYSTEM_TEXT_FONT);
	}

	public FontUIResource getUserTextFont() {
		return getFont(USER_TEXT_FONT);
	}

	public FontUIResource getMenuTextFont() {
		return getFont(MENU_TEXT_FONT);
	}

	public FontUIResource getWindowTitleFont() {
		return getFont(WINDOW_TITLE_FONT);
	}

	public FontUIResource getSubTextFont() {
		return getFont(SUB_TEXT_FONT);
	}

	private FontUIResource getFont(int key) {
		return fontDelegate.getFont(key);
	}

	/**
	 * FontDelegates add an extra level of indirection to obtaining fonts.
	 */
	private static class FontDelegate {
		private static int[] defaultMapping = {
			CONTROL_TEXT_FONT, SYSTEM_TEXT_FONT,
			USER_TEXT_FONT, MENU_TEXT_FONT,
			WINDOW_TITLE_FONT, SUB_TEXT_FONT
		};
		FontUIResource fonts[];

		// menu and window are mapped to controlFont
		public FontDelegate() {
			fonts = new FontUIResource[6];
		}

		public FontUIResource getFont(int type) {
			type = defaultMapping[type];
			if (fonts[type] == null) {
				Font f = getPrivilegedFont(type);

				if (f == null) {
					f = new Font(getDefaultFontName(type),
											 getDefaultFontStyle(type),
											 getDefaultFontSize(type));
				}
				fonts[type] = new FontUIResource(f);
			}
			return fonts[type];
		}

		/**
		 * This is the same as invoking
		 * <code>Font.getFont(key)</code>, with the exception
		 * that it is wrapped inside a <code>doPrivileged</code> call.
		 */
		protected Font getPrivilegedFont(final int key) {
			return (Font)java.security.AccessController.doPrivileged(
					new java.security.PrivilegedAction() {
				public Object run() {
					return Font.getFont(getDefaultPropertyName(key));
				}
			}
			);
		}
	}

	public ColorUIResource getControl() {
			return getSecondary3();//new ColorUIResource(Color.green);
	}
	public ColorUIResource getControlShadow() {
			return getSecondary2();
	}
	public ColorUIResource getControlDarkShadow() {
			return getSecondary1();
	}
	public ColorUIResource getControlInfo() {
			return getBlack();
	}
	public ColorUIResource getControlHighlight() {
			return getWhite();
	}
	public ColorUIResource getControlDisabled() {
			return getSecondary2();
	}

	public ColorUIResource getPrimaryControl() {
			return getPrimary3();
	}
	public ColorUIResource getPrimaryControlShadow() {
			return getPrimary2();
	}
	public ColorUIResource getPrimaryControlDarkShadow() {
			return getPrimary1();
	}
	public ColorUIResource getPrimaryControlInfo() {
			return getBlack();
	}
	public ColorUIResource getPrimaryControlHighlight() {
			return getWhite();
	}

	/**
	 * Returns the color used, by default, for the text in labels
	 * and titled borders.
	 */
	public ColorUIResource getSystemTextColor() {
			return getBlack();
	}
	public ColorUIResource getControlTextColor() {
			return getControlInfo();
	}
	public ColorUIResource getInactiveControlTextColor() {
			return getControlDisabled();
	}
	public ColorUIResource getInactiveSystemTextColor() {
			return getSecondary2();
	}
	public ColorUIResource getUserTextColor() {
			return getBlack();
	}
	public ColorUIResource getTextHighlightColor() {
			return getPrimary3();
	}
	public ColorUIResource getHighlightedTextColor() {
			return getControlTextColor();
	}

	public ColorUIResource getWindowBackground() {
			return getWhite();
	}
	public ColorUIResource getWindowTitleBackground() {
			return getPrimary3(); }
	public ColorUIResource getWindowTitleForeground() {
			return getBlack();
	}
	public ColorUIResource getWindowTitleInactiveBackground() {
			return getSecondary3();
	}
	public ColorUIResource getWindowTitleInactiveForeground() {
			return getBlack();
	}

	public ColorUIResource getMenuBackground() {
			return getSecondary3();
	}
	public ColorUIResource getMenuForeground() {
			return  getBlack();
	}
	public ColorUIResource getMenuSelectedBackground() {
			return getPrimary2();
	}
	public ColorUIResource getMenuSelectedForeground() {
			return getBlack();
	}
	public ColorUIResource getMenuDisabledForeground() {
			return getSecondary2();
	}
	public ColorUIResource getSeparatorBackground() {
			return getWhite();
	}
	public ColorUIResource getSeparatorForeground() {
			return getPrimary1();
	}
	public ColorUIResource getAcceleratorForeground() {
			return getPrimary1();
	}
	public ColorUIResource getAcceleratorSelectedForeground() {
			return getBlack();
	}


}
