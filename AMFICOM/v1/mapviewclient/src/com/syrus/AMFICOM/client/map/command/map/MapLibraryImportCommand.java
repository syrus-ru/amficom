/*-
 * $$Id: MapLibraryImportCommand.java,v 1.20 2006/04/14 12:04:07 arseniy Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.map;

import static com.syrus.AMFICOM.general.ObjectEntities.SITENODE_TYPE_CODE;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JDesktopPane;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.command.ImportCommand;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.controllers.MapLibraryController;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.XmlComplementor;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.xml.XmlStorableObject;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.xml.MapLibraryDocument;
import com.syrus.AMFICOM.map.xml.XmlMapLibrary;
import com.syrus.AMFICOM.map.xml.XmlSiteNodeType;
import com.syrus.AMFICOM.map.xml.XmlSiteNodeTypeSort.Enum;
import com.syrus.AMFICOM.resource.AbstractBitmapImageResource;
import com.syrus.util.Log;

/**
 * Класс $RCSfile: MapLibraryImportCommand.java,v $ используется для закрытия 
 * карты при сохранении на экране
 * самого окна карты. При этом в азголовке окна отображается информация о том,
 * что активной карты нет, и карта центрируется по умолчанию
 * 
 * @version $Revision: 1.20 $, $Date: 2006/04/14 12:04:07 $
 * @author $Author: arseniy $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapLibraryImportCommand extends ImportCommand {
	JDesktopPane desktop;

	ApplicationContext aContext;

	/**
	 * окно карты
	 */
	MapFrame mapFrame;
	
	static {
		XmlComplementorRegistry.registerComplementor(SITENODE_TYPE_CODE, new XmlComplementor() {
			public void complementStorableObject(
					final XmlStorableObject storableObject,
					final String importType,
					final ComplementationMode mode)
			throws CreateObjectException, UpdateObjectException {
				switch (mode) {
				case PRE_IMPORT:
					final XmlSiteNodeType xmlSiteNodeType = (XmlSiteNodeType) storableObject;
					if (!xmlSiteNodeType.isSetImage()) {
						final Enum sort = xmlSiteNodeType.getSort();
						MapLibrary defaultMapLibrary = MapLibraryController.getDefaultMapLibrary();
						for(SiteNodeType siteNodeType : defaultMapLibrary.getSiteNodeTypes()) {
							if(siteNodeType.getSort().value() == sort.intValue() - 1) {
								try {
									final AbstractBitmapImageResource abstractBitmapImageResource = StorableObjectPool.getStorableObject(siteNodeType.getImageId(), true);
									xmlSiteNodeType.setImage(abstractBitmapImageResource.getCodename());
								} catch(ApplicationException e) {
									throw new CreateObjectException("Cannot get default image resource!", e); 
								}
							}
						}
					}
					break;
				case POST_IMPORT:
					break;
				case EXPORT:
					break;
				}
			}
		});
	}

	public MapLibraryImportCommand(JDesktopPane desktop, ApplicationContext aContext) {
		super();
		this.desktop = desktop;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		this.mapFrame = MapDesktopCommand.findMapFrame(this.desktop);

		if(this.mapFrame == null) {
			return;
		}

		try {
			MapLibrary mapLibrary = null;
			Log.debugMessage("Import map library", INFO); //$NON-NLS-1$

			String fileName = ImportCommand
					.openFileForReading(MapPropertiesManager.getLastDirectory());
			if(fileName == null) {
				return;
			}

			File file = new File(fileName);
			MapPropertiesManager.setLastDirectory(file.getParent());

			String ext = file.getAbsolutePath().substring(
					file.getAbsolutePath().lastIndexOf(".")); //$NON-NLS-1$

			if(ext == null) {
				ext = ".xml"; //$NON-NLS-1$
			}

			if(ext.equals(".xml")) { //$NON-NLS-1$
				mapLibrary = loadXML(fileName);
			}

			if(mapLibrary == null) {
				return;
			}

			StorableObjectPool.flush(mapLibrary, LoginManager.getUserId(), true);

			Map map = this.mapFrame.getMapView().getMap();
			map.addMapLibrary(mapLibrary);
			this.aContext.getDispatcher().firePropertyChange(
					new MapEvent(
						this, 
						MapEvent.LIBRARY_SET_CHANGED,
						map.getMapLibraries()));

			setResult(Command.RESULT_OK);
		} catch(DatabaseException e) {
			Log.errorMessage(e);
			setResult(Command.RESULT_NO);
		} catch(IllegalObjectEntityException e) {
			Log.errorMessage(e);
			setResult(Command.RESULT_NO);
		} catch(IdentifierGenerationException e) {
			Log.errorMessage(e);
			setResult(Command.RESULT_NO);
		} catch(XmlException e) {
			Log.errorMessage(e);
			setResult(Command.RESULT_NO);
		} catch(IOException e) {
			Log.errorMessage(e);
			setResult(Command.RESULT_NO);
		} catch(ApplicationException e) {
			Log.errorMessage(e);
			setResult(Command.RESULT_NO);
		}
	}

	protected MapLibrary loadXML(String fileName)
			throws XmlException, IOException, ApplicationException {
		MapLibrary mapLibrary = null;

		File xmlfile = new File(fileName);

		// Create an instance of a type generated from schema to hold the XML.
		// Parse the instance into the type generated from the schema.
		MapLibraryDocument doc = 
			MapLibraryDocument.Factory.parse(xmlfile);

		if(!validateXml(doc)) {
			throw new XmlException("Invalid XML"); //$NON-NLS-1$
		}

		Identifier userId = LoginManager.getUserId();

		String user_dir = System.getProperty("user.dir"); //$NON-NLS-1$
		System.setProperty("user.dir",  xmlfile.getParent()); //$NON-NLS-1$

		XmlMapLibrary xmlLibrary = doc.getMapLibrary();
		
		mapLibrary = MapLibrary.createInstance(userId, xmlLibrary.getImportType(), xmlLibrary);

		StorableObjectPool.flush(mapLibrary, userId, true);

		//TODO siteNodeTypes and physicalLinkTypes should be saved when mapLibrary is saved
		for(SiteNodeType siteNodeType : mapLibrary.getSiteNodeTypes()) {
			StorableObjectPool.flush(siteNodeType, userId, true);
		}
		for(PhysicalLinkType physicalLinkType : mapLibrary.getPhysicalLinkTypes()) {
			StorableObjectPool.flush(physicalLinkType, userId, true);
		}
		
		System.setProperty("user.dir",  user_dir); //$NON-NLS-1$

		LocalXmlIdentifierPool.flush();

		return mapLibrary;
	}

	public boolean validateXml(XmlObject xml) {
		boolean isXmlValid = false;

		// A collection instance to hold validation error messages.
		ArrayList validationMessages = new ArrayList();

		// Validate the XML, collecting messages.
		isXmlValid = xml.validate(new XmlOptions()
				.setErrorListener(validationMessages));

		if(!isXmlValid) {
			Log.debugMessage("Invalid XML: ", WARNING); //$NON-NLS-1$
			for(int i = 0; i < validationMessages.size(); i++) {
				XmlError error = (XmlError )validationMessages.get(i);
				Log.debugMessage(error.getMessage(), WARNING);
				Log.debugMessage(error.getObjectLocation().toString(), WARNING);
			}
		}
		return isXmlValid;
	}
}
