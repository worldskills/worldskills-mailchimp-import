import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.HashMap;

@XmlRootElement
@XmlType
@JsonIgnoreProperties(ignoreUnknown = true)
public class Record{

	private String email;
	private String status;

	private HashMap<String, String> fields;

	public Record() {
		this.fields = new HashMap<>();
	}

	@XmlElement(name="email_address")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@XmlElement(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@XmlElement(name="merge_fields")
	public HashMap<String, String> getFields() {
		return fields;
	}

	public void setFields(HashMap<String, String> fields) {
		this.fields = fields;
	}

	@Override public String toString() {
		return "Record{" + "email='" + email + '\'' + ", status='" + status + '\'' + ", fields=" + fields + '}';
	}
}
