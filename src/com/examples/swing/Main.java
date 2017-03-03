/*
 * Copyright (C) 2017, IRIT(SMAC)-Creative Technology
 * 
 */

package com.examples.swing;

import com.examples.upnp.VisionneuseServer;

public class Main {

	public static void main(String[] args) {
		Thread serverThread = new Thread(new VisionneuseServer());
        serverThread.setDaemon(false);
        serverThread.start();
	}
}
