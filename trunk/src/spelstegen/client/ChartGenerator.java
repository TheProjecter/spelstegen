package spelstegen.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ChartGenerator extends RemoteService {
    public String generateScoreHistoryChart(League league, Season season);
}