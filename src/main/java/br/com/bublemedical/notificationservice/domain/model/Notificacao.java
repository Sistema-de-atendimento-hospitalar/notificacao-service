package br.com.bublemedical.notificationservice.domain.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_notificacao")
public class Notificacao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notificacao_id")
	private Long notificacaoId;
	
	@Column(name = "nome")
	private String nome;

	@Column(name = "email")
	private String email;
	
	@Column(name = "token")
	private String token;
	
	@Column(name = "data_hora_expiracao")
	private LocalDateTime dataHoraExpiracao;

	public Long getNotificacaoId() {
		return notificacaoId;
	}

	public void setNotificacaoId(Long notificacaoId) {
		this.notificacaoId = notificacaoId;
	}

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
