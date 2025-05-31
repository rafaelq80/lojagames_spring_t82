package com.generation.lojagames.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.lojagames.model.Categoria;
import com.generation.lojagames.repository.CategoriaRepository;
import com.generation.lojagames.service.UsuarioService;
import com.generation.lojagames.util.TestBuilder;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class CategoriaControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	private static final String USUARIO_ROOT_EMAIL = "root@root.com";
	private static final String USUARIO_ROOT_SENHA = "rootroot";
	private static final String BASE_URL_CATEGORIAS = "/categorias";

	@BeforeAll
	void start(){
		categoriaRepository.deleteAll();
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuarioRoot());
	}
	
	@Test
	@DisplayName("✔ 01 - Deve cadastrar uma nova categoria com sucesso")
	public void deveCadastrarCategoria() {
		
		// Given
		Categoria categoria = TestBuilder.criarCategoria(null, "Categoria 01");

		// When
		HttpEntity<Categoria> requisicao = new HttpEntity<>(categoria);
		ResponseEntity<Categoria> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_CATEGORIAS, HttpMethod.POST, requisicao, Categoria.class);

		// Then
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals("Categoria 01", resposta.getBody().getTipo());
	}

	@Test
	@DisplayName("✔ 02 - Deve atualizar uma categoria existente")
	public void deveAtualizarUmaCategoria() {
		
		//Given
		Categoria categoria = TestBuilder.criarCategoria(null, "Categoria 02");
		Categoria categoriaCadastrado = categoriaRepository.save(categoria);
		
		Categoria categoriaUpdate = TestBuilder.criarCategoria(categoriaCadastrado.getId(), "Categoria 03");

		//When
		HttpEntity<Categoria> requisicao = new HttpEntity<>(categoriaUpdate);

		ResponseEntity<Categoria> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_CATEGORIAS, HttpMethod.PUT, requisicao, Categoria.class);

		//Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals("Categoria 03", resposta.getBody().getTipo());
	}

	@Test
	@DisplayName("✔ 03 - Deve listar todas as categorias")
	public void deveListarTodasCategorias() {
		
		//Given
		categoriaRepository.save(TestBuilder.criarCategoria(null, "Categoria 04"));
		categoriaRepository.save(TestBuilder.criarCategoria(null, "Categoria 05"));

		//When
		ResponseEntity<Categoria[]> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_CATEGORIAS, HttpMethod.GET, null, Categoria[].class);

		//Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}
	
	@Test
	@DisplayName("✔ 04 - Deve listar uma categoria específica - pelo id")
	public void deveListarUmaCategoriaPorId() {
		
		//Given
		Categoria categoria = categoriaRepository.save(TestBuilder.criarCategoria(null, "Categoria 06"));
		var id = categoria.getId();
		
		//When
		ResponseEntity<Categoria> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_CATEGORIAS + "/" + id, HttpMethod.GET, null, Categoria.class);

		//Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}
	
	@Test
	@DisplayName("✔ 05 - Deve listar todas as categorias - por tipo")
	public void deveListarCategoriasPorTipo() {
		
		//Given
		categoriaRepository.save(TestBuilder.criarCategoria(null, "Categoria 07"));
		String tipo = "08";
				
		//When
		ResponseEntity<Categoria[]> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_CATEGORIAS + "/tipo/" + tipo, HttpMethod.GET, null, Categoria[].class);

		//Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}
	
	@Test
	@DisplayName("✔ 06 - Deve deletar uma categoria")
	public void deveDeletarUmaCategoria() {
		
		//Given
		Categoria categoria = categoriaRepository.save(TestBuilder.criarCategoria(null, "Categoria 08"));
		var id = categoria.getId();
				
		//When
		ResponseEntity<Void> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_CATEGORIAS + "/" + id, HttpMethod.DELETE, null, Void.class);

		//Then
		assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());

	}
	
}
