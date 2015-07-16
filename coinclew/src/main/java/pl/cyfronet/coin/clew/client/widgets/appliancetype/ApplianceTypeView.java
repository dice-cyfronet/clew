package pl.cyfronet.coin.clew.client.widgets.appliancetype;

import pl.cyfronet.coin.clew.client.widgets.BootstrapHelpers;
import pl.cyfronet.coin.clew.client.widgets.appliancetype.IApplianceTypeView.IApplianceTypePresenter;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.constants.LabelType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.WhiteSpace;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class ApplianceTypeView extends Composite implements IApplianceTypeView, ReverseViewInterface<IApplianceTypePresenter> {
	private static ApplianceTypeViewUiBinder uiBinder = GWT.create(ApplianceTypeViewUiBinder.class);
	interface ApplianceTypeViewUiBinder extends UiBinder<Widget, ApplianceTypeView> {}
	interface Styles extends CssResource {
		String config();
	}
	
	private IApplianceTypePresenter presenter;

	@UiField HTML name;
	@UiField HTML description;
	@UiField ApplianceTypeMessages messages;
	@UiField Button start;
	@UiField FlowPanel initialConfigsContainer;
	@UiField CheckBox checked;
	@UiField Styles style;
	@UiField ListBox initialConfigs;
	@UiField FlowPanel flavorContainer;
	@UiField FlowPanel computeSiteContainer;
	@UiField ListBox computeSites;
	@UiField HTML computeSitesLabel;
	@UiField HTML teamsLabel;
	@UiField ListBox teams;
	
	public ApplianceTypeView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("start")
	void startClicked(ClickEvent event) {
		presenter.onStartApplianceType();
	}
	
	@UiHandler("computeSites")
	void computeSiteChanged(ChangeEvent event) {
		getPresenter().onComputeSiteChanged();
	}

	@Override
	public HasText getName() {
		return name;
	}

	@Override
	public HasText getDescription() {
		description.removeStyleName("muted");
		
		return description;
	}

	@Override
	public void setEmptyDescription() {
		description.setText(messages.emptyDescriptionLabel());
		description.addStyleName("muted");
	}

	@Override
	public void setPresenter(IApplianceTypePresenter presenter) {
		this.presenter = presenter;
		
	}

	@Override
	public IApplianceTypePresenter getPresenter() {
		return presenter;
	}

	@Override
	public void setStartButtonBusyState(boolean busy) {
		BootstrapHelpers.setButtonBusyState(start, busy);
	}

	@Override
	public void addInitialConfigsProgressIndicator() {
		Icon icon = new Icon(IconType.SPINNER);
		icon.addStyleName("icon-spin");
		initialConfigsContainer.add(icon);
	}

	@Override
	public void clearInitialConfigsContainer() {
		initialConfigsContainer.clear();
	}

	@Override
	public void addNoInitialConfigsLabel() {
		Label label = new Label(messages.noInitialConfigLabel());
		label.setType(LabelType.WARNING);
		initialConfigsContainer.add(label);
	}

	@Override
	public void addInitialConfigValue(String configId, String name) {
		initialConfigs.addItem(name, configId);
	}

	@Override
	public void enableControls(boolean enable) {
		start.setEnabled(enable);
		checked.setEnabled(enable);
	}

	@Override
	public HasValue<Boolean> getChecked() {
		return checked;
	}

	@Override
	public void showInitialConfigs() {
		initialConfigs.setVisible(true);
	}

	@Override
	public HasValue<String> getInitialConfigs() {
		return new HasValue<String>() {
			@Override
			public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
				return null;
			}

			@Override
			public void fireEvent(GwtEvent<?> event) {
			}

			@Override
			public String getValue() {
				return initialConfigs.getValue();
			}

			@Override
			public void setValue(String value) {
				initialConfigs.setSelectedValue(value);
			}

			@Override
			public void setValue(String value, boolean fireEvents) {
				initialConfigs.setSelectedValue(value);
			}
		};
	}

	@Override
	public void showFlavorProgress(boolean show) {
		flavorContainer.clear();
		
		Icon spinner = new Icon(IconType.SPINNER);
		spinner.getElement().getStyle().setMarginRight(10, Unit.PX);
		spinner.setSpin(true);
		flavorContainer.add(spinner);
		
		
		InlineHTML loadingText = new InlineHTML(messages.loadingFlavor());
		loadingText.getElement().getStyle().setFontWeight(FontWeight.NORMAL);
		flavorContainer.add(loadingText);
	}

	@Override
	public void showFlavorInformation(String name, Integer hourlyCost) {
		flavorContainer.clear();
		com.google.gwt.user.client.ui.Label label = new com.google.gwt.user.client.ui.Label(messages.flavorInfo(name, 
				"$" + NumberFormat.getFormat("0.0000").format(((float) hourlyCost / 10000))));
		label.getElement().getStyle().setWhiteSpace(WhiteSpace.NORMAL);
		label.getElement().getStyle().setMarginRight(5, Unit.PX);
		label.getElement().getStyle().setFontWeight(FontWeight.NORMAL);
		label.getElement().getStyle().setProperty("fontSize", "smaller");
		flavorContainer.add(label);
	}

	@Override
	public void showFlavorError() {
		flavorContainer.clear();
		Label label = new Label(LabelType.IMPORTANT, messages.flavorError());
		label.getElement().getStyle().setWhiteSpace(WhiteSpace.NORMAL);
		label.getElement().getStyle().setMarginRight(5, Unit.PX);
		flavorContainer.add(label);
	}

	@Override
	public void showComputeSiteProgressIndicator(boolean show) {
		if(show) {
			Icon icon = new Icon(IconType.SPINNER);
			icon.setSpin(true);
			computeSiteContainer.add(icon);
		} else {
			computeSiteContainer.clear();
		}
	}

	@Override
	public void showNoComputeSitesMessage() {
		computeSiteContainer.add(new Label(LabelType.IMPORTANT, messages.noComputeSite()));
	}

	@Override
	public void addComputeSite(String computeSiteId, String computeSiteName) {
		computeSites.addItem(computeSiteName, computeSiteId);
	}

	@Override
	public void showComputeSiteSelector() {
		computeSitesLabel.setVisible(true);
		computeSites.setVisible(true);
	}

	@Override
	public String getAnyComputeSiteLabel() {
		return messages.anyComputeSiteLabel();
	}

	@Override
	public HasValue<String> getComputeSites() {
		return new HasValue<String>() {
			@Override
			public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
				return null;
			}

			@Override
			public void fireEvent(GwtEvent<?> event) {
			}

			@Override
			public String getValue() {
				return computeSites.getValue();
			}

			@Override
			public void setValue(String value) {
				computeSites.setSelectedValue(value);
			}

			@Override
			public void setValue(String value, boolean fireEvents) {
				computeSites.setSelectedValue(value);
			}
		};
	}

	@Override
	public void showNoFlavorInformation() {
		flavorContainer.clear();
		com.google.gwt.user.client.ui.Label label = new com.google.gwt.user.client.ui.Label(messages.noFlavorInfo());
		label.getElement().getStyle().setWhiteSpace(WhiteSpace.NORMAL);
		label.getElement().getStyle().setMarginRight(5, Unit.PX);
		label.getElement().getStyle().setFontWeight(FontWeight.NORMAL);
		label.getElement().getStyle().setProperty("fontSize", "smaller");
		flavorContainer.add(label);
	}

	@Override
	public void showTeamSelector() {
		teamsLabel.setVisible(true);
		teams.setVisible(true);
	}

	@Override
	public void addTeam(String teamId, String teamName) {
		teams.addItem(teamName, teamId);
	}

	@Override
	public String getAnyTeamLabel() {
		return messages.anyTeamLabel();
	}

	@Override
	public HasValue<String> getTeams() {
		return new HasValue<String>() {
			@Override
			public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
				return null;
			}

			@Override
			public void fireEvent(GwtEvent<?> event) {
			}

			@Override
			public String getValue() {
				return teams.getValue();
			}

			@Override
			public void setValue(String value) {
				teams.setSelectedValue(value);
			}

			@Override
			public void setValue(String value, boolean fireEvents) {
				teams.setSelectedValue(value);
			}
		};
	}

	@Override
	public void enableStartButton(boolean enable) {
		start.setEnabled(!enable);
	}
}