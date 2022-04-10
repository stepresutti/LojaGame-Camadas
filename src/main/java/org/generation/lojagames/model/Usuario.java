package org.generation.lojagames.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table (name = "tb_usuario")
@Getter
@Setter
// Ao colocar os getters e setters não preciso escrever embaixo
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nome;

    @Schema(example = "email@email.com.br")
    @NotNull(message = "O atributo Usuário é Obrigatório!")
    @Email(message = "O atributo Usuário deve ser um email válido!")
    private String usuario;

    @NotBlank(message = "O atributo Senha é Obrigatório!")
    @Size(min = 8, message = "A Senha deve ter no mínimo 8 caracteres")
    private String senha;

    private String foto;

    //----um usuario tem uma lista de postagens----

    @OneToMany(mappedBy="usuario", cascade=CascadeType.REMOVE)
    @JsonIgnoreProperties("usuario")
    private List<Produto> produtoList;


    public Usuario(Long id, String nome, String usuario, String senha, String foto) {
        this.id = id;
        this.nome = nome;
        this.usuario = usuario;
        this.senha = senha;
        this.foto = foto;
    }

    public Usuario() {}

}
