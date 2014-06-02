package pl.cyfronet.coin.clew.client.widgets.appliancetypeeditor;

import pl.cyfronet.coin.clew.client.widgets.BootstrapHelpers;
import pl.cyfronet.coin.clew.client.widgets.appliancetypeeditor.IApplianceTypeEditorView.IApplianceTypeEditorPresenter;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class ApplianceTypeEditorView extends Composite implements IApplianceTypeEditorView, ReverseViewInterface<IApplianceTypeEditorPresenter> {
	private static ApplianceTypeEditorViewUiBinder uiBinder = GWT.create(ApplianceTypeEditorViewUiBinder.class);
	interface ApplianceTypeEditorViewUiBinder extends UiBinder<Widget, ApplianceTypeEditorView> {}
	
	private IApplianceTypeEditorPresenter presenter;
	
	@UiField Modal modal;
	@UiField TextBox name;
	@UiField TextArea description;
	@UiField CheckBox shared;
	@UiField CheckBox scalable;
	@UiField ListBox visibleFor;
	@UiField Label errorLabel;
	@UiField ApplianceTypeEditorMessages messages;
	@UiField Button update;
	@UiField Button save;
	@UiField UListElement coresList;
	@UiField UListElement ramList;
	@UiField UListElement diskList;
	@UiField TextBox cores;
	@UiField TextBox ram;
	@UiField TextBox disk;
	
	public ApplianceTypeEditorView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("close")
	void closeClicked(ClickEvent event) {
		modal.hide();
	}
	
	@UiHandler("save")
	void saveClicked(ClickEvent event) {
		getPresenter().onSave();
	}
	
	@UiHandler("update")
	void updateClicked(ClickEvent event) {
		getPresenter().onUpdate();
	}
	
	@Override
	public void setPresenter(IApplianceTypeEditorPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IApplianceTypeEditorPresenter getPresenter() {
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
	public HasText getName() {
		return name;
	}

	@Override
	public HasText getDescription() {
		return description;
	}

	@Override
	public HasValue<Boolean> getShared() {
		return new HasValue<Boolean>() {
			@Override
			public HandlerRegistration addValueChangeHandler(
					ValueChangeHandler<Boolean> handler) {
				return null;
			}

			@Override
			public void fireEvent(GwtEvent<?> event) {
			}

			@Override
			public Boolean getValue() {
				return shared.getValue();
			}

			@Override
			public void setValue(Boolean value) {
				shared.setValue(value);
			}

			@Override
			public void setValue(Boolean value, boolean fireEvents) {
			}
		};
	}

	@Override
	public HasValue<Boolean> getScalable() {
		return new HasValue<Boolean>() {
			@Override
			public HandlerRegistration addValueChangeHandler(
					ValueChangeHandler<Boolean> handler) {
				return null;
			}

			@Override
			public void fireEvent(GwtEvent<?> event) {
			}

			@Override
			public Boolean getValue() {
				return scalable.getValue();
			}

			@Override
			public void setValue(Boolean value) {
				scalable.setValue(value);
			}

			@Override
			public void setValue(Boolean value, boolean fireEvents) {
			}
		};
	}

	@Override
	public HasValue<String> getVisibleFor() {
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
				return visibleFor.getValue();
			}

			@Override
			public void setValue(String value) {
				visibleFor.setSelectedValue(value);
			}

			@Override
			public void setValue(String value, boolean fireEvents) {
			}
		};
	}

	@Override
	public HasValue<String> getDisk() {
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
				return getNormalizedValue(disk.getValue());
			}

			@Override
			public void setValue(String value) {
				disk.setValue(getNormalizedLabel(value));
			}

			@Override
			public void setValue(String value, boolean fireEvents) {
			}
		};
	}

	@Override
	public HasValue<String> getRam() {
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
				return getNormalizedValue(ram.getValue());
			}

			@Override
			public void setValue(String value) {
				ram.setValue(getNormalizedLabel(value));
			}

			@Override
			public void setValue(String value, boolean fireEvents) {
			}
		};
	}

	@Override
	public HasValue<String> getCores() {
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
				return getNormalizedValue(cores.getValue());
			}

			@Override
			public void setValue(String value) {
				cores.setValue(getNormalizedLabel(value));
			}

			@Override
			public void setValue(String value, boolean fireEvents) {
			}
		};
	}
	
	private String getNormalizedValue(String label) {
		if(messages.defaultValueLabel().equals(label)) {
			return "0";
		} else {
			return label;
		}
	}
	
	private String getNormalizedLabel(String value) {
		if("0".equals(value)) {
			return messages.defaultValueLabel();
		} else {
			return value;
		}
	}

	@Override
	public void displayNameEmptyMessage() {
		errorLabel.setText(messages.nameEmptyMessage());
		errorLabel.getElement().getStyle().setVisibility(Visibility.VISIBLE);
	}

	@Override
	public void clearErrorMessages() {
		errorLabel.getElement().getStyle().setVisibility(Visibility.HIDDEN);
		errorLabel.setText("");
	}

	@Override
	public void setUpdateBusyState(boolean busy) {
		BootstrapHelpers.setButtonBusyState(update, busy);
	}

	@Override
	public void displayGeneralError() {
		errorLabel.setText(messages.updateErrorMessage());
		errorLabel.getElement().getStyle().setVisibility(Visibility.VISIBLE);
	}

	@Override
	public void showUpdateControl(boolean show) {
		update.setVisible(show);
	}

	@Override
	public void showSaveControl(boolean show) {
		save.setVisible(show);
	}

	@Override
	public void setSaveBusyState(boolean busy) {
		BootstrapHelpers.setButtonBusyState(save, busy);
	}

	@Override
	public void displayGeneralSaveError() {
		errorLabel.setText(messages.saveErrorMessage());
		errorLabel.getElement().getStyle().setVisibility(Visibility.VISIBLE);
	}

	@Override
	public String getDefaultOptionLabel() {
		return messages.defaultValueLabel();
	}

	@Override
	public void addCoreOption(String value, String label) {
		appendOption(value, label, coresList, getCores());
	}

	private void appendOption(String value, String label, UListElement list, final HasValue<String> textBox) {
		Element item = DOM.createElement("li");
		Element anchor = DOM.createAnchor();
		anchor.setAttribute("data-value", value);
		anchor.setInnerHTML(label);
		anchor.setAttribute("href", "#");
		item.appendChild(anchor);
		Event.sinkEvents(anchor, Event.ONCLICK);
		Event.setEventListener(anchor, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				textBox.setValue(Element.as(event.getEventTarget()).getAttribute("data-value"));
				event.preventDefault();
			}
		});
		list.appendChild(item);
	}

	@Override
	public void addDiskOption(String value, String label) {
		appendOption(value, label, diskList, getDisk());
	}

	@Override
	public void addRamOption(String value, String label) {
		appendOption(value, label, ramList, getRam());
	}
}