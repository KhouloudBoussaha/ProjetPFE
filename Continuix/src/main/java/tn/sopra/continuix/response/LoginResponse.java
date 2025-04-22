package tn.sopra.continuix.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    @JsonProperty("accessToken")
    private String accessToken;
    @JsonProperty("tokenType")
    private String tokenType = "Bearer";
    @JsonProperty("role")
    private String role;
    @JsonProperty("userId")
    private Long userId; // Add userId field



    public LoginResponse(String accessToken, Long userId, String role) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.role = role;
    }

}
