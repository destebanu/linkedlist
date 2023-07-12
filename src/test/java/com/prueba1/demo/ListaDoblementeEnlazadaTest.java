package com.prueba1.demo;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ListaDoblementeEnlazadaTest {
	private ListaDoblementeEnlazada lista;

	@BeforeEach
	void setUp() {
		lista = new ListaDoblementeEnlazada();
	}

	@Test
	void agregarAlInicio() {
		lista.agregarAlInicio(3);
		lista.agregarAlInicio(2);
		lista.agregarAlInicio(1);

		Assertions.assertEquals("1 2 3", imprimirLista());
	}

	@Test
	void agregarAlFinal() {
		lista.agregarAlFinal(1);
		lista.agregarAlFinal(2);
		lista.agregarAlFinal(3);

		Assertions.assertEquals("1 2 3", imprimirLista());
	}

	@Test
	void imprimirListaVacia() {
		Assertions.assertEquals("", imprimirLista());
	}

	@Test
	void borrarElemento() {
		lista.agregarAlInicio(1);
		lista.agregarAlInicio(2);
		lista.agregarAlInicio(3);

		lista.cabeza.siguiente = null;

		Assertions.assertEquals("3", imprimirLista());
	}

	private String imprimirLista() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(outputStream);
		PrintStream originalOut = System.out;
		System.setOut(printStream);

		lista.imprimirLista();

		System.out.flush();
		System.setOut(originalOut);

		return outputStream.toString().trim();
	}
}
