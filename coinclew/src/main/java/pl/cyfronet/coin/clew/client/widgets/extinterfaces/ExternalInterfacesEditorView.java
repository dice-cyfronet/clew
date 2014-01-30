package pl.cyfronet.coin.clew.client.widgets.extinterfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import pl.cyfronet.coin.clew.client.widgets.BootstrapHelpers;
import pl.cyfronet.coin.clew.client.widgets.extinterfaces.IExternalInterfacesView.IExternalInterfacesPresenter;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.HelpBlock;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class ExternalInterfacesEditorView extends Composite implements IExternalInterfacesView, ReverseViewInterface<IExternalInterfacesPresenter> {
	private static ExternalInterfacesEditorViewUiBinder uiBinder = GWT.create(ExternalInterfacesEditorViewUiBinder.class);
	interface ExternalInterfacesEditorViewUiBinder extends UiBinder<Widget, ExternalInterfacesEditorView> {}
	interface Styles extends CssResource {
		String mapping();
		String name();
		String links();
		String actions();
		String link();
		String altName();
		String altLinks();
	}
	
	private Icon externalInterfacesLoadingIndicator;
	private Icon endpointsLoadingIndicator;
	private Label noExternalInterfacesLabel;
	private Label noEndpointsLabel;
	private IExternalInterfacesPresenter presenter;
	
	@UiField Modal modal;
	@UiField FlowPanel externalInterfaceContainer;
	@UiField FlowPanel endpointContainer;
	@UiField ExternalInterfacesEditorMessages messages;
	@UiField ListBox externalInterfaceTransportProtocol;
	@UiField ListBox externalInterfaceApplicationProtocol;
	@UiField TextBox externalInterfaceName;
	@UiField Button addExternalInterface;
	@UiField TextBox externalInterfacePort;
	@UiField Label errorLabel;
	@UiField Styles style;
	@UiField TextBox endpointName;
	@UiField TextBox endpointInvocationPath;
	@UiField ListBox endpointType;
	@UiField ListBox endpointTargetPort;
	@UiField TextArea endpointDescription;
	@UiField TextArea endpointDescriptor;
	@UiField HelpBlock endpointTargetPortHelpBlock;
	@UiField TextBox proxySendTimeout;
	@UiField TextBox proxyReadTimeout;

	public ExternalInterfacesEditorView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("close")
	void closeClicked(ClickEvent event) {
		modal.hide();
	}
	
	@UiHandler("externalInterfaceTransportProtocol")
	void transportProtocolChanged(ChangeEvent event) {
		getPresenter().onTransportProtocolChanged();
	}
	
	@UiHandler("addExternalInterface")
	void addExternalInterfaceClicked(ClickEvent event) {
		getPresenter().onAddExternalInterface();
	}
	
	@UiHandler("addEndpoint")
	void addEndpointClicked(ClickEvent event) {
		getPresenter().onAddEndpoint();
	}
	
	@Override
	public void setPresenter(IExternalInterfacesPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IExternalInterfacesPresenter getPresenter() {
		return presenter;
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

	@Override
	public HasValue<String> getTransportProtocol() {
		return new HasValue<String>() {
			@Override
			public HandlerRegistration addValueChangeHandler(
					ValueChangeHandler<String> handler) {
				return null;
			}

			@Override
			public void fireEvent(GwtEvent<?> event) {
			}

			@Override
			public String getValue() {
				return externalInterfaceTransportProtocol.getValue();
			}

			@Override
			public void setValue(String value) {
				externalInterfaceTransportProtocol.setSelectedValue(value);
			}

			@Override
			public void setValue(String value, boolean fireEvents) {
			}
		};
	}

	@Override
	public void setApplicationProtocolEnabled(boolean enabled) {
		externalInterfaceApplicationProtocol.setEnabled(enabled);
	}

	@Override
	public IsWidget addMapping(int beforePosition, final String mappingId, String serviceName,
			int targetPort, String transportProtocol, String httpUrl,
			String httpsUrl, String publicIp, String sourcePort, Map<String, String> properties) {
		FlowPanel panel = new FlowPanel();
		panel.addStyleName(style.mapping());
		
		HTML name = new HTML(serviceName + " (" + transportProtocol + ")");
		name.addStyleName(style.altName());
		panel.add(name);
		
		if (httpUrl != null || httpsUrl != null) {
			FlowPanel links = new FlowPanel();
			links.addStyleName(style.altLinks());
			
			if (httpUrl != null) {
				if (httpUrl.isEmpty()) {
					HTML http = new InlineHTML("http&nbsp;");
					links.add(http);
				} else {
					Anchor httpAnchor = new Anchor("http&nbsp;", true, httpUrl);
					httpAnchor.setTarget("_blank");
					links.add(httpAnchor);
				}
				
				links.add(new Icon(IconType.ARROW_RIGHT));
				links.add(new InlineHTML("&nbsp;" + targetPort));
			}
			
			if (httpsUrl != null) {
				if (links.getWidgetCount() > 0) {
					links.add(new InlineHTML(",&nbsp;"));
				}
				
				if (httpsUrl.isEmpty()) {
					HTML https = new InlineHTML("https&nbsp;");
					links.add(https);
				} else {
					Anchor httpsAnchor = new Anchor("https&nbsp;", true, httpsUrl);
					httpsAnchor.setTarget("_blank");
					links.add(httpsAnchor);
				}

				links.add(new Icon(IconType.ARROW_RIGHT));
				links.add(new InlineHTML("&nbsp;" + targetPort));
			}
			
			if (properties != null) {
				List<String> keys = new ArrayList<String>(properties.keySet());
				Collections.sort(keys);
				String propertiesValue = "";
				
				
				for (String key : keys) {
					propertiesValue += ", " + key + ": " + properties.get(key);
				}
				
				if (!propertiesValue.isEmpty()) {
					links.add(new InlineHTML(propertiesValue));
				}
			}
			
			panel.add(links);
		} else {
			InlineHTML tcpUdpMapping = new InlineHTML(messages.getTcpUdpMapping(publicIp, sourcePort, targetPort));
			tcpUdpMapping.addStyleName(style.altLinks());
			panel.add(tcpUdpMapping);
		}
		
		Button removeButton = new Button();
		removeButton.setIcon(IconType.REMOVE);
		removeButton.setSize(ButtonSize.MINI);
		removeButton.setType(ButtonType.DANGER);
		removeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getPresenter().onRemoveMapping(mappingId);
			}
		});
		
		FlowPanel actions = new FlowPanel();
		actions.addStyleName(style.actions());
		actions.add(removeButton);
		panel.add(actions);
		externalInterfaceContainer.insert(panel, beforePosition);
		
		return panel;
	}

	@Override
	public void removeMappingTemplate(IsWidget widget) {
		externalInterfaceContainer.remove(widget);
	}

	@Override
	public HasText getExternalInterfaceName() {
		return externalInterfaceName;
	}

	@Override
	public void setAddExternalInterfaceBusyState(boolean busy) {
		BootstrapHelpers.setButtonBusyState(addExternalInterface, busy);
	}

	@Override
	public HasText getExternalInterfacePort() {
		return externalInterfacePort;
	}

	@Override
	public HasValue<String> getApplicationProtocol() {
		return new HasValue<String>() {
			@Override
			public HandlerRegistration addValueChangeHandler(
					ValueChangeHandler<String> handler) {
				return null;
			}

			@Override
			public void fireEvent(GwtEvent<?> event) {
			}

			@Override
			public String getValue() {
				return externalInterfaceApplicationProtocol.getValue();
			}

			@Override
			public void setValue(String value) {
				externalInterfaceApplicationProtocol.setSelectedValue(value);
			}

			@Override
			public void setValue(String value, boolean fireEvents) {
			}
		};
	}

	@Override
	public void displayNameOrPortEmptyMessage() {
		errorLabel.setText(messages.nameOrPortEmpty());
		errorLabel.getElement().getStyle().setVisibility(Visibility.VISIBLE);
	}

	@Override
	public void displayWrongPortFormatMessages() {
		errorLabel.setText(messages.wrongPortFormat());
		errorLabel.getElement().getStyle().setVisibility(Visibility.VISIBLE);
	}

	@Override
	public void clearErrorMessages() {
		errorLabel.getElement().getStyle().setVisibility(Visibility.HIDDEN);
	}

	@Override
	public HasText getEndpointName() {
		return endpointName;
	}

	@Override
	public HasText getInvocationPath() {
		return endpointInvocationPath;
	}

	@Override
	public HasValue<String> getEndpointType() {
		return new HasValue<String>() {
			@Override
			public HandlerRegistration addValueChangeHandler(
					ValueChangeHandler<String> handler) {
				return null;
			}

			@Override
			public void fireEvent(GwtEvent<?> event) {
			}

			@Override
			public String getValue() {
				return endpointType.getValue();
			}

			@Override
			public void setValue(String value) {
				endpointType.setSelectedValue(value);
			}

			@Override
			public void setValue(String value, boolean fireEvents) {
			}
		};
	}

	@Override
	public HasValue<String> getTargetPort() {
		return new HasValue<String>() {
			@Override
			public HandlerRegistration addValueChangeHandler(
					ValueChangeHandler<String> handler) {
				return null;
			}

			@Override
			public void fireEvent(GwtEvent<?> event) {
			}

			@Override
			public String getValue() {
				return endpointTargetPort.getValue();
			}

			@Override
			public void setValue(String value) {
				endpointTargetPort.setSelectedValue(value);
			}

			@Override
			public void setValue(String value, boolean fireEvents) {
			}
		};
	}

	@Override
	public HasText getEndpointDescription() {
		return endpointDescription;
	}

	@Override
	public HasText getEndpointDescriptor() {
		return endpointDescriptor;
	}

	@Override
	public IsWidget addEndpoint(int beforePosition, final String endpointId, String endpointName, String httpUrl, String httpsUrl) {
		FlowPanel panel = new FlowPanel();
		panel.addStyleName(style.mapping());
		
		HTML name = new HTML(endpointName);
		name.addStyleName(style.name());
		panel.add(name);

		FlowPanel links = new FlowPanel();
		links.addStyleName(style.links());
		
		if (httpUrl != null) {
			Anchor httpAnchor = new Anchor("http", true, httpUrl);
			httpAnchor.setTarget("_blank");
			httpAnchor.addStyleName(style.link());
			links.add(httpAnchor);
		}
		
		if (httpsUrl != null) {
			Anchor httpsAnchor = new Anchor("https", true, httpsUrl);
			httpsAnchor.setTarget("_blank");
			httpsAnchor.addStyleName(style.link());
			links.add(httpsAnchor);
		}
		
		if (links.getWidgetCount() == 0) {
			links.add(new HTML("&nbsp;"));
		}
		
		panel.add(links);
		
		Button removeButton = new Button();
		removeButton.setIcon(IconType.REMOVE);
		removeButton.setSize(ButtonSize.MINI);
		removeButton.setType(ButtonType.DANGER);
		removeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getPresenter().onRemoveEndpoint(endpointId);
			}
		});
		
		FlowPanel actions = new FlowPanel();
		actions.addStyleName(style.actions());
		actions.add(removeButton);
		panel.add(actions);
		endpointContainer.insert(panel, beforePosition);
		
		return panel;
	}

	@Override
	public void removeEndpoint(IsWidget widget) {
		endpointContainer.remove(widget);
	}

	@Override
	public void addHttpMappingEndpointOption(String portMappintTemplateid, String serviceName, int targetPort) {
		endpointTargetPort.addItem(serviceName + "(" + targetPort + ")", portMappintTemplateid);
	}

	@Override
	public void showEndpointTargetPortHelpBlock(boolean show) {
		endpointTargetPortHelpBlock.setVisible(show);
	}

	@Override
	public void clearEndpointTargetPorts() {
		endpointTargetPort.clear();
	}

	@Override
	public void displayEndpointNameInvocationPathOrPortMappingIdEmptyMessage() {
		errorLabel.setText(messages.endpointNameInvocationPathOrPortMappingIdEmptyMessage());
		errorLabel.getElement().getStyle().setVisibility(Visibility.VISIBLE);
	}

	@Override
	public void showCannotRemoveMappingMessage() {
		Window.alert(messages.cannotRemoveMappingEndpointsExist());
	}

	@Override
	public boolean confirmMappingRemoval() {
		return Window.confirm(messages.confirmMappingRemoval());
	}

	@Override
	public boolean confirmEndpointRemoval() {
		return Window.confirm(messages.confirmEndpointRemoval());
	}

	@Override
	public void selectFirstTargetPort() {
		if (endpointTargetPort.getItemCount() > 1) {
			endpointTargetPort.setSelectedIndex(0);
		}
	}

	@Override
	public HasValue<String> getProxySendTimeout() {
		return proxySendTimeout;
	}

	@Override
	public HasValue<String> getProxyReadTimeout() {
		return proxyReadTimeout;
	}

	@Override
	public void displayWorngProxySendTimeoutMessage() {
		errorLabel.setText(messages.wrongProxySendTimeout());
		errorLabel.getElement().getStyle().setVisibility(Visibility.VISIBLE);
	}

	@Override
	public void displayWorngProxyReadTimeoutMessage() {
		errorLabel.setText(messages.wrongProxyReadTimeout());
		errorLabel.getElement().getStyle().setVisibility(Visibility.VISIBLE);
	}
}