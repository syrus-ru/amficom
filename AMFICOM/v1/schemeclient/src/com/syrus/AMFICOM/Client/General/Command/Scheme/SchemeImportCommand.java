/*-
 * $Id: SchemeImportCommand.java,v 1.1 2005/09/02 05:58:13 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.xmlbeans.XmlException;

import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.general.ClonedIdsPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.util.Log;

public class SchemeImportCommand extends AbstractCommand {

	public void execute() {
/*		final String fileName = openFileForReading("");
		if(fileName == null)
			return;

		File file = new File(fileName);

		String ext = file.getAbsolutePath().substring(
				file.getAbsolutePath().lastIndexOf("."));

		if(ext == null) {
			ext = ".xml";
		}
		
		final String extension = ext;

		if(extension.equals(".xml")) {
			try {
				Scheme scheme = loadXML(fileName);
				System.out.println(scheme.getName());
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
	
	protected Scheme loadXML(String fileName)
	throws CreateObjectException, XmlException, IOException {
		Scheme scheme = null;
		
		File xmlfile = new File(fileName);
		
//		Create an instance of a type generated from schema to hold the XML.
//		Parse the instance into the type generated from the schema.
		
		System.out.println("start parse scheme");
		com.syrus.amficom.scheme.xml.SchemesDocument doc = 
			com.syrus.amficom.scheme.xml.SchemesDocument.Factory.parse(xmlfile);
		System.out.println("end parse scheme");
//		if(!validateXml(doc)) {
//			throw new XmlException("Invalid XML");
//		}
		
		Identifier userId = LoginManager.getUserId();
		Identifier domainId = LoginManager.getDomainId();
		
//		make sure default types loaded
		String user_dir = System.getProperty("user.dir");
		System.setProperty("user.dir",  xmlfile.getParent());
		
		com.syrus.amficom.scheme.xml.Schemes xmlSchemes = doc.getSchemes();
		com.syrus.amficom.scheme.xml.Scheme[] xmlSchemesArray = xmlSchemes.getSchemeArray();
		for(int i = 0; i < xmlSchemesArray.length; i++) {
			com.syrus.amficom.scheme.xml.Scheme xmlScheme = xmlSchemesArray[i];
			scheme = Scheme.createInstance(
					userId,
					domainId,
					// XXX xmlScheme.getimporttype();
					"ucm",
					xmlScheme,
					new ClonedIdsPool());
			scheme.setName(scheme.getName()
					+ "(imported "
//					+ MapPropertiesManager.getDateFormat()
//					.format(new Date(System.currentTimeMillis())) 
					+ " from \'"
					+ xmlfile.getName() + "\')");
			
//			scheme.addMapLibrary(MapLibraryController.getDefaultMapLibrary());
			
			// only one map imported
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

		return fileName;*/
	}
}

