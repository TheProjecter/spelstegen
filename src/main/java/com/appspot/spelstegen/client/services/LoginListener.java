package com.appspot.spelstegen.client.services;

import com.appspot.spelstegen.client.entities.Player;

public interface LoginListener {

	public void loggedIn(Player player);
	
	public void loggedOut();
	
}
