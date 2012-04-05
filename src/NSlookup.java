import java.util.*;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.*;

// To change this template, choose Tools | Templates
// and open the template in the editor.

public class NSlookup {
	// Creating an empty list of properties.
	Properties environment = new Properties(); 
	InitialDirContext idc;

	public NSlookup(){ 
		environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");		
		try {
			
			// Starting the directory operation.
			idc = new InitialDirContext(environment); 
		} catch (NamingException ne) {
			System.out.println("Error in nameserver lookup initiation " + ne);
			return;
		}
	}

	public String mxLookup(String domain){
		System.out.println("Domain in: "+domain);
		String serverResult = "";
		Attributes attrs = null;
		String[] mxAttributes = {"MX"};
		String mxAttr = "";

		try {
			
			// Retrieving the attributes associated with the object
			attrs = idc.getAttributes(domain, mxAttributes); 
		} catch (NamingException ex) {
			System.out.println("Error in nameserver lookup while getting attributes " + ex);
			return null;
		}

		// Getting the mx attributes from the retrieved attributes
		Attribute attr = attrs.get("MX");  

		// Checking if there are any MX servers and  
		// taking out the first one.   
		if (attr != null) {   
			try {
				mxAttr = (String) attr.get(0);
			} 
			catch (NamingException ne) {
				System.out.println("Error in nameserver lookup" + ne);
				return null;
			}

			// We split the taken MX server and split it to further 
			String[] serverName = mxAttr.split(" ");
			serverResult = serverName[serverName.length - 1];
		}
		return serverResult;
	}
}