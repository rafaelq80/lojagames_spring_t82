package com.generation.lojagames.util;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.generation.lojagames.model.Categoria;
import com.generation.lojagames.model.Produto;
import com.generation.lojagames.model.Usuario;
import com.generation.lojagames.model.UsuarioLogin;

public class TestBuilder {

    public static Usuario criarUsuario(Long id, String nome, String email, String senha, LocalDate dataNascimento) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome(nome);
        usuario.setUsuario(email);
        usuario.setSenha(senha);
        usuario.setFoto("");
        usuario.setDataNascimento(dataNascimento);
        return usuario;
    }
    
    public static UsuarioLogin criarUsuarioLogin(String email, String senha) {
        UsuarioLogin usuarioLogin = new UsuarioLogin();
        usuarioLogin.setId(null);
        usuarioLogin.setNome("");
        usuarioLogin.setUsuario(email);
        usuarioLogin.setSenha(senha);
        usuarioLogin.setFoto("");
        usuarioLogin.setDataNascimento(null);
        usuarioLogin.setToken("");
        return usuarioLogin;
    }

    public static Categoria criarCategoria(Long id, String tipo) {
        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setTipo(tipo);
        return categoria;
    }
    
    public static Produto criarProduto(Long id, String nome, BigDecimal preco, Categoria categoria) {
        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome(nome);
        produto.setPreco(preco);
        produto.setFoto("");
        produto.setCategoria(categoria);
        return produto;
    }
    
    public static Usuario criarUsuarioRoot() {
    	LocalDate dataNascimento =  LocalDate.of(2000, 10, 9);
        return criarUsuario(null, "Root", "root@root.com", "rootroot", dataNascimento);
    }
    
	public static Categoria criarCategoriaTeste() {
		return criarCategoria(null, "Aventura");
	}
}

