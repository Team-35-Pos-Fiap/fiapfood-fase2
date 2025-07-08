---- PERFIS ----
insert into perfil (nome) values ('Dono');
insert into perfil (nome) values ('Cliente');

---- USUARIO 1 ----
insert into endereco (id, cidade, bairro, estado, endereco, numero, complemento, cep)
values (RANDOM_UUID(), 'São Gonçalo', 'Centro', 'Rio de Janeiro', 'Rua 1', '10', 'Casa 25', '24455486');

insert into login (id, matricula, senha) values (RANDOM_UUID(), 'us0001', '123');

insert into usuario (id, nome, email, ativo, data_criacao, data_atualizacao, id_perfil, id_endereco, id_login)
values (RANDOM_UUID(), 'João Motta', 'joao@fiapfood.com', 1, current_timestamp, null, 1,
    (SELECT id FROM endereco WHERE cidade = 'São Gonçalo'),
    (SELECT id FROM login WHERE matricula = 'us0001'));

---- USUARIO 2 ----
insert into endereco (id, cidade, bairro, estado, endereco, numero, complemento, cep)
values (RANDOM_UUID(), 'Rio de Janeiro', 'Copacabana', 'RJ', 'Avenida Atlântica', '1500', 'Apto 302', '22021001');

insert into login (id, matricula, senha) values (RANDOM_UUID(), 'us0002', '123');

insert into usuario (id, nome, email, ativo, data_criacao, data_atualizacao, id_perfil, id_endereco, id_login)
values (RANDOM_UUID(), 'Carla Rodrigues', 'carla.rodrigues@fiapfood.com', 1, current_timestamp, null, 2,
    (SELECT id FROM endereco WHERE cidade = 'Rio de Janeiro'),
    (SELECT id FROM login WHERE matricula = 'us0002'));

---- USUARIO 3 ----
insert into endereco (id, cidade, bairro, estado, endereco, numero, complemento, cep)
values (RANDOM_UUID(), 'São Paulo', 'Pinheiros', 'SP', 'Rua dos Pinheiros', '1340', 'Conjunto 25', '05422002');

insert into login (id, matricula, senha) values (RANDOM_UUID(), 'us0003', '123');

insert into usuario (id, nome, email, ativo, data_criacao, data_atualizacao, id_perfil, id_endereco, id_login)
values (RANDOM_UUID(), 'Rafael Santos', 'rafael.santos@fiapfood.com', 0, current_timestamp, current_timestamp, 1,
        (SELECT id FROM endereco WHERE cidade = 'São Paulo'),
        (SELECT id FROM login WHERE matricula = 'us0003'));

---- USUARIO 4 ----
insert into endereco (id, cidade, bairro, estado, endereco, numero, complemento, cep)
values (RANDOM_UUID(), 'Belo Horizonte', 'Savassi', 'MG', 'Rua Pernambuco', '1322', null, '30130151');

insert into login (id, matricula, senha) values (RANDOM_UUID(), 'us0004', '123');

insert into usuario (id, nome, email, ativo, data_criacao, data_atualizacao, id_perfil, id_endereco, id_login)
values (RANDOM_UUID(), 'Juliana Mendes', 'juliana.mendes@fiapfood.com', 0, current_timestamp, current_timestamp, 2,
        (SELECT id FROM endereco WHERE cidade = 'Belo Horizonte'),
        (SELECT id FROM login WHERE matricula = 'us0004'));

---- USUARIO 5 ----
insert into endereco (id, cidade, bairro, estado, endereco, numero, complemento, cep)
values (RANDOM_UUID(), 'Brasília', 'Asa Sul', 'DF', 'SQS 308', null, 'Bloco C Apto 303', '70355530');

insert into login (id, matricula, senha) values (RANDOM_UUID(), 'us0005', '123');

insert into usuario (id, nome, email, ativo, data_criacao, data_atualizacao, id_perfil, id_endereco, id_login)
values (RANDOM_UUID(), 'Marcelo Alves', 'marcelo.alves@fiapfood.com', 1, current_timestamp, null, 1,
        (SELECT id FROM endereco WHERE cidade = 'Brasília'),
        (SELECT id FROM login WHERE matricula = 'us0005'));

---- USUARIO 6 ----
insert into endereco (id, cidade, bairro, estado, endereco, numero, complemento, cep)
values (RANDOM_UUID(), 'Curitiba', 'Batel', 'PR', 'Alameda Dr. Carlos de Carvalho', '555', 'Sala 1201', '80430180');

insert into login (id, matricula, senha) values (RANDOM_UUID(), 'us0006', '123');

insert into usuario (id, nome, email, ativo, data_criacao, data_atualizacao, id_perfil, id_endereco, id_login)
values (RANDOM_UUID(), 'Amanda Costa', 'amanda.costa@fiapfood.com', 1, current_timestamp, null, 1,
        (SELECT id FROM endereco WHERE cidade = 'Curitiba'),
        (SELECT id FROM login WHERE matricula = 'us0006'));

---- USUARIO 7 ----
insert into endereco (id, cidade, bairro, estado, endereco, numero, complemento, cep)
values (RANDOM_UUID(), 'Salvador', 'Barra', 'BA', 'Avenida Oceânica', '2135', null, '40140130');

insert into login (id, matricula, senha) values (RANDOM_UUID(), 'us0007', '123');

insert into usuario (id, nome, email, ativo, data_criacao, data_atualizacao, id_perfil, id_endereco, id_login)
values (RANDOM_UUID(), 'Bruno Oliveira', 'bruno.oliveira@fiapfood.com', 0, current_timestamp, current_timestamp, 1,
        (SELECT id FROM endereco WHERE cidade = 'Salvador'),
        (SELECT id FROM login WHERE matricula = 'us0007'));

---- USUARIO 8 ----
insert into endereco (id, cidade, bairro, estado, endereco, numero, complemento, cep)
values (RANDOM_UUID(), 'Recife', 'Boa Viagem', 'PE', 'Avenida Boa Viagem', '3320', 'Apto 1802', '51030300');

insert into login (id, matricula, senha) values (RANDOM_UUID(), 'us0008', '123');

insert into usuario (id, nome, email, ativo, data_criacao, data_atualizacao, id_perfil, id_endereco, id_login)
values (RANDOM_UUID(), 'Patricia Lima', 'patricia.lima@fiapfood.com', 0, current_timestamp, current_timestamp, 2,
        (SELECT id FROM endereco WHERE cidade = 'Recife'),
        (SELECT id FROM login WHERE matricula = 'us0008'));

---- USUARIO 9 ----
insert into endereco (id, cidade, bairro, estado, endereco, numero, complemento, cep)
values (RANDOM_UUID(), 'Fortaleza', 'Meireles', 'CE', 'Avenida Beira Mar', '850', null, '60165121');

insert into login (id, matricula, senha) values (RANDOM_UUID(), 'us0009', '123');

insert into usuario (id, nome, email, ativo, data_criacao, data_atualizacao, id_perfil, id_endereco, id_login)
values (RANDOM_UUID(), 'Fernando Gomes', 'fernando.gomes@fiapfood.com', 1, current_timestamp, null, 2,
        (SELECT id FROM endereco WHERE cidade = 'Fortaleza'),
        (SELECT id FROM login WHERE matricula = 'us0009'));

---- USUARIO 10 ----
insert into endereco (id, cidade, bairro, estado, endereco, numero, complemento, cep)
values (RANDOM_UUID(), 'Porto Alegre', 'Moinhos de Vento', 'RS', 'Rua Padre Chagas', '342', 'Casa', '90570080');

insert into login (id, matricula, senha) values (RANDOM_UUID(), 'us0010', '123');

insert into usuario (id, nome, email, ativo, data_criacao, data_atualizacao, id_perfil, id_endereco, id_login)
values (RANDOM_UUID(), 'Daniela Pereira', 'daniela.pereira@fiapfood.com', 1, current_timestamp, null, 2,
        (SELECT id FROM endereco WHERE cidade = 'Porto Alegre'),
        (SELECT id FROM login WHERE matricula = 'us0010'));

---- USUARIO 11 ----
insert into endereco (id, cidade, bairro, estado, endereco, numero, complemento, cep)
values (RANDOM_UUID(), 'Manaus', 'Adrianópolis', 'AM', 'Avenida André Araújo', null, 'Condomínio Tulipas', '69057025');

insert into login (id, matricula, senha) values (RANDOM_UUID(), 'us0011', '123');

insert into usuario (id, nome, email, ativo, data_criacao, data_atualizacao, id_perfil, id_endereco, id_login)
values (RANDOM_UUID(), 'Ricardo Souza', 'ricardo.souza@fiapfood.com', 0, current_timestamp, current_timestamp, 2,
        (SELECT id FROM endereco WHERE cidade = 'Manaus'),
        (SELECT id FROM login WHERE matricula = 'us0011'));

---- USUARIO 12 ----
insert into endereco (id, cidade, bairro, estado, endereco, numero, complemento, cep)
values (RANDOM_UUID(), 'Florianópolis', 'Jurerê Internacional', 'SC', 'Avenida dos Búzios', '1780', 'Casa 15', '88053300');

insert into login (id, matricula, senha) values (RANDOM_UUID(), 'us0012', '123');

insert into usuario (id, nome, email, ativo, data_criacao, data_atualizacao, id_perfil, id_endereco, id_login)
values (RANDOM_UUID(), 'Luciana Ferreira', 'luciana.ferreira@fiapfood.com', 0, current_timestamp, current_timestamp, 2,
        (SELECT id FROM endereco WHERE cidade = 'Florianópolis'),
        (SELECT id FROM login WHERE matricula = 'us0012'));