/**
 * 
 */
package com.appspot.spelstegen.server.persistence.jdo;

import java.util.Collection;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.appspot.spelstegen.client.entities.League;
import com.appspot.spelstegen.client.entities.Match;
import com.appspot.spelstegen.client.entities.Player;
import com.appspot.spelstegen.client.entities.Sport;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * JDO implementation of persistence manager
 * 
 * @author Per Mattsson
 */
public class JDOPersistenceManager implements com.appspot.spelstegen.server.persistence.PersistenceManager {
	
//	private static final PersistenceManagerFactory pmf =
//        JDOHelper.getPersistenceManagerFactory("transactions-optional");

	/**
	 * Constructor
	 */
	public JDOPersistenceManager() {}

	@Override
	public void storeMatch(Match match) {
		makePersistent(match);
		
		/*
		 
		 PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			League league = this.getLeague(leagueId);
		} finally {
			if (tx.isActive()) {
                tx.rollback();
            }
		}
		makePersistent(match);
		 
		 */
	}

	@Override
	public void storePlayer(Player player) {
		makePersistent(player);
	}

	@Override
	public League getLeague(Long leagueId) {
		Key key = KeyFactory.createKey(League.class.getSimpleName(), leagueId);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		return pm.getObjectById(League.class, key);
	}

	@Override
	public List<League> getLeagues(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Match> getMatches(Long leagueId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Player getPlayer(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Player getPlayer(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Player> getPlayers(Long leagueId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sport> getSports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void storeLeague(League league) {
		makePersistent(league);
		
	}
	
	private void makePersistent(Object pc) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            pm.makePersistent(pc);
        } finally {
            pm.close();
        }
	}

	@Override
	public Collection<League> getAllLeagues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Player> getAllPlayers() {
		// TODO Auto-generated method stub
		return null;
	}
}
