package pl.cyfronet.coin.clew.client.widgets;

import java.util.HashMap;
import java.util.Map;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;

public class BootstrapHelpers {
	private static Map<Button, IconType> originalIcons = new HashMap<Button, IconType>();
	
	public static void setButtonBusyState(Button button, boolean busy) {
		if(busy) {
			originalIcons.put(button, getIcon(button));
			button.setIcon(IconType.SPINNER);
			button.setIconSpin(true);
			button.setEnabled(false);
		} else {
			IconType originalIcon = originalIcons.remove(button);
			
			if(originalIcon != null) {
				button.setIconSpin(false);
				button.setIcon(originalIcon);
			}
			
			button.setEnabled(true);
		}
	}
	
	public static void setListBoxValue(ListBox listBox, String value) {
		for(int i = 0; i < listBox.getItemCount(); i++) {
			if(listBox.getValue(i) != null && listBox.getValue(i).equals(value)) {
				listBox.setSelectedIndex(i);
				
				break;
			}
		}
	}

	private static IconType getIcon(Button button) {
		Element iconElement = DOM.getChild(button.getElement(), 0);
		
		if(iconElement != null && iconElement.getClassName() != null &&
				iconElement.getClassName().startsWith("fa fa-")) {
			return IconType.valueOf(iconElement.getClassName().substring(6).replaceAll("-", "_")
					.toUpperCase());
		}
		
		return IconType.QUESTION;
	}
}