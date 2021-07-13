package com.desafio.zup.repository;
import com.desafio.zup.model.Comic;
import com.desafio.zup.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComicRepository extends JpaRepository<Comic,Long> {
    @Query("SELECT s FROM Comic s WHERE s.usuario = ?1")
    List<Comic> findAllByCpf(Usuario usuario);
}
