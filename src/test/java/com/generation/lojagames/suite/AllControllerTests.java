package com.generation.lojagames.suite;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.generation.lojagames.controller.CategoriaControllerTest;
import com.generation.lojagames.controller.ProdutoControllerTest;
import com.generation.lojagames.controller.UsuarioControllerTest;

@Suite
@SelectClasses({
    CategoriaControllerTest.class,
    UsuarioControllerTest.class,
    ProdutoControllerTest.class
})
public class AllControllerTests { }
