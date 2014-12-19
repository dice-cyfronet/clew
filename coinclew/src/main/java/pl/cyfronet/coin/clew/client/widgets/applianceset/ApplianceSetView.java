package pl.cyfronet.coin.clew.client.widgets.applianceset;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Label;

import pl.cyfronet.coin.clew.client.widgets.BootstrapHelpers;
import pl.cyfronet.coin.clew.client.widgets.applianceset.IApplianceSetView.IApplianceSetPresenter;

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
	private Label noInstanceLabel;

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
	public void showNoInstancesLabel(boolean show) {
		if (show) {
			if (noInstanceLabel == null) {
				noInstanceLabel = new Label(messages.noInstancesLabel());
				instancesContainer.add(noInstanceLabel);
			}
		} else {
			if (noInstanceLabel != null) {
				instancesContainer.remove(noInstanceLabel);
				noInstanceLabel = null;
			}
		}
	}

	@Override
	public HasWidgets getInstanceContainer() {
		return instancesContainer;
	}

	@Override
	public void setShutdownBusyState(boolean busy) {
		BootstrapHelpers.setButtonBusyState(shutdown, busy);
	}

	@Override
	public boolean confirmShutdown() {
		return Window.confirm(messages.shutdownConfirmation());
	}

	@Override
	public void showEmptyNamePlaceholder() {
		name.setHTML(messages.getEmptyName());
	}
}
