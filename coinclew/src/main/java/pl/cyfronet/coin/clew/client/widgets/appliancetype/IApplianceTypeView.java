package pl.cyfronet.coin.clew.client.widgets.appliancetype;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;

public interface IApplianceTypeView extends IsWidget {
	interface IApplianceTypePresenter {
		void onStartApplianceType();
	}

	HasText getName();
	HasText getDescription();
	void setEmptyDescription();
	void setStartButtonBusyState(boolean busy);
}