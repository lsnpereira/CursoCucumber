package br.ce.wcaquino.steps;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.junit.Assert;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.NotaAluguel;
import br.ce.wcaquino.entidades.TipoAluguel;
import br.ce.wcaquino.servicos.AlguelService;
import br.ce.wcaquino.utils.DateUtils;
import cucumber.api.DataTable;
import cucumber.api.java.pt.Dado;
import cucumber.api.java.pt.Então;
import cucumber.api.java.pt.Quando;


public class AlugarFilmeSteps {
	
	private Filme filme;
	private AlguelService aluguel = new AlguelService();
	private NotaAluguel nota;
	private String erro;
	private TipoAluguel tipoAluguel;
	
	
	@Dado("^um filme com estoque de (\\d+) unidades$")
	public void umFilmeComEstoqueDeUnidades(int arg1) throws Throwable {
		
		filme = new Filme();
		filme.setEstoque(arg1);
	    
	}

	@Dado("^que o preço do aluguel seja de R\\$ (\\d+)$")
	public void queOPreçoDoAluguelSejaDeR$(int arg1) throws Throwable {
	    
		filme.setAluguel(arg1);
		
	}
	
	@Dado("^um filme$")
	public void um_filme(DataTable table) throws Throwable {
	    
		Map<String, String> map = table.asMap(String.class, String.class);
		filme = new Filme();
		filme.setEstoque(Integer.parseInt(map.get("estoque")));
		filme.setAluguel(Integer.parseInt(map.get("preco")));
		String tipo = map.get("tipo");
		tipoAluguel = tipo.equals("semanal")? TipoAluguel.SEMANAL: 
			tipo.equals("extendido")? TipoAluguel.EXTENDIDO: TipoAluguel.COMUM;
	    
	}

	@Quando("^alugar$")
	public void alugar() throws Throwable {
	    try {
	    	
	    	nota = aluguel.alugar(filme, tipoAluguel);
	    	
	    }catch (RuntimeException e) {
	    	
			erro = e.getMessage();
			
		}
			
	}

	@Então("^o preço do aluguel será de R\\$ (\\d+)$")
	public void oPreçoDoAluguelSeráDeR$(int arg1) throws Throwable {
		
		Assert.assertEquals(arg1, nota.getPreco());
	    
	}

	@Então("^o estoque do filme será (\\d+) unidade$")
	public void oEstoqueDoFilmeSeráUnidade(int arg1) throws Throwable {
	    
		Assert.assertEquals(arg1, filme.getEstoque());
		
	}
	
	@Então("^não será possível por falta de estoque$")
	public void nãoSeráPossívelPorFaltaDeEstoque() throws Throwable {
	 
		Assert.assertEquals("Filme sem estoque", erro);
	}
	
	@Dado("^que o tipo do aluguel	seja (.*)$")
	public void queOTipoDoAluguelSejaExtendido(String tipo) throws Throwable {
	    
		tipoAluguel = tipo.equals("semanal")? TipoAluguel.SEMANAL: 
			tipo.equals("extendido")? TipoAluguel.EXTENDIDO: TipoAluguel.COMUM;
		
	}

	@Então("^a data de entrega será em (\\d+) dias?$")
	public void aDataDeEntregaSeráEmDias(int arg1) throws Throwable {
		
		System.out.println("Dias passados no arg1 " +arg1);
		Date dataEsperada = DateUtils.obterDataDiferencaDias(arg1);
		Date dataReal = nota.getDataEntrega();
		
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		Assert.assertEquals(format.format(dataEsperada), format.format(dataReal));
	   
	}

	@Então("^a pontuação será de (\\d+) pontos$")
	public void aPontuaçãoSeráDePontos(int arg1) throws Throwable {
		Assert.assertEquals(arg1, nota.getPontuacao());
	    
	}
	
	
}
