import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

public class RESTUtil {


	public static Response sendPOST(String url, String auth, Object data) {

		ResteasyClientBuilder builder = new ResteasyClientBuilder();
		ResteasyClient client = builder.build();

		WebTarget target = client.target(url);

		Response response = target.request().header("Authorization", "Basic " + auth)
			.post(Entity.json(data));

		return response;
	}

	@XmlRootElement
	@XmlType
	public static class ErrorView{

		String type;
		String title;
		Integer status;
		String detail;
		String instance;

		public ErrorView() {
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public String getDetail() {
			return detail;
		}

		public void setDetail(String detail) {
			this.detail = detail;
		}

		public String getInstance() {
			return instance;
		}

		public void setInstance(String instance) {
			this.instance = instance;
		}

		@Override public String toString() {
			return "ErrorView{" + "type='" + type + '\'' + ", title='" + title + '\'' + ", status=" + status + ", detail='" + detail + '\''
				+ ", instance='" + instance + '\'' + '}';
		}
	}


}
