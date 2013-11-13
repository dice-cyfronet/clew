package pl.cyfronet.coin.clew.client.widgets.applianceset;

import pl.cyfronet.coin.clew.client.widgets.applianceset.IApplianceSetView.IApplianceSetPresenter;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Label;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class ApplianceSetView extends Composite implements IApplianceSetView, ReverseViewInterface<IApplianceSetPresenter> {
	private static AppliancesetViewUiBinder uiBinder = GWT.create(AppliancesetViewUiBinder.class);
	interface AppliancesetViewUiBinder extends UiBinder<Widget, ApplianceSetView> {}
	
	private IApplianceSetPresenter presenter;

	@UiField HTML name;
	@UiField FlowPanel instancesContainer;
	@UiField ApplianceSetMessages messages;
	@UiField Button shutdown;
	
	public ApplianceSetView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("shutdown")
	void shutdownClicked(ClickEvent event) {
		getPresenter().onShutdown();
	}
	
	@Override
	public void setPresenter(IApplianceSetPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IApplianceSetPresenter getPresenter() {
		return presenter;
	}

	@Override
	public HasText getName() {
		return name;
	}

	@Override
	public void addNoInstancesLabel() {
		Label label = new Label(messages.noInstancesLabel());
		instancesContainer.add(label);
	}

	@Override
	public HasWidgets getInstanceContainer() {
		return instancesContainer;
	}

	@Override
	public void setShutdownBusyState(boolean busy) {
		if (busy) {
			shutdown.state().loading();
		} else {
			shutdown.state().reset();
		}
	}

	@Override
	public boolean confirmShutdown() {
		return Window.confirm(messages.shutdownConfirmation());
	}
}