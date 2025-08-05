package br.com.fiapfood.utils;

import br.com.fiapfood.core.entities.dto.atendimento.AtendimentoCoreDto;
import br.com.fiapfood.core.entities.dto.endereco.DadosEnderecoCoreDto;
import br.com.fiapfood.core.entities.dto.endereco.EnderecoCoreDto;
import br.com.fiapfood.core.entities.dto.item.ItemOutputCoreDto;
import br.com.fiapfood.core.entities.dto.login.LoginCoreDto;
import br.com.fiapfood.core.entities.dto.paginacao.PaginacaoCoreDto;
import br.com.fiapfood.core.entities.dto.perfil.PerfilCoreDto;
import br.com.fiapfood.core.entities.dto.restaurante.CadastrarRestauranteCoreDto;
import br.com.fiapfood.core.entities.dto.restaurante.DadosRestauranteCoreDto;
import br.com.fiapfood.core.entities.dto.restaurante.RestaurantePaginacaoCoreDto;
import br.com.fiapfood.core.entities.dto.tipo_culinaria.TipoCulinariaCoreDto;
import br.com.fiapfood.core.entities.dto.usuario.*;
import br.com.fiapfood.infraestructure.controllers.request.atendimento.AtendimentoDto;
import br.com.fiapfood.infraestructure.controllers.request.endereco.DadosEnderecoDto;
import br.com.fiapfood.infraestructure.controllers.request.endereco.EnderecoDto;
import br.com.fiapfood.infraestructure.controllers.request.item.ItemDto;
import br.com.fiapfood.infraestructure.controllers.request.login.LoginDto;
import br.com.fiapfood.infraestructure.controllers.request.perfil.PerfilDto;
import br.com.fiapfood.infraestructure.controllers.request.restaurante.CadastrarRestauranteDto;
import br.com.fiapfood.infraestructure.controllers.request.tipo_culinaria.TipoCulinariaDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.CadastrarUsuarioDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.UsuarioDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class DtoDataGenerator {
    public static LoginDto loginDtoValido() {
        return new LoginDto(
                UUID.fromString("88ecf851-3a78-475b-be3c-9a4a9aedec44"),
                "us0010",
                "123"
        );
    }

    public static LoginCoreDto loginCoreDtoValido() {
        return new LoginCoreDto(
                UUID.fromString("88ecf851-3a78-475b-be3c-9a4a9aedec44"),
                "us0010",
                "123"
        );
    }

    public static DadosEnderecoDto dadosEnderecoDtoValido() {
        return new DadosEnderecoDto(
                "São Gonçalo",
                "24455450",
                "Nova Cidade",
                "Rua Aquidabã",
                "Rio de Janeiro",
                79,
                "Casa 8"
        );
    }

    public static DadosEnderecoCoreDto dadosEnderecoCoreDtoValido() {
        return new DadosEnderecoCoreDto(
                "São Gonçalo",
                "24455450",
                "Nova Cidade",
                "Rua Aquidabã",
                "Rio de Janeiro",
                79,
                "Casa 8"
        );
    }

    public static EnderecoDto enderecoDtoValido() {
        return new EnderecoDto(
                UUID.fromString("acc1fc92-d526-46ea-92e9-ac23fd838885"),
                "São Gonçalo",
                "24455450",
                "Nova Cidade",
                "Rua Aquidabã",
                "Rio de Janeiro",
                79,
                "Casa 8"
        );
    }

    public static EnderecoCoreDto enderecoCoreDtoValido() {
        return new EnderecoCoreDto(
                UUID.fromString("acc1fc92-d526-46ea-92e9-ac23fd838885"),
                "São Gonçalo",
                "24455450",
                "Nova Cidade",
                "Rua Aquidabã",
                "Rio de Janeiro",
                79,
                "Casa 8"
        );
    }

    public static PerfilDto perfilDtoValido() {
        return new PerfilDto(
                1,
                "Admin",
                LocalDate.now(),
                null
        );
    }

    public static PerfilCoreDto perfilCoreDtoValido() {
        return new PerfilCoreDto(
                1,
                "Admin",
                LocalDate.now(),
                null
        );
    }

    public static CadastrarUsuarioDto cadastrarUsuarioDtoValido() {
        return new CadastrarUsuarioDto(
                "John Doe",
                1,
                loginDtoValido(),
                "john.doe@email.com",
                dadosEnderecoDtoValido()
        );
    }

    public static CadastrarUsuarioCoreDto cadastrarUsuarioCoreDtoValido() {
        return new CadastrarUsuarioCoreDto(
                "John Doe",
                1,
                loginCoreDtoValido(),
                "john.doe@email.com",
                dadosEnderecoCoreDtoValido()
        );
    }

    public static UsuarioDto usuarioDtoValido() {
        return new UsuarioDto(
                UUID.fromString("c626f4f2-9693-4fbd-9086-ee8b0bf5febb"),
                "John Doe",
                perfilDtoValido(),
                loginDtoValido(),
                true,
                "john.doe@email.com",
                LocalDateTime.now(),
                null,
                enderecoDtoValido()
        );
    }

    public static DadosUsuarioCoreDto  dadosUsuarioCoreDtoValido() {
        return new DadosUsuarioCoreDto(
                UUID.fromString("c626f4f2-9693-4fbd-9086-ee8b0bf5febb"),
                "John Doe",
                perfilCoreDtoValido(),
                loginCoreDtoValido(),
                true,
                "john.doe@email.com",
                LocalDateTime.now(),
                null,
                enderecoCoreDtoValido()
        );
    }

    public static DadosUsuarioResumidoCoreDto dadosUsuarioResumidoCoreDto() {
        return new DadosUsuarioResumidoCoreDto(
                UUID.fromString("cf05db14-7993-4564-bff9-c258b5c7387c"),
                "John Doe",
                "us0001",
                "john.doe@email.com"
        );
    }

    public static TipoCulinariaDto tipoCulinariaDtoValido() {
        return new TipoCulinariaDto(1, "Brasileira");
    }

    public static TipoCulinariaCoreDto tipoCulinariaCoreDtoValido() {
        return new TipoCulinariaCoreDto(1, "Brasileira");
    }

    public static AtendimentoCoreDto atendimentoCoreDtoValido() {
        return new AtendimentoCoreDto(
                UUID.fromString("3cb55da2-47b5-4625-98d4-92bdd29cbf06"),
                "Atendimento Teste",
                LocalTime.of(9, 0),
                LocalTime.of(18, 0)
        );
    }

    public static AtendimentoDto atendimentoDtoValido() {
        return new AtendimentoDto(
                UUID.fromString("3cb55da2-47b5-4625-98d4-92bdd29cbf06"),
                "Atendimento Teste",
                LocalTime.of(9, 0),
                LocalTime.of(18, 0)
        );
    }

    public static DadosRestauranteCoreDto dadosRestauranteCoreDtoValido() {
        List<AtendimentoCoreDto> atendimentos = List.of(atendimentoCoreDtoValido(), atendimentoCoreDtoValido());

        return new DadosRestauranteCoreDto(
                UUID.fromString("0ec1442d-71cf-4c31-b95c-7fc80af6682e"),
                "Restaurante Teste",
                enderecoCoreDtoValido(),
                true,
                dadosUsuarioResumidoCoreDto(),
                tipoCulinariaCoreDtoValido(),
                atendimentos
        );

    }

    public static RestaurantePaginacaoCoreDto restaurantePaginacaoCoreDtoValido() {
        List<AtendimentoCoreDto> atendimentos = List.of(atendimentoCoreDtoValido(), atendimentoCoreDtoValido());

        DadosRestauranteCoreDto restauranteDto = new DadosRestauranteCoreDto(
                UUID.randomUUID(),
                "Restaurante Teste",
                enderecoCoreDtoValido(),
                true,
                dadosUsuarioResumidoCoreDto(),
                tipoCulinariaCoreDtoValido(),
                atendimentos
        );

        List<DadosRestauranteCoreDto> restauranteDtos = List.of(restauranteDto, restauranteDto);
        PaginacaoCoreDto paginacao = new PaginacaoCoreDto(1, 1, 2);
        return new RestaurantePaginacaoCoreDto(restauranteDtos, paginacao);

    }

    public static CadastrarRestauranteDto cadastrarRestauranteDtoValido() {
        List<AtendimentoDto> atendimentos = List.of(atendimentoDtoValido(), atendimentoDtoValido());

        return new CadastrarRestauranteDto(
                "Restaurante Teste",
                dadosEnderecoDtoValido(),
                dadosUsuarioResumidoCoreDto().id(),
                tipoCulinariaDtoValido().id(),
                atendimentos
        );

    }

    public static CadastrarRestauranteCoreDto cadastrarRestauranteCoreDtoValido() {
        List<AtendimentoCoreDto> atendimentos = List.of(atendimentoCoreDtoValido(), atendimentoCoreDtoValido());

        return new CadastrarRestauranteCoreDto(
                "Restaurante Teste",
                dadosEnderecoCoreDtoValido(),
                dadosUsuarioResumidoCoreDto().id(),
                tipoCulinariaDtoValido().id(),
                atendimentos
        );

    }

    public static UsuarioPaginacaoInputDto usuarioPaginacaoInputDtoValido() {
        return new UsuarioPaginacaoInputDto(
            List.of(dadosUsuarioInputDtoValido(), dadosUsuarioInputDtoValido()),
                paginacaoCoreDtoValido()
        );
    }

    public static DadosUsuarioInputDto dadosUsuarioInputDtoValido() {
        return new DadosUsuarioInputDto(
                UUID.fromString("c626f4f2-9693-4fbd-9086-ee8b0bf5febb"),
                "John Doe",
                1,
                loginCoreDtoValido(),
                true,
                "john.doe@email.com",
                LocalDateTime.now(),
                null,
                enderecoCoreDtoValido()
        );
    }

    public static PaginacaoCoreDto paginacaoCoreDtoValido() {
        return new PaginacaoCoreDto(1,1,2);
    }

    public static ItemOutputCoreDto itemOutputCoreDtoValido() {
        return new ItemOutputCoreDto(
                UUID.fromString("1192a947-5a29-4d60-b110-51f2e3ef8fc4"),
                "Item Teste",
                "Descricao item teste",
                BigDecimal.valueOf(10.35),
                true,
                true,
                "/images/item-teste.jpg"
        );
    }

    public static ItemDto itemDtoValido() {
        return new ItemDto(
                UUID.fromString("1192a947-5a29-4d60-b110-51f2e3ef8fc4"),
                "Item Teste",
                "Descricao item teste",
                BigDecimal.valueOf(10.35),
                true,
                true,
                "/images/item-teste.jpg"
        );
    }
}