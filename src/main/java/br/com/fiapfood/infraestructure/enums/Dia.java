package br.com.fiapfood.infraestructure.enums;

import java.util.Arrays;

public enum Dia {
	SEGUNDA_FEIRA("Segunda-feira"), 
	TERÇA_FEIRA("Terça-feira"),
	QUARTA_FEIRA("Quarta-feira"), 
	QUINTA_FEIRA("Quinta-feira"),
	SEXTA_FEIRA("Sexta-feira"), 
	SÁBADO("Sábado"),
	DOMINGO("Domingo");
	
	private final String dia;
	
	private Dia(String dia) {
		this.dia = dia;
	}
	
	public String getValue() {
		return dia;
	}
		
	public static Dia getDia(String dia) {
		return Arrays.asList(Dia.values())
				     .stream()
				     .filter(d -> d.getValue().equalsIgnoreCase(dia))
				     .findFirst()
				     .get();
	}
}