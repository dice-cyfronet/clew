package pl.cyfronet.coin.clew.client.widgets.initialconfigembed;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;

public interface IInitialConfigEmbedView extends IsWidget {
	interface IInitialConfigEmbedPresenter {
		void onStartApplications();
	}

	void showModal(boolean b);
	void showLoadingProgress(boolean b);
	Map<String, HasText> addParameters(String applianceTypeName, String configName, List<String> parameters);
}