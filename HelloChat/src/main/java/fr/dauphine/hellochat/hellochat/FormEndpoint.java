package fr.dauphine.hellochat.hellochat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author macbookpro
 */

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/form/{room}/{user}", encoders = FormEncoder.class, decoders = FormDecoder.class)
public class FormEndpoint {

	private final Logger log = Logger.getLogger(getClass().getName());
	private Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
	// private Set<FormSession> sessions = Collections.synchronizedSet(new
	// HashSet<FormSession>());

	@OnOpen
	public void onOpen(Session session, @PathParam("room") final String room, @PathParam("user") final String user)
			throws IOException, EncodeException {

		sessions.add(session);
		System.out.println("Established session now: " + session.getId() + " room: "+ room + " user: " + user);

	}

	@OnMessage
	public void onMessage(final Session session, FormMessageChat chatMessage) throws IOException, EncodeException {
		System.out.println("test received Message: " + chatMessage.getMessage() + " . From: " + session.getId());

		String username = (String) session.getUserProperties().get("username");
		FormMessageChat answerMessage = new FormMessageChat();
		Iterator<Session> iterator = sessions.iterator();
		String myRoom = (String) session.getUserProperties().get("room");

		if (username == null) {
			session.getUserProperties().put("username", chatMessage.getMessage());
			session.getUserProperties().put("room", chatMessage.getSender());

			answerMessage.setMessage("You are connected as " + chatMessage.getMessage());
			answerMessage.setSender(chatMessage.getMessage());
			answerMessage.setReceived(new Date());

			session.getBasicRemote().sendObject(answerMessage);

		} else {
			answerMessage.setMessage(chatMessage.getMessage());
			answerMessage.setSender(username);
			answerMessage.setReceived(new Date());

			for (Session sess : session.getOpenSessions()) {
				if (sess.isOpen() && sess.getUserProperties().get("room").equals(myRoom)) {
					sess.getBasicRemote().sendObject(answerMessage);
				}
			}
		}

		Set<String> tmp = getUserNames(session);
		FormMessageUsers formMessageUsers = new FormMessageUsers();
		formMessageUsers.setMessage("FormMessageUsers");
		formMessageUsers.setUsers(tmp);

		for (Session sess : session.getOpenSessions()) {
			if (sess.isOpen() && sess.getUserProperties().get("room").equals(myRoom)) {
				sess.getBasicRemote().sendObject(formMessageUsers);
			}
		}

	}

	@OnClose
	public void onClose(Session session) throws IOException, EncodeException {
		System.out.println(session + ", DisConnected client...");
		// session.close();
		sessions.remove(session);
		session.close();

		Set<String> tmp = getUserNames(session);
		FormMessageUsers formMessageUsers = new FormMessageUsers();
		formMessageUsers.setMessage("FormMessageUsers");
		formMessageUsers.setUsers(tmp);

		String myRoom = (String) session.getUserProperties().get("room");
		for (Session sess : session.getOpenSessions()) {
			if (sess.isOpen() && sess.getUserProperties().get("room").equals(myRoom)) {
				sess.getBasicRemote().sendObject(formMessageUsers);
			}
		}
	}

	@OnError
	public void onError(Throwable t) {
		t.printStackTrace();
	}

	private Set<String> getUserNames(Session session) {
		HashSet<String> retSet = new HashSet<String>();
		String myRoom = (String) session.getUserProperties().get("room");

		for (Session sess : session.getOpenSessions()) {
			String sessUsername = sess.getUserProperties().get("username").toString();
			if (sess.isOpen() && (sessUsername != null) && sess.getUserProperties().get("room").equals(myRoom)) {
				retSet.add(sess.getUserProperties().get("username").toString());
			}
		}
		return retSet;
	}
}
