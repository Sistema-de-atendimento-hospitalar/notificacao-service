package br.com.bublemedical.notificationservice.domain.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ValidarTokenRequest {

	@NotNull(message = "token não pode ser nula")
	@NotBlank(message = "token não pode está em branco")
	@Size(min = 6, max = 6)
	private String token;

	@NotNull(message = "Email não pode ser nulo")
	@Email(message = "Email não é válido")
	@NotBlank(message = "Email não pode está em branco")
	private String email;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
