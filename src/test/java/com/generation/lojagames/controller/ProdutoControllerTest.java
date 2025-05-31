package com.generation.lojagames.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

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
import com.generation.lojagames.model.Produto;
import com.generation.lojagames.repository.CategoriaRepository;
import com.generation.lojagames.repository.ProdutoRepository;
import com.generation.lojagames.service.UsuarioService;
import com.generation.lojagames.util.TestBuilder;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class ProdutoControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	private Categoria categoriaTeste;
	private BigDecimal preco;
	
	private static final String USUARIO_ROOT_EMAIL = "root@root.com";
	private static final String USUARIO_ROOT_SENHA = "rootroot";
	private static final String BASE_URL_PRODUTOS = "/produtos";

	@BeforeAll
	void start(){
		
		produtoRepository.deleteAll();
		
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuarioRoot());
			
		categoriaTeste = categoriaRepository.save(TestBuilder.criarCategoriaTeste());
		
		preco = new BigDecimal(250.50);
	}
	
	@Test
	@DisplayName("✔ 01 - Deve cadastrar um novo produto com sucesso")
	public void deveCadastrarProduto() {
		
		// Given
		Produto produto = TestBuilder.criarProduto(null, "Produto 01", preco, categoriaTeste);

		// When
		HttpEntity<Produto> requisicao = new HttpEntity<>(produto);
		ResponseEntity<Produto> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_PRODUTOS, HttpMethod.POST, requisicao, Produto.class);

		// Then
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals("Produto 01", resposta.getBody().getNome());
	}

	@Test
	@DisplayName("✔ 02 - Deve atualizar um produto existente")
	public void deveAtualizarUmProduto() {
		
		//Given
		Produto produto = TestBuilder.criarProduto(null, "Produto 02", preco, categoriaTeste);
		Produto produtoCadastrado = produtoRepository.save(produto);
		
		Produto produtoUpdate = TestBuilder.criarProduto(produtoCadastrado.getId(), "Produto 03", preco, categoriaTeste);

		//When
		HttpEntity<Produto> requisicao = new HttpEntity<>(produtoUpdate);

		ResponseEntity<Produto> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_PRODUTOS, HttpMethod.PUT, requisicao, Produto.class);

		//Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals("Produto 03", resposta.getBody().getNome());
	}

	@Test
	@DisplayName("✔ 03 - Deve listar todos os produtos")
	public void deveListarTodosProdutos() {
		
		//Given
		produtoRepository.save(TestBuilder.criarProduto(null, "Produto 04", preco, categoriaTeste));
		produtoRepository.save(TestBuilder.criarProduto(null, "Produto 05", preco, categoriaTeste));

		//When
		ResponseEntity<Produto[]> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_PRODUTOS, HttpMethod.GET, null, Produto[].class);

		//Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}
	
	@Test
	@DisplayName("✔ 04 - Deve listar um produto específico - pelo id")
	public void deveListarUmProdutoPorId() {
		
		//Given
		Produto produto = produtoRepository.save(TestBuilder.criarProduto(null, "Produto 06", preco, categoriaTeste));
		var id = produto.getId();
		
		//When
		ResponseEntity<Produto> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_PRODUTOS + "/" + id, HttpMethod.GET, null, Produto.class);

		//Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}
	
	@Test
	@DisplayName("✔ 05 - Deve listar todos os produtos - por nome")
	public void deveListarProdutosPorDescricao() {
		
		//Given
		produtoRepository.save(TestBuilder.criarProduto(null, "Produto 07", preco, categoriaTeste));
		String nome = "07";
				
		//When
		ResponseEntity<Produto[]> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_PRODUTOS + "/nome/" + nome, HttpMethod.GET, null, Produto[].class);

		//Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}
	
	@Test
	@DisplayName("✔ 06 - Deve deletar um produto")
	public void deveDeletarUmProduto() {
		
		//Given
		Produto produto = produtoRepository.save(TestBuilder.criarProduto(null, "Produto 08", preco, categoriaTeste));
		var id = produto.getId();
				
		//When
		ResponseEntity<Void> resposta = testRestTemplate
				.withBasicAuth(USUARIO_ROOT_EMAIL, USUARIO_ROOT_SENHA)
				.exchange(BASE_URL_PRODUTOS + "/" + id, HttpMethod.DELETE, null, Void.class);

		//Then
		assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());

	}
	
}
