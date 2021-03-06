package pl.cyfronet.coin.clew.client.widgets.development;

import pl.cyfronet.coin.clew.client.widgets.development.IDevelopmentView.IDevelopmentPresenter;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class DevelopmentView extends Composite implements IDevelopmentView, ReverseViewInterface<IDevelopmentPresenter> {
	private static DevelopmentViewUiBinder uiBinder = GWT.create(DevelopmentViewUiBinder.class);
	interface DevelopmentViewUiBinder extends UiBinder<Widget, DevelopmentView> {}
	
	private IDevelopmentPresenter presenter;
	private Label noInstancesLabel;
	private Label noAtomicServicesLabel;
	private Icon instanceLoadingIndicator;
	
	@UiField FlowPanel atomicServicesContainer;
	@UiField FlowPanel runningInstancesContainer;
	@UiField DevelopmentMessages messages;
	@UiField FlowPanel headerRow;

	public DevelopmentView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("manageUserKeys")
	void manageUserKeysClicked(ClickEvent event) {
		getPresenter().onManageUserKeysClicked();
	}
	
	@UiHandler("startDevInstance")
	void startDevInstanceClicked(ClickEvent event) {
		getPresenter().onStartDevInstance();
	}
	
	@Override
	public void setPresenter(IDevelopmentPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IDevelopmentPresenter getPresenter() {
		return presenter;
	}

	@Override
	public void showNoRunningInstancesLabel(boolean show) {
		if (show) {
			if (noInstancesLabel == null) {
				noInstancesLabel = new Label(messages.noRunningInstanceLabel());
				runningInstancesContainer.add(noInstancesLabel);
			}
		} else {
			if (noInstancesLabel != null) {
				runningInstancesContainer.remove(noInstancesLabel);
				noInstancesLabel = null;
			}
		}
	}

	@Override
	public void showNoAtomicServicesLabel(boolean show) {
		if (show) {
			if (noAtomicServicesLabel == null) {
				noAtomicServicesLabel = new Label(messages.noAtomicServicesLabel());
				atomicServicesContainer.add(noAtomicServicesLabel);
			}
		} else {
			if (noAtomicServicesLabel != null) {
				atomicServicesContainer.remove(noAtomicServicesLabel);
				noAtomicServicesLabel = null;
			}
		}
	}

	@Override
	public HasWidgets getAtomicServicesContainer() {
		return atomicServicesContainer;
	}

	@Override
	public void addAtomicServiceProgressIndicator() {
		Icon icon = new Icon(IconType.SPINNER);
		icon.addStyleName("icon-spin");
		atomicServicesContainer.add(icon);
	}

	@Override
	public HasWidgets getInstanceContainer() {
		return runningInstancesContainer;
	}

	@Override
	public void showInstanceLoadingIndicator(boolean show) {
		if (show) {
			if (instanceLoadingIndicator == null) {
				instanceLoadingIndicator = new Icon(IconType.SPINNER);
				instanceLoadingIndicator.addStyleName("icon-spin");
				runningInstancesContainer.add(instanceLoadingIndicator);
			}
		} else {
			if (instanceLoadingIndicator != null) {
				runningInstancesContainer.remove(instanceLoadingIndicator);
				instanceLoadingIndicator = null;
			}
		}
	}

	@Override
	public void showHeaderRow(boolean show) {
		headerRow.setVisible(show);
	}

	@Override
	public void insert(Widget widget, int index) {
		atomicServicesContainer.insert(widget, index);
	}

	@Override
	public void insertInstance(Widget widget, int index) {
		runningInstancesContainer.insert(widget, index);
	}
}