INSERT INTO cliente(NOME) VALUES ('CARLOS');
INSERT INTO cliente(NOME) VALUES ('MARIA');

INSERT INTO usuario(login,senha) VALUES ('cadu','$2a$10$C4K5YPE7F7gDiOa78KHF0OPi1ryZLVny13JKV6XIh1TkxnpPCA9g2');
INSERT INTO usuario(login,senha) VALUES ('joao','$2a$10$A03jUeMTk0LvTvN4KPnKBeK5IqFZR5e78cWNCEEUN8bBQ//ud.XVy');

INSERT INTO permissao(id,nome) VALUES (1,'CLIENTE_READ');
INSERT INTO permissao(id,nome) VALUES (2,'CLIENTE_WRITE');

INSERT INTO role(id,nome) VALUES (1,'ADMIN');
INSERT INTO role(id,nome) VALUES (2,'USER');

INSERT INTO role_permissao(role_id,permissao_id) VALUES (1,1);
INSERT INTO role_permissao(role_id,permissao_id) VALUES (1,2);
INSERT INTO role_permissao(role_id,permissao_id) VALUES (2,1);

INSERT INTO usuario_role(usuario_login,role_id) VALUES ('cadu',1);
INSERT INTO usuario_role(usuario_login,role_id) VALUES ('joao',2);
