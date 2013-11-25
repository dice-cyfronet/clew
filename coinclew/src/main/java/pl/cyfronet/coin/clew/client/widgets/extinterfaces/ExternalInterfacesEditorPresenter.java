package pl.cyfronet.coin.clew.client.widgets.extinterfaces;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.widgets.extinterfaces.IExternalInterfacesView.IExternalInterfacesPresenter;

import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = ExternalInterfacesEditorView.class)
public class ExternalInterfacesEditorPresenter extends BasePresenter<IExternalInterfacesView, MainEventBus> implements IExternalInterfacesPresenter {
	public void onStart() {
		eventBus.addPopup(view);
	}
	
	public void onShowExternalInterfacesEditor(String applianceInstanceId) {
		view.showModal(true);
		loadExternalInterfacesAndEndpoints();
	}

	private void loadExternalInterfacesAndEndpoints() {
		view.getExternalInterfaceContainer().clear();
		view.showNoExternalInterfacesLabel(false);
		view.showExternalInterfacesLoadingIndicator(true);
		view.getEndpointsContainer().clear();
		view.showNoEndpointsLabel(false);
		view.showEndpointsLoadingIndicator(true);
	}
}