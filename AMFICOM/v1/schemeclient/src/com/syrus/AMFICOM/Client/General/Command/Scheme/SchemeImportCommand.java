/*-
 * $Id: SchemeImportCommand.java,v 1.3 2005/09/05 17:43:19 bass Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.xmlbeans.XmlException;

import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.XmlComplementor;
import com.syrus.AMFICOM.general.XmlComplementorRegistry;
import com.syrus.AMFICOM.general.xml.XmlStorableObject;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.xml.SchemesDocument;
import com.syrus.AMFICOM.scheme.xml.XmlScheme;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeSeq;
import com.syrus.util.Log;

public class SchemeImportCommand extends AbstractCommand {
	static {
		XmlComplementorRegistry.registerComplementor(SCHEME_CODE, new XmlComplementor() {
			public void complementStorableObject(
					final XmlStorableObject scheme,
					final String importType)
			throws CreateObjectException, UpdateObjectException {
				((XmlScheme) scheme).setDomainId(LoginManager.getDomainId().getXmlTransferable(importType));
			}
		});
	}

	@Override
	public void execute() {
		String user_dir = System.getProperty("user.dir");
		
		final String fileName = openFileForReading(user_dir);
		if(fileName == null)
			return;

		String ext = fileName.substring(fileName.lastIndexOf("."));

		if(ext.equals(".xml")) {
			try {
				@SuppressWarnings("unused") Scheme scheme = loadXML(fileName);
			} catch (CreateObjectException e) {
				Log.errorException(e);
			} catch (XmlException e) {
				JOptionPane.showMessageDialog(Environment.getActiveWindow(), LangModelScheme.getString("Message.error.parse_xml"), LangModelScheme.getString("Message.error"), JOptionPane.ERROR_MESSAGE);
				Log.errorException(e);
			} catch (IOException e) {
				Log.errorException(e);
			}
		}
	}
	
	protected Scheme loadXML(String fileName) throws CreateObjectException, XmlException, IOException {
		Scheme scheme = null;
		
		File xmlfile = new File(fileName);
		
//		Create an instance of a type generated from schema to hold the XML.
//		Parse the instance into the type generated from the schema.
		
		System.out.println("start parse scheme");
		SchemesDocument doc = SchemesDocument.Factory.parse(xmlfile);
		System.out.println("end parse scheme");
		
//		if(!validateXml(doc)) {
//			throw new XmlException("Invalid XML");
//		}
		
//		make sure default types loaded
		String user_dir = System.getProperty("user.dir");
		System.setProperty("user.dir",  xmlfile.getParent());
		
		XmlSchemeSeq xmlSchemes = doc.getSchemes();
		XmlScheme[] xmlSchemesArray = xmlSchemes.getSchemeArray();
		for(int i = 0; i < xmlSchemesArray.length; i++) {
			XmlScheme xmlScheme = xmlSchemesArray[i];
			scheme = Scheme.createInstance(LoginManager.getUserId(), xmlScheme, "ucm");
			
			scheme.setName(scheme.getName()
					+ "(imported "
//					+ MapPropertiesManager.getDateFormat()
//					.format(new Date(System.currentTimeMillis())) 
					+ " from \'"
					+ xmlfile.getName() + "\')");
			
//			scheme.addMapLibrary(MapLibraryController.getDefaultMapLibrary());

			break;
		}
		System.setProperty("user.dir",  user_dir);
		return scheme;
	}
	
	protected static final String openFileForReading(String path) {
		String fileName = null;
		JFileChooser fileChooser = new JFileChooser();

		ChoosableFileFilter esfFilter = new ChoosableFileFilter(
				"esf",
				"Export Save File");
		fileChooser.addChoosableFileFilter(esfFilter);

		ChoosableFileFilter xmlFilter = new ChoosableFileFilter(
				"xml",
				"Export Save File");
		fileChooser.addChoosableFileFilter(xmlFilter);

		fileChooser.setCurrentDirectory(new File(path));
		fileChooser.setDialogTitle("Выберите файл для чтения");
		fileChooser.setMultiSelectionEnabled(false);

		int option = fileChooser.showOpenDialog(Environment.getActiveWindow());
		if(option == JFileChooser.APPROVE_OPTION) {
			fileName = fileChooser.getSelectedFile().getPath();
			if(!(fileName.endsWith(".xml") || fileName.endsWith(".esf")))
				return null;
		}

		if(fileName == null)
			return null;

		if(!(new File(fileName)).exists())
			return null;

		return fileName;
	}
}
