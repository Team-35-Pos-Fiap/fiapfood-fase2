package br.com.fiapfood.infraestructure.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiapfood.core.controllers.interfaces.ITipoCulinariaCoreController;
import br.com.fiapfood.infraestructure.controllers.request.tipo_culinaria.NomeDto;
import br.com.fiapfood.infraestructure.controllers.request.tipo_culinaria.TipoCulinariaDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/tipos-culinaria")
@Slf4j
public class TipoCulinariaController {

    private final ITipoCulinariaCoreController tipoCulinariaCoreController;
    
    public TipoCulinariaController(ITipoCulinariaCoreController tipoCulinariaCoreController) {
        this.tipoCulinariaCoreController = tipoCulinariaCoreController;
    }

    @GetMapping
    public ResponseEntity<List<TipoCulinariaDto>> buscarTodos(){
        log.info("buscarTodos()");

        return ResponseEntity.ok().body(tipoCulinariaCoreController.buscarTodos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TipoCulinariaDto> buscarPorId(@PathVariable @Valid @NotNull final Integer id){
        log.info("buscarPorId() - id: {}", id);

        return ResponseEntity.ok().body(tipoCulinariaCoreController.buscarPorId(id));
    }
    
    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid @NotNull final NomeDto tipoCulinaria){
        log.info("cadastrar() - nome: {}", tipoCulinaria.nome());

        tipoCulinariaCoreController.cadastrar(tipoCulinaria.nome());
        
		return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable @Valid @NotNull final Integer id, @RequestBody @Valid @NotNull final NomeDto tipoCulinaria){
        log.info("atualizar() - id: {} - nome: {}", id, tipoCulinaria.nome());

        tipoCulinariaCoreController.atualizar(id, tipoCulinaria.nome());
        
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}