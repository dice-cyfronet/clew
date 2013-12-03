package pl.cyfronet.coin.clew.client.widgets.initialconfigseditor;

import com.google.gwt.i18n.client.Messages;

public interface InitialConfigsEditorMessages extends Messages {
	String modalTitle();
	String closeButtonLabel();
	String noConfigsLabel();
	String nameLabel();
	String namePlaceholder();
	String payloadLabel();
	String payloadPlaceholder();
	String processConfig();
	String payloadHelp();
	String nameOrPayloadEmpty();
	String nameNotUnique();
	String getEditLabel();
	String removalConfirmMessage();
}