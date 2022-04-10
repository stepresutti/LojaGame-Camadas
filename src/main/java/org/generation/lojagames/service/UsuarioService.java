package org.generation.lojagames.service;

import org.apache.tomcat.util.codec.binary.Base64;
import org.generation.lojagames.model.Usuario;
import org.generation.lojagames.model.UsuarioLogin;
import org.generation.lojagames.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario>cadastrarUsuario (Usuario usuario){

        if(usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) {
            return Optional.empty();

        }

        usuario.setSenha(criptografarSenha(usuario.getSenha()));

        return Optional.of(usuarioRepository.save(usuario));

    }

    public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin){
        //verificando se existe esse usuario e senha
        Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());


        if(usuario.isPresent()) {
            if(compararSenhas(usuarioLogin.get().getSenha(), usuario.get().getSenha())) {
                usuarioLogin.get().setId(usuario.get().getId());
                usuarioLogin.get().setNome(usuario.get().getNome());
                usuarioLogin.get().setFoto(usuario.get().getFoto());
                usuarioLogin.get().setSenha(usuario.get().getSenha());
                usuarioLogin.get().setToken(geradorBasicToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha()));

                //logou
                return usuarioLogin;
            }
        }
        return Optional.empty();

    }

    private boolean compararSenhas(String senhaDigitada, String senhaDoBanco) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(senhaDigitada, senhaDoBanco);

    }

    private String criptografarSenha(String senha) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return encoder.encode(senha);

    }

    private String geradorBasicToken(String usuario, String senha) {

        String token = usuario + ":" + senha;

        byte[] tokenBase64 = Base64. encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
        return "Basic " + new String(tokenBase64);

    }

    public Optional<Usuario> atualizarUsuario(Usuario usuario) {
        return null;
    }
}
