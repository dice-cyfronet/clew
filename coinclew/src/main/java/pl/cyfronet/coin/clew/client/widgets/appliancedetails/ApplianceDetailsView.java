package pl.cyfronet.coin.clew.client.widgets.appliancedetails;

import java.util.Map;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.Radio;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.LabelType;

import pl.cyfronet.coin.clew.client.widgets.BootstrapHelpers;
import pl.cyfronet.coin.clew.client.widgets.appliancedetails.IApplianceDetailsView.IApplianceDetailsPresenter;
import pl.cyfronet.coin.clew.client.widgets.listbox.EditableListBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class ApplianceDetailsView extends Composite implements IApplianceDetailsView, ReverseViewInterface<IApplianceDetailsPresenter> {
	private static ApplianceDetailsViewUiBinder uiBinder = GWT.create(ApplianceDetailsViewUiBinder.class);
	interface ApplianceDetailsViewUiBinder extends UiBinder<Widget, ApplianceDetailsView> {}
	
	private IApplianceDetailsPresenter presenter;
	
	@UiField Modal modal;
	@UiField Button start;
	@UiField ApplianceDetailsMessages messages;
	@UiField FlowPanel container;
	@UiField FlowPanel nameContainer;
	@UiField FlowPanel keyProgress;
	@UiField FlowPanel detailsProgress;

	public ApplianceDetailsView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("close")
	void closeClicked(ClickEvent event) {
		modal.hide();
	}
	
	@UiHandler("start")
	void startClicked(ClickEvent event) {
		getPresenter().onStartInstance();
	}
	
	@Override
	public void setPresenter(IApplianceDetailsPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IApplianceDetailsPresenter getPresenter() {
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
	public HasValue<Boolean> addKey(String id, String name) {
		Radio button = new Radio(name);
		button.setName("key");
		button.setFormValue(id);
		container.add(button);
		
		return button;
	}

	@Override
	public void setStartBusyState(boolean busy) {
		BootstrapHelpers.setButtonBusyState(start, busy);
	}

	@Override
	public HasText addName(String name) {
		FormLabel label = new FormLabel();
		label.setText(messages.nameLabel(name));
		TextBox nameBox = new TextBox();
		nameBox.setPlaceholder(messages.namePlaceholder());
		nameContainer.add(label);
		nameContainer.add(nameBox);
		
		return nameBox;
	}

	@Override
	public HasWidgets getKeyContainer() {
		return container;
	}

	@Override
	public HasWidgets getNameContainer() {
		return nameContainer;
	}

	@Override
	public String getDefaultValueLabel() {
		return messages.defaultValueLabel();
	}

	@Override
	public HasValue<String> addCores(Map<String, String> options, String value, final String applianceTypeId) {
		FormLabel label = new FormLabel();
		label.setText(messages.coresLabel());
		final EditableListBox listBox = createListBox(options, value);
		nameContainer.add(label);		
		nameContainer.add(listBox);
		listBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				getPresenter().onPreferenceChanged(applianceTypeId);
			}
		});
		
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
				return listBox.getValue();
			}

			@Override
			public void setValue(String value) {
				listBox.setValue(value);
			}

			@Override
			public void setValue(String value, boolean fireEvents) {
				listBox.setValue(value);
			}
		};
	}

	private EditableListBox createListBox(Map<String, String> options, String value) {
		EditableListBox result = new EditableListBox();
		
		for(String v : options.keySet()) {
			result.addOption(options.get(v), v);
		}
		
		result.setValue(value);
		
		return result;
	}

	@Override
	public HasValue<String> addRam(Map<String, String> options, String value, final String applianceTypeId) {
		FormLabel label = new FormLabel();
		label.setText(messages.ramLabel());
		final EditableListBox listBox = createListBox(options, value);
		nameContainer.add(label);
		nameContainer.add(listBox);
		listBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				getPresenter().onPreferenceChanged(applianceTypeId);
			}
		});
		
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
				return listBox.getValue();
			}

			@Override
			public void setValue(String value) {
				listBox.setValue(value);
			}

			@Override
			public void setValue(String value, boolean fireEvents) {
				listBox.setValue(value);
			}
		};
	}

	@Override
	public HasValue<String> addDisk(Map<String, String> options, String value, final String applianceTypeId) {
		FormLabel label = new FormLabel();
		label.setText(messages.diskLabel());
		final EditableListBox listBox = createListBox(options, value);
		//in plgrid disk customizations is not possible
		listBox.setEnabled(false);
		nameContainer.add(label);
		nameContainer.add(listBox);
		listBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				getPresenter().onPreferenceChanged(applianceTypeId);
			}
		});
		
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
				return listBox.getValue();
			}

			@Override
			public void setValue(String value) {
				listBox.setValue(value);
			}

			@Override
			public void setValue(String value, boolean fireEvents) {
				listBox.setValue(value);
			}
		};
	}

	@Override
	public void showFlavorProgress(HasWidgets container, boolean show) {
		if(show) {
			container.clear();
			
			Icon spinner = new Icon(IconType.SPINNER);
			spinner.setSpin(true);
			spinner.getElement().getStyle().setMarginRight(10, Unit.PX);
			container.add(spinner);
			container.add(new InlineHTML(messages.loadingFlavor()));
		} else {
			container.clear();
		}
	}

	@Override
	public void showFlavorError(HasWidgets container) {
		container.clear();
		
		Label label = new Label(messages.flavorError());
		label.setType(LabelType.DANGER);
		container.add(label);
	}

	@Override
	public void showFlavorInformation(HasWidgets container, String name, Integer hourlyCost) {
		container.clear();
		
		Label label = new Label(LabelType.INFO, messages.flavorDetails(name,
				"$" + NumberFormat.getFormat("0.0000").format(((float) hourlyCost / 10000))));
		container.add(label);
	}

	@Override
	public HasWidgets addFlavorContainer() {
		FlowPanel flavorContainer = new FlowPanel();
		flavorContainer.getElement().getStyle().setMarginBottom(20, Unit.PX);
		nameContainer.add(flavorContainer);
		
		return flavorContainer;
	}

	@Override
	public void showKeyProgress(boolean show) {
		keyProgress.setVisible(show);
	}

	@Override
	public void showDetailsProgress(boolean show) {
		detailsProgress.setVisible(show);
	}

	@Override
	public void showPreferencesError(HasWidgets container) {
		container.clear();
		
		Label label = new Label(messages.preferencesError());
		label.setType(LabelType.DANGER);
		container.add(label);
	}

	@Override
	public String getAnyComputeSiteLabel() {
		return messages.anyComputeSiteLabel();
	}

	@Override
	public HasValue<String> addComputeSites(Map<String, String> computeSiteLabels, String chosenComputeSiteId) {
		FormLabel label = new FormLabel();
		label.setText(messages.computeSiteLabel());
		final ListBox computeSites = new ListBox();
		
		for(String value : computeSiteLabels.keySet()) {
			computeSites.addItem(computeSiteLabels.get(value), value);
		}
		
		if(chosenComputeSiteId != null && computeSiteLabels.keySet().contains(chosenComputeSiteId)) {
			BootstrapHelpers.setListBoxValue(computeSites, chosenComputeSiteId);
		}
		
		nameContainer.add(label);
		nameContainer.add(computeSites);
		
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
				return computeSites.getValue(computeSites.getSelectedIndex());
			}

			@Override
			public void setValue(String value) {
				BootstrapHelpers.setListBoxValue(computeSites, value);
			}

			@Override
			public void setValue(String value, boolean fireEvents) {
				BootstrapHelpers.setListBoxValue(computeSites, value);
			}
		};
	}

	@Override
	public HasValue<String> addTeamsSelector(Map<String, String> options, String selectedTeam) {
		FormLabel label = new FormLabel();
		label.setText(messages.teamsLabel());
		final ListBox listBox = new ListBox();
		
		for(String id : options.keySet()) {
			listBox.addItem(options.get(id), id);
		}
		
		if(selectedTeam != null) {
			BootstrapHelpers.setListBoxValue(listBox, selectedTeam);
		}
		
		nameContainer.add(label);
		nameContainer.add(listBox);
		
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
				return listBox.getValue(listBox.getSelectedIndex());
			}

			@Override
			public void setValue(String value) {
				BootstrapHelpers.setListBoxValue(listBox, value);
			}

			@Override
			public void setValue(String value, boolean fireEvents) {
				BootstrapHelpers.setListBoxValue(listBox, value);
			}
		};
	}

	@Override
	public String getAnyTeamLabel() {
		return messages.anyTeamLabel();
	}

	@Override
	public void addSeparator() {
		nameContainer.getElement().appendChild(DOM.createElement("hr"));
	}
}
