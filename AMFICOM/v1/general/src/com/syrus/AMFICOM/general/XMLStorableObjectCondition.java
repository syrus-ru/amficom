/*-
* $Id: XMLStorableObjectCondition.java,v 1.2 2005/08/19 14:41:20 arseniy Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.syrus.util.Log;


/**
 * @version $Revision: 1.2 $, $Date: 2005/08/19 14:41:20 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module general
 */
public abstract class XMLStorableObjectCondition<T extends StorableObjectCondition> extends StorableObjectXMLData {

	protected T condition;
	protected StorableObjectXMLDriver driver;

	protected XMLStorableObjectCondition(final T condition, final StorableObjectXMLDriver driver) {
		this.condition = condition;
		this.driver = driver;
	}

	public abstract Set<Identifier> getIdsByCondition() throws IllegalDataException;

	protected final String getBaseQuery() {
		return "//"
				+ this.driver.getPackageName()
				+ "/"
				+ "*[starts-with(name(),'"
				+ ObjectEntities.codeToString(this.condition.getEntityCode())
				+ Identifier.SEPARATOR
				+ "')]";
	}

	protected final Set<Identifier> getIdsByCondition(final String query, final boolean useParent) throws IllegalDataException {
		Log.debugMessage("XMLStorableObjectCondition.getIdsByCondition | query:" + query, Level.FINEST);
		try {
			final NodeList idNodeList = XPathAPI.selectNodeList(this.driver.getDoc(), query);
			final int size = idNodeList.getLength();
			if (size == 0)
				return Collections.emptySet();

			final Set<Identifier> idSet = new HashSet<Identifier>(size);
			for (int i = 0; i < idNodeList.getLength(); i++) {
				Node node = idNodeList.item(i);
				node = useParent ? node.getParentNode() : node;
				idSet.add(new Identifier(node.getNodeName()));
			}

			return idSet;
		} catch (TransformerException e) {
			final String msg = "XMLStorableObjectCondition.getIdsByCondition | Caught " + e.getMessage();
			Log.errorMessage(msg);
			throw new IllegalDataException(msg, e);
		}
	}

	public Set<Identifier> getIdsByCondition(final String field, final String fieldValue) throws IllegalDataException {
		return this.getIdsByCondition(this.getBaseQuery() + '/' + field + "[text()='" + fieldValue + "']", true);
	}

}

