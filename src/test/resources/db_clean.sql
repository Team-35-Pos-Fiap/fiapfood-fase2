-- Delete dependent records first
DELETE FROM item;             -- Items depend on restaurante and imagem
DELETE FROM atendimento;      -- Atendimento depends on restaurante
DELETE FROM restaurante;      -- Restaurante depends on endereco, tipo_culinaria, usuario
DELETE FROM usuario;          -- Usuario depends on endereco, login, perfil
DELETE FROM endereco;         -- Endereco can be shared, so delete after usuario and restaurante
DELETE FROM login;            -- Login is linked only by usuario
DELETE FROM imagem;           -- Images used in items

DELETE FROM tipo_culinaria;  -- Tipo_culinaria independent, but used by restaurante
DELETE FROM perfil;           -- Perfil independent, used by usuario