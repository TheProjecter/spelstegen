package spelstegen.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ChartGeneratorAsync {
    public void generateScoreHistoryChart(League league, Season season, AsyncCallback<String> callback);
} 