set foreign_key_checks = 0;

drop table if exists endereco;
drop table if exists restaurante;
drop table if exists login;
drop table if exists usuario;
drop table if exists tipo_culinaria;
drop table if exists atendimento;
drop table if exists item;
drop table if exists imagem;
drop table if exists perfil;

set foreign_key_checks = 1;

create table perfil (
	id int not null auto_increment,
	nome varchar(50) not null,
	data_criacao date not null,
	data_inativacao date null,
	primary key(id)
);

create table endereco (
    id binary(16) not null,
    cidade varchar(100) not null,
    bairro varchar(100) not null,
    estado varchar(100) not null,
    endereco varchar(150) not null,
    numero int null,
    complemento varchar(150) null,
    cep varchar(15) not null,
    primary key(id)
);

create table login (
    id binary(16) not null,
    matricula varchar(6) not null,
    senha varchar(10) not null,
    primary key(id)
);

create table usuario (
    id binary(16) not null,
	nome varchar(150) not null,
	email varchar(70) not null,
	ativo tinyint(1) not null default 1,
	data_criacao datetime not null,
	data_atualizacao datetime null,
	id_perfil int not null,
    id_endereco binary(16) not null,
    id_login binary(16) not null,
	primary key(id),
    unique key UK_endereco (id_endereco),
    unique key UK_login (id_login),
    unique key UK_email (email),
    constraint FK_perfil foreign key (id_perfil) references perfil (id),
    constraint FK_login foreign key (id_login) references login (id),
    constraint FK_endereco_usuario foreign key (id_endereco) references endereco (id)
);

create table tipo_culinaria (
	id int not null auto_increment,
	nome varchar(50) not null,
	primary key(id)
);

create table restaurante (
    id binary(16) not null,
    nome varchar(255) not null,
	ativo tinyint(1) not null default 1,
    id_endereco binary(16) not null,
    id_usuario binary(16) not null,
    id_tipo_culinaria int not null,
    primary key (id),
	constraint FK_endereco_restaurante  foreign key(id_endereco) references endereco(id),
    constraint FK_usuario foreign key(id_usuario) references usuario(id),
    constraint FK_tipo_culinaria foreign key(id_tipo_culinaria) references tipo_culinaria(id)
);

create table atendimento (
	id_atendimento binary(16) not null,
	dia enum('SEGUNDA_FEIRA', 'TERÇA_FEIRA', 'QUARTA_FEIRA', 'QUINTA_FEIRA', 'SEXTA_FEIRA', 'SÁBADO', 'DOMINGO'),
	inicio_atendimento time not null,
	termino_atendimento time not null,
	id_restaurante binary(16) not null,	  
	primary key (id_atendimento),
	constraint FK_atendimento_restaurante foreign key(id_restaurante) references restaurante(id)
);

create table imagem (
	id binary(16) not null,
	nome varchar(50) not null,
	tipo varchar(20) not null,
	conteudo blob not null,
	primary key(id)
);

create table item (
	id binary(16) not null,
	nome varchar(255) not null,
    descricao varchar(255) null,
 	preco decimal(9, 2) not null,
 	disponivel_consumo_presencial tinyint(1) not null default 0,
	disponivel tinyint(1) not null default 1,
	id_imagem binary(16) not null,
	id_restaurante binary(16) not null,	  
	primary key (id),
    constraint FK_imagem foreign key(id_imagem) references imagem(id),
	constraint FK_atendimento_item foreign key(id_restaurante) references restaurante(id)
);