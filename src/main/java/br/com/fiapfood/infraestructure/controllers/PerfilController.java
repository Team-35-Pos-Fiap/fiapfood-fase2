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

import br.com.fiapfood.core.controllers.interfaces.IPerfilCoreController;
import br.com.fiapfood.infraestructure.controllers.request.perfil.NomeDto;
import br.com.fiapfood.infraestructure.controllers.request.perfil.PerfilDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/perfis")
@Slf4j
public class PerfilController {

    private final IPerfilCoreController perfilCoreController;
    
    public PerfilController(IPerfilCoreController perfilCoreController) {
        this.perfilCoreController = perfilCoreController;
    }

    @GetMapping
    public ResponseEntity<List<PerfilDto>> buscarTodos(){
        log.info("buscarTodos()");

        return ResponseEntity.ok().body(perfilCoreController.buscarTodos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PerfilDto> buscarPorId(@PathVariable @Valid @NotNull final Integer id){
        log.info("buscarPorId() - id: {}", id);

        return ResponseEntity.ok().body(perfilCoreController.buscarPorId(id));
    }
    
    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid @NotNull final NomeDto perfil){
        log.info("cadastrar() - nome: {}", perfil.nome());

        perfilCoreController.cadastrar(perfil.nome());
        
		return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @PatchMapping("/{id}/nome")
    public ResponseEntity<Void> atualizarNome(@PathVariable @Valid @NotNull final Integer id,  @RequestBody @Valid @NotNull final NomeDto perfil){
        log.info("atualizar() - id: {} - nome: {}", id, perfil.nome());

        perfilCoreController.atualizarNome(id, perfil.nome());
        
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    @PatchMapping("/{id}/inativa")
    public ResponseEntity<Void> inativar(@PathVariable @Valid @NotNull final Integer id){
        log.info("remover() - id: {}", id);

        perfilCoreController.inativar(id);
        
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    @PatchMapping("/{id}/reativa")
    public ResponseEntity<Void> reativar(@PathVariable @Valid @NotNull final Integer id){
        log.info("remover() - id: {}", id);

        perfilCoreController.reativar(id);
        
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}