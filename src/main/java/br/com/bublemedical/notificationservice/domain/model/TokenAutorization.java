package br.com.bublemedical.notificationservice.domain.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Entity
@Table(name = "t_token_autorization")
public class TokenAutorization {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "token_autorization_id")
	private Long notificacaoId;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "cpf", length = 11, nullable = false)
	private String cpf;

	@Column(name = "token", nullable = false)
	private String token;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@Column(name = "data_hora_expiracao", nullable = false)
	private LocalDateTime dataHoraExpiracao = LocalDateTime.now();

	public Long getNotificacaoId() {
		return notificacaoId;
	}

	public void setNotificacaoId(Long notificacaoId) {
		this.notificacaoId = notificacaoId;
	}

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getDataHoraExpiracao() {
		return dataHoraExpiracao;
	}

	public void setDataHoraExpiracao(LocalDateTime dataHoraExpiracao) {
		this.dataHoraExpiracao = dataHoraExpiracao;
	}

}
