package id.ac.ui.cs.advprog.buildingstore.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorizationRequest {
    @NotBlank(message = "Token must not be blank")
    private String token;
}
