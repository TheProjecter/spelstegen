package spelstegen.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ChartGenerator extends RemoteService {
    public String generatePointsHistoryChart();
}