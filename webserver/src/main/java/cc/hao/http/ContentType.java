package cc.hao.http;

public enum ContentType {

	CSS("CSS"),
	GIF("GIF"),
	HTM("HTM"),
	HTML("HTML"),
	ICO("ICO"),
	JPG("JPG"),
	JPEG("JPEG"),
	PNG("PNG"),
	TXT("TXT"),
	XML("XML"),
	JSON("JSON"),
	MD("HTML");

	private final String content;
	
	private ContentType(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		switch (this) {
		case CSS:
			return "Content-Type:text/css";
		case ICO:
		case GIF:
			return "Content-Type:text/gif";
		case HTM:
		case HTML:
		case MD:
			return "Content-Type:text/html";
		case JPG:
		case JPEG:
			return "Content-Type:text/jpeg";
		case PNG:
			return "Content-Type:text/png";
		case TXT:
			return "Content-Type:text/plain";
		case XML:
			return "Content-Type:text/xml";
		case JSON:
			return "Content-Type:text/json";
		default:
			return null;
		}
	}
	
}
