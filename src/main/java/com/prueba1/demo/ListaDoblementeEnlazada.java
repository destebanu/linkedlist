package com.prueba1.demo;

//Clase ListaDoblementeEnlazada que contiene los métodos de la lista
class ListaDoblementeEnlazada {
	Nodo cabeza;

	public ListaDoblementeEnlazada() {
		this.cabeza = null;
	}

	// Método para agregar un elemento al inicio de la lista
	public void agregarAlInicio(int valor) {
		Nodo nuevoNodo = new Nodo(valor);

		if (cabeza == null) {
			cabeza = nuevoNodo;
		} else {
			nuevoNodo.siguiente = cabeza;
			cabeza.anterior = nuevoNodo;
			cabeza = nuevoNodo;
		}
	}

	// Método para agregar un elemento al final de la lista
	public void agregarAlFinal(int valor) {
		Nodo nuevoNodo = new Nodo(valor);

		if (cabeza == null) {
			cabeza = nuevoNodo;
		} else {
			Nodo nodoActual = cabeza;
			while (nodoActual.siguiente != null) {
				nodoActual = nodoActual.siguiente;
			}
			nodoActual.siguiente = nuevoNodo;
			nuevoNodo.anterior = nodoActual;
		}
	}

	// Método para imprimir los elementos de la lista
	public void imprimirLista() {
		Nodo nodoActual = cabeza;
		while (nodoActual != null) {
			System.out.print(nodoActual.valor + " ");
			nodoActual = nodoActual.siguiente;
		}
		System.out.println();
	}
}
