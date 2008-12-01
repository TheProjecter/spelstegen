package spelstegen.client;

import spelstegen.client.entities.League;
import spelstegen.client.entities.Season;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ChartGenerator extends RemoteService {
    public String generateScoreHistoryChart(League league, Season season);
}