/*-
 * $Id: SchemeExportCommand.java,v 1.15 2006/02/15 12:19:50 stas Exp $
 *
 * Copyright ? 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.xml.SchemesDocument;
import com.syrus.AMFICOM.scheme.xml.XmlScheme;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeSeq;
import com.syrus.util.Log;
import com.syrus.util.transport.xml.XmlConversionException;

public class SchemeExportCommand extends ImportExportCommand {
	SchemeTabbedPane pane;
	
	public SchemeExportCommand(SchemeTabbedPane pane) {
		this.pane = pane;
	}
	
	@Override
	public void execute() {
		super.execute();
		
		Scheme scheme = null;
		try {
			scheme = this.pane.getCurrentPanel().getSchemeResource().getScheme();
		} catch (ApplicationException e1) {
			Log.errorMessage(e1);
		}
		if (scheme != null) {

			final String fileName = openFileForWriting(
					LangModelScheme.getString("Title.save.scheme_xml")); //$NON-NLS-1$
			if(fileName == null)
				return;
			
			try {
				XmlScheme xmlScheme = XmlScheme.Factory.newInstance();
				scheme.getXmlTransferable(xmlScheme, AMFICOM_IMPORT, false);
				
				final File schemeFile = new File(fileName);
				saveSchemeXML(schemeFile, xmlScheme);
			} catch (final XmlConversionException xce) {
				Log.errorMessage(xce);
			}		
		}
	}
	
	private void saveSchemeXML(File f, XmlScheme xmlScheme) {
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		java.util.Map prefixes = new HashMap();
		prefixes.put("http://syrus.com/AMFICOM/scheme/xml", "scheme");
		xmlOptions.setSaveSuggestedPrefixes(prefixes);
		
		SchemesDocument doc = SchemesDocument.Factory.newInstance(xmlOptions);
		
		XmlSchemeSeq xmlSchemeSeq = doc.addNewSchemes();
		xmlSchemeSeq.setSchemeArray(new XmlScheme[] {xmlScheme});
		
		try {
			// Writing the XML Instance to a file.
			doc.save(f, xmlOptions);
			Log.debugMessage("XML Instance Document saved at : " + f.getPath(), Level.FINER);
		} catch(IOException e) {
			Log.errorMessage(e);
		}
		Log.debugMessage("Check if XML valid...", Level.FINER);
		boolean isXmlValid = validateXml(doc);
		if(isXmlValid) {
			Log.debugMessage("Done successfully", Level.WARNING);
		} else {
			Log.debugMessage("Done with errors (see logs/error for more)", Level.WARNING);
		}
	}
}
