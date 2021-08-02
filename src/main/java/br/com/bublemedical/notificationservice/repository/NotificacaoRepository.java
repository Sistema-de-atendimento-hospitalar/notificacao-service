package br.com.bublemedical.notificationservice.repository;

import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.bublemedical.notificationservice.domain.model.TokenAutorization;

@Repository
public interface NotificacaoRepository extends JpaRepository<TokenAutorization, Long> {

	Optional<TokenAutorization> findFirstByEmail(String email, Sort sort);

}
