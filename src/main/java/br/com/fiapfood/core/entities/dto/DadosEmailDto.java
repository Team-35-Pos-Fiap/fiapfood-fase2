package br.com.fiapfood.core.entities.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosEmailDto(@NotBlank(message = "O campo email precisa estar preenchido.")
                                 @Email(message = "O e-mail precisa ser válido")
                                 @Size(min = 3, max = 70, message = "O campo email precisa ter entre 3 e 70 caracteres.")
                                 String email) { }