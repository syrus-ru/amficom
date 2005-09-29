package com.syrus.impexp.unicablemap;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.xml.MapLibraryDocument;
import com.syrus.AMFICOM.map.xml.XmlMapLibrary;
import com.syrus.AMFICOM.map.xml.XmlSiteNodeType;
import com.syrus.AMFICOM.map.xml.XmlSiteNodeTypeSeq;
import com.syrus.AMFICOM.map.xml.XmlSiteNodeTypeSort;
import com.syrus.impexp.unicablemap.map.SiteType;

/**
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/09/29 15:28:33 $
 * @module mapviewclient_v1
 */
public class UniCableMapLibraryExportCommand 
{
	UniCableMapDatabase ucmDatabase;

	String fileName;

	public UniCableMapLibraryExportCommand(UniCableMapDatabase ucmDatabase, String fileName)
	{
		this.ucmDatabase = ucmDatabase;
		this.fileName = fileName;
	}

	public void execute()
	{
		if(this.fileName == null)
			return;
		File file = new File(this.fileName);

		String ext = file.getAbsolutePath().substring(
				file.getAbsolutePath().lastIndexOf("."));

		if(ext == null) {
			ext = ".xml";
		}

			if(ext.equals(".xml")) {
				saveXML(this.fileName);
			}
//			else if(ext.equals(".esf")) {
//				saveESF(this.fileName);
//			}
	}
	
	private void saveXML(String fileName) {
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		xmlOptions.setSavePrettyPrintIndent(2);
		java.util.Map prefixes = new HashMap();
		prefixes.put("http://syrus.com/AMFICOM/map/xml", "map");
		prefixes.put("http://syrus.com/AMFICOM/general/xml", "general");
		xmlOptions.setSaveSuggestedPrefixes(prefixes);
		xmlOptions.setSaveAggressiveNamespaces();

		MapLibraryDocument doc = 
			MapLibraryDocument.Factory.newInstance(xmlOptions);

		XmlMapLibrary xmlMapLibrary = doc.addNewMapLibrary();

		XmlIdentifier uid = xmlMapLibrary.addNewId();
		uid.setStringValue("cblibrary");
		xmlMapLibrary.setName("UCM");
		xmlMapLibrary.setCodename("UCM");
		xmlMapLibrary.setDescription("");
		xmlMapLibrary.setImportType("ucm");

		final XmlSiteNodeTypeSeq xmlSiteNodeTypes = xmlMapLibrary.addNewSiteNodeTypes();

		Collection<SiteType> importSiteNodeTypes = new LinkedList<SiteType>();

		System.out.print("Scanning wells types... ");
		long t1 = System.currentTimeMillis();
		Collection<UniCableMapObject> ucmWellTypes = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_WELL_TYPE));
		System.out.print(ucmWellTypes.size() + " objects... ");
		createSiteTypes(importSiteNodeTypes, ucmWellTypes, XmlSiteNodeTypeSort.WELL.toString());
		long t2 = System.currentTimeMillis();
		System.out.println(" Done in " + (t2 - t1) + " ms!");

		System.out.print("Filling XML document... ");
		XmlSiteNodeType[] xmlSiteNodeTypesArray = new XmlSiteNodeType[importSiteNodeTypes.size()];
		int sitesIndex = 0;
		for(SiteType siteType : importSiteNodeTypes) {
			xmlSiteNodeTypesArray[sitesIndex++] = siteType.getXmlSiteNodeType();
		}

		xmlSiteNodeTypes.setSiteNodeTypeArray(xmlSiteNodeTypesArray);

		long t3 = System.currentTimeMillis();
		System.out.println(" Done in " + (t3 - t2) + " ms!");
		
		System.out.print("Validating XML document... ");
		boolean isXmlValid = UCMParser.validateXml(doc);
		long t4 = System.currentTimeMillis();
		System.out.println(" Done in " + (t4 - t3) + " ms!");
		if(isXmlValid) {
//		if(true) {
			System.out.print("Writing XML document... ");
			File f = new File(fileName);

			try {
				doc.save(f, xmlOptions);
			} catch(IOException e) {
				e.printStackTrace();
			}
			long t5 = System.currentTimeMillis();
			System.out.println("\nXML Instance Document saved at : " + f.getPath() + " in " + (t5 - t4) + " ms!");
		}
	}

	private void createSiteTypes(Collection<SiteType> siteTypes, Collection<UniCableMapObject> objects, String sort) {
		for(UniCableMapObject ucmObject : objects) {
			SiteType siteType = SiteType.parseSiteType(this.ucmDatabase, ucmObject, sort);
			siteTypes.add(siteType);
		}
	}
}
