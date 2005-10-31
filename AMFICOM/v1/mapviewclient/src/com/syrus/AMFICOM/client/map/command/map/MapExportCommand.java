/*-
 * $$Id: MapExportCommand.java,v 1.35 2005/10/31 12:30:09 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.map;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.command.ExportCommand;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.xml.MapsDocument;
import com.syrus.util.Log;

/**
 * Класс $RCSfile: MapExportCommand.java,v $ используется для закрытия карты при
 * сохранении на экране самого окна карты. При этом в азголовке окна
 * отображается информация о том, что активной карты нет, и карта центрируется
 * по умолчанию
 * 
 * @version $Revision: 1.35 $, $Date: 2005/10/31 12:30:09 $
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapExportCommand extends ExportCommand {

	JDesktopPane desktop;

	ApplicationContext aContext;

	/**
	 * окно карты
	 */
	MapFrame mapFrame;

	public MapExportCommand(JDesktopPane desktop, ApplicationContext aContext) {
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

		Log.debugMessage("Exporting map", INFO); //$NON-NLS-1$

		Map map = this.mapFrame.getMap();

		String fileName = ExportCommand.openFileForWriting(MapPropertiesManager
				.getLastDirectory());
		if(fileName == null) {
			return;
		}
		File file = new File(fileName);
		MapPropertiesManager.setLastDirectory(file.getParent());

		String ext = file.getAbsolutePath().substring(
				file.getAbsolutePath().lastIndexOf(MapEditorResourceKeys.DOT));

		if(ext == null) {
			ext = MapEditorResourceKeys.EXTENSION_DOT_XML;
		}

		if(ext.equals(MapEditorResourceKeys.EXTENSION_DOT_XML)) {
			saveXML(map, fileName);
		}
		setResult(Command.RESULT_OK);
	}

	protected void saveXML(Map map, String fileName) {
		try {
			XmlOptions xmlOptions = new XmlOptions();
			xmlOptions.setSavePrettyPrint();
			xmlOptions.setSavePrettyPrintIndent(2);
			java.util.Map<String, String> prefixes = new HashMap<String, String>();
			prefixes.put("http://syrus.com/AMFICOM/map/xml", "map"); //$NON-NLS-1$ //$NON-NLS-2$
			prefixes.put("http://syrus.com/AMFICOM/general/xml", "general"); //$NON-NLS-1$ //$NON-NLS-2$
			xmlOptions.setSaveSuggestedPrefixes(prefixes);
			xmlOptions.setSaveAggressiveNamespaces();
	
			MapsDocument doc = 
				MapsDocument.Factory.newInstance(xmlOptions);

			map.getXmlTransferable(doc.addNewMaps().addNewMap(), "amficom", false); //$NON-NLS-1$

			// Validate the new XML
			if (validateXml(doc)) {
				File f = new File(fileName);
	
				try {
					// Writing the XML Instance to a file.
					doc.save(f, xmlOptions);
				} catch(IOException e) {
					JOptionPane.showMessageDialog(
							Environment.getActiveWindow(), 
							e.getLocalizedMessage(), 
							I18N.getString(MapEditorResourceKeys.ERROR_WRITE_ERROR), 
							JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
				Log.debugMessage("\nXML Instance Document saved at : " + f.getPath(), INFO); //$NON-NLS-1$
			}
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
		}
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
