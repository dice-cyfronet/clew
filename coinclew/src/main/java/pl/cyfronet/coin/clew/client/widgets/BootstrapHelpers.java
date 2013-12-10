package pl.cyfronet.coin.clew.client.widgets;

import java.util.HashMap;
import java.util.Map;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;

public class BootstrapHelpers {
	private static Map<Button, IconType> originalIcons = new HashMap<Button, IconType>();
	
	public static void setButtonBusyState(Button button, boolean busy) {
		if (busy) {
			originalIcons.put(button, getIcon(button));
			button.setIcon(IconType.SPINNER);
			spinIcon(button);
			button.setEnabled(false);
		} else {
			IconType originalIcon = originalIcons.remove(button);
			stopSpin(button);
			
			if (originalIcon != null) {
				button.setIcon(originalIcon);
			} else {
				button.setIcon(IconType.QUESTION);
			}
			
			button.setEnabled(true);
		}
	}

	private static void stopSpin(Button button) {
		Element iconElement = DOM.getChild(button.getElement(), 0);
		
		if (iconElement != null && iconElement.getClassName() != null &&
				iconElement.getClassName().endsWith("icon-spin")) {
			iconElement.setClassName(iconElement.getClassName().substring(0, iconElement.getClassName().length() - 9).trim());
		}
	}

	private static void spinIcon(Button button) {
		Element iconElement = DOM.getChild(button.getElement(), 0);
		
		if (iconElement != null && iconElement.getClassName() != null &&
				iconElement.getClassName().startsWith("icon-")) {
			iconElement.setClassName(iconElement.getClassName() + " icon-spin");
		}
	}

	private static IconType getIcon(Button button) {
		Element iconElement = DOM.getChild(button.getElement(), 0);
		
		if (iconElement != null && iconElement.getClassName() != null &&
				iconElement.getClassName().startsWith("icon-")) {
			return IconType.valueOf(iconElement.getClassName().substring(5).replaceAll("-", "_")
					.toUpperCase());
		}
		
		return IconType.QUESTION;
	}
}