/*-
 * $Id: AbstractApplication.java,v 1.34 2006/05/29 09:37:03 stas Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import com.syrus.AMFICOM.client.UI.AMFICOMMetalTheme;
import com.syrus.AMFICOM.client.UI.AMFICOMOceanTheme;
import com.syrus.AMFICOM.client.UI.dialogs.ModuleCodeDialog;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.extensions.ExtensionLauncher;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClientSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.AMFICOM.general.XMLSessionEnvironment;
import com.syrus.AMFICOM.general.ClientSessionEnvironment.SessionKind;
import com.syrus.AMFICOM.resources.ResourceHandler;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.34 $, $Date: 2006/05/29 09:37:03 $
 * @author $Author: stas $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public abstract class AbstractApplication {

	public static final String		KEY_MODULE_CODE				= "Module.Code";
	public static final String		KEY_MODULE_TITLE			= "Module.Title.I18NKey";

	public static final String		KEY_LOOK_AND_FEEL	= "LookAndFeel";
	public static final String		CONTROL_FONT_SIZE	= "ControlFontSize";
	public static final String		SYSTEM_FONT_SIZE	= "SystemFontSize";
	public static final String		SUB_FONT_SIZE			= "SubFontSize";
	
	public static final String		LOOK_AND_FEEL_KUNSTSTOFF	= "Kunststoff";
	public static final String		LOOK_AND_FEEL_METAL			= "Metal";
	public static final String		LOOK_AND_FEEL_MOTIF			= "Motif";
	public static final String		LOOK_AND_FEEL_WINDOWS		= "Windows";
	public static final String		LOOK_AND_FEEL_GTK = "GTK";		

	public static final String XMLSESSION_KEY = "XMLSession";
	public static final String XML_PATH_KEY = "XMLPath";

	protected ApplicationContext	aContext;

	protected Dispatcher			dispatcher;

	private static LoginRestorer	loginRestorer;

	protected String				applicationCode;

	private static boolean			resourcesInitialized		= false;
	private static boolean			themeInitialized			= false;

	
	public AbstractApplication(final String applicationName, 
	                           final String applicationCode) {
		this(applicationName);
		this.setApplicationCode(applicationCode);
	}

	public AbstractApplication(final String applicationName) {
		Application.init(applicationName);		
		
		I18N.addResourceBundle(ApplicationProperties.getString(I18N.RESOURCE_BUNDLE_KEY, null));
		
		this.setApplicationCode(
			ApplicationProperties.getString(KEY_MODULE_CODE, null));
	}
	
	private void setApplicationCode(final String applicationCode) {
		this.applicationCode = applicationCode;
		this.initTheme();
		if (this.isLaunchable()) {
			this.aContext = new ApplicationContext();
			this.dispatcher = new Dispatcher();
			this.aContext.setDispatcher(this.dispatcher);
			try {
				this.init0();
				this.init();
			} catch (final ApplicationException e) {
				AbstractMainFrame.showErrorMessage(null, e);
				AbstractMainFrame.checkForExit();
			}
		} else {
			AbstractMainFrame.checkForExit();
		}
	}	

	private synchronized void initResources() {
		if (!resourcesInitialized) {			
			this.initTheme();
			I18N.addResourceBundle("com.syrus.AMFICOM.client.resource.general");
			this.initUIConstats();	
			UIManager.getDefaults().addResourceBundle("com.syrus.AMFICOM.client.resource.swing");					
			resourcesInitialized = true;
		}
	}

	private synchronized void initTheme() {
		if (!themeInitialized) {
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);

			try {
				UIManager.setLookAndFeel(this.getLookAndFeel());
			} catch (UnsupportedLookAndFeelException ulfe) {
				Log.errorMessage(ulfe);
			}
			themeInitialized = true;
		}
	}

	private LookAndFeel getLookAndFeel() {
		String lookAndFeel = ApplicationProperties.getString(KEY_LOOK_AND_FEEL, 
				LOOK_AND_FEEL_WINDOWS);
		LookAndFeel lnf = null;
		if (lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_METAL)) {
			MetalLookAndFeel.setCurrentTheme(new AMFICOMOceanTheme());
			lnf = new MetalLookAndFeel();
		} else if (lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_KUNSTSTOFF)) {
			try {
				final Class<?> clazz = Class.forName("com.incors.plaf.kunststoff.KunststoffLookAndFeel");
				final Method method = clazz.getMethod("setCurrentTheme", MetalTheme.class);
				method.invoke(clazz, new AMFICOMMetalTheme());
				lnf = (LookAndFeel) clazz.newInstance();
			} catch (final RuntimeException re) {
				throw re;
			} catch (final Exception e) {
				return this.getDefaultLookAndFeel();
			}
		} else if (lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_WINDOWS)) {
			lnf = new WindowsLookAndFeel();
		} else if (lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_MOTIF)) {
			lnf = new MotifLookAndFeel();
		} else if (lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_GTK)) {
			try {
				lnf = (LookAndFeel) Class.forName("com.sun.java.swing.plaf.gtk.GTKLookAndFeel").newInstance();
			} catch (final RuntimeException re) {
				throw re;
			} catch (final Exception e) {
				return this.getDefaultLookAndFeel();
			}
		} else {
			return this.getDefaultLookAndFeel();
		}
		return lnf.isSupportedLookAndFeel()
				? lnf
				: this.getDefaultLookAndFeel();
	}

	/**
	 * Returns a fail-safe pluggable look-and-feel.
	 */
	private LookAndFeel getDefaultLookAndFeel() {
		try {
			return (LookAndFeel) (Class.forName(
					UIManager.getSystemLookAndFeelClassName()).newInstance());
		} catch (Exception e) {
			return UIManager.getLookAndFeel();
		}
	}

	private void initUIConstats() {
		UIManager.put(ResourceKeys.SIMPLE_DATE_FORMAT, 
			new SimpleDateFormat(
				I18N.getString(ResourceKeys.SIMPLE_DATE_FORMAT)));

		UIManager.put(ResourceKeys.HOURS_MINUTES_SECONDS_DATE_FORMAT, 
			new SimpleDateFormat(
				I18N.getString(
					ResourceKeys.HOURS_MINUTES_SECONDS_DATE_FORMAT)));
		
		final ExtensionLauncher extensionLauncher = ExtensionLauncher.getInstance();
		final ClassLoader classLoader = AbstractApplication.class.getClassLoader();
		extensionLauncher.addExtensions(classLoader.getResource("xml/ccresource.xml"));
		
		extensionLauncher.getExtensionHandler(ResourceHandler.class.getName());
		UIManager.put(ResourceKeys.TABLE_NO_FOCUS_BORDER, 
			new UIDefaults.LazyValue() {

				public Object createValue(UIDefaults table) {
					return new EmptyBorder(1, 2, 1, 2);
				}
		});

		this.initUIStyles();
	}
	
	private void initUIStyles() {
		UIDefaults defaults = UIManager.getLookAndFeelDefaults();
		
		UIManager.put("IconedRenderer.selectedBackground", Color.BLUE);
		UIManager.put("IconedRenderer.selectedForeground", Color.WHITE);

		defaults.put("Table.background", Color.WHITE);
		defaults.put("Table.foreground", Color.BLACK);
		defaults.put("Table.gridColor", Color.BLACK);
		defaults.put("Viewport.background", Color.WHITE);

		{
			Font font = defaults.getFont("TextField.font");
			font = new Font(font.getFamily(), font.getStyle(), 12);
			defaults.put("TextField.font", font);			
		}
		
//		 switch off
//		{
//			Font font = UIManager.getFont("Table.font");
//			font = new Font(font.getName(), Font.BOLD, font.getSize());
//			defaults.put("Table.selectedFont", font);
//		}		
		
		defaults.put("AComboBox.font", UIManager.getFont("ComboBox.font"));		
		defaults.put("ComboBox.background", defaults.get("window"));
		defaults.put("ComboBox.disabledBackground", defaults.get("window"));

		defaults.put(ResourceKeys.COLOR_GRAPHICS_BACKGROUND, Color.WHITE);
	}

	protected abstract void init();
	
	private final void init0() throws CommunicationException, IllegalDataException {

		this.initResources();

		if (loginRestorer == null) {
			synchronized (this) {
				if (loginRestorer == null) {
					loginRestorer = new LoginRestoreCommand(null);
				}
			}			
		}

		final String xmlSession = ApplicationProperties.getString(XMLSESSION_KEY, Boolean.FALSE.toString());
		boolean usingXMLSession = xmlSession.equalsIgnoreCase(Boolean.TRUE.toString()) || xmlSession.equalsIgnoreCase("yes");
		if (!usingXMLSession) {
			final int kind = ApplicationProperties.getInt(ClientSessionEnvironment.SESSION_KIND_KEY, -1);
			final SessionKind sessionKind = SessionKind.valueOf(kind);
			try {
				ClientSessionEnvironment.createInstance(sessionKind, loginRestorer);				
			} catch (final CommunicationException ce) {
				this.dispatcher.firePropertyChange(new StatusMessageEvent(this,
						StatusMessageEvent.STATUS_SERVER,
						I18N.getString("Common.StatusBar.ConnectionError")));				
				throw ce;				
			} catch (final IllegalDataException ide) {
				this.dispatcher.firePropertyChange(new StatusMessageEvent(this,
						StatusMessageEvent.STATUS_SERVER,
						I18N.getString("Common.StatusBar.IllegalSessionKind")));
				
				throw ide;
			}

			final ClientSessionEnvironment clientSessionEnvironment = ClientSessionEnvironment.getInstance();

			PropertyChangeListener connectionListener = new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					final Boolean boolean1 = (Boolean) evt.getNewValue();
					if (boolean1.booleanValue()) {
						AbstractApplication.this.dispatcher.firePropertyChange(new StatusMessageEvent(this, 
							StatusMessageEvent.STATUS_SERVER,
							clientSessionEnvironment.getServerName()));
					} else {
						AbstractApplication.this.dispatcher.firePropertyChange(new StatusMessageEvent(this, 
							StatusMessageEvent.STATUS_SERVER,
							I18N.getString("Common.StatusBar.ConnectionError")));
					}

				}
			};

			clientSessionEnvironment.addPropertyListener(connectionListener);
		} else {
			XMLSessionEnvironment.createInstance();
			this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_SERVER, "XML"));
		}

	}

	private final boolean isLaunchable() {
		// TODO just only development bypass
		if (this.applicationCode == null) { 
			return true; 
		}

		ModuleCodeDialog sDialog = new ModuleCodeDialog(this.applicationCode, 
				I18N.getString(
					ApplicationProperties.getString(KEY_MODULE_TITLE, "")));

		return sDialog.getResult() == JOptionPane.OK_OPTION;
	}

	public void startMainFrame(	final AbstractMainFrame mainFrame,
								Image image) {
		mainFrame.setIconImage(image);
		mainFrame.setVisible(true);
	}

}
