package br.com.fiapfood.utils;

import br.com.fiapfood.infraestructure.entities.*;
import br.com.fiapfood.infraestructure.enums.Dia;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EntityDataGenerator {

    public static PerfilEntity perfilEntityValido() {
        return new PerfilEntity(
                1,
                "Dono",
                LocalDate.now(),
                null
        );
    }

    public static EnderecoEntity enderecoEntityValido() {
        return new EnderecoEntity(
                null,
                "São Gonçalo",
                "24455450",
                "Nova Cidade",
                "Rua Aquidabã",
                "Rio de Janeiro",
                79,
                "Casa 8"
        );
    }

    public static LoginEntity loginEntityValido() {
        return new LoginEntity(
                null,
                "us0010",
                "123"
        );
    }

    public static UsuarioEntity usuarioEntityValido() {
        return new UsuarioEntity(
                null,
                "Nome Teste",
                "nome@email.com",
                LocalDateTime.now(),
                null,
                true,
                enderecoEntityValido(),
                perfilEntityValido(),
                loginEntityValido()
        );
    }

    public static TipoCulinariaEntity tipoCulinariaEntityValido() {
        return new TipoCulinariaEntity(
                3,
                "Mexicana"
        );
    }

    public static AtendimentoEntity atendimentoEntityValido() {
        return new AtendimentoEntity(
                null,
                Dia.SÁBADO,
                LocalTime.of(9, 0),
                LocalTime.of(17, 0)
        );
    }

    public static ImagemEntity imagemEntityValida() {
        return new ImagemEntity(
                null,
                "exemplo.jpg",
                "Conteúdo de imagem".getBytes(StandardCharsets.UTF_8),
                "image/jpeg"
        );
    }

    public static ItemEntity itemEntityValido() {
        return new ItemEntity(
                null,
                "Hambúrguer Artesanal",
                "Pão brioche, carne 180g, queijo cheddar, cebola caramelizada",
                new BigDecimal("29.90"),
                true,
                true,
                imagemEntityValida()
        );
    }

    public static RestauranteEntity restauranteEntityValido() {
        return new RestauranteEntity(
            null,
            "Novo Restaurante",
            enderecoEntityValido(),
            usuarioEntityValido(),
            tipoCulinariaEntityValido(),
            true,
            List.of(itemEntityValido()),
            List.of(atendimentoEntityValido())
        );
    }

}
