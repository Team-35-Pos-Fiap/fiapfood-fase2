package br.com.fiapfood.utils;

import br.com.fiapfood.core.entities.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class CoreEntityDataGenerator {
    public static Perfil corePerfilEntityValido() {
        return Perfil.criar(
                1,
                "Dono",
                LocalDate.now(),
                null
        );
    }

    public static Login coreLoginEntityValido() {
        return Login.criar(
                UUID.fromString("631fe67c-567b-4513-86e1-43354c72a5a3"),
                "us0001",
                "123"
        );
    }

    public static Endereco coreEnderecoEntityValido() {
        return new Endereco(
                UUID.fromString("263e71ee-0143-4c92-837c-fbb21f2b2f59"),
                "São Gonçalo",
                "24455450",
                "Nova Cidade",
                "Rua Aquidabã",
                "Rio de Janeiro",
                79,
                "Casa 8"
        );
    }

    public static Usuario coreUsuarioEntityAtivoValido() {
        return Usuario.criar(
                UUID.fromString("ad604453-c693-4bc4-9028-06d763299282"),
                "John Doe",
                1,
                coreLoginEntityValido(),
                true,
                "john.doe@email.com",
                LocalDateTime.now(),
                null,
                coreEnderecoEntityValido()
        );
    }

    public static Usuario coreUsuarioEntityInativoValido() {
        return Usuario.criar(
                UUID.fromString("ad604453-c693-4bc4-9028-06d763299282"),
                "John Doe",
                1,
                coreLoginEntityValido(),
                false,
                "john.doe@email.com",
                LocalDateTime.now(),
                null,
                coreEnderecoEntityValido()
        );
    }

    public static TipoCulinaria coreTipoCulinariaEntityValido() {
        return TipoCulinaria.criar(
                3,
                "Mexicana"
        );
    }

    public static Atendimento coreAtendimentoEntityValido() {
        return Atendimento.criar(
                UUID.fromString("3e459c60-f3cc-4d77-8547-58a1f4988cb2"),
                "Sábado",
                LocalTime.of(9, 0),
                LocalTime.of(17, 0)
        );
    }

    public static Imagem coreImagemEntityValido() {
        return Imagem.criar(
                UUID.fromString("0cbb0d76-90fb-4e78-a37c-e85c5f3ec28d"),
                "imagem_teste.png",
                "Conteúdo de imagem".getBytes(StandardCharsets.UTF_8),
                "image/png"
        );
    }

    public static Item coreItemEntityValido() {
        return Item.criar(
                UUID.fromString("63b2143b-1e3a-47b5-92f4-f8cc4a3f9a0b"),
                "Hambúrguer Artesanal",
                "Pão brioche, carne 180g, queijo cheddar, cebola caramelizada",
                new BigDecimal("29.90"),
                true,
                true,
                coreImagemEntityValido()
        );
    }

    public static Restaurante coreRestauranteEntityValido() {
        return Restaurante.criar(
                UUID.fromString("9a53e79b-15ff-4426-8f5d-5356318050cb"),
                "Restaurante da Esquina",
                coreEnderecoEntityValido(),
                UUID.fromString("c21e5cf4-b539-4c3b-a510-11d6c4a45f36"),
                2,
                true,
                List.of(coreAtendimentoEntityValido()),
                List.of(coreItemEntityValido())
        );
    }

}
