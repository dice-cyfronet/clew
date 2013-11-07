package pl.cyfronet.coin.clew.client.widgets.root;

import com.google.gwt.user.client.ui.IsWidget;

public interface IRootView extends IsWidget {
	public interface IRootPresenter {
		
	}
	
	void setMenu(IsWidget widget);
	void setBody(IsWidget widget);
}