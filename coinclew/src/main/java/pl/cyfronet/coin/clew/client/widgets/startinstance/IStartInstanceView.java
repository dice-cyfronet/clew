package pl.cyfronet.coin.clew.client.widgets.startinstance;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

public interface IStartInstanceView extends IsWidget {
	interface IStartInstancePresenter {
		
	}

	void show();
	void showProgressIndicator();
	void clearApplianceTypeContainer();
	void addNoApplianceTypesLabel();
	HasWidgets getApplianceTypeContainer();
	void hide();
}