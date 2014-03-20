package pl.cyfronet.coin.clew.client.widgets.initialconfig;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;

public interface IInitialConfigView extends IsWidget{
	interface IInitialConfigPresenter {
		void onRemove();
		void onEdit();
	}

	HasText getName();
	void addParameter(String parameter);
	void addNoParametersLabel();
	void clearParameterContainer();
	void cancelEdit();
}