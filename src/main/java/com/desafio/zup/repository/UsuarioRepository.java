package com.desafio.zup.repository;
import com.desafio.zup.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
@Query("SELECT u FROM Usuario u WHERE u.cpf = ?1")
Usuario findByCpf(String cpf);

    @Query("SELECT u FROM Usuario u WHERE u.cpf = ?1")
    Optional<Usuario> ChecarCpfJaCadastrado(String cpf);

    @Query("SELECT s FROM Usuario s WHERE s.email= ?1")
    Optional<Usuario> findByEmail(String email);
}
