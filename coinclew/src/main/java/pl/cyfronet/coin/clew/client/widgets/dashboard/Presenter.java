package pl.cyfronet.coin.clew.client.widgets.dashboard;

public interface Presenter {
	void onShowStartAppModal();
	void onStartAppModalShown();
	void onFilter(String text);
}