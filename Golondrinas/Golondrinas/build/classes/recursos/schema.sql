
PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS usuarios (
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre    TEXT NOT NULL,
    telefono  TEXT,
    usuario   TEXT NOT NULL UNIQUE,
    clave     TEXT NOT NULL,
    rol       TEXT NOT NULL CHECK (rol IN ('ADMIN', 'EMPLEADO'))
);

CREATE TABLE IF NOT EXISTS clientes (
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre    TEXT NOT NULL,
    telefono  TEXT,
    direccion TEXT
);

CREATE TABLE IF NOT EXISTS rutas (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    estacion_salida TEXT NOT NULL,
    destino         TEXT NOT NULL,
    precio_base     REAL NOT NULL CHECK (precio_base >= 0)
);

CREATE TABLE IF NOT EXISTS facturas (
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    id_cliente       INTEGER NOT NULL,
    id_ruta          INTEGER NOT NULL,
    id_usuario       INTEGER NOT NULL,
    tipo_asiento     TEXT NOT NULL CHECK (tipo_asiento IN ('NORMAL', 'VENTANILLA', 'VIP')),
    cantidad_asientos INTEGER NOT NULL CHECK (cantidad_asientos BETWEEN 1 AND 3),
    cantidad_equipaje INTEGER NOT NULL DEFAULT 0 CHECK (cantidad_equipaje >= 0),
    iva              REAL NOT NULL,
    fecha            TEXT NOT NULL,
    hora             TEXT NOT NULL,
    total            REAL NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id),
    FOREIGN KEY (id_ruta)    REFERENCES rutas(id),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

INSERT INTO usuarios (nombre, telefono, usuario, clave, rol)
SELECT 'Administrador', '', 'admin', 'admin123', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM usuarios);

INSERT INTO usuarios (nombre, telefono, usuario, clave, rol)
SELECT 'Empleado de prueba', '', 'empleado', 'emp123', 'EMPLEADO'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE usuario = 'empleado');

INSERT INTO rutas (estacion_salida, destino, precio_base)
SELECT 'Coatepeque', 'Guatemala', 120.00
WHERE NOT EXISTS (SELECT 1 FROM rutas);