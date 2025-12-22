-- Usa IDs altos o deja que la base de datos decida si usas secuencias
INSERT INTO roles (id, nombre_rol) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (id, nombre_rol) VALUES (2, 'ROLE_JUGADOR');

-- Asegúrate de que los IDs de usuario también sean únicos
INSERT INTO usuario (id, nombre_usuario, password, nombre, email, genero) 
VALUES (1, 'admin', '$2a$10$86YIisXvG7uY.80M4UuGduuU1SlI.Kj.uQonmXAn2Oly9A9FpC6Yy', 'Admin', 'admin@test.com', 'MASCULINO');

INSERT INTO usuario (id, nombre_usuario, password, nombre, email, genero) 
VALUES (2, 'jugador1', '$2a$10$86YIisXvG7uY.80M4UuGduuU1SlI.Kj.uQonmXAn2Oly9A9FpC6Yy', 'Jugador', 'jugador@test.com', 'MASCULINO');

-- Vincula usando los IDs exactos
INSERT INTO usuario_rol (usuario_id, rol_id) VALUES (1, 1);
INSERT INTO usuario_rol (usuario_id, rol_id) VALUES (2, 2);