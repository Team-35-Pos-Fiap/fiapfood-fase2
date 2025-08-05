---- PERFIS ---

INSERT INTO
  perfil (id, nome, data_criacao, data_inativacao)
VALUES
  (1, 'Dono', current_timestamp, null);

INSERT INTO
  perfil (id, nome, data_criacao, data_inativacao)
VALUES
  (2, 'Cliente', current_timestamp, null);

INSERT INTO
  perfil (id, nome, data_criacao, data_inativacao)
VALUES
  (
    3,
    'Administrador',
    current_timestamp,
    current_timestamp
  );

INSERT INTO
  perfil (id, nome, data_criacao, data_inativacao)
VALUES
  (4, 'Entregador', current_timestamp, null);

---- USUARIO 1 ----
INSERT INTO
  endereco (
    id,
    cidade,
    bairro,
    estado,
    endereco,
    numero,
    complemento,
    cep
  )
VALUES
  (
    UNHEX (
      REPLACE('de6762a9-e373-4a05-a6bb-d345a759b26f', '-', '')
    ),
    'Niterói',
    'Barreto',
    'Rio de Janeiro',
    'Avenida do Contorno',
    '5',
    'Quadra 2',
    '24455786'
  );

INSERT INTO
  login (id, matricula, senha)
VALUES
  (
    UNHEX (
      REPLACE('c303266f-9d32-4dde-8f4c-d8ee13b24ae9', '-', '')
    ),
    'us0001',
    '123'
  );

INSERT INTO
  usuario (
    id,
    nome,
    email,
    ativo,
    data_criacao,
    data_atualizacao,
    id_perfil,
    id_endereco,
    id_login
  )
VALUES
  (
    UNHEX (
      REPLACE('cf05db14-7993-4564-bff9-c258b5c7387c', '-', '')
    ),
    'José Magalhaes',
    'jose.magalhaes@fiapfood.com',
    TRUE,
    current_timestamp,
    null,
    1,
    UNHEX (
      REPLACE('de6762a9-e373-4a05-a6bb-d345a759b26f', '-', '')
    ),
    UNHEX (
      REPLACE('c303266f-9d32-4dde-8f4c-d8ee13b24ae9', '-', '')
    )
  );

---- USUARIO 2 ----
INSERT INTO
  endereco (
    id,
    cidade,
    bairro,
    estado,
    endereco,
    numero,
    complemento,
    cep
  )
VALUES
  (
    UNHEX (
      REPLACE('229f4bff-c6be-4008-8911-bef0c79735e2', '-', '')
    ),
    'Rio de Janeiro',
    'Copacabana',
    'RJ',
    'Avenida Atlântica',
    '1500',
    'Apto 302',
    '22021001'
  );

INSERT INTO
  login (id, matricula, senha)
VALUES
  (
    UNHEX (
      REPLACE('dbc8695f-fe37-4741-9c6d-7bf5e96dfe6d', '-', '')
    ),
    'us0002',
    '123'
  );

INSERT INTO
  usuario (
    id,
    nome,
    email,
    ativo,
    data_criacao,
    data_atualizacao,
    id_perfil,
    id_endereco,
    id_login
  )
VALUES
  (
    UNHEX (
      REPLACE('b48bc2dc-fd87-462d-a8a6-6e74674d0338', '-', '')
    ),
    'Carla Rodrigues',
    'carla.rodrigues@fiapfood.com',
    TRUE,
    current_timestamp,
    null,
    2,
    UNHEX (
      REPLACE('229f4bff-c6be-4008-8911-bef0c79735e2', '-', '')
    ),
    UNHEX (
      REPLACE('dbc8695f-fe37-4741-9c6d-7bf5e96dfe6d', '-', '')
    )
  );

---- USUARIO 3 ----
INSERT INTO
  endereco (
    id,
    cidade,
    bairro,
    estado,
    endereco,
    numero,
    complemento,
    cep
  )
VALUES
  (
    UNHEX (
      REPLACE('78c76348-d821-4808-ac67-a5e599191b23', '-', '')
    ),
    'São Paulo',
    'Pinheiros',
    'SP',
    'Rua dos Pinheiros',
    '1340',
    'Conjunto 25',
    '05422002'
  );

INSERT INTO
  login (id, matricula, senha)
VALUES
  (
    UNHEX (
      REPLACE('2b84cf36-9333-42af-b013-eccec84a2270', '-', '')
    ),
    'us0003',
    '123'
  );

INSERT INTO
  usuario (
    id,
    nome,
    email,
    ativo,
    data_criacao,
    data_atualizacao,
    id_perfil,
    id_endereco,
    id_login
  )
VALUES
  (
    UNHEX (
      REPLACE('602a4056-68d0-44f0-8284-14b0cf7a75b6', '-', '')
    ),
    'Rafael Santos',
    'rafael.santos@fiapfood.com',
    FALSE,
    current_timestamp,
    current_timestamp,
    1,
    UNHEX (
      REPLACE('78c76348-d821-4808-ac67-a5e599191b23', '-', '')
    ),
    UNHEX (
      REPLACE('2b84cf36-9333-42af-b013-eccec84a2270', '-', '')
    )
  );

---- USUARIO 4 ----
INSERT INTO
  endereco (
    id,
    cidade,
    bairro,
    estado,
    endereco,
    numero,
    complemento,
    cep
  )
VALUES
  (
    UNHEX (
      REPLACE('1524d4a4-3f99-46d2-92cb-890f5e690f74', '-', '')
    ),
    'Belo Horizonte',
    'Savassi',
    'MG',
    'Rua Pernambuco',
    '1322',
    null,
    '30130151'
  );

INSERT INTO
  login (id, matricula, senha)
VALUES
  (
    UNHEX (
      REPLACE('2de80d8c-3665-4beb-858a-d5f242b822be', '-', '')
    ),
    'us0004',
    '123'
  );

INSERT INTO
  usuario (
    id,
    nome,
    email,
    ativo,
    data_criacao,
    data_atualizacao,
    id_perfil,
    id_endereco,
    id_login
  )
VALUES
  (
    UNHEX (
      REPLACE('60127300-b56a-4394-a208-d9ef8eb864c7', '-', '')
    ),
    'Juliana Mendes',
    'juliana.mendes@fiapfood.com',
    FALSE,
    current_timestamp,
    current_timestamp,
    2,
    UNHEX (
      REPLACE('1524d4a4-3f99-46d2-92cb-890f5e690f74', '-', '')
    ),
    UNHEX (
      REPLACE('2de80d8c-3665-4beb-858a-d5f242b822be', '-', '')
    )
  );

---- TIPOS DE CULINARIA ----
INSERT INTO
  tipo_culinaria (id, nome)
VALUES
  (1, 'Brasileira');

INSERT INTO
  tipo_culinaria (id, nome)
VALUES
  (2, 'Italiana');

INSERT INTO
  tipo_culinaria (id, nome)
VALUES
  (3, 'Japonesa');

INSERT INTO
  tipo_culinaria (id, nome)
VALUES
  (4, 'Churrasco');

INSERT INTO
  tipo_culinaria (id, nome)
VALUES
  (5, 'Vegetariana');

---- RESTAURANTE 1 ----
INSERT INTO
  endereco (
    id,
    cidade,
    bairro,
    estado,
    endereco,
    numero,
    complemento,
    cep
  )
VALUES
  (
    UNHEX (
      REPLACE('0f324a42-4e15-4b40-8001-f531c6b306dc', '-', '')
    ),
    'Manaus',
    'Educandos',
    'AM',
    'Beco das Rosas',
    null,
    null,
    '69070061'
  );

INSERT INTO
  restaurante (
    id,
    nome,
    id_endereco,
    id_tipo_culinaria,
    ativo,
    id_usuario
  )
VALUES
  (
    UNHEX (
      REPLACE('40d5955e-c0bd-41da-b434-e46fa69bda14', '-', '')
    ),
    'Restaurante Sabor Brasil',
    UNHEX (
      REPLACE('0f324a42-4e15-4b40-8001-f531c6b306dc', '-', '')
    ),
    1,
    TRUE,
    UNHEX (
      REPLACE('602a4056-68d0-44f0-8284-14b0cf7a75b6', '-', '')
    )
  );

---- RESTAURANTE 2 ----
INSERT INTO
  endereco (
    id,
    cidade,
    bairro,
    estado,
    endereco,
    numero,
    complemento,
    cep
  )
VALUES
  (
    UNHEX (
      REPLACE('4cbd1ae7-163f-4f85-9c66-665f9f665840', '-', '')
    ),
    'Belo Horizonte',
    'Ouro Preto',
    'MG',
    'Rua Conceição do Mato Dentro',
    null,
    null,
    '31310240'
  );

INSERT INTO
  restaurante (
    id,
    nome,
    id_endereco,
    id_tipo_culinaria,
    ativo,
    id_usuario
  )
VALUES
  (
    UNHEX (
      REPLACE('fc8a9535-d6be-465f-8bf1-d9885e91c91d', '-', '')
    ),
    'La Bella Pasta',
    UNHEX (
      REPLACE('4cbd1ae7-163f-4f85-9c66-665f9f665840', '-', '')
    ),
    2,
    FALSE,
    UNHEX (
      REPLACE('602a4056-68d0-44f0-8284-14b0cf7a75b6', '-', '')
    )
  );

---- RESTAURANTE 3 ----
INSERT INTO
  endereco (
    id,
    cidade,
    bairro,
    estado,
    endereco,
    numero,
    complemento,
    cep
  )
VALUES
  (
    UNHEX (
      REPLACE('f8ff8bb3-c028-4ac1-91e2-70fd3c6b0115', '-', '')
    ),
    'São Paulo',
    'Jardim Castelo',
    'SP',
    'Rua Filipe Nicoletti',
    null,
    null,
    '03728240'
  );

INSERT INTO
  restaurante (
    id,
    nome,
    id_endereco,
    id_tipo_culinaria,
    ativo,
    id_usuario
  )
VALUES
  (
    UNHEX (
      REPLACE('a72181a6-7699-4686-a5ec-1a0431764e62', '-', '')
    ),
    'Sushi House',
    UNHEX (
      REPLACE('f8ff8bb3-c028-4ac1-91e2-70fd3c6b0115', '-', '')
    ),
    3,
    TRUE,
    UNHEX (
      REPLACE('cf05db14-7993-4564-bff9-c258b5c7387c', '-', '')
    )
  );

---- RESTAURANTE 4 ----
INSERT INTO
  endereco (
    id,
    cidade,
    bairro,
    estado,
    endereco,
    numero,
    complemento,
    cep
  )
VALUES
  (
    UNHEX (
      REPLACE('5a4ff077-8399-42a4-a281-2c0642cbfbe1', '-', '')
    ),
    'Curitiba',
    'Tatuquara',
    'PR',
    'Rua Paulo Vilimavicius',
    null,
    null,
    '81480133'
  );

INSERT INTO
  restaurante (
    id,
    nome,
    id_endereco,
    id_tipo_culinaria,
    ativo,
    id_usuario
  )
VALUES
  (
    UNHEX (
      REPLACE('21adec7d-d4f7-4999-a4dd-eaf0c242b3bd', '-', '')
    ),
    'Churrascaria Gaúcha',
    UNHEX (
      REPLACE('5a4ff077-8399-42a4-a281-2c0642cbfbe1', '-', '')
    ),
    4,
    TRUE,
    UNHEX (
      REPLACE('cf05db14-7993-4564-bff9-c258b5c7387c', '-', '')
    )
  );

---- RESTAURANTE 5 ----
INSERT INTO
  endereco (
    id,
    cidade,
    bairro,
    estado,
    endereco,
    numero,
    complemento,
    cep
  )
VALUES
  (
    UNHEX (
      REPLACE('10914a8c-059e-4953-a1f5-909dffc2b316', '-', '')
    ),
    'Brasília',
    'Santa Maria',
    'DF',
    'Quadra CL',
    '410',
    'Bloco E',
    '72510245'
  );

INSERT INTO
  restaurante (
    id,
    nome,
    id_endereco,
    id_tipo_culinaria,
    ativo,
    id_usuario
  )
VALUES
  (
    UNHEX (
      REPLACE('9a7f3e70-8343-47e9-8fb0-2253cb03575f', '-', '')
    ),
    'Veggie Life',
    UNHEX (
      REPLACE('10914a8c-059e-4953-a1f5-909dffc2b316', '-', '')
    ),
    5,
    FALSE,
    UNHEX (
      REPLACE('cf05db14-7993-4564-bff9-c258b5c7387c', '-', '')
    )
  );

---- ATENDIMENTO RESTAURANTE 1 ----
INSERT INTO
  atendimento (
    id_atendimento,
    id_restaurante,
    dia,
    inicio_atendimento,
    termino_atendimento
  )
VALUES
  (
    UNHEX (
      REPLACE('f1c72b63-a558-4fa9-b3e6-6cf244378e71', '-', '')
    ),
    UNHEX (
      REPLACE('40d5955e-c0bd-41da-b434-e46fa69bda14', '-', '')
    ),
    2,
    '11:00:00',
    '16:00:00'
  ),
  (
    UNHEX (
      REPLACE('a5b3c9be-1d9e-41cd-9301-3d7de184c9bb', '-', '')
    ),
    UNHEX (
      REPLACE('40d5955e-c0bd-41da-b434-e46fa69bda14', '-', '')
    ),
    5,
    '12:00:00',
    '18:00:00'
  );

---- ATENDIMENTO  2 ----
INSERT INTO
  atendimento (
    id_atendimento,
    id_restaurante,
    dia,
    inicio_atendimento,
    termino_atendimento
  )
VALUES
  (
    UNHEX (
      REPLACE('9f2e99d9-86b7-4c5e-8ae5-3a3a8df6b7b0', '-', '')
    ),
    UNHEX (
      REPLACE('fc8a9535-d6be-465f-8bf1-d9885e91c91d', '-', '')
    ),
    3,
    '10:00:00',
    '15:00:00'
  );

---- ATENDIMENTO  3 ----
INSERT INTO
  atendimento (
    id_atendimento,
    id_restaurante,
    dia,
    inicio_atendimento,
    termino_atendimento
  )
VALUES
  (
    UNHEX (
      REPLACE('d9cfd0be-82d6-4fc3-9bd9-c12943a9e4cc', '-', '')
    ),
    UNHEX (
      REPLACE('a72181a6-7699-4686-a5ec-1a0431764e62', '-', '')
    ),
    4,
    '18:00:00',
    '23:00:00'
  ),
  (
    UNHEX (
      REPLACE('c7e01c25-49a0-47d8-9a65-bd5edc08f775', '-', '')
    ),
    UNHEX (
      REPLACE('a72181a6-7699-4686-a5ec-1a0431764e62', '-', '')
    ),
    6,
    '17:00:00',
    '22:00:00'
  );

---- ATENDIMENTO  4 ----
INSERT INTO
  atendimento (
    id_atendimento,
    id_restaurante,
    dia,
    inicio_atendimento,
    termino_atendimento
  )
VALUES
  (
    UNHEX (
      REPLACE('4f59e8f6-0ef4-45de-a8e6-6a1f33a3a765', '-', '')
    ),
    UNHEX (
      REPLACE('21adec7d-d4f7-4999-a4dd-eaf0c242b3bd', '-', '')
    ),
    1,
    '12:00:00',
    '18:00:00'
  );

---- ATENDIMENTO 5 ----
INSERT INTO
  atendimento (
    id_atendimento,
    id_restaurante,
    dia,
    inicio_atendimento,
    termino_atendimento
  )
VALUES
  (
    UNHEX (
      REPLACE('c1eb0660-ec7e-4a6b-8c66-79257c0c55b2', '-', '')
    ),
    UNHEX (
      REPLACE('9a7f3e70-8343-47e9-8fb0-2253cb03575f', '-', '')
    ),
    7,
    '09:00:00',
    '13:00:00'
  ),
  (
    UNHEX (
      REPLACE('3f71d374-6f10-4ebf-8f12-f5f4b4e4b989', '-', '')
    ),
    UNHEX (
      REPLACE('9a7f3e70-8343-47e9-8fb0-2253cb03575f', '-', '')
    ),
    3,
    '10:00:00',
    '14:00:00'
  );

---- IMAGEM 1: Feijoada ----
INSERT INTO
  imagem (id, nome, conteudo, tipo)
VALUES
  (
    UNHEX (
      REPLACE('2c1f1f01-dc4b-447b-9a38-35b54cdbbe55', '-', '')
    ),
    'feijoada.jpg',
    X'FFD8FFE000104A464946',
    'image/jpeg'
  );

---- IMAGEM 2: Pão de Queijo ----
INSERT INTO
  imagem (id, nome, conteudo, tipo)
VALUES
  (
    UNHEX (
      REPLACE('8de176c3-0fcd-40b0-897c-c43847c2b321', '-', '')
    ),
    'pao_queijo.jpg',
    X'FFD8FFE000104A464946',
    'image/jpeg'
  );

---- IMAGEM 3: Lasanha ----
INSERT INTO
  imagem (id, nome, conteudo, tipo)
VALUES
  (
    UNHEX (
      REPLACE('4a3ff91d-d8b0-49aa-8171-0b6761f1c62e', '-', '')
    ),
    'lasanha.jpg',
    X'FFD8FFE000104A464946',
    'image/jpeg'
  );

---- IMAGEM 4: Bruschetta ----
INSERT INTO
  imagem (id, nome, conteudo, tipo)
VALUES
  (
    UNHEX (
      REPLACE('1b2e6f2c-cd34-4f20-bd69-e5078272f60a', '-', '')
    ),
    'bruschetta.jpg',
    X'FFD8FFE000104A464946',
    'image/jpeg'
  );

---- IMAGEM 5: Sushi Combo ----
INSERT INTO
  imagem (id, nome, conteudo, tipo)
VALUES
  (
    UNHEX (
      REPLACE('b947b166-c037-4d03-a5c0-030fcb28a505', '-', '')
    ),
    'sushi_combo.jpg',
    X'FFD8FFE000104A464946',
    'image/jpeg'
  );

---- IMAGEM 6: Temaki ----
INSERT INTO
  imagem (id, nome, conteudo, tipo)
VALUES
  (
    UNHEX (
      REPLACE('a0e632ef-4ff7-47a6-bc99-1d004e41a2a6', '-', '')
    ),
    'temaki.jpg',
    X'FFD8FFE000104A464946',
    'image/jpeg'
  );

---- IMAGEM 7: Picanha ----
INSERT INTO
  imagem (id, nome, conteudo, tipo)
VALUES
  (
    UNHEX (
      REPLACE('6fd3276f-b93d-44c7-92b5-f37a1c0896c4', '-', '')
    ),
    'picanha.jpg',
    X'FFD8FFE000104A464946',
    'image/jpeg'
  );

---- IMAGEM 8: Costela ----
INSERT INTO
  imagem (id, nome, conteudo, tipo)
VALUES
  (
    UNHEX (
      REPLACE('2de40ce0-49a2-48ce-87e3-64c3e4b1743f', '-', '')
    ),
    'costela.jpg',
    X'FFD8FFE000104A464946',
    'image/jpeg'
  );

---- IMAGEM 9: Salada Vegana ----
INSERT INTO
  imagem (id, nome, conteudo, tipo)
VALUES
  (
    UNHEX (
      REPLACE('1792c2b1-b9e1-4649-bdd5-d39ce1705cb5', '-', '')
    ),
    'salada_vegana.jpg',
    X'FFD8FFE000104A464946',
    'image/jpeg'
  );

---- IMAGEM 10: Prato vegano ----
INSERT INTO
  imagem (id, nome, conteudo, tipo)
VALUES
  (
    UNHEX (
      REPLACE('601ad0e7-9de1-4be4-a578-6a591e6cfdfb', '-', '')
    ),
    'prato_vegano.jpg',
    X'FFD8FFE000104A464946',
    'image/jpeg'
  );

---- ITENS RESTAURANTE 1 ----
INSERT INTO
  item (
    id,
    nome,
    descricao,
    preco,
    disponivel_consumo_presencial,
    disponivel,
    id_restaurante,
    id_imagem
  )
VALUES
  (
    UNHEX (
      REPLACE('cd1938c2-91d5-4c71-bd63-9946bcff5f2d', '-', '')
    ),
    'Feijoada Completa',
    'Feijoada com arroz, couve e laranja',
    45.00,
    TRUE,
    TRUE,
    UNHEX (
      REPLACE('40d5955e-c0bd-41da-b434-e46fa69bda14', '-', '')
    ),
    UNHEX (
      REPLACE('2c1f1f01-dc4b-447b-9a38-35b54cdbbe55', '-', '')
    )
  ),
  (
    UNHEX (
      REPLACE('5a1c47f7-b4d2-4456-975f-5e1a4e5aa128', '-', '')
    ),
    'Moqueca Baiana',
    'Moqueca de peixe com dendê e leite de coco',
    52.00,
    TRUE,
    TRUE,
    UNHEX (
      REPLACE('40d5955e-c0bd-41da-b434-e46fa69bda14', '-', '')
    ),
    UNHEX (
      REPLACE('8de176c3-0fcd-40b0-897c-c43847c2b321', '-', '')
    )
  );

---- ITENS RESTAURANTE 2 ----
INSERT INTO
  item (
    id,
    nome,
    descricao,
    preco,
    disponivel_consumo_presencial,
    disponivel,
    id_restaurante,
    id_imagem
  )
VALUES
  (
    UNHEX (
      REPLACE('86340bd6-7d41-41d2-bd6f-1786ae0a1f2c', '-', '')
    ),
    'Spaghetti alla Carbonara',
    'Spaghetti com pancetta e molho de ovo com parmesão',
    38.00,
    TRUE,
    TRUE,
    UNHEX (
      REPLACE('fc8a9535-d6be-465f-8bf1-d9885e91c91d', '-', '')
    ),
    UNHEX (
      REPLACE('1b2e6f2c-cd34-4f20-bd69-e5078272f60a', '-', '')
    )
  ),
  (
    UNHEX (
      REPLACE('2022f94c-207b-4417-99e0-5b6b6a2ef74a', '-', '')
    ),
    'Lasanha à Bolonhesa',
    'Lasanha com molho de carne e queijo gratinado',
    44.00,
    TRUE,
    TRUE,
    UNHEX (
      REPLACE('fc8a9535-d6be-465f-8bf1-d9885e91c91d', '-', '')
    ),
    UNHEX (
      REPLACE('4a3ff91d-d8b0-49aa-8171-0b6761f1c62e', '-', '')
    )
  );

---- ITENS RESTAURANTE 3 ----
INSERT INTO
  item (
    id,
    nome,
    descricao,
    preco,
    disponivel_consumo_presencial,
    disponivel,
    id_restaurante,
    id_imagem
  )
VALUES
  (
    UNHEX (
      REPLACE('d36db8ed-0ac9-48e0-bc96-30db15a7e4df', '-', '')
    ),
    'Combo Sushi 12 peças',
    'Sushi variado com salmão, atum e kani',
    48.00,
    FALSE,
    TRUE,
    UNHEX (
      REPLACE('a72181a6-7699-4686-a5ec-1a0431764e62', '-', '')
    ),
    UNHEX (
      REPLACE('b947b166-c037-4d03-a5c0-030fcb28a505', '-', '')
    )
  ),
  (
    UNHEX (
      REPLACE('0d2fa474-3a0f-4f10-9290-0e3b9a730a7b', '-', '')
    ),
    'Temaki de Salmão',
    'Cone de alga recheado com salmão e arroz',
    22.00,
    TRUE,
    TRUE,
    UNHEX (
      REPLACE('a72181a6-7699-4686-a5ec-1a0431764e62', '-', '')
    ),
    UNHEX (
      REPLACE('a0e632ef-4ff7-47a6-bc99-1d004e41a2a6', '-', '')
    )
  );

---- ITENS RESTAURANTE 4 ----
INSERT INTO
  item (
    id,
    nome,
    descricao,
    preco,
    disponivel_consumo_presencial,
    disponivel,
    id_restaurante,
    id_imagem
  )
VALUES
  (
    UNHEX (
      REPLACE('bce5f3c0-d4e4-4957-8f99-b95f12354d13', '-', '')
    ),
    'Rodízio de Carnes',
    'Diversos cortes nobres à vontade',
    75.00,
    TRUE,
    TRUE,
    UNHEX (
      REPLACE('21adec7d-d4f7-4999-a4dd-eaf0c242b3bd', '-', '')
    ),
    UNHEX (
      REPLACE('2de40ce0-49a2-48ce-87e3-64c3e4b1743f', '-', '')
    )
  ),
  (
    UNHEX (
      REPLACE('2e9a8fd4-4f7f-4bbf-bb37-9e7e4198f6b6', '-', '')
    ),
    'Picanha na Chapa',
    'Picanha fatiada servida com farofa e vinagrete',
    60.00,
    FALSE,
    TRUE,
    UNHEX (
      REPLACE('21adec7d-d4f7-4999-a4dd-eaf0c242b3bd', '-', '')
    ),
    UNHEX (
      REPLACE('6fd3276f-b93d-44c7-92b5-f37a1c0896c4', '-', '')
    )
  );

---- ITENS RESTAURANTE 5 ----
INSERT INTO
  item (
    id,
    nome,
    descricao,
    preco,
    disponivel_consumo_presencial,
    disponivel,
    id_restaurante,
    id_imagem
  )
VALUES
  (
    UNHEX (
      REPLACE('a03284df-e7e4-4e03-b9db-9d9f14495f8a', '-', '')
    ),
    'Prato Vegano do Dia',
    'Arroz integral, legumes salteados e tofu grelhado',
    35.00,
    FALSE,
    TRUE,
    UNHEX (
      REPLACE('9a7f3e70-8343-47e9-8fb0-2253cb03575f', '-', '')
    ),
    UNHEX (
      REPLACE('601ad0e7-9de1-4be4-a578-6a591e6cfdfb', '-', '')
    )
  ),
  (
    UNHEX (
      REPLACE('dd8b05d6-9c64-44ee-b3a1-4b421da129f4', '-', '')
    ),
    'Hambúrguer de Grão-de-Bico',
    'Servido com batatas rústicas e maionese vegana',
    32.00,
    FALSE,
    FALSE,
    UNHEX (
      REPLACE('9a7f3e70-8343-47e9-8fb0-2253cb03575f', '-', '')
    ),
    UNHEX (
      REPLACE('1792c2b1-b9e1-4649-bdd5-d39ce1705cb5', '-', '')
    )
  );