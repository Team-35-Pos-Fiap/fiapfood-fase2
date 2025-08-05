package br.com.fiapfood.infraestructure.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.fiapfood.core.controllers.interfaces.IRestauranteCoreController;
import br.com.fiapfood.infraestructure.controllers.request.atendimento.AtendimentoDto;
import br.com.fiapfood.infraestructure.controllers.request.endereco.DadosEnderecoDto;
import br.com.fiapfood.infraestructure.controllers.request.item.DescricaoDto;
import br.com.fiapfood.infraestructure.controllers.request.item.DisponibilidadeDto;
import br.com.fiapfood.infraestructure.controllers.request.item.ImagemDto;
import br.com.fiapfood.infraestructure.controllers.request.item.ItemDto;
import br.com.fiapfood.infraestructure.controllers.request.item.PrecoDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.CadastrarRestauranteDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.DadosDonoDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.DadosTipoCulinariaDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.NomeDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.RestauranteDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.RestaurantePaginacaoDto;
import br.com.fiapfood.infraestructure.controllers.response.MensagemResponse;
import br.com.fiapfood.infraestructure.controllers.response.SucessoResponse;
import br.com.fiapfood.infraestructure.utils.MensagensUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/restaurantes")
@Slf4j
public class RestauranteController {

    private final IRestauranteCoreController restauranteCoreController;

    public RestauranteController(IRestauranteCoreController restauranteCoreController) {
        this.restauranteCoreController = restauranteCoreController;
    }

    @GetMapping
    public ResponseEntity<RestaurantePaginacaoDto> buscarRestaurantes(@RequestParam(defaultValue = "1") @Valid @Positive(message = "O parâmetro página precisa ser maior do que 0.") 
    																  final Integer pagina) {
        log.info("buscarRestaurantes() - pagina {}", pagina);

        return ResponseEntity.ok().body(restauranteCoreController.buscarTodos(pagina));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestauranteDto> buscarRestaurantePorid(@PathVariable @NotNull @Valid final UUID id) {
        log.info("buscarRestaurantePorid():id {}", id);

        return ResponseEntity.ok().body(restauranteCoreController.buscarPorId(id));
    }
 
    @PostMapping
    public ResponseEntity<Void> cadastrar(@Valid @RequestBody @NotNull final CadastrarRestauranteDto restaurante) {
        log.info("cadastrar() - dados do restaurante: {}", restaurante);

        restauranteCoreController.cadastrar(restaurante);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @PatchMapping("/{id}/status/inativa")
	public ResponseEntity<MensagemResponse> inativar(@Valid @PathVariable @NotNull final UUID id) {
		log.info("inativar():id {}", id);

		restauranteCoreController.inativar(id);

		MensagemResponse sucessoResponse = new SucessoResponse(MensagensUtil.recuperarMensagem(MensagensUtil.SUCESSO_INATIVACAO_RESTAURANTE));
		
		return ResponseEntity.ok(sucessoResponse);
	}
	
	@PatchMapping("/{id}/status/reativa")
	public ResponseEntity<MensagemResponse> reativar(@Valid @PathVariable @NotNull final UUID id) {
		log.info("reativar():id {}", id);

		restauranteCoreController.reativar(id);
		
		MensagemResponse sucessoResponse = new SucessoResponse(MensagensUtil.recuperarMensagem(MensagensUtil.SUCESSO_REATIVACAO_RESTAURANTE));
		
		return ResponseEntity.ok(sucessoResponse);
	} 

    @PatchMapping("/{id}/tipo-culinaria")
    public ResponseEntity<MensagemResponse> atualizarTipoCulinaria(@PathVariable @NotNull @Valid final UUID id, @RequestBody @Valid @NotNull final DadosTipoCulinariaDto dados) {
        log.info("atualizarTipoCulinaria(): id {} - idTipoCulinaria: {}", id, dados.idTipoCulinaria());
        
        restauranteCoreController.atualizarTipoCulinaria(id, dados.idTipoCulinaria());
        
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
    }
    

    @PatchMapping("/{id}/dono")
    public ResponseEntity<MensagemResponse> atualizarDono(@PathVariable @NotNull @Valid final UUID id, @RequestBody @Valid @NotNull final DadosDonoDto dados) {
        log.info("atualizarDono(): id {} - idDono: {}", id, dados.idDono());
        
        restauranteCoreController.atualizarDono(id, dados.idDono());

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
    }
    

    @PatchMapping("/{id}/nome")
    public ResponseEntity<MensagemResponse> atualizarNome(@PathVariable @NotNull @Valid final UUID id, @RequestBody @Valid @NotNull final NomeDto dados) {
        log.info("atualizarNome(): id {} - nome: {}", id, dados.nome());
        
        restauranteCoreController.atualizarNome(id, dados.nome());

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
    }
    
    @PatchMapping("/{id}/endereco")
    public ResponseEntity<MensagemResponse> atualizarEndereco(@PathVariable @NotNull @Valid final UUID id, @RequestBody @Valid @NotNull final DadosEnderecoDto dadosEndereco) {
        log.info("atualizarEndereco(): id {} - dados do endereço: {}", id, dadosEndereco);
        
        restauranteCoreController.atualizarEndereco(id, dadosEndereco);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
    }
    
    @PatchMapping("/{id-restaurante}/atendimentos")
    public ResponseEntity<MensagemResponse> atualizarAtendimento(@PathVariable(name = "id-restaurante") @NotNull @Valid final UUID idRestaurante, @RequestBody @NotNull final AtendimentoDto atendimento) {
        log.info("atualizarAtendimento(): id {} - dados do atendimento: {}", idRestaurante, atendimento);
        
        restauranteCoreController.atualizarAtendimento(idRestaurante, atendimento);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
    }
    
    @PostMapping("/{id-restaurante}/atendimentos")
    public ResponseEntity<MensagemResponse> adicionarAtendimento(@PathVariable(name = "id-restaurante") @NotNull @Valid final UUID idRestaurante, @RequestBody @NotNull final AtendimentoDto atendimento) {
        log.info("adicionarAtendimento(): id {} - dados do atendimento: {}", idRestaurante, atendimento);
        
        restauranteCoreController.adicionarAtendimento(idRestaurante, atendimento);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
    }
    
    @DeleteMapping("/{id-restaurante}/atendimentos/{id-atendimento}")
    public ResponseEntity<MensagemResponse> excluirAtendimento(@PathVariable(name = "id-restaurante") @NotNull @Valid final UUID idRestaurante, 
    														   @PathVariable(name = "id-atendimento") @NotNull @Valid final UUID idAtendimento) {
        log.info("excluirAtendimento(): id {} - dados do atendimento: {}", idRestaurante, idAtendimento);
        
        restauranteCoreController.excluirAtendimento(idRestaurante, idAtendimento);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
    }
    
    @PatchMapping("/{id-restaurante}/itens/{id-item}/descricao")
	public ResponseEntity<Void> atualizarDescricaoItem(@PathVariable(name = "id-restaurante") @NotNull @Valid final UUID idRestaurante, 
			   										   @PathVariable(name = "id-item") @NotNull @Valid final UUID idItem,
			   										   @RequestBody @NotNull final DescricaoDto dados) {
    	log.info("atualizarDescricaoItem(): id restaurante: {} - id item: {} - descricao: {}", idRestaurante, idItem, dados.descricao());
         
        restauranteCoreController.atualizarDescricaoItem(idRestaurante, idItem, dados.descricao());

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
    
    @PatchMapping("/{id-restaurante}/itens/{id-item}/nome")
	public ResponseEntity<Void> atualizarNomeItem(@PathVariable(name = "id-restaurante") @NotNull @Valid final UUID idRestaurante, 
			   								      @PathVariable(name = "id-item") @NotNull @Valid final UUID idItem,
			   								      @RequestBody @NotNull final NomeDto dados) {
    	log.info("atualizarDescricaoItem(): id restaurante: {} - id item: {} - descricao: {}", idRestaurante, idItem, dados.nome());
         
        restauranteCoreController.atualizarNomeItem(idRestaurante, idItem, dados.nome());

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
    
    @PatchMapping("/{id-restaurante}/itens/{id-item}/disponibilidade-consumo-presencial")
	public ResponseEntity<Void> atualizarDisponibilidadeConsumoPresencialItem(@PathVariable(name = "id-restaurante") @NotNull @Valid final UUID idRestaurante, 
			      														  	  @PathVariable(name = "id-item") @NotNull @Valid final UUID idItem,
			      														  	  @RequestBody @NotNull final DisponibilidadeDto dados) {
    	log.info("atualizarDisponibilidadeConsumoPresencial(): id restaurante: {} - id item: {} - disponibilidadeDto: {}", idRestaurante, idItem, dados.isDisponivel());
        
        restauranteCoreController.atualizarDisponibilidadeConsumoPresencialItem(idRestaurante, idItem, dados.isDisponivel());

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
    
    @PatchMapping("/{id-restaurante}/itens/{id-item}/disponibilidade")
	public ResponseEntity<Void> atualizarDisponibilidadeItem(@PathVariable(name = "id-restaurante") @NotNull @Valid final UUID idRestaurante, 
			      											 @PathVariable(name = "id-item") @NotNull @Valid final UUID idItem,
			      											 @RequestBody @NotNull final DisponibilidadeDto dados) {
    	log.info("atualizarDisponibilidadeItem(): id restaurante: {} - id item: {} - disponibilidadeDto: {}", idRestaurante, idItem, dados.isDisponivel());
        
        restauranteCoreController.atualizarDisponibilidadeItem(idRestaurante, idItem, dados.isDisponivel());

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
    
    @PatchMapping(value = "/{id-restaurante}/itens/{id-item}/imagem", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })	
	public ResponseEntity<Void> atualizarImagemItem(@PathVariable(name = "id-restaurante") @NotNull @Valid final UUID idRestaurante, 
			  										@PathVariable(name = "id-item") @NotNull @Valid final UUID idItem,
			  										@NotNull @RequestParam final MultipartFile imagem) {
    	log.info("atualizarImagemItem(): id restaurante: {} - id item: {} - imagem: {}", idRestaurante, idItem, imagem);
        
        restauranteCoreController.atualizarImagemItem(idRestaurante, idItem, imagem);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
    
    @PatchMapping("/{id-restaurante}/itens/{id-item}/preco")
	public ResponseEntity<Void> atualizarPrecoItem(@PathVariable(name = "id-restaurante") @NotNull @Valid final UUID idRestaurante, 
											   	   @PathVariable(name = "id-item") @NotNull @Valid final UUID idItem,
											   	   @RequestBody @NotNull final PrecoDto dados) {
    	log.info("atualizarPrecoItem(): id restaurante: {} - id item: {} - preço: {}", idRestaurante, idItem, dados);
        
        restauranteCoreController.atualizarPrecoItem(idRestaurante, idItem, dados.preco());

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
       
    @GetMapping("/{id-restaurante}/itens/{id-item}/imagem/download")
	public ResponseEntity<?> baixarImagemItem(@PathVariable(name = "id-restaurante") @NotNull @Valid final UUID idRestaurante, 
											  @PathVariable(name = "id-item") @NotNull @Valid final UUID idItem) {
    	log.info("baixarImagemItem(): id item: {}", idItem);
        
    	ImagemDto imagem = restauranteCoreController.baixarImagemItem(idRestaurante, idItem);
		
		return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imagem.nome() + "\"").body(imagem.conteudo());
	}
    
    @GetMapping("/{id-restaurante}/itens/{id-item}")
	public ResponseEntity<ItemDto> buscarItemPorId(@PathVariable(name = "id-restaurante") @NotNull @Valid final UUID idRestaurante, 
		   	   								       @PathVariable(name = "id-item") @NotNull @Valid final UUID idItem) {
    	log.info("buscarItemPorid():idRestaurante: {} - idItem: {}", idRestaurante, idItem);

		return ResponseEntity.ok().body(restauranteCoreController.buscarItemPorId(idRestaurante, idItem));
	}
    
    @GetMapping("/{id-restaurante}/itens")
	public ResponseEntity<List<ItemDto>> buscarTodosItens(@PathVariable(name = "id-restaurante") @NotNull @Valid final UUID idRestaurante) {
    	log.info("buscarTodosItens() - id restaurante: {}", idRestaurante);

		return ResponseEntity.ok().body(restauranteCoreController.buscarTodosItens(idRestaurante));
	}
    
    @PostMapping(value = "/{id-restaurante}/itens", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })	
	public ResponseEntity<Void> cadastrar(@PathVariable(name = "id-restaurante") @NotNull @Valid final UUID idRestaurante,
										  @Valid @NotNull @RequestParam final String nome, @RequestParam final String descricao, 
										  @NotNull @RequestParam final BigDecimal preco, @NotNull @RequestParam final Boolean disponivelParaConsumoPresencial, 
										  @NotNull @RequestParam final MultipartFile imagem) {
    	log.info("cadastrar item() - id restaurante: {} - nome: {} - descricao: {} - preço: {} - disponivelParaConsumoPresencial: {}", idRestaurante, nome, descricao, preco, disponivelParaConsumoPresencial);
    	
    	restauranteCoreController.cadastrar(idRestaurante, nome, descricao, preco, disponivelParaConsumoPresencial, imagem);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
