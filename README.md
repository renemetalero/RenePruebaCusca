# api RenePruebaCusca
Repository for a RenePruebaCusca

Es una api para poder hacer ordenes de productos con los detalles y tambien para poder realizar el pago de dicha transaccion.

#NOTA IMPORTANTE DEBE DE CAMBIAR LA PALABRA USER_DATABASE POR EL DE SU BASE DE DATOS

# PETICIONES DESDE POSTMAN 
se realizan consumiendo el endpoint para poder tener el token 
# /api-prueba-cuscatlan/jwt/authenticate



# Url swaguer: 
http://localhost:8082/api-prueba-cuscatlan/swagger/swagger-ui/index.html#/


# SCRIPT CREACION DE LA BASE DE DATOS EN POSTGRESQL

-- Database: order_registration

-- DROP DATABASE IF EXISTS order_registration

CREATE DATABASE order_registration WITH OWNER = "USER_DATABASE" ENCODING = 'UTF8' LC_COLLATE = 'en_US.UTF-8' LC_CTYPE = 'en_US.UTF-8' TABLESPACE = pg_default CONNECTION LIMIT = -1 IS_TEMPLATE = False;

GRANT TEMPORARY, CONNECT ON DATABASE order_registration TO PUBLIC;

GRANT ALL ON DATABASE order_registration TO "USER_DATABASE";


# SCRIPT CREACION DE LA BASE DE SCHEMA
-- SCHEMA: sch_orders

-- DROP SCHEMA IF EXISTS sch_orders ;

CREATE SCHEMA IF NOT EXISTS sch_orders AUTHORIZATION "USER_DATABASE";

GRANT ALL ON SCHEMA sch_orders TO "USER_DATABASE";

ALTER DEFAULT PRIVILEGES FOR ROLE USER_DATABASE IN SCHEMA sch_orders GRANT INSERT, SELECT, UPDATE, DELETE, REFERENCES, TRIGGER ON TABLES TO "USER_DATABASE";


# SCRIPT CREACION DE LA TABLA ORDER EN EL SCHEMA SCH_ORDERS

-- Table: sch_orders.order

-- DROP TABLE IF EXISTS sch_orders."order";

CREATE TABLE IF NOT EXISTS sch_orders.order(
    id_order SERIAL PRIMARY KEY not null,
    creation_date timestamp without time zone,
    modification_date timestamp without time zone,
    customer varchar(250),
    order_status varchar(50),
    payment_method varchar(50),
    payment_status varchar(50),
    total numeric);
ALTER TABLE IF EXISTS sch_orders.order OWNER to "USER_DATABASE";

# SCRIPT CREACION DE LA TABLA ORDER_DETAILS EN EL SCHEMA SCH_ORDERS

-- Table: sch_orders_details

-- DROP TABLE IF EXISTS sch_orders.order_details;

CREATE TABLE IF NOT EXISTS sch_orders.order_details ( 
    id_order_detail SERIAL not null, 
    id_order INT, 
    id_product INT, 
    quantity INT, 
    price numeric, 
    subtotal numeric, 
    CONSTRAINT order_details_pk PRIMARY KEY (id_order_detail), 
    CONSTRAINT order_details_fk_1 FOREIGN KEY (id_order) 
    REFERENCES sch_orders.order (id_order) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION )
TABLESPACE pg_default;
ALTER TABLE IF EXISTS sch_orders.order_details OWNER to "USER_DATABASE";

# SCRIPT CREACION DE LA TABLA PAYMENT EN EL SCHEMA SCH_ORDERS
CREATE TABLE payment (
    id_payment serial not null,
    id_order INT UNIQUE,  -- Agregar la restricción UNIQUE aquí
    names VARCHAR(50),
    surnames VARCHAR(50),
    email VARCHAR(100),
    phone varchar(20),
    number_card varchar(30),
    CONSTRAINT payment_pk PRIMARY KEY (id_payment), 
    CONSTRAINT order_fk_1 FOREIGN KEY (id_order) 
    REFERENCES sch_orders.order (id_order) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);   
ALTER TABLE IF EXISTS sch_orders.payment OWNER to "USER_DATABASE";
-- Se actualiza la zona horaria de la base de datos
SET TIME ZONE 'America/El_Salvador';

# SCRIPT CREACION DE FUNCION Y TRIGUER PARA ACTUALIZAR EL CAMPO creation_date en la tabla order
-- Crear una función que crea la fecha con la zona horaria de El Salvador y el formato deseado
CREATE OR REPLACE FUNCTION insert_current_timestamp_order_date_order()
RETURNS TRIGGER AS $$
BEGIN
  NEW.creation_date = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER insert_order_date_order
BEFORE INSERT ON sch_orders.order
FOR EACH ROW
EXECUTE FUNCTION insert_current_timestamp_order_date_order();

# SCRIPT CREACION DE FUNCION Y TRIGUER PARA ACTUALIZAR EL CAMPO modification_date en la tabla order
-- Crear una función que actualiza la fecha con la zona horaria de El Salvador y el formato deseado
CREATE OR REPLACE FUNCTION update_current_timestamp_order_date_order()
RETURNS TRIGGER AS $$
BEGIN
  NEW.modification_date = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_order_date_order
BEFORE UPDATE ON sch_orders.order
FOR EACH ROW
EXECUTE FUNCTION update_current_timestamp_order_date_order();

# REALIZA LOS INSERT EN LAS TABLAS PARA TENER DATA EN LAS TRES TABLAS

INSERT INTO sch_orders."order" (id_order, creation_date, customer, order_status, payment_method, payment_status, total, modification_date) VALUES (8, '2025-11-13 21:50:00.354596', 'jose', NULL, NULL, NULL, 0, NULL);
INSERT INTO sch_orders."order" (id_order, creation_date, customer, order_status, payment_method, payment_status, total, modification_date) VALUES (9, '2025-11-13 21:58:26', 'cesar', NULL, NULL, NULL, 0, NULL);
INSERT INTO sch_orders."order" (id_order, creation_date, customer, order_status, payment_method, payment_status, total, modification_date) VALUES (10, '2025-11-13 21:59:10', 'cesar', NULL, NULL, NULL, 0, NULL);
INSERT INTO sch_orders."order" (id_order, creation_date, customer, order_status, payment_method, payment_status, total, modification_date) VALUES (11, '2025-11-13 21:59:51.976934', 'rene', NULL, NULL, NULL, 0, NULL);
INSERT INTO sch_orders."order" (id_order, creation_date, customer, order_status, payment_method, payment_status, total, modification_date) VALUES (12, '2025-11-13 22:04:35.455241', 'rene', NULL, NULL, NULL, 0, NULL);
INSERT INTO sch_orders."order" (id_order, creation_date, customer, order_status, payment_method, payment_status, total, modification_date) VALUES (13, '2025-11-13 23:20:47.943637', 'rene', NULL, NULL, NULL, 0, NULL);
INSERT INTO sch_orders."order" (id_order, creation_date, customer, order_status, payment_method, payment_status, total, modification_date) VALUES (17, '2025-11-13 00:22:58.666868', 'rene', NULL, NULL, NULL, NULL, NULL);
INSERT INTO sch_orders."order" (id_order, creation_date, customer, order_status, payment_method, payment_status, total, modification_date) VALUES (20, '2025-11-13 00:30:34.676096', 'rene', 'P', 'TC', 'P', 12.32, NULL);
INSERT INTO sch_orders."order" (id_order, creation_date, customer, order_status, payment_method, payment_status, total, modification_date) VALUES (21, '2025-11-13 00:34:27.256009', 'rene', 'P', 'TC', 'P', 0, NULL);
INSERT INTO sch_orders."order" (id_order, creation_date, customer, order_status, payment_method, payment_status, total, modification_date) VALUES (22, '2025-11-13 00:36:10.03303', 'rene', 'P', 'TC', 'P', 0, NULL);
INSERT INTO sch_orders."order" (id_order, creation_date, customer, order_status, payment_method, payment_status, total, modification_date) VALUES (23, '2025-11-13 00:38:05.627589', 'rene', 'P', 'TC', 'P', 286.80, NULL);
INSERT INTO sch_orders."order" (id_order, creation_date, customer, order_status, payment_method, payment_status, total, modification_date) VALUES (25, '2025-11-13 01:32:57.654161', 'rene', 'P', 'TC', 'P', 286.80, NULL);
INSERT INTO sch_orders."order" (id_order, creation_date, customer, order_status, payment_method, payment_status, total, modification_date) VALUES (24, NULL, 'renec', 'P', 'TC', 'P', 455, '2025-11-13 01:53:22.225361');
INSERT INTO sch_orders."order" (id_order, creation_date, customer, order_status, payment_method, payment_status, total, modification_date) VALUES (26, NULL, 'renec', 'P', 'TC', 'P', 455, '2025-11-13 01:54:11.177518');
INSERT INTO sch_orders."order" (id_order, creation_date, customer, order_status, payment_method, payment_status, total, modification_date) VALUES (27, NULL, 'renet', 'P', 'TC', 'P', 455, '2025-11-13 01:57:31.984166');
INSERT INTO sch_orders."order" (id_order, creation_date, customer, order_status, payment_method, payment_status, total, modification_date) VALUES (29, '2025-11-13 11:57:40.364154', 'rene u', 'C', 'TC', 'P', 286.80, NULL);
INSERT INTO sch_orders."order" (id_order, creation_date, customer, order_status, payment_method, payment_status, total, modification_date) VALUES (30, '2025-11-13 12:00:08.372498', 'rene u', 'C', 'TC', 'P', 286.80, NULL);
INSERT INTO sch_orders."order" (id_order, creation_date, customer, order_status, payment_method, payment_status, total, modification_date) VALUES (31, '2025-11-13 16:37:22.035284', 'rene u', 'C', 'TC', 'P', 286.80, NULL);


INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (8, 8, 1, 2, 109.95, 0);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (9, 9, 1, 2, 109.95, 0);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (10, 10, 1, 2, 109.95, 0);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (11, 11, 1, 2, 109.95, 0);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (12, 12, 1, 2, 109.95, 0);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (13, 12, 2, 2, 22.3, 0);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (14, 13, 1, 2, 109.95, 0);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (15, 13, 2, 2, 22.3, 0);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (16, 20, 0, 2, 109.95, 219.90);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (17, 20, 0, 3, 22.3, 66.9);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (18, 21, 0, 2, 109.95, 219.90);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (19, 21, 0, 3, 22.3, 66.9);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (20, 22, 0, 2, 109.95, 219.90);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (21, 22, 0, 3, 22.3, 66.9);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (22, 23, 0, 2, 109.95, 219.90);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (23, 23, 0, 3, 22.3, 66.9);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (24, 24, 0, 2, 109.95, 219.90);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (25, 24, 0, 3, 22.3, 66.9);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (26, 24, 0, 2, 64, 128);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (27, 24, 0, 3, 109, 327);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (28, 25, 0, 2, 109.95, 219.90);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (29, 25, 0, 3, 22.3, 66.9);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (30, 26, 0, 2, 64, 128);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (31, 26, 0, 3, 109, 327);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (32, 27, 0, 2, 64, 128);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (33, 27, 0, 3, 109, 327);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (36, 29, 0, 2, 109.95, 219.90);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (37, 29, 0, 3, 22.3, 66.9);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (38, 30, 0, 2, 109.95, 219.90);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (39, 30, 0, 3, 22.3, 66.9);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (40, 31, 0, 2, 109.95, 219.90);
INSERT INTO sch_orders.order_details (id_order_detail, id_order, id_product, quantity, price, subtotal) VALUES (41, 31, 0, 3, 22.3, 66.9);


INSERT INTO sch_orders.payment (id_payment, id_order, names, surnames, email, phone, number_card) VALUES (1, 29, 'jose', 'hernandez', 'jose@gmail.com', '+50371512658', '***********-7849');
