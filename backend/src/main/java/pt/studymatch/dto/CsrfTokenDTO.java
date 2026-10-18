package pt.studymatch.dto;

public class CsrfTokenDTO {

    private String headerName;

    private String token;

    public CsrfTokenDTO() {
    }

    public CsrfTokenDTO(String headerName, String token) {
        this.headerName = headerName;
        this.token = token;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
