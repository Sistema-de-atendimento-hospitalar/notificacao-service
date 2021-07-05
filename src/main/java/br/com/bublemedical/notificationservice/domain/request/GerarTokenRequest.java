package br.com.bublemedical.notificationservice.domain.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class GerarTokenRequest {
	
	@NotNull(message = "Nome não pode ser nula")
	@NotBlank(message = "Nome não pode está em branco")
	private String nome;

	@NotNull(message = "Email não pode ser nulo")
	@Email(message = "Email não é válido")
	@NotBlank(message = "Email não pode está em branco")
	private String email;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
