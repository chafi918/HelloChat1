/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.dauphine.hellochat.hellochat;

import java.util.Iterator;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

/**
 *
 * @author macbookpro
 */
public class FormEncoder implements Encoder.Text<FormMessage> {
	@Override
	public void init(final EndpointConfig config) {
	}
 
	@Override
	public void destroy() {
	}
 
	@Override
	public String encode(final FormMessage formMessage) throws EncodeException {
		
		if (formMessage instanceof FormMessageChat) {
			FormMessageChat formMessageChat = (FormMessageChat) formMessage;
			
			return Json.createObjectBuilder()
					.add("message", formMessageChat.getMessage())
					.add("sender", formMessageChat.getSender())
					.add("received", formMessageChat.getReceived().toString()).build()
					.toString();
		}if (formMessage instanceof FormMessageUsers) {
			FormMessageUsers formMessageUsers = (FormMessageUsers) formMessage;
			
			Iterator<String> iterator2 = formMessageUsers.getUsers().iterator();
			JsonArrayBuilder jab = Json.createArrayBuilder();
	        while (iterator2.hasNext()) {
	        	jab.add((String)iterator2.next());
	        }
	        return Json.createObjectBuilder()
	        		.add("message", "FormMessageUsers")
					.add("users", jab).build()
					.toString();
		}
		return null;
		
	}
	
/*	public String encode(final Set<String> users) throws EncodeException {
		Iterator<String> iterator2 = users.iterator();
		JsonArrayBuilder jab = Json.createArrayBuilder();
        while (iterator2.hasNext()) {
        	jab.add((String)iterator2.next());
        }
        
		String a =  Json.createObjectBuilder()
				.add("users", jab).build()
				.toString();
		return a;
	}*/

}
