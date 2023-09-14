create table revinfo
(
    rev      integer not null
        primary key,
    revtstmp bigint
);

alter table revinfo
    owner to postgres;

create sequence revinfo_seq
    increment by 50;

alter sequence revinfo_seq owner to postgres;

create table users_aud
(
    id                    integer not null,
    rev                   integer not null
        constraint fkc4vk4tui2la36415jpgm9leoq
            references revinfo,
    revtype               smallint,
    uuid                  uuid,
    activated             boolean,
    activation_key        varchar(20),
    activity_service      varchar(255),
    activity_zone         varchar(255),
    birthday              date,
    email                 varchar(50),
    failed_login_attempts integer,
    first_name            varchar(50),
    is_account_non_locked boolean,
    last_name             varchar(50),
    password              varchar(255),
    phone                 varchar(255),
    post                  varchar(50),
    reset_password_key    varchar(255),
    type_contract         varchar(255)
        constraint users_aud_type_contract_check
            check ((type_contract)::text = ANY
                   ((ARRAY ['CDI'::character varying, 'CDD'::character varying, 'PROFESSIONNALISATION'::character varying, 'APPRENTISSAGE'::character varying, 'TEMPS_PARTIEL'::character varying, 'CHANTIER'::character varying, 'PARTAGE'::character varying, 'INTERMITTENT'::character varying, 'INTERIM'::character varying, 'ANNUALISE'::character varying])::text[])),
    username              varchar(50),
    manager_id            integer,
    organization_id       integer,
    primary key (rev, id)
);

alter table users_aud
    owner to postgres;

create table organization_aud
(
    id                     integer not null,
    rev                    integer not null
        constraint fkjc7pc3ombplinmhjykfmu9ckg
            references revinfo,
    revtype                smallint,
    uuid                   uuid,
    activity               varchar(255),
    business_creation_date date,
    business_name          varchar(255),
    capital                bigint,
    director_name          varchar(255),
    is_principal           boolean default false,
    rib                    varchar(34),
    siren                  varchar(9),
    primary key (rev, id)
);

alter table organization_aud
    owner to postgres;

