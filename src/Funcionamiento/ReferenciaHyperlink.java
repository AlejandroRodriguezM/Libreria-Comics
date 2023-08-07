package Funcionamiento;

public class ReferenciaHyperlink {
    private String displayText;
    private String url;

    public ReferenciaHyperlink(String displayText, String url) {
        this.displayText = displayText;
        this.url = url;
    }

    public String getDisplayText() {
        return displayText;
    }

    public String getUrl() {
        return url;
    }

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
