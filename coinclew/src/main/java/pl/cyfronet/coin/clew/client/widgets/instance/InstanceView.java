package pl.cyfronet.coin.clew.client.widgets.instance;

import java.util.HashMap;
import java.util.Map;

import pl.cyfronet.coin.clew.client.widgets.BootstrapHelpers;
import pl.cyfronet.coin.clew.client.widgets.instance.IInstanceView.IInstancePresenter;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ButtonGroup;
import com.github.gwtbootstrap.client.ui.Collapse;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.Popover;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.constants.LabelType;
import com.github.gwtbootstrap.client.ui.constants.Placement;
import com.github.gwtbootstrap.client.ui.constants.Trigger;
import com.github.gwtbootstrap.client.ui.constants.VisibilityChange;
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
		String statusLabel();
	}
	
	private IInstancePresenter presenter;
	private Button shutdown;
	private Button externalInterfaces;
	private Button saveButton;
	private Label noServiceLabel;
	private Label noWebApplicationLabel;
	private Label noOtherServicesLabel;
	private boolean collapsed;
	private Map<String, Label> httpStatuses;
	private Map<String, Label> httpsStatuses;
	
	
	@UiField HTML name;
//	@UiField HTML spec;
	@UiField InstanceMessages messages;
	@UiField HTML ip;
	@UiField HTML location;
	@UiField FlowPanel status;
	@UiField ButtonGroup controls;
	@UiField Collapse collapse;
	@UiField FlowPanel webApplicationsContainer;
	@UiField FlowPanel serviceContainer;
	@UiField Styles style;
	@UiField FlowPanel otherServiceContainer;
	@UiField HTML description;
	@UiField Label bill;
	@UiField Button showDetails;
	@UiField Tooltip prepaidTooltip;
	@UiField FlowPanel details;
	@UiField FlowPanel noVmsLabel;

	public InstanceView() {
		initWidget(uiBinder.createAndBindUi(this));
		collapsed = true;
		httpStatuses = new HashMap<String, Label>();
		httpsStatuses = new HashMap<String, Label>();
	}
	
	@UiHandler("showDetails")
	void showDetailsClicked(ClickEvent event) {
		collapse.toggle();
		collapsed = !collapsed;
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
	public HasHTML getLocation() {
		return location;
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
	public IsWidget addService(String name, String httpUrl, String httpsUrl, String descriptor,
			String redirectionId, String httpUrlStatus, String httpsUrlStatus) {
		FlowPanel panel = new FlowPanel();
		panel.addStyleName(style.service());
		
		FlowPanel namePanel = new FlowPanel();
		InlineHTML nameWidget = new InlineHTML(name);
		nameWidget.addStyleName(style.detailsName());
		namePanel.add(nameWidget);
		panel.add(namePanel);
		
		FlowPanel links = new FlowPanel();
		panel.add(links);
		links.addStyleName(style.links());
		
		if (httpUrl != null) {
			Label status = new Label(httpUrlStatus);
			links.add(createEndpointPanel(status, httpUrl, httpUrlStatus, "http"));
			httpStatuses.put(redirectionId, status);
		}
		
		if (httpsUrl != null) {
			Label status = new Label(httpsUrlStatus);
			links.add(createEndpointPanel(status, httpsUrl, httpsUrlStatus, "https"));
			httpsStatuses.put(redirectionId, status);
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
		
		namePanel.add(descriptorButton);
		serviceContainer.add(panel);
		
		return panel;
	}

	@Override
	public IsWidget addWebApplication(String name, String httpUrl, String httpsUrl,
			String redirectionId, String httpUrlStatus, String httpsUrlStatus) {
		FlowPanel panel = new FlowPanel();
		panel.addStyleName(style.service());
		
		HTML nameWidget = new HTML(name);
		nameWidget.addStyleName(style.detailsName());
		panel.add(nameWidget);
		
		FlowPanel links = new FlowPanel();
		panel.add(links);
		links.addStyleName(style.links());
		
		if (httpUrl != null) {
			Label status = new Label(httpUrlStatus);
			links.add(createEndpointPanel(status, httpUrl, httpUrlStatus, "http"));
			httpStatuses.put(redirectionId, status);
		}
		
		if (httpsUrl != null) {
			Label status = new Label(httpsUrlStatus);
			links.add(createEndpointPanel(status, httpsUrl, httpsUrlStatus, "https"));
			httpsStatuses.put(redirectionId, status);
		}
		
		webApplicationsContainer.add(panel);
		
		return panel;
	}

	private Widget createEndpointPanel(Label statusLabel, String httpUrl, String httpUrlStatus, String anchorName) {
		FlowPanel endpointPanel = new FlowPanel();
		endpointPanel.getElement().getStyle().setProperty("display", "table-row");
		Anchor http = new Anchor(anchorName, httpUrl);
		http.setTarget("_blank");
		http.addStyleName(style.anchor());
		endpointPanel.add(http);
		statusLabel.addStyleName(style.statusLabel());
		statusLabel.setType(getStatusLabelType(httpUrlStatus));
		
		FlowPanel statusPanel = new FlowPanel();
		statusPanel.getElement().getStyle().setProperty("display", "table-cell");
		statusPanel.add(statusLabel);
		endpointPanel.add(statusPanel);
		
		return endpointPanel;
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
			externalInterfaces.setEnabled(false);
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
			saveButton.setEnabled(false);
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
	public void removeWebapp(IsWidget widget) {
		webApplicationsContainer.remove(widget);
	}

	@Override
	public void removeService(IsWidget widget) {
		serviceContainer.remove(widget);
	}

	@Override
	public IsWidget addOtherService(String serviceName, String publicIp, String port, String helpBlock) {
		FlowPanel panel = new FlowPanel();
		panel.addStyleName(style.service());
		
		FlowPanel namePanel = new FlowPanel();
		InlineHTML nameWidget = new InlineHTML(serviceName);
		nameWidget.addStyleName(style.detailsName());
		namePanel.add(nameWidget);
		panel.add(namePanel);
		
		HTML info = new HTML(publicIp + ":" + port);
		info.addStyleName(style.links());
		panel.add(info);
		
		if (helpBlock != null) {
			Button helpButton = new Button("", IconType.SIGNIN);
			helpButton.setSize(ButtonSize.MINI);
			helpButton.setToggle(true);
			helpButton.setType(ButtonType.INFO);
			helpButton.addStyleName(style.descriptor());
			helpButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Popover.changeVisibility(event.getRelativeElement(), VisibilityChange.TOGGLE.get());
				}
			});
			
			Popover helpPopover = new Popover();
			helpPopover.setText(helpBlock);
			helpPopover.setWidget(helpButton);
			helpPopover.setPlacement(Placement.LEFT);
			helpPopover.setHeading(messages.sshHelpHeader());
			helpPopover.setTrigger(Trigger.MANUAL);
			
			panel.add(new InlineHTML("&nbsp;"));
			namePanel.add(helpPopover);
		}
		
		otherServiceContainer.add(panel);
		
		return panel;
	}

	@Override
	public void showNoOtherServicesLabel(boolean show) {
		if (show) {
			if (noOtherServicesLabel == null) {
				noOtherServicesLabel = new Label(messages.noOtherServices());
				otherServiceContainer.add(noOtherServicesLabel);
			}
		} else {
			if (noOtherServicesLabel != null) {
				otherServiceContainer.remove(noOtherServicesLabel);
				noOtherServicesLabel = null;
			}
		}
	}

	@Override
	public void removeOtherService(IsWidget widget) {
		otherServiceContainer.remove(widget);
	}

	@Override
	public void setUnsatisfiedState(String stateExplanation) {
		status.clear();
		
		Label unsatisfiedLabel = new Label(messages.unsatisfiedLabel());
		unsatisfiedLabel.setType(LabelType.IMPORTANT);
		
		Tooltip tooltip = new Tooltip(stateExplanation);
		tooltip.add(unsatisfiedLabel);
		status.add(tooltip);
	}

	@Override
	public String getSshHelpBlock(String publicIp, String sourcePort) {
		return messages.sshHelpBlock(publicIp, sourcePort);
	}

	@Override
	public void setNoDescription() {
		description.setText(messages.getEmptyDescription());
	}

	@Override
	public HasText getDescription() {
		return description;
	}

	@Override
	public HasText getCost() {
		return bill;
	}

	@Override
	public void setStatus(String status) {
		Widget label = new Label(status);
		
		if(status != null) {
			if(status.equals("active")) {
				((Label) label).setType(LabelType.SUCCESS);
			} else {
				((Label) label).setType(LabelType.WARNING);
			}
		} else {
			label = new HTML("&nbsp;");
		}
		
		this.status.clear();
		this.status.add(label);
	}

	@Override
	public void enableSave(boolean enable) {
		if(saveButton != null) {
			saveButton.setEnabled(enable);
		}
	}

	@Override
	public void enableExternalInterfaces(boolean enable) {
		if(externalInterfaces != null) {
			externalInterfaces.setEnabled(enable);
		}
	}

	@Override
	public void enableCollapsable(boolean enable) {
		showDetails.setEnabled(enable);
	}

	@Override
	public void collapseDetails() {
		if(!collapsed) {
			showDetailsClicked(null);
			showDetails.setActive(false);
		}
	}
	
	private LabelType getStatusLabelType(String status) {
		if("pending".equals(status)) {
			return LabelType.WARNING;
		} else if("ok".equals(status)) {
			return LabelType.SUCCESS;
		} else if("lost".equals(status)) {
			return LabelType.IMPORTANT;
		} else {
			return LabelType.DEFAULT;
		}
	}

	@Override
	public void updateHttpStatus(String redirectionId, String status) {
		Label statusLabel = httpStatuses.get(redirectionId);
		
		if(statusLabel != null) {
			statusLabel.setType(getStatusLabelType(status));
			statusLabel.setText(status);
		}
	}

	@Override
	public void updateHttpsStatus(String redirectionId, String status) {
		Label statusLabel = httpsStatuses.get(redirectionId);
		
		if(statusLabel != null) {
			statusLabel.setType(getStatusLabelType(status));
			statusLabel.setText(status);
		}
	}

	@Override
	public void setFlavorDetails(String prepaidUntil, String flavorName, Float cpus, Float ram, Float hdd) {
		prepaidTooltip.setText(messages.prepaidUntil(prepaidUntil, flavorName, cpus, ram, hdd));
		prepaidTooltip.reconfigure();
	}

	@Override
	public void showDetailsPanel(boolean show) {
		details.setVisible(show);
	}

	@Override
	public void showNoVmsLabel(boolean show) {
		noVmsLabel.setVisible(show);
	}

	@Override
	public String getNoVmsLabel() {
		return messages.noVmsShortLabel();
	}
}