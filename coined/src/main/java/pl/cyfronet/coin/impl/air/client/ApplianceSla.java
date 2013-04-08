package pl.cyfronet.coin.impl.air.client;

public class ApplianceSla {
	private Float  mean_response_time;
	private Integer concurrent_requests;
	private Float requests_throughput;
	
	public Float getMean_response_time() {
		return mean_response_time;
	}

	public void setMean_response_time(Float mean_response_time) {
		this.mean_response_time = mean_response_time;
	}

	public Integer getConcurrent_requests() {
		return concurrent_requests;
	}

	public void setConcurrent_requests(Integer concurrent_requests) {
		this.concurrent_requests = concurrent_requests;
	}

	public Float getRequests_throughput() {
		return requests_throughput;
	}

	public void setRequests_throughput(Float requests_throughput) {
		this.requests_throughput = requests_throughput;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ApplianceSla [mean_response_time=" + mean_response_time
				+ ", concurrent_requests=" + concurrent_requests
				+ ", requests_throughput=" + requests_throughput + "]";
	}
}
