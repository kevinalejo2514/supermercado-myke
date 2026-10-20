-- ============================================
-- Script SQL para la base de datos del supermercado
-- Compatible con MySQL 8.x
-- ============================================

CREATE DATABASE IF NOT EXISTS supermercado;
USE supermercado;

-- Tabla: categorias
CREATE TABLE categorias (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    PRIMARY KEY (id)
);

-- Tabla: proveedores
CREATE TABLE proveedores (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(30),
    correo VARCHAR(100),
    PRIMARY KEY (id)
);

-- Tabla: productos
CREATE TABLE productos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(120) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    codigo_barras VARCHAR(50) NOT NULL,
    categoria_id BIGINT,
    proveedor_id BIGINT,
    PRIMARY KEY (id),
    UNIQUE (codigo_barras),
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id),
    CONSTRAINT fk_producto_proveedor FOREIGN KEY (proveedor_id) REFERENCES proveedores(id)
);

-- Tabla: clientes
CREATE TABLE clientes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    documento VARCHAR(30) NOT NULL,
    telefono VARCHAR(30),
    PRIMARY KEY (id),
    UNIQUE (documento)
);

-- Tabla: ventas
CREATE TABLE ventas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    fecha DATETIME NOT NULL,
    total DECIMAL(12,2) NOT NULL,
    cliente_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_venta_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

-- Tabla: detalles_venta
CREATE TABLE detalles_venta (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    venta_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_detalle_venta FOREIGN KEY (venta_id) REFERENCES ventas(id),
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);

-- ============================================
-- Datos de ejemplo para probar en Postman
-- ============================================

INSERT INTO categorias (nombre, descripcion) VALUES
('Lacteos', 'Productos derivados de la leche'),
('Bebidas', 'Refrescos, jugos y aguas'),
('Limpieza', 'Productos de aseo del hogar');

INSERT INTO proveedores (nombre, telefono, correo) VALUES
('Lacteos S.A.', '3001112223', 'contacto@lacteos.com'),
('Bebidas del Valle', '3004445556', 'ventas@bebidasdelvalle.com'),
('Limpieza Total', '3007778889', 'pedidos@limpiezatotal.com');

INSERT INTO productos (nombre, precio, stock, codigo_barras, categoria_id, proveedor_id) VALUES
('Leche entera 1L', 2500.00, 50, '7701234567890', 1, 1),
('Yogur natural 500g', 1800.00, 30, '7701234567891', 1, 1),
('Coca Cola 2L', 3500.00, 40, '7701234567892', 2, 2),
('Agua mineral 500ml', 1500.00, 100, '7701234567893', 2, 2),
('Detergente líquido 1L', 4500.00, 25, '7701234567894', 3, 3),
('Jabón en polvo 1kg', 5500.00, 20, '7701234567895', 3, 3);

INSERT INTO clientes (nombre, documento, telefono) VALUES
('Juan Pérez', '12345678', '3001111111'),
('María Gómez', '87654321', '3002222222'),
('Carlos Ruiz', '11223344', '3003333333');
