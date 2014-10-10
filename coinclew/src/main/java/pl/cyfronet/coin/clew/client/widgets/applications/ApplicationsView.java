package pl.cyfronet.coin.clew.client.widgets.applications;

import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.constants.IconType;

import pl.cyfronet.coin.clew.client.widgets.applications.IApplicationsView.IApplicationsPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class ApplicationsView extends Composite implements IApplicationsView, ReverseViewInterface<IApplicationsPresenter> {
	private static ApplicationsViewUiBinder uiBinder = GWT.create(ApplicationsViewUiBinder.class);
	interface ApplicationsViewUiBinder extends UiBinder<Widget, ApplicationsView> {}
	
	private IApplicationsPresenter presenter;
	private Icon loadingIndicator;
	private Label noInstancesLabel;
	
	@UiField FlowPanel instanceContainer;
	@UiField ApplicationsMessages messages;
	@UiField FlowPanel headerRow;

	public ApplicationsView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("startInstance")
	void startInstanceButtonClicked(ClickEvent event) {
		getPresenter().onStartInstance();
	}

	@Override
	public HasWidgets getInstanceContainer() {
		return instanceContainer;
	}

	@Override
	public void showLoadingIndicator(boolean show) {
		if (show) {
			if (loadingIndicator == null) {
				loadingIndicator = new Icon(IconType.SPINNER);
				loadingIndicator.addStyleName("icon-spin");
				instanceContainer.add(loadingIndicator);
			}
		} else {
			if (loadingIndicator != null) {
				instanceContainer.remove(loadingIndicator);
				loadingIndicator = null;
			}
		}
	}

	@Override
	public void showNoInstancesLabel(boolean show) {
		if (show) {
			if (noInstancesLabel == null) {
				noInstancesLabel = new Label(messages.noInstancesLabel());
				instanceContainer.add(noInstancesLabel);
			}
		} else {
			if (noInstancesLabel != null) {
				instanceContainer.remove(noInstancesLabel);
				noInstancesLabel = null;
			}
		}
	}

	@Override
	public void setPresenter(IApplicationsPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IApplicationsPresenter getPresenter() {
		return presenter;
	}

	@Override
	public void showHeaderRow(boolean show) {
		headerRow.setVisible(show);
	}

	@Override
	public void showNoInitialConfigurationsMessage() {
		Window.alert(messages.noInitialConfigurationsMessage());
	}
}