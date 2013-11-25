package pl.cyfronet.coin.clew.client.widgets.extinterfaces;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.Modal;
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

public class ExternalInterfacesEditorView extends Composite implements IExternalInterfacesView {
	private static ExternalInterfacesEditorViewUiBinder uiBinder = GWT.create(ExternalInterfacesEditorViewUiBinder.class);
	interface ExternalInterfacesEditorViewUiBinder extends UiBinder<Widget, ExternalInterfacesEditorView> {}
	
	private Icon externalInterfacesLoadingIndicator;
	private Icon endpointsLoadingIndicator;
	private Label noExternalInterfacesLabel;
	private Label noEndpointsLabel;
	
	@UiField Modal modal;
	@UiField FlowPanel externalInterfaceContainer;
	@UiField FlowPanel endpointContainer;
	@UiField ExternalInterfacesEditorMessages messages;

	public ExternalInterfacesEditorView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("close")
	void closeClicked(ClickEvent event) {
		modal.hide();
	}

	@Override
	public void showModal(boolean show) {
		if (show) {
			modal.show();
		} else {
			modal.hide();
		}
	}

	@Override
	public void showEndpointsLoadingIndicator(boolean show) {
		if (show) {
			if (endpointsLoadingIndicator == null) {
				endpointsLoadingIndicator = new Icon(IconType.SPINNER);
				endpointsLoadingIndicator.addStyleName("icon-spin");
				endpointContainer.add(endpointsLoadingIndicator);
			}
		} else {
			if (endpointsLoadingIndicator != null) {
				endpointContainer.remove(endpointsLoadingIndicator);
				endpointsLoadingIndicator = null;
			}
		}
	}

	@Override
	public void showNoEndpointsLabel(boolean show) {
		if (show) {
			if (noEndpointsLabel == null) {
				noEndpointsLabel = new Label(messages.noEndpointsLabel());
				endpointContainer.add(noEndpointsLabel);
			}
		} else {
			if (noEndpointsLabel != null) {
				endpointContainer.remove(noEndpointsLabel);
				noEndpointsLabel = null;
			}
		}
	}

	@Override
	public HasWidgets getEndpointsContainer() {
		return endpointContainer;
	}

	@Override
	public void showExternalInterfacesLoadingIndicator(boolean show) {
		if (show) {
			if (externalInterfacesLoadingIndicator == null) {
				externalInterfacesLoadingIndicator = new Icon(IconType.SPINNER);
				externalInterfacesLoadingIndicator.addStyleName("icon-spin");
				externalInterfaceContainer.add(externalInterfacesLoadingIndicator);
			}
		} else {
			if (externalInterfacesLoadingIndicator != null) {
				externalInterfaceContainer.remove(externalInterfacesLoadingIndicator);
				externalInterfacesLoadingIndicator = null;
			}
		}
	}

	@Override
	public void showNoExternalInterfacesLabel(boolean show) {
		if (show) {
			if (noExternalInterfacesLabel == null) {
				noExternalInterfacesLabel = new Label(messages.noExternalInterfacesLabel());
				externalInterfaceContainer.add(noExternalInterfacesLabel);
			}
		} else {
			if (noExternalInterfacesLabel != null) {
				externalInterfaceContainer.remove(noExternalInterfacesLabel);
				noExternalInterfacesLabel = null;
			}
		}
	}

	@Override
	public HasWidgets getExternalInterfaceContainer() {
		return externalInterfaceContainer;
	}
}