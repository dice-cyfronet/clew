package pl.cyfronet.coin.clew.client.widgets.initialconfigseditor;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.widgets.initialconfigseditor.IInitialConfigsEditorView.IInitialConfigsEditorPresenter;

import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = InitialConfigsEditorView.class)
public class InitialConfigsEditorPresenter extends BasePresenter<IInitialConfigsEditorView, MainEventBus> implements IInitialConfigsEditorPresenter {
	public void onStart() {
		eventBus.addPopup(view);
	}
	
	public void onShowInitialConfigsEditor(String applianceTypeId) {
		view.showModal(true);
	}
}