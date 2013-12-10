package pl.cyfronet.coin.clew.client.widgets.instance;

import pl.cyfronet.coin.clew.client.widgets.BootstrapHelpers;
import pl.cyfronet.coin.clew.client.widgets.instance.IInstanceView.IInstancePresenter;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ButtonGroup;
import com.github.gwtbootstrap.client.ui.Collapse;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class InstanceView extends Composite implements IInstanceView, ReverseViewInterface<IInstancePresenter> {
	private static InstanceViewUiBinder uiBinder = GWT.create(InstanceViewUiBinder.class);
	interface InstanceViewUiBinder extends UiBinder<Widget, InstanceView> {}
	interface Styles extends CssResource {
		String anchor();
		String descriptor();
		String service();
		String detailsName();
		String links();
	}
	
	private IInstancePresenter presenter;
	private Button shutdown;
	private Button externalInterfaces;
	private Button saveButton;
	private Label noAccessInfoLabel;
	
	@UiField HTML name;
//	@UiField HTML spec;
	@UiField InstanceMessages messages;
	@UiField HTML ip;
	@UiField HTML location;
	@UiField HTML status;
	@UiField ButtonGroup controls;
	@UiField Collapse collapse;
	@UiField FlowPanel webApplicationsContainer;
	@UiField FlowPanel serviceContainer;
	@UiField Styles style;
	@UiField HTML accessInfoLabel;
	@UiField FlowPanel accessInfoContainer;
	private Label noServiceLabel;
	private Label noWebApplicationLabel;

	public InstanceView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("showDetails")
	void showDetailsClicked(ClickEvent event) {
		collapse.toggle();
	}
	
	@Override
	public void setPresenter(IInstancePresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IInstancePresenter getPresenter() {
		return presenter;
	}

	@Override
	public HasText getName() {
		return name;
	}

//	@Override
//	public HasText getSpec() {
//		return spec;
//	}

	@Override
	public String getSpecStanza(String cpu, String ram, String disk) {
		return messages.getSpec(cpu != null ? cpu : "0", ram != null ? ram : "0", disk != null ? disk : "0");
	}

	@Override
	public HasHTML getIp() {
		return ip;
	}

	@Override
	public HasText getLocation() {
		return location;
	}

	@Override
	public HasHTML getStatus() {
		return status;
	}

	@Override
	public void setShutdownBusyState(boolean busy) {
		if (shutdown != null) {
			BootstrapHelpers.setButtonBusyState(shutdown, busy);
		}
	}

	@Override
	public boolean confirmInstanceShutdown() {
		return Window.confirm(messages.shutdownConfirmation());
	}

	@Override
	public void addShutdownControl() {
		if (shutdown == null) {
			shutdown = new Button();
			shutdown.setIcon(IconType.OFF);
			shutdown.setType(ButtonType.DANGER);
			shutdown.setSize(ButtonSize.MINI);
			shutdown.setLoadingText("<i class='icon-spinner icon-spin'></i>");
			shutdown.setTitle(messages.shutdownTooltip());
			shutdown.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					getPresenter().onShutdownClicked();
				}
			});
			controls.add(shutdown);
		}
	}

	@Override
	public IsWidget addService(String name, String httpUrl, String httpsUrl, String descriptor) {
		FlowPanel panel = new FlowPanel();
		panel.addStyleName(style.service());
		
		InlineHTML nameWidget = new InlineHTML(name);
		nameWidget.addStyleName(style.detailsName());
		panel.add(nameWidget);
		
		FlowPanel links = new FlowPanel();
		panel.add(links);
		links.addStyleName(style.links());
		
		if (httpUrl != null) {
			Anchor http = new Anchor("http", httpUrl);
			http.setTarget("_blank");
			http.addStyleName(style.anchor());
			links.add(http);
		}
		
		if (httpsUrl != null) {
			Anchor https = new Anchor("https", httpsUrl);
			https.setTarget("_blank");
			https.addStyleName(style.anchor());
			links.add(https);
		}
		
		Button descriptorButton = new Button();
		descriptorButton.setIcon(IconType.FILE);
		descriptorButton.setType(ButtonType.INFO);
		descriptorButton.addStyleName(style.descriptor());
		descriptorButton.setSize(ButtonSize.MINI);
		
		if (descriptor != null && !descriptor.trim().isEmpty()) {
			descriptorButton.setTitle(messages.descriptorButtonTooltip());
		} else {
			descriptorButton.setTitle(messages.noDescriptorButtonTooltip());
		}
		
		if (descriptor != null && !descriptor.trim().isEmpty()) {
			final Modal modal = new Modal();
			modal.setTitle(messages.descriptorModalTitle());
			modal.add(new HTML(descriptor));
			panel.add(modal);
			descriptorButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					modal.show();
				}
			});
		} else {
			descriptorButton.setEnabled(false);
		}
		
		panel.add(descriptorButton);
		serviceContainer.add(panel);
		
		return panel;
	}

	@Override
	public IsWidget addWebApplication(String name, String httpUrl, String httpsUrl) {
		FlowPanel panel = new FlowPanel();
		panel.addStyleName(style.service());
		
		InlineHTML nameWidget = new InlineHTML(name);
		nameWidget.addStyleName(style.detailsName());
		panel.add(nameWidget);
		
		FlowPanel links = new FlowPanel();
		panel.add(links);
		links.addStyleName(style.links());
		
		if (httpUrl != null) {
			Anchor http = new Anchor("http", httpUrl);
			http.setTarget("_blank");
			http.addStyleName(style.anchor());
			links.add(http);
		}
		
		if (httpsUrl != null) {
			Anchor https = new Anchor("https", httpsUrl);
			https.setTarget("_blank");
			https.addStyleName(style.anchor());
			links.add(https);
		}
		
		webApplicationsContainer.add(panel);
		
		return panel;
	}

	@Override
	public void showNoServicesLabel(boolean show) {
		if (show) {
			if (noServiceLabel == null) {
				noServiceLabel = new Label(messages.noServicesLabel());
				serviceContainer.add(noServiceLabel);
			}
		} else {
			if (noServiceLabel != null) {
				serviceContainer.remove(noServiceLabel);
				noServiceLabel = null;
			}
		}
	}

	@Override
	public void showNoWebApplicationsLabel(boolean show) {
		if (show) {
			if (noWebApplicationLabel == null) {
				noWebApplicationLabel = new Label(messages.noWebApplicationsLabel());
				webApplicationsContainer.add(noWebApplicationLabel);
			}
		} else {
			if (noWebApplicationLabel != null) {
				webApplicationsContainer.remove(noWebApplicationLabel);
				noWebApplicationLabel = null;
			}
		}
	}

	@Override
	public void addExternalInterfacesControl() {
		if (externalInterfaces == null) {
			externalInterfaces = new Button();
			externalInterfaces.setIcon(IconType.COGS);
			externalInterfaces.setType(ButtonType.WARNING);
			externalInterfaces.setSize(ButtonSize.MINI);
			externalInterfaces.setLoadingText("<i class='icon-spinner icon-spin'></i>");
			externalInterfaces.setTitle(messages.externalInterfacesTooltip());
			externalInterfaces.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					getPresenter().onExternalInterfacesClicked();
				}
			});
			controls.insert(externalInterfaces, 0);
		}
	}

	@Override
	public void addSaveControl() {
		if (saveButton == null) {
			saveButton = new Button();
			saveButton.setIcon(IconType.SAVE);
			saveButton.setType(ButtonType.WARNING);
			saveButton.setSize(ButtonSize.MINI);
			saveButton.setLoadingText("<i class='icon-spinner icon-spin'></i>");
			saveButton.setTitle(messages.saveTooltip());
			saveButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					getPresenter().onSave();
				}
			});
			controls.insert(saveButton, 0);
		}
	}

	@Override
	public void showNoAccessInfoLabel(boolean show) {
		if (show) {
			if (noAccessInfoLabel == null) {
				noAccessInfoLabel = new Label(messages.noAccessInfos());
				accessInfoContainer.add(noAccessInfoLabel);
			}
		} else {
			if (noAccessInfoLabel != null) {
				accessInfoContainer.remove(noAccessInfoLabel);
				noAccessInfoLabel = null;
			}
		}
	}

	@Override
	public void showAccessInfoSection() {
		accessInfoLabel.setVisible(true);
		accessInfoContainer.setVisible(true);
	}

	@Override
	public IsWidget addAccessInfo(String serviceName, String publicIp, String port) {
		FlowPanel panel = new FlowPanel();
		panel.addStyleName(style.service());
		
		InlineHTML nameWidget = new InlineHTML(serviceName);
		nameWidget.addStyleName(style.detailsName());
		panel.add(nameWidget);
		
		InlineHTML info = new InlineHTML(publicIp + ":" + port);
		panel.add(info);
		accessInfoContainer.add(panel);
		
		return panel;
	}

	@Override
	public void removeAccessInfo(IsWidget widget) {
		accessInfoContainer.remove(widget);
	}

	@Override
	public void removeWebapp(IsWidget widget) {
		webApplicationsContainer.remove(widget);
	}

	@Override
	public void removeService(IsWidget widget) {
		serviceContainer.remove(widget);
	}
}