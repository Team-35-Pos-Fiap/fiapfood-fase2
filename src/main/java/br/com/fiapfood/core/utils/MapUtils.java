package br.com.fiapfood.core.utils;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {
	private Map<Class<?>, Object> dados = new HashMap<>();
	
	public <T> void adicionarItens(T valor, Class<T> chave) {
		dados.put(chave, chave.cast(valor));
	}
	
	public <T> T recuperarItens(Class<T> chave) {
		return chave.cast(dados.get(chave));
	}
	
	public Map<Class<?>, Object> getMap() {
		return dados;
	}
}