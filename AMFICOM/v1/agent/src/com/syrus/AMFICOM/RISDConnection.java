/*
 * $Id: RISDConnection.java,v 1.3 2004/06/21 14:56:29 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM;

import com.syrus.util.Log;
import java.util.Hashtable;
import javax.naming.*;
import oracle.aurora.jndi.sess_iiop.ServiceCtx;

/**
 * @version $Revision: 1.3 $, $Date: 2004/06/21 14:56:29 $
 * @author $Author: bass $
 * @module agent_v1
 */
public class RISDConnection {
	public static final String DEFAULT_serviceURL = "sess_iiop://research:2481:research";
	public static final String DEFAULT_user = "amficom";
	public static final String DEFAULT_password = "amficom";
	private String serviceURL;
	private String username;
	private String password;

	public RISDConnection() {
		this.setDefaults();
	}
  
  public RISDConnection(String serviceURL, String username, String password) {
    this.serviceURL = serviceURL;
  	this.username = username;
		this.password = password;
  }

  public void setDefaults() {
    this.serviceURL = DEFAULT_serviceURL;
    this.username = DEFAULT_user;
    this.password = DEFAULT_password;
  }

  public Object getServerObject(String objectName) {
    Hashtable env = new Hashtable ();
		env.put(Context.URL_PKG_PREFIXES, "oracle.aurora.jndi");
		env.put(Context.SECURITY_PRINCIPAL, this.username);
		env.put(Context.SECURITY_CREDENTIALS, this.password);
		env.put(Context.SECURITY_AUTHENTICATION, ServiceCtx.NON_SSL_LOGIN);
    Object serverObject = null;
    try {
      InitialContext ic = new InitialContext(env);
      serverObject = ic.lookup(this.serviceURL + objectName);
    }
    catch(Exception ex) {
      Log.errorException(ex);
    }
    return serverObject;
  }
}
