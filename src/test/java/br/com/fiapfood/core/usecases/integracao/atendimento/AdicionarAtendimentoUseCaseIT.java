package br.com.fiapfood.core.usecases.integracao.atendimento;

import br.com.fiapfood.core.entities.Atendimento;
import br.com.fiapfood.core.entities.Restaurante;
import br.com.fiapfood.core.entities.dto.atendimento.AtendimentoCoreDto;
import br.com.fiapfood.core.gateways.impl.RestauranteGateway;
import br.com.fiapfood.core.gateways.interfaces.IRestauranteGateway;
import br.com.fiapfood.core.presenters.AtendimentoPresenter;
import br.com.fiapfood.core.usecases.atendimento.impl.AdicionarAtendimentoUseCase;
import br.com.fiapfood.core.usecases.atendimento.interfaces.IAdicionarAtendimentoUseCase;
import br.com.fiapfood.infraestructure.repositories.interfaces.IRestauranteRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.fiapfood.utils.DtoDataGenerator.atendimentoCoreDtoValido;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class AdicionarAtendimentoUseCaseIT {

    private final String ATENDIMENTO_DUPLICADO = "Não é possível adicionar o atendimento, pois já existe um outro atendimento para o mesmo dia.";
    private final String RESTAURANTE_INATIVO = "Não é possível adicionar o atendimento pois o restaurante se encontra inativo.";

    @Autowired
    private IAdicionarAtendimentoUseCase adicionarAtendimentoUseCase;

    @Autowired
    private IRestauranteGateway restauranteGateway;

//    @Test
//    void deveAdicionarAtendimentoComSucesso() {
//        UUID idRestaurante = UUID.fromString("40d5955e-c0bd-41da-b434-e46fa69bda14");
//
//        AtendimentoCoreDto atendimentoCoreDto = new AtendimentoCoreDto(
//                UUID.randomUUID(),
//                "TERÇA_FEIRA",
//                LocalTime.of(14, 0),
//                LocalTime.of(18, 0)
//        );
//        String diaAtendimento = atendimentoCoreDto.dia();
//
//        adicionarAtendimentoUseCase.adicionar(idRestaurante, atendimentoCoreDto);
//
//        Restaurante restaurante = restauranteGateway.buscarPorId(idRestaurante);
//
//        assertThat(new ArrayList<>(restaurante.getAtendimentos()))
//                .extracting(Atendimento::getDia)
//                .contains(diaAtendimento);
//    }
}
