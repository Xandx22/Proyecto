-- zoo_db.sql (completo con inserts)
CREATE DATABASE IF NOT EXISTS `zoo_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `zoo_db`;

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  correo VARCHAR(100) NOT NULL UNIQUE,
  contrasena VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

-- Usuario admin (correo: admin@zoo.com  contraseña: admin123)
INSERT INTO usuarios (nombre, correo, contrasena) VALUES
('Admin', 'admin@zoo.com', '$2y$10$M0pW7XhOPoU6A5BnWUBimOw7.6IEi79Kxzj7D1odRXoYz6YAVeHcW');

-- Entidades fuertes
CREATE TABLE IF NOT EXISTS Habitat (
  id_habitat INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL UNIQUE,
  tipo ENUM('Selva','Sabana','Acuático','Desértico') NOT NULL,
  capacidad_max INT DEFAULT 0,
  ubicacion VARCHAR(150)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Cuidador (
  id_cuidador INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  edad INT,
  especialidad ENUM('Mamíferos','Aves','Reptiles','Anfibios') NOT NULL,
  contacto VARCHAR(50) UNIQUE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Alimento (
  id_alimento INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  tipo ENUM('Carne','Frutas','Vegetales','Otros') NOT NULL,
  cantidad_disponible INT DEFAULT 0,
  fecha_caducidad DATE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Animal (
  id_animal INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  especie VARCHAR(100) NOT NULL,
  edad INT,
  sexo ENUM('M','F') NOT NULL,
  fecha_llegada DATE NOT NULL,
  estado_salud VARCHAR(200),
  id_habitat INT NOT NULL,
  FOREIGN KEY (id_habitat) REFERENCES Habitat(id_habitat) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Subtipos (ISA)
CREATE TABLE IF NOT EXISTS Animal_Permanente (
  id_animal INT PRIMARY KEY,
  tiempo_residencia INT,
  historial_medico TEXT,
  FOREIGN KEY (id_animal) REFERENCES Animal(id_animal) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Animal_Temporal (
  id_animal INT PRIMARY KEY,
  zoologico_origen VARCHAR(150),
  fecha_retorno DATE,
  FOREIGN KEY (id_animal) REFERENCES Animal(id_animal) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Animal_Mamifero (
  id_animal INT PRIMARY KEY,
  gestacion INT,
  crias_promedio INT,
  FOREIGN KEY (id_animal) REFERENCES Animal(id_animal) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Animal_Ave (
  id_animal INT PRIMARY KEY,
  envergadura DECIMAL(5,2),
  vuelo BOOLEAN,
  FOREIGN KEY (id_animal) REFERENCES Animal(id_animal) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Animal_Reptil (
  id_animal INT PRIMARY KEY,
  venenoso BOOLEAN,
  FOREIGN KEY (id_animal) REFERENCES Animal(id_animal) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Animal_Anfibio (
  id_animal INT PRIMARY KEY,
  habitat_natal VARCHAR(100),
  FOREIGN KEY (id_animal) REFERENCES Animal(id_animal) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Relaciones
CREATE TABLE IF NOT EXISTS Supervisa (
  id_cuidador INT NOT NULL,
  id_habitat INT NOT NULL,
  PRIMARY KEY (id_cuidador, id_habitat),
  FOREIGN KEY (id_cuidador) REFERENCES Cuidador(id_cuidador) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (id_habitat) REFERENCES Habitat(id_habitat) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS RegistroAlimentacion (
  id_animal INT NOT NULL,
  id_alimento INT NOT NULL,
  fecha DATE NOT NULL,
  cantidad INT NOT NULL,
  id_cuidador INT NOT NULL,
  PRIMARY KEY (id_animal, id_alimento, fecha),
  FOREIGN KEY (id_animal) REFERENCES Animal(id_animal) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (id_alimento) REFERENCES Alimento(id_alimento) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (id_cuidador) REFERENCES Cuidador(id_cuidador) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Cria (
  id_cria INT NOT NULL,
  id_padre INT NOT NULL,
  id_madre INT NOT NULL,
  fecha_nacimiento DATE NOT NULL,
  PRIMARY KEY (id_cria, id_padre, id_madre),
  FOREIGN KEY (id_padre) REFERENCES Animal(id_animal) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (id_madre) REFERENCES Animal(id_animal) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Inserts ejemplo para poblar tablas
INSERT INTO Habitat (nombre, tipo, capacidad_max, ubicacion) VALUES
('Bosque Tropical', 'Selva', 50, 'Sector Sur'),
('Lago Central', 'Acuático', 20, 'Sector Norte'),
('Sabana Alta', 'Sabana', 40, 'Sector Este'),
('Dunas Secas', 'Desértico', 25, 'Sector Oeste'),
('Valle Humedo', 'Selva', 30, 'Sector Sur Este');

INSERT INTO Cuidador (nombre, edad, especialidad, contacto) VALUES
('Luis Pérez', 35, 'Mamíferos', 'luis@zoo.com'),
('María Gómez', 28, 'Aves', 'maria@zoo.com'),
('José Hernández', 42, 'Reptiles', 'jose@zoo.com'),
('Ana Torres', 31, 'Anfibios', 'ana@zoo.com'),
('Miguel Ruiz', 29, 'Mamíferos', 'miguel@zoo.com'),
('Sofía Martínez', 26, 'Aves', 'sofia@zoo.com');

INSERT INTO Alimento (nombre, tipo, cantidad_disponible, fecha_caducidad) VALUES
('Carne vacuna', 'Carne', 200, '2026-01-15'),
('Fruta mixta', 'Frutas', 150, '2025-12-01'),
('Verduras frescas', 'Vegetales', 180, '2025-11-20'),
('Pienso balanceado', 'Otros', 300, '2027-05-01'),
('Pescado congelado', 'Carne', 120, '2026-03-01');

-- Primeros animales (asegúrate que los habitats con esos ids existen según inserts anteriores)
INSERT INTO Animal (nombre, especie, edad, sexo, fecha_llegada, estado_salud, id_habitat) VALUES
('Tigre A', 'Panthera tigris', 5, 'M', '2022-06-15', 'Bueno', 1),
('Elefante B', 'Loxodonta africana', 12, 'F', '2019-03-10', 'Bueno', 3),
('Cocodrilo C', 'Crocodylus niloticus', 7, 'M', '2021-08-20', 'Recuperándose', 2),
('Loro D', 'Ara macao', 3, 'F', '2023-05-12', 'Excelente', 2),
('Rana E', 'Lithobates catesbeianus', 1, 'F', '2024-01-05', 'Bueno', 5),
('Serpiente F', 'Bothrops asper', 4, 'M', '2020-11-11', 'Controlada', 4),
('Jirafa G', 'Giraffa camelopardalis', 6, 'F', '2021-04-04', 'Bueno', 3),
('Mono H', 'Cebus capucinus', 2, 'M', '2023-09-09', 'Bueno', 1);

-- Subtipos de algunos animales
INSERT INTO Animal_Mamifero (id_animal, gestacion, crias_promedio) VALUES
(1, 104, 2),
(2, 660, 1),
(7, 450, 1),
(8, 165, 1);

INSERT INTO Animal_Ave (id_animal, envergadura, vuelo) VALUES
(4, 1.20, 1);

INSERT INTO Animal_Reptil (id_animal, venenoso) VALUES
(6, 1),
(3, 0);

INSERT INTO Animal_Anfibio (id_animal, habitat_natal) VALUES
(5, 'Charcos y humedales');

-- Relaciones supervisa
INSERT INTO Supervisa (id_cuidador, id_habitat) VALUES
(1,1),(2,2),(3,2),(4,5),(5,3),(6,2);

-- Registro de alimentacion (ejemplo)
INSERT INTO RegistroAlimentacion (id_animal, id_alimento, fecha, cantidad, id_cuidador) VALUES
(1,1,'2025-09-01',5,1),
(2,4,'2025-09-02',50,5),
(4,2,'2025-09-03',2,2),
(3,5,'2025-09-02',4,3);

-- Crías (ejemplo)
INSERT INTO Cria (id_cria, id_padre, id_madre, fecha_nacimiento) VALUES
(9,1,2,'2024-02-10'),
(10,8,7,'2025-03-15');
