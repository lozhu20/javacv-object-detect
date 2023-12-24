create database db_carryyou;

drop table if exists public.tb_user;

create table public.tb_user
(
    id           varchar(36)  not null default gen_random_uuid() primary key,
    user_id      varchar(50)  not null,
    username     varchar(100) not null,
    password     varchar(50)  not null,
    status       char(2)      not null,
    role_id      varchar(20)  not null,
    created_time timestamp             default current_timestamp,
    created_by   varchar(50)           default 'anonymous',
    updated_time timestamp             default current_timestamp,
    updated_by   varchar(50)           default 'anonymous'
);
comment on table public.tb_user is '用户信息表';
comment on column public.tb_user.id is '主键id';
comment on column public.tb_user.user_id is '用户id';
comment on column public.tb_user.username is '用户名';
comment on column public.tb_user.password is '登陆密码';
comment on column public.tb_user.status is '用户状态(01:正常,99:注销)';



drop table if exists public.tb_role;

create table public.tb_role
(
    id           varchar(36)  not null default gen_random_uuid() primary key,
    role_id      varchar(50)  not null,
    role_name    varchar(100) not null,
    created_time timestamp             default current_timestamp,
    created_by   varchar(50)           default 'anonymous',
    updated_time timestamp             default current_timestamp,
    updated_by   varchar(50)           default 'anonymous'
);
comment on table public.tb_role is '角色信息表';
comment on column public.tb_role.id is '主键id';
comment on column public.tb_role.role_id is '角色id';
comment on column public.tb_role.role_name is '角色名称';



drop table if exists public.tb_image;

create table public.tb_image
(
    id           varchar(36)  not null default gen_random_uuid() primary key,
    image_name   varchar(200) not null,
    size         int          not null,
    save_path    varchar(200) not null,
    use_as       varchar(2)   not null,
    created_time timestamp             default current_timestamp,
    created_by   varchar(50)           default 'anonymous',
    updated_time timestamp             default current_timestamp,
    updated_by   varchar(50)           default 'anonymous'
);
comment on table public.tb_image is '图片信息表';
comment on column public.tb_image.id is '主键id';
comment on column public.tb_image.image_name is '图片名称';
comment on column public.tb_image.size is '图片大小';
comment on column public.tb_image.save_path is '存储路径';
comment on column public.tb_image.use_as is '用途(01:品种检测,02:成熟度检测,03:不支持的类型)';
