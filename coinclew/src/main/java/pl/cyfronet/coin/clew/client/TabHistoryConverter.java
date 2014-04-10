package pl.cyfronet.coin.clew.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

@History(type = HistoryConverterType.NONE)
public class TabHistoryConverter implements HistoryConverter<MainEventBus> {
	private static final Logger log = LoggerFactory.getLogger(TabHistoryConverter.class);
	
	@Override
	public void convertFromToken(String historyName, String param, MainEventBus eventBus) {
		log.debug("Converting history token {} with params {}", historyName, param);
		
		if("startInstance".equals(historyName)) {
			eventBus.dispatch("startInstance", param);
		} else {
			eventBus.dispatch(historyName);
		}
	}

	@Override
	public boolean isCrawlable() {
		return false;
	}
}