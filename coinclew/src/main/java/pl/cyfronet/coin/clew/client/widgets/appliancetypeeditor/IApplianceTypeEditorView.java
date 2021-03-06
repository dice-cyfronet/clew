package pl.cyfronet.coin.clew.client.widgets.appliancetypeeditor;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;

public interface IApplianceTypeEditorView extends IsWidget {
	interface IApplianceTypeEditorPresenter {
		void onUpdate();
		void onSave();
	}

	void showModal(boolean show);
	HasText getName();
	HasText getDescription();
	HasValue<Boolean> getShared();
	HasValue<Boolean> getScalable();
	HasValue<String> getVisibleFor();
	HasValue<String> getDisk();
	HasValue<String> getRam();
	HasValue<String> getCores();
	void displayNameEmptyMessage();
	void clearErrorMessages();
	void setUpdateBusyState(boolean busy);
	void displayGeneralError();
	void showUpdateControl(boolean show);
	void showSaveControl(boolean show);
	void setSaveBusyState(boolean busy);
	void displayGeneralSaveError();
	String getDefaultOptionLabel();
	void addCoreOption(String value, String label);
	void addDiskOption(String value, String label);
	void addRamOption(String value, String label);
}