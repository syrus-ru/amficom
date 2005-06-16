/*-
 * $Id: AbstractApplication.java,v 1.2 2005/06/16 09:54:34 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import com.incors.plaf.kunststoff.KunststoffLookAndFeel;

import com.syrus.AMFICOM.client.UI.AMFICOMMetalTheme;
import com.syrus.AMFICOM.client.UI.dialogs.ModuleCodeDialog;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.resource.LangModel;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ClientSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/16 09:54:34 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient_v1
 */
public abstract class AbstractApplication {

	public static final String KEY_MODULE_CODE = "ModuleCode";
	public static final String KEY_MODULE_TITLE = "ModuleTitle";
	
	public static final String KEY_LOOK_AND_FEEL = "LookAndFeel";
	
	public static final String	LOOK_AND_FEEL_KUNSTSTOFF	= "Kunststoff";
	public static final String	LOOK_AND_FEEL_METAL			= "Metal";
	public static final String	LOOK_AND_FEEL_MOTIF			= "Motif";
	public static final String	LOOK_AND_FEEL_WINDOWS		= "Windows";
	
	protected ApplicationContext	aContext;

	protected Dispatcher			dispatcher;

	private static LoginRestorer	loginRestorer;

	protected String applicationTitle;
	protected String applicationCode;
	
	private static boolean resourcesInitialized = false;
	private static boolean themeInitialized = false;
	
	public AbstractApplication(String applicationTitle, String applicationCode) {
		this.applicationTitle = applicationTitle;
		this.applicationCode = applicationCode;
		this.initTheme();
		if (this.isLaunchable()) {
			this.aContext = new ApplicationContext();
			this.dispatcher = new Dispatcher();
			this.aContext.setDispatcher(this.dispatcher);
			this.init();
		} else {
			Environment.checkForExit();
		}
	}
	
	public AbstractApplication() {
		this(ApplicationProperties.getString(KEY_MODULE_TITLE, ""), ApplicationProperties.getString(KEY_MODULE_CODE, null));
	}
	
	private void initResources() {
		if (!resourcesInitialized) {
			synchronized(this) {
				if (!resourcesInitialized) {
					this.initTheme();
					this.initUIConstats();					
					resourcesInitialized = true;
				}
			}
		}
	}

	private void initTheme() {
		if (!themeInitialized) {
			synchronized (this) {
				if (!themeInitialized) {
					JFrame.setDefaultLookAndFeelDecorated(true);
					JDialog.setDefaultLookAndFeelDecorated(true);

					try {
						UIManager.setLookAndFeel(this.getLookAndFeel());
					} catch (UnsupportedLookAndFeelException ulfe) {
						Log.errorException(ulfe);
					}
					themeInitialized = true;
				}
			}
		}
	}
	
	private LookAndFeel getLookAndFeel() {
		String lookAndFeel = ApplicationProperties.getString(KEY_LOOK_AND_FEEL, LOOK_AND_FEEL_WINDOWS);
		LookAndFeel lnf = null;
		if (lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_METAL)) {
			MetalLookAndFeel.setCurrentTheme(new AMFICOMMetalTheme());
			lnf = new MetalLookAndFeel();
		} else if (lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_KUNSTSTOFF)) {
			KunststoffLookAndFeel.setCurrentTheme(new AMFICOMMetalTheme());
			lnf = new KunststoffLookAndFeel();
		} else if (lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_WINDOWS)) {
			lnf = new WindowsLookAndFeel();
		} else if (lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_MOTIF)) {
			lnf = new MotifLookAndFeel();
		}
//			else if (lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_GTK)) {
//				plaf = (LookAndFeel) (GTKLookAndFeel.class.newInstance());
//		}
		else {
			return this.getDefaultLookAndFeel();
		}
		if (lnf.isSupportedLookAndFeel()) {
			return lnf;
		}
		return this.getDefaultLookAndFeel();
	}

	/**
	 * Returns a fail-safe pluggable look-and-feel.
	 */
	private LookAndFeel getDefaultLookAndFeel() {
		try {
			return (LookAndFeel) (Class.forName(UIManager.getSystemLookAndFeelClassName()).newInstance());
		} catch (Exception e) {
			return UIManager.getLookAndFeel();
		}
	}
	
	private void initUIConstats() {
		UIManager.put(ResourceKeys.SIMPLE_DATE_FORMAT, new SimpleDateFormat(LangModel
				.getString(ResourceKeys.SIMPLE_DATE_FORMAT)));
		
		UIManager.put(ResourceKeys.HOURS_MINUTES_SECONDS_DATE_FORMAT, new SimpleDateFormat(LangModel
			.getString(ResourceKeys.HOURS_MINUTES_SECONDS_DATE_FORMAT)));
		
		UIManager.put(ResourceKeys.ICON_OPEN_SESSION, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/open_session.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		
		UIManager.put(ResourceKeys.ICON_CLOSE_SESSION, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
		"images/close_session.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		
		UIManager.put(ResourceKeys.ICON_DOMAIN_SELECTION, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
		"images/domains.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		
		UIManager.put(ResourceKeys.ICON_GENERAL, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/general.gif")));
		UIManager.put(ResourceKeys.ICON_DELETE, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
		"images/delete.gif")));

		UIManager.put(ResourceKeys.ICON_OPEN_FILE, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/openfile.gif")));

		UIManager.put(ResourceKeys.ICON_ADD_FILE, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/addfile.gif")));
		UIManager.put(ResourceKeys.ICON_REMOVE_FILE, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/removefile.gif")));

		UIManager.put(ResourceKeys.ICON_MINI_PATHMODE, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/pathmode.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		
		UIManager.put(ResourceKeys.ICON_MINI_FOLDER, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
		"images/folder.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		

		UIManager.put(ResourceKeys.ICON_MINI_PORT, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/port.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		
		UIManager.put(ResourceKeys.ICON_MINI_TESTING, new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/testing.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		
		
		UIManager.put(ResourceKeys.ICON_MINI_MEASUREMENT_SETUP, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/testsetup.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH)));

		UIManager.put(ResourceKeys.ICON_MINI_RESULT, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/result.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH)));

		UIManager.put(ResourceKeys.IMAGE_LOGIN_LOGO, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				return new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/logo2.jpg"));
			}
		});

		UIManager.put(ResourceKeys.INSETS_NULL, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				return new Insets(0, 0, 0, 0);
			}
		});

		UIManager.put(ResourceKeys.INSETS_ICONED_BUTTON, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				return new Insets(1, 1, 1, 1);
			}
		});

		UIManager.put(ResourceKeys.TABLE_NO_FOCUS_BORDER, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				return new EmptyBorder(1, 2, 1, 2);
			}
		});

		UIManager.put(ResourceKeys.SIZE_BUTTON, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				return new Dimension(24, 24);
			}
		});

		UIManager.put(ResourceKeys.SIZE_NULL, new Dimension(0, 0));

		UIDefaults defaults = UIManager.getLookAndFeelDefaults();

		defaults.put("Table.background", Color.WHITE);
		defaults.put("Table.foreground", Color.BLACK);
		defaults.put("Table.gridColor", Color.BLACK);
		defaults.put("Viewport.background", Color.WHITE);

//		Font font = new Font("Dialog", Font.PLAIN, 10);

		{
			Font font = defaults.getFont("TextField.font");
			font = new Font(font.getFamily(), font.getStyle(), 12);
			defaults.put("TextField.font", font);
		}

//		defaults.put("ComboBox.font", font);
		defaults.put("ComboBox.background", defaults.get("window"));
		defaults.put("ComboBox.disabledBackground", defaults.get("window"));

		defaults.put(ResourceKeys.COLOR_GRAPHICS_BACKGROUND, Color.WHITE);
	}
	
	protected void init() {
		
		this.initResources();
		
		if (loginRestorer == null) {
			synchronized (this) {
				if (loginRestorer == null) {
					loginRestorer = new LoginRestorer() {

						// TODO rebuild
						public String getLogin() {
							return null;
						}

						public String getPassword() {
							return null;
						}

						public boolean restoreLogin() {
							return false;
						}
					};
				}
			}

			ClientSessionEnvironment.setLoginRestorer(loginRestorer);

		}

		ClientSessionEnvironment clientSessionEnvironment1;
		try {
			clientSessionEnvironment1 = ClientSessionEnvironment.getInstance(ApplicationProperties.getInt(
				ClientSessionEnvironment.SESSION_KIND_KEY, -1));
		} catch (CommunicationException e) {
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_SERVER,
																		LangModelGeneral
																				.getString("StatusBar.ConnectionError")));
			return;
		} catch (IllegalDataException e) {
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_SERVER,
																		LangModelGeneral
																				.getString("StatusBar.IllegalSessionKind")));
			return;
		}
		final ClientSessionEnvironment clientSessionEnvironment = clientSessionEnvironment1;

		final Dispatcher dispatcher1 = this.dispatcher;

		PropertyChangeListener connectionListener = new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				Boolean boolean1 = (Boolean) evt.getNewValue();
				if (boolean1.booleanValue()) {
					dispatcher1.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_SERVER,
																			clientSessionEnvironment.getServerName()));
				} else {
					dispatcher1
							.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_SERVER,
																		LangModelGeneral
																				.getString("StatusBar.ConnectionError")));
				}

			}
		};

		clientSessionEnvironment.addPropertyListener(connectionListener);
	}

	private final boolean isLaunchable() {
		// TODO just only development bypass
		if (this.applicationCode == null) {
			return true;
		}
		
		ModuleCodeDialog sDialog = new ModuleCodeDialog(this.applicationCode, this.applicationTitle);

		return sDialog.getResult() == JOptionPane.OK_OPTION;
	}
	
	public void startMainFrame(	final AbstractMainFrame mainFrame,
								Image image) {
		mainFrame.setIconImage(image);
		mainFrame.setVisible(true);
	}

}
