package br.com.fiapfood.infraestructure.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiapfood.core.controllers.interfaces.IPerfilCoreController;
import br.com.fiapfood.core.entities.dto.PerfilDto;
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
}