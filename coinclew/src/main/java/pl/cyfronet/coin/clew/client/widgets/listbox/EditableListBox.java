package pl.cyfronet.coin.clew.client.widgets.listbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class EditableListBox extends Composite {
	private static EditableListBoxUiBinder uiBinder = GWT.create(EditableListBoxUiBinder.class);
	
	interface EditableListBoxUiBinder extends UiBinder<Widget, EditableListBox> {}
	
	private Map<String, String> labels;
	
	private List<ChangeHandler> handlers;
	
	private Timer keyTimer;
	
	@UiField
	UListElement list;
	
	@UiField
	TextBox textBox;
	
	@UiField
	ButtonElement dropdownButton;

	public EditableListBox() {
		labels = new HashMap<>();
		handlers = new ArrayList<>();
		initWidget(uiBinder.createAndBindUi(this));
		textBox.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(keyTimer != null) {
					keyTimer.cancel();
				}
				
				keyTimer = new Timer() {
					@Override
					public void run() {
						valueChanged();
					}
				};
				keyTimer.schedule(500);
			}
		});
	}

	public void addOption(String label, String value) {
		labels.put(value, label);
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
				String value = Element.as(event.getEventTarget()).getAttribute("data-value");
				textBox.setValue(labels.get(value));
				valueChanged();
				event.preventDefault();
			}
		});
		list.appendChild(item);
	}

	public void setValue(String value) {
		textBox.setValue(labels.get(value));
	}

	public void addChangeHandler(ChangeHandler changeHandler) {
		handlers.add(changeHandler);
	}

	public String getValue() {
		String label = textBox.getValue();
		
		for(String key : labels.keySet()) {
			if(labels.get(key).equals(label)) {
				return key;
			}
		}
		
		return label;
	}
	
	public void setEnabled(boolean enabled) {
		textBox.setEnabled(enabled);
		
		if(enabled) {
			dropdownButton.removeClassName("disabled");
		} else {
			dropdownButton.addClassName("disabled");
		}
	}

	private void valueChanged() {
		for(ChangeHandler handler : handlers) {
			handler.onChange(null);
		}
	}
}