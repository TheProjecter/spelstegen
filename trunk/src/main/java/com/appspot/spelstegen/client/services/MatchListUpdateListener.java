package com.appspot.spelstegen.client.services;

import java.util.List;

import com.appspot.spelstegen.client.entities.Match;

/**
 * Interface that should be implemented by classes who wants
 * to be notified when match list for current league is updated.
 * 
 * @author Per Mattsson
 */
public interface MatchListUpdateListener {

	public void matchListUpdated(List<Match> matches);
}
