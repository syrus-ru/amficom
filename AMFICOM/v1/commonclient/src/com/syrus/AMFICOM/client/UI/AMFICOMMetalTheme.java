package com.syrus.AMFICOM.client.UI;

import java.awt.Font;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

/**
 * @version $Revision: 1.3 $, $Date: 2005/09/09 18:54:27 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class AMFICOMMetalTheme extends DefaultMetalTheme {
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

	public AMFICOMMetalTheme() {
		this.install();
		UIDefaults def = UIManager.getDefaults();
		def.put("ComboBox.disabledBackground", def.get("ComboBox.background"));
	}

	void install() {
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

	@Override
	public ColorUIResource getControl() {
			return super.getSecondary3();//new ColorUIResource(Color.green);
	}
	@Override
	public ColorUIResource getControlShadow() {
			return super.getSecondary2();
	}
	@Override
	public ColorUIResource getControlDarkShadow() {
			return super.getSecondary1();
	}
	@Override
	public ColorUIResource getControlInfo() {
			return super.getBlack();
	}
	@Override
	public ColorUIResource getControlHighlight() {
			return super.getWhite();
	}
	@Override
	public ColorUIResource getControlDisabled() {
			return super.getSecondary2();
	}

	@Override
	public ColorUIResource getPrimaryControl() {
			return super.getPrimary3();
	}
	@Override
	public ColorUIResource getPrimaryControlShadow() {
			return super.getPrimary2();
	}
	@Override
	public ColorUIResource getPrimaryControlDarkShadow() {
			return super.getPrimary1();
	}
	@Override
	public ColorUIResource getPrimaryControlInfo() {
			return super.getBlack();
	}
	@Override
	public ColorUIResource getPrimaryControlHighlight() {
			return super.getWhite();
	}

	/**
	 * Returns the color used, by default, for the text in labels
	 * and titled bord@Override
	ers.
	 */
	@Override
	public ColorUIResource getSystemTextColor() {
			return super.getBlack();
	}
	@Override
	public ColorUIResource getControlTextColor() {
			return super.getControlInfo();
	}
	@Override
	public ColorUIResource getInactiveControlTextColor() {
			return super.getControlDisabled();
	}
	@Override
	public ColorUIResource getInactiveSystemTextColor() {
			return super.getSecondary2();
	}
	@Override
	public ColorUIResource getUserTextColor() {
			return super.getBlack();
	}
	@Override
	public ColorUIResource getTextHighlightColor() {
			return super.getPrimary3();
	}
	@Override
	public ColorUIResource getHighlightedTextColor() {
			return super.getControlTextColor();
	}

	@Override
	public ColorUIResource getWindowBackground() {
			return super.getWhite();
	}
	@Override
	public ColorUIResource getWindowTitleBackground() {
			return super.getPrimary3(); 
	}
	@Override
	public ColorUIResource getWindowTitleForeground() {
			return super.getBlack();
	}
	@Override
	public ColorUIResource getWindowTitleInactiveBackground() {
			return super.getSecondary3();
	}
	@Override
	public ColorUIResource getWindowTitleInactiveForeground() {
			return super.getBlack();
	}

	@Override
	public ColorUIResource getMenuBackground() {
		return super.getSecondary3();
	}

	@Override
	public ColorUIResource getMenuForeground() {
		return super.getBlack();
	}

	@Override
	public ColorUIResource getMenuSelectedBackground() {
		return super.getPrimary2();
	}

	@Override
	public ColorUIResource getMenuSelectedForeground() {
		return super.getBlack();
	}

	@Override
	public ColorUIResource getMenuDisabledForeground() {
		return super.getSecondary2();
	}

	@Override
	public ColorUIResource getSeparatorBackground() {
		return super.getWhite();
	}

	@Override
	public ColorUIResource getSeparatorForeground() {
		return super.getPrimary1();
	}

	@Override
	public ColorUIResource getAcceleratorForeground() {
		return super.getPrimary1();
	}

	@Override
	public ColorUIResource getAcceleratorSelectedForeground() {
		return super.getBlack();
	}

//	protected ColorUIResource getSecondary3() { return secondary3; }
//  private static final ColorUIResource secondary3 = new ColorUIResource(
//      234, 234, 234);
}
