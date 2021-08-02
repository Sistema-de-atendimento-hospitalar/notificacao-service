package br.com.bublemedical.notificationservice.domain.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

public class TokenRequest {

	@NotNull(message = "Email não pode ser nulo")
	@Email(message = "Email não é válido")
	@NotBlank(message = "Email não pode está em branco")
	private String email;
	
	@CPF
	@NotNull(message = "CPF não pode ser nula")
	@NotBlank(message = "CPF não pode está em branco")
	@Length(min = 11,  max = 11)
	private String cpf;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	

}
