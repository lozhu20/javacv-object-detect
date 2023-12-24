insert into public.tb_user(user_id, username, password, status, role_id)
values ('U0001', '用户1', '1234561', '01', 'ROLE_ADMIN'),
       ('U0002', '用户2', '1234562', '01', 'ROLE_ADMIN'),
       ('U0003', '用户3', '1234563', '01', 'ROLE_USER'),
       ('U0004', '用户4', '1234564', '01', 'ROLE_USER'),
       ('U0005', '用户5', '1234565', '99', 'ROLE_USER');


insert into public.tb_role(role_id, role_name)
values ('ROLE_ADMIN', '管理员'),
       ('ROLE_USER', '普通用户');

