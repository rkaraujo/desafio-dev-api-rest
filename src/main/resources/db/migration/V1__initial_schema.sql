create table if not exists pessoa (
	id_pessoa serial,
	nome varchar(255) not null,
	cpf varchar(30) not null,
	data_nascimento date not null,
	primary key( id_pessoa )
);

create table if not exists conta (
	id_conta serial,
	id_pessoa int not null,
	saldo numeric(20,2) not null,
	limite_saque_diario numeric(10,2) not null,
	flag_ativo boolean not null,
	tipo_conta smallint not null,
	data_criacao timestamp not null,
	primary key( id_conta ),
	constraint fk_conta_pessoa
      foreign key(id_pessoa)
	  references pessoa(id_pessoa)
);
create index conta_id_pessoa on conta (id_pessoa);

create table if not exists transacao (
    id_transacao bigserial,
    id_conta int not null,
    valor numeric(10,2) not null,
    data_transacao timestamp not null,
	primary key( id_transacao ),
	constraint fk_transacao_conta
      foreign key(id_conta)
	  references conta(id_conta)
);
create index transacao_id_conta on transacao (id_conta);

