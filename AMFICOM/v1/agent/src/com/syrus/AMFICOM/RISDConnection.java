/*
 * $Id: RISDConnection.java,v 1.5 2004/07/20 12:54:10 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM;


import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import oracle.aurora.jndi.sess_iiop.ServiceCtx;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2004/07/20 12:54:10 $
 * @author $Author: arseniy $
 * @module agent_v1
 */

public class RISDConnection {
	public static final String DEFAULT_SERVICE_URL = "sess_iiop://research:2481:research";
	public static final String DEFAULT_USER = "amficom";
	public static final String DEFAULT_PASSWORD = "amficom";
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
    this.serviceURL = DEFAULT_SERVICE_URL;
    this.username = DEFAULT_USER;
    this.password = DEFAULT_PASSWORD;
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

	public String getServiceURL() {
		return this.serviceURL;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}
}
