package pl.cyfronet.coin.clew.client.widgets.dashboard;

import java.util.HashMap;
import java.util.Map;

import pl.cyfronet.coin.clew.client.controller.cf.applianceinstance.ApplianceInstance.Status;
import pl.cyfronet.coin.clew.client.widgets.dashboard.DashboardPresenter.View;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ButtonGroup;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.Collapse;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.RadioButton;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.constants.LabelType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class DashboardPanel extends Composite implements View {
	private static DashboardPanelUiBinder uiBinder = GWT.create(DashboardPanelUiBinder.class);
	interface DashboardPanelUiBinder extends UiBinder<Widget, DashboardPanel> {}
	
	@UiField Modal startAppPopup;
	@UiField FlexTable appsTable;
	@UiField Icon appListSpinner;
	@UiField TextBox filterAppsBox;
	@UiField Button startSelectedApps;
	@UiField FlexTable instances;
	
	private Provider<Presenter> presenter;
	private Timer filterTimer;
	private DashboardMessages messages;
	private Map<Integer, Collapse> collapsables;

	@Inject
	public DashboardPanel(Provider<Presenter> presenter, DashboardMessages messages) {
		this.presenter = presenter;
		this.messages = messages;
		initWidget(uiBinder.createAndBindUi(this));
		collapsables = new HashMap<Integer, Collapse>();
		init();
	}

	@UiHandler("showStartAppModal")
	void onShowStartAppModal(ClickEvent event) {
		presenter.get().onShowStartAppModal();
	}
	
	@UiHandler("startSelectedApps")
	void onStartSelected(ClickEvent event) {
		presenter.get().onStartSelected();
	}
	
	@UiHandler("filterAppsBox")
	void onFilter(KeyUpEvent event) {
		if (filterTimer != null) {
			filterTimer.cancel();
		} else {
			filterTimer = new Timer() {
				@Override
				public void run() {
					presenter.get().onFilter(filterAppsBox.getText());
					filterTimer = null;
				}
			};
		}
		
		filterTimer.schedule(500);
	}

	@Override
	public void showStartAppPopup() {
		startAppPopup.show();
	}

	@Override
	public void clearAppsTable() {
		appsTable.clear();
	}

	@Override
	public void setAppsSpinnerVisible(boolean visible) {
		appListSpinner.setVisible(visible);
		appListSpinner.setSpin(visible);
	}

	@Override
	public void addStartButton(final int i) {
		Button startButton = new Button("", IconType.PLAY);
		startButton.setType(ButtonType.SUCCESS);
		startButton.setSize(ButtonSize.MINI);
		startButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.get().onStartSingle(i);
			}
		});
		appsTable.setWidget(i, 0, startButton);
	}

	@Override
	public HasValue<Boolean> addCheckButton(int i) {
		CheckBox checkBox = new CheckBox();
		appsTable.setWidget(i, 1, checkBox);
		
		return checkBox;
	}

	@Override
	public void setAppVisibility(int i, boolean visible) {
		appsTable.getRowFormatter().setVisible(i, visible);
	}

	@Override
	public void setStartSelectedWidgetBusyState(boolean busy) {
		if (busy) {
			startSelectedApps.setEnabled(false);
			startSelectedApps.setIcon(IconType.SPINNER);
		} else {
			startSelectedApps.setEnabled(true);
			startSelectedApps.setIcon(IconType.PLAY);
		}
	}

	@Override
	public void hideStartAppPopup() {
		startAppPopup.hide();
	}

	@Override
	public void setStartAppWidgetBusyState(int i, boolean busy) {
		Button startButton = (Button) appsTable.getWidget(i, 0);
		
		if (busy) {
			startButton.setEnabled(false);
			startButton.setIcon(IconType.SPINNER);
		} else {
			startButton.setEnabled(true);
			startButton.setIcon(IconType.PLAY);
		}
	}

	@Override
	public void addAppDescription(int i, String description) {
		appsTable.setText(i, 3, description);
	}

	@Override
	public void addAppName(int i, String name) {
		appsTable.setHTML(i, 2, "<strong>" + name + "</strong>");
	}

	@Override
	public void setInstanceName(int i, String name) {
		instances.setText(getDetailsRow(i), 0, name);
	}

	@Override
	public void setInstanceIp(int i, String ip) {
		instances.setText(getDetailsRow(i), 1, ip);
	}
	
	@Override
	public void setInstanceActionsAndDetails(final int i) {
		ButtonGroup buttonGroup = new ButtonGroup();
		Button detailsButton = new Button("", IconType.ALIGN_JUSTIFY);
		detailsButton.setSize(ButtonSize.MINI);
		detailsButton.setType(ButtonType.WARNING);
		detailsButton.setTitle(messages.getDetailsButtonTooltip());
		detailsButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.get().onInstanceDetailsShow(i);
			}
		});
		buttonGroup.add(detailsButton);
		
		Button shutdownButton = new Button("", IconType.OFF);
		shutdownButton.setSize(ButtonSize.MINI);
		shutdownButton.setType(ButtonType.DANGER);
		shutdownButton.setTitle(messages.getShutdownButtonTooltip());
		shutdownButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.get().onInstanceShutdown(i);
			}
		});
		buttonGroup.add(shutdownButton);
		instances.setWidget(getDetailsRow(i), 5, buttonGroup);
		instances.getFlexCellFormatter().setColSpan(getCollapsibleRow(i), 0, 6);
		
		FlowPanel collapse = new FlowPanel();
		collapse.add(new Label("Instance details"));
		Collapse details = new Collapse();
		details.add(collapse);
		instances.setWidget(getCollapsibleRow(i), 0, details);
		instances.getCellFormatter().getElement(getCollapsibleRow(i), 0).getStyle().setBorderStyle(BorderStyle.NONE);
		collapsables.put(i, details);
	}

	@Override
	public void setInstanceStatus(int i, Status status) {
		instances.setText(getDetailsRow(i), 4, status != null ? status.toString() : null);
	}

	@Override
	public void setInstanceSpec(int i, String spec) {
		instances.setText(getDetailsRow(i), 3, spec);
	}

	@Override
	public void setInstanceLocation(int i, String location) {
		instances.setText(getDetailsRow(i), 2, location);
	}
	
	@Override
	public void confirmShutdown(Command command) {
		if  (Window.confirm(messages.getShutdownConfirmationMessage())) {
			command.execute();
		}
	}
	
	@Override
	public void toggleInstanceDetails(int i) {
		collapsables.get(i).toggle();
	}
	
	private void init() {
		Element caption = DOM.createCaption();
		caption.setInnerText(messages.getInstancesTableCaption());
		instances.getElement().appendChild(caption);
		
		Element header = DOM.createTHead();
		instances.getElement().appendChild(header);
		
		Element tr = DOM.createTR();
		header.appendChild(tr);
		
		Element nameHeader = DOM.createTH();
		nameHeader.setInnerText(messages.getInstanceNameHeader());
		tr.appendChild(nameHeader);
		
		Element ipHeader = DOM.createTH();
		ipHeader.setInnerText(messages.getInstanceIpHeader());
		tr.appendChild(ipHeader);
		
		Element locationHeader = DOM.createTH();
		locationHeader.setInnerText(messages.getInstanceLocationHeader());
		tr.appendChild(locationHeader);
		
		Element specHeader = DOM.createTH();
		specHeader.setInnerText(messages.getInstanceSpecHeader());
		tr.appendChild(specHeader);
		
		Element statusHeader = DOM.createTH();
		statusHeader.setInnerText(messages.getInstanceStatusHeader());
		tr.appendChild(statusHeader);
		
		Element actionsHeader = DOM.createTH();
		actionsHeader.setInnerText(messages.getInstanceActionsHeader());
		tr.appendChild(actionsHeader);
	}
	
	private int getDetailsRow(int i) {
		return i * 2;
	}
	
	private int getCollapsibleRow(int i) {
		return i * 2 + 1;
	}

	@Override
	public HasValue<Boolean> addInitialConfig(int row, String applianceTypeId, String configurationName) {
		FlowPanel panel = null;
		
		if (!(appsTable.getWidget(row, 3) instanceof FlowPanel)) {
			String description = appsTable.getText(row, 3);
			panel = new FlowPanel();
			panel.add(new HTML(description));
			appsTable.setWidget(row, 3, panel);
		} else {
			panel = (FlowPanel) appsTable.getWidget(row, 3);
		}
		
		RadioButton radio = new RadioButton(applianceTypeId, configurationName);
		panel.add(radio);
		
		return radio;
	}

	@Override
	public void enableStartButton(int j, boolean enabled) {
		Button startButton = (Button) appsTable.getWidget(j, 0);
		startButton.setEnabled(enabled);
	}

	@Override
	public void enableCheckButton(int j, boolean enabled) {
		CheckBox checkBox = (CheckBox) appsTable.getWidget(j, 1);
		checkBox.setEnabled(enabled);
	}

	@Override
	public void addNoConfigurationMessage(int j) {
		String description = appsTable.getText(j, 3);
		VerticalPanel panel = new VerticalPanel();
		panel.add(new HTML(description));
		
		Label label = new Label(LabelType.WARNING, messages.getNoInitialConfigurationMessage());
		panel.add(label);
		appsTable.setWidget(j, 3, panel);
	}

	@Override
	public void hideInstanceRow(int instanceIndex) {
		// TODO Auto-generated method stub
		
	}
}