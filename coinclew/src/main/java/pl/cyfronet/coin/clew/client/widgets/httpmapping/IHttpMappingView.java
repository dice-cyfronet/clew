package pl.cyfronet.coin.clew.client.widgets.httpmapping;

import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;

public interface IHttpMappingView extends IsWidget {
	interface IHttpMappingPresenter {
		void onShowDescriptor();

		void onInitChangeHttpAlias();
		
		void onInitChangeHttpsAlias();

		void onUpdateAlias();
	}

	void updateHttpsStatus(String httpsUrlStatus);
	
	void updateHttpStatus(String httpUrlStatus);

	HasText getName();

	void showHttpPanel(boolean show);

	void setHttpHref(String httpUrl);

	void setHttpsHref(String httpsUrl);

	void showHttpsPanel(boolean show);

	void showDescriptorButton(boolean show);

	void showNoHttpAliasLabel(boolean show);

	void showHttpAlias(boolean show);

	void setHttpAlias(String httpAlias, String httpUrl);

	void showNoHttpsAliasLabel(boolean show);

	void showHttpsAlias(boolean show);

	void setHttpsAlias(String httpsAlias, String httpsUrl);

	HasHTML getDescriptor();

	void showDescriptorModal(boolean show);

	void showChangeAliasModal(boolean show);

	HasText getAlias();

	void showAliasBusyState(boolean busy);
}