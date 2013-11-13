package pl.cyfronet.coin.clew.client.widgets.applianceset;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;

public interface IApplianceSetView extends IsWidget {
	interface IApplianceSetPresenter {
		
	}

	HasText getName();
}