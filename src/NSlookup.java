import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.*;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


public class NSlookup 
{
	Properties environment = new Properties(); /* Create a hashtable of properties*/ 
	InitialDirContext idc;

	public NSlookup(){
		environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");		
		try {
			idc = new InitialDirContext(environment); /* Start directory operation*/
		} catch (NamingException ne) {
			System.out.println("Error in nameserver lookup init " + ne);
		}
	}

	public String mxLookup(String domain){
		System.out.println("Domain in: "+domain);
		String serverResult = "";
		Attributes attrs = null;
		String[] mxAttributes = {"MX"};
		String mxAttr = "";

		try {
			attrs = idc.getAttributes(domain, mxAttributes); /* retreieve the mxAttributes */
		}
		catch (NamingException ex) {
			System.out.println("Error in nameserver lookup get attributes " + ex);
			return null;
		}

		Attribute attr = attrs.get("MX"); /* Get the MX attribute */ 

		if (attr != null) {   
			
			System.out.println("Attr: " + attr);
			try {
				mxAttr = (String) attr.get(0);
//				System.out.println(mxAttr);
			} 
			catch (NamingException ne) {
//				Logger.getLogger(NSlookup.class.getName()).log(Level.SEVERE, null, ne);
				System.out.println("Error in nameserver lookup" + ne);
			}

			String[] parts = mxAttr.split(" ");
			System.out.println(parts.length);
			if(parts.length > 0){
				int i = 0;
				while(i < parts.length){
					System.out.println("Parts + " + parts[i]);
					i++;
				}
			}
			serverResult = parts[parts.length - 1];
		}

		return serverResult;
	}


}
