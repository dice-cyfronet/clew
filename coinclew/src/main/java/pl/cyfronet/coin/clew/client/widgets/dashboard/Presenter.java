package pl.cyfronet.coin.clew.client.widgets.dashboard;

public interface Presenter {
	void onShowStartAppModal();
	void onStartAppModalShown();
	void onFilter(String text);
	void onStartSelected();
	void onStartSingle(int i);
	void onInstanceDetailsShow(int i);
	void onInstanceShutdown(int i);
}