drop database if exists reto4grupo5_m;
create database reto4grupo5_m;
use reto4grupo5_m;

CREATE TABLE Musico (
    IDMusico INT AUTO_INCREMENT PRIMARY KEY,
    NombreArtistico VARCHAR(255) UNIQUE NOT NULL,
    Imagen VARCHAR(255) NOT NULL,
    Descripcion TEXT NOT NULL,
    Caracteristica ENUM('solista', 'grupo') NOT NULL
);

CREATE TABLE Podcaster (
    IDPodcaster INT AUTO_INCREMENT PRIMARY KEY,
    NombreArtistico VARCHAR(255) UNIQUE NOT NULL,
    Imagen VARCHAR(255) NOT NULL,
    Genero VARCHAR(255),
    Descripcion TEXT NOT NULL
);

CREATE TABLE Audio (
    IDAudio INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(255) NOT NULL,
    Duracion TIME NOT NULL,
    Audio VARCHAR(255) NOT NULL, 
    Tipo ENUM('podcast', 'canción') NOT NULL
);

CREATE TABLE Podcast (
    IDAudio INT AUTO_INCREMENT PRIMARY KEY,
    Colaboradores TEXT,
    IdPodcaster INT NOT NULL,
    FOREIGN KEY (IdPodcaster) REFERENCES Podcaster(IDPodcaster) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (IDAudio) REFERENCES Audio(IDAudio) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Album (
    IDAlbum INT AUTO_INCREMENT PRIMARY KEY,
    Titulo VARCHAR(255) NOT NULL,
    Año YEAR NOT NULL,
    Genero VARCHAR(50) NOT NULL,
    Imagen VARCHAR(255), 
    IDMusico INT NOT NULL,
    FOREIGN KEY (IDMusico) REFERENCES Musico(IDMusico) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE Cancion (
    IDAudio INT AUTO_INCREMENT PRIMARY KEY,
    IDAlbum INT NOT NULL,
    FOREIGN KEY (IDAudio) REFERENCES Audio(IDAudio) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (IDAlbum) REFERENCES Album(IDAlbum) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Cliente (
    IDCliente INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(30) NOT NULL,
    Apellido VARCHAR(30) NOT NULL,
    Usuario VARCHAR(30) UNIQUE NOT NULL,
    Contrasena VARCHAR(255) NOT NULL,
    FechaNacimiento DATE NOT NULL,
    FechaRegistro DATE NOT NULL,
    TipoCliente ENUM('free', 'premium') NOT NULL
);

CREATE TABLE Premium (
    IDCliente INT AUTO_INCREMENT PRIMARY KEY,
    FechaCaducidad DATE NOT NULL,
    FOREIGN KEY (IDCliente) REFERENCES Cliente(IDCliente) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Playlist (
    IDList INT AUTO_INCREMENT PRIMARY KEY,
    Titulo VARCHAR(255) NOT NULL,
    FechaCreacion DATE NOT NULL,
    IDCliente INT NOT NULL,
    FOREIGN KEY (IDCliente) REFERENCES Cliente(IDCliente) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Playlist_Canciones (
    IDList INT AUTO_INCREMENT NOT NULL,
    IDAudio INT NOT NULL,
    fechaPlaylist_Cancion DATE NOT NULL,
    FOREIGN KEY (IDList) REFERENCES Playlist(IDList) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (IDAudio) REFERENCES Audio(IDAudio) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (IDList, IDAudio)
);

CREATE TABLE Gustos (
    IDCliente INT AUTO_INCREMENT NOT NULL,
    IDAudio INT NOT NULL,
    FOREIGN KEY (IDCliente) REFERENCES Cliente(IDCliente) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (IDAudio) REFERENCES Audio(IDAudio) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (IDCliente, IDAudio)
);
CREATE TABLE Estadisticas (
    IDAudio INT AUTO_INCREMENT PRIMARY KEY,
    TopCanciones INT,
    TopPodcast INT,
    MasEscuchados INT,
    TopPlaylist INT,
    FOREIGN KEY (IDAudio) REFERENCES Audio(IDAudio) ON DELETE CASCADE ON UPDATE CASCADE    

);


use reto4grupo5_m;

DELIMITER //
CREATE TRIGGER ActualizarEstadoClientesPremium
AFTER INSERT ON Premium
FOR EACH ROW
BEGIN
    DECLARE fecha_fin DATE;
    DECLARE id_cliente INT;
    
    SET fecha_fin = DATE_ADD(NEW.FechaCaducidad, INTERVAL 1 YEAR);
    SET id_cliente = NEW.IDCliente;
    
    IF fecha_fin < CURDATE() THEN
        UPDATE Cliente
        SET TipoCliente = 'free'
        WHERE IDCliente = id_cliente;
    END IF;
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER BorrarCancionesPorMusico
AFTER DELETE ON Musico
FOR EACH ROW
BEGIN
    DELETE FROM Cancion
    WHERE IDAlbum IN (SELECT IDAlbum FROM Album WHERE IDMusico = OLD.IDMusico);
END;
//
DELIMITER ;


DELIMITER //
CREATE TRIGGER CrearPlaylistMeGusta
AFTER INSERT ON Cliente
FOR EACH ROW
BEGIN
    DECLARE nueva_playlist_id INT;
    
    INSERT INTO Playlist (Titulo, FechaCreacion, IDCliente)
    VALUES ('Me gusta', CURDATE(), NEW.IDCliente);
    
    SET nueva_playlist_id = LAST_INSERT_ID();
    
    INSERT INTO Playlist_Canciones (IDList, IDAudio, fechaPlaylist_Cancion)
    SELECT nueva_playlist_id, IDAudio, CURDATE()
    FROM Gustos
    WHERE IDCliente = NEW.IDCliente;
END;
//
DELIMITER ;


DELIMITER //
CREATE TRIGGER Insertar_Estadistica_Audio AFTER INSERT ON Audio
FOR EACH ROW
BEGIN
    INSERT INTO Estadisticas (IDAudio, TopCanciones, TopPodcast, MasEscuchados, TopPlaylist)
    VALUES (NEW.IDAudio, 0, 0, 0, 0);
END;
//
DELIMITER ;


EVENTOS

DELIMITER //
CREATE EVENT LimpiarRegistrosReproducciones
ON SCHEDULE EVERY 1 MONTH
STARTS CURRENT_DATE + INTERVAL 1 MONTH
DO
BEGIN
    DELETE FROM Reproducciones
    WHERE FechaReproduccion < DATE_SUB(CURDATE(), INTERVAL 1 YEAR);
END;
//
DELIMITER ;


DELIMITER //
CREATE EVENT AlertaCumpleañosSpotyelo
ON SCHEDULE EVERY 1 YEAR
STARTS '2024-04-24'
DO
BEGIN
    INSERT INTO Alertas (FechaAlerta, Mensaje)
    VALUES (CONCAT(YEAR(CURRENT_DATE) + 1, '-04-24'), '¡Feliz cumpleaños Spotyelo!');
END;
//
DELIMITER ;


INSERT INTO `reto4grupo5_m`.`musico` (`IDMusico`, `NombreArtistico`, `Imagen`, `Descripcion`, `Caracteristica`) VALUES ('1', 'Zetak', 'zetak.jpg', 'Pello Reparaz Escala (Arbizu, Navarra; 22 de abril de 1990) es un músico, compositor y productor navarro que desarrolla su carrera principalmente en euskera.​ A los 17 años creó el grupo Vendetta y desde los 29 años se encuentra inmerso en su nuevo proyecto, Zetak. Estudió magisterio en la Universidad Pública de Navarra y ha sido profesor en la escuela de música de Berriozar, así como en el colegio público de Etxarri.', 'solista');
INSERT INTO `reto4grupo5_m`.`musico` (`IDMusico`, `NombreArtistico`, `Imagen`, `Descripcion`, `Caracteristica`) VALUES ('2', 'Morat', 'morat.jpg', 'Así es. Morat es un joven grupo que atesora magia, talento y cabeza. Lo forman Juan Pablo Isaza, guitarra y voz, Juan Pablo Villamil, guitarra, bajo y voz, Martín Vargas, percusión y coros, y Simón Vargas, bajo y coros; cuatro colombianos con una media de edad de 23 años.', 'grupo');
INSERT INTO `reto4grupo5_m`.`musico` (`IDMusico`, `NombreArtistico`, `Imagen`, `Descripcion`, `Caracteristica`) VALUES ('3', 'Metallica', 'metallica.jpg', 'Metallica es una de las bandas de heavy metal más influyentes y exitosas de todos los tiempos. Fundada en Los Ángeles en 1981 por el guitarrista y vocalista James Hetfield y el baterista Lars Ulrich, la banda ha experimentado varios cambios en su formación a lo largo de los años, pero siempre ha mantenido su enfoque en la innovación y la excelencia musical. Conocidos por su estilo agresivo, riffs de guitarra potentes, solos virtuosos y letras profundas y a menudo provocativas, Metallica ha dejado una marca indeleble en el mundo del heavy metal. Han vendido millones de álbumes en todo el mundo y han ganado numerosos premios y reconocimientos a lo largo de su carrera.', 'grupo');
INSERT INTO `reto4grupo5_m`.`musico` (`IDMusico`, `NombreArtistico`, `Imagen`, `Descripcion`, `Caracteristica`) VALUES ('4', 'Fito y Fitipaldis', 'fitoyfitipaldis.jpg', 'Fito & Fitipaldis es un grupo de rock español formado en Bilbao en 1998 por el cantante, compositor y guitarrista Fito Cabrales, quien anteriormente había sido líder de la banda Platero y Tú. El nombre \"Fitipaldis\" proviene de una adaptación del término \"freaky de Bilbao\", refiriéndose a un personaje ficticio creado por Fito. La música de Fito & Fitipaldis es una mezcla de rock and roll, blues, rockabilly y otros estilos musicales, con letras que abordan temas como el amor, la vida cotidiana, la nostalgia y la sociedad. Sus canciones suelen tener un enfoque melódico y accesible.', 'grupo');
INSERT INTO `reto4grupo5_m`.`musico` (`IDMusico`, `NombreArtistico`, `Imagen`, `Descripcion`, `Caracteristica`) VALUES ('5', 'Extremoduro', 'extremoduro.jpg', 'Extremoduro es una banda de rock español formada en Plasencia, Extremadura, en 1987. Liderada por el vocalista y principal compositor Roberto Iniesta, conocido como \"Robe\", la banda ha sido una de las más influyentes en el panorama del rock en español. Es conocido por su estilo musical diverso que abarca desde el rock urbano y el hard rock hasta el rock alternativo y el punk. Sus letras, a menudo crudas y poéticas, han explorado temas como el amor, la vida, la muerte, la sociedad y las experiencias personales.', 'grupo');
INSERT INTO `reto4grupo5_m`.`Musico` (`IDMusico`, `NombreArtistico`, `Imagen`, `Descripcion`, `Caracteristica`) VALUES ('6', 'Duki', 'duki.jpg', 'Duki es un rapero y cantante argentino nacido en Buenos Aires en 1996. Es uno de los nombres más destacados en la escena del trap y el hip-hop en Argentina y América Latina. Su nombre real es Mauro Ezequiel Lombardo, pero es más conocido por su nombre artístico, Duki. Comenzó a ganar reconocimiento en la escena musical a través de sus freestyles y colaboraciones con otros artistas en plataformas como YouTube y SoundCloud.', 'solista');


INSERT INTO `reto4grupo5_m`.`Podcaster` (`IDPodcaster`, `NombreArtistico`, `Imagen`, `Genero`, `Descripcion`) VALUES ('1', 'TheWildProject', 'TheWildProject.jpg', 'Actualidad', 'Actualidad, deportes, charlas con los invitados más interesantes, ciencia, anécdotas y curiosidades, debates, filosofía, psicología, misterio, terror.. y muchísimo más. Cada semana hablando claro y sin miedo sobre el mundo que nos rodea.');
INSERT INTO `reto4grupo5_m`.`Podcaster` (`IDPodcaster`, `NombreArtistico`, `Imagen`, `Genero`, `Descripcion`) VALUES ('2', 'Joe Rogan', 'JoeRogan.jpg', 'Hablar', 'Rogan es el presentador de The Joe Rogan Experience , una larga conversación con invitados que es uno de los podcasts más populares y que actualmente se distribuye exclusivamente en Spotify.  ');
INSERT INTO `reto4grupo5_m`.`Podcaster` (`IDPodcaster`, `NombreArtistico`, `Imagen`, `Genero`, `Descripcion`) VALUES ('3', 'Huberman', 'Huberman.jpg', 'Ciencia', 'El doctor Andrew Huberman te invita a introducirte en el fantástico e intrincado mundo de la neurociencia. Desde la Universidad de Stanford, Huberman habla sobre los adelantos que hay en esta rama de la neurobiología y para ello entrevista a personalidades especialistas en la materia.');





INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('1', 'Hitzeman', '00:03:21', 'hitzeman.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('2', 'Zeinen Ederra Izango Den', '00:03:16', 'zeinenederraizangoden.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('3', 'Sutondoko Kantuak', '00:03:17', 'sutondokantuak.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('4', 'Akelarretan', '00:03:22', 'akelarretan.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('5', 'Zu', '00:03:14', 'zu.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('6', 'Errepidean', '00:03:35', 'errepidean.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('7', 'Deskontrola', '00:03:28', 'deskontrola.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('8', 'Itzulera', '00:03:26', 'itzulera.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('9', 'En Coma', '00:03:24', 'encoma.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('10', 'Idiota', '00:03:03', 'idiota.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('11', 'Al Aire', '00:02:49', 'alaire.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('12', '11 Besos', '00:01:58', '11besos.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('13', 'Punto y Aparte', '00:03:30', 'puntoyaparte.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('14', 'Cuando Nadie Ve', '00:03:38', 'cuandonadieve.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('15', 'Ladrona', '00:04:15', 'ladrona.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('16', 'Eres tu', '00:03:25', 'erestu.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('17', 'La Ultima Vez', '00:03:36', 'laultimavez.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('18', 'Fight Fire With Fire', '00:04:45', 'fightfirewithfire.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('19', 'Ride The Lightning', '00:06:37', 'ridethelightning', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('20', 'For Whom The Bell Tol', '00:05:08', 'ForWhomTheBellTol.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('21', 'Enter Sandman', '00:05:31', 'EnterSandman', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('22', 'Sad But True', '00:05:24', 'SadButTrue.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('23', 'Holier Than Thou', '00:03:47', 'HolierThanThou.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('24', 'Battery', '00:05:12', 'Battery.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('25', 'Master of Puppets', '00:08:36', 'MasterofPuppets.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('26', 'The Thing That Should', '00:06:37', 'TheThingThatShould.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('27', 'The Four Horsemen', '00:07:12', 'TheFourHorsemen.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('28', 'Motorbreath', '00:03:08', 'Motorbreath.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('29', 'Whiplash', '00:04:09', 'Whiplash.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('30', 'Sobra la luz', '00:03:35', 'Sobralaluz.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('31', 'Por la boca vive el pez', '00:04:30', 'Porlabocaviveelpez.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('32', 'Me Equivocaria Otra Vez', '00:05:03', 'Meequivocariaotravez', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('33', 'Abrazado a la Tristeza', '00:03:29', 'Abrazadoalatristeza.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('34', 'Las Nubes de tu Pelo', '00:03:29', 'Lasnubesdetupelo.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('35', 'Soldadito Marinero', '00:03:59', 'Soldaditomarinero.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('36', 'La Casa por el Tejado', '00:04:03', 'Lacasaporeltejado.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('37', 'El Ojo que me Mira', '00:03:07', 'Elojoquememira.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('38', 'Todo a Cien', '00:03:50', 'Todoacien.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('39', 'Tarde o Temprano', '00:05:39', 'Tardeotemprano.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('40', 'Antes de que Cuente Diez ', '00:04:41', 'Antesdequecuentediez.wav ', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('41', 'Catorce Vidas Son Dos Gatos', '00:06:26', 'Catorcevidassondosgatos.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('42', 'Standby', '00:03:36', 'standby.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('43', 'Puta', '00:05:23', 'puta.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('44', 'A Fuego', '00:05:28', 'Afuego.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('45', 'La Vereda de la Puerta de Atras', '00:04:25', 'Laveredadelapuertadeatras.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('46', 'Calle Esperanza', '00:07:02', 'CalleEsperanza.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('47', 'Tango Suicida', '00:08:07', ' TangoSuicida.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('48', 'Si Te Vas', '00:08:36', 'SiTeVas.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('49', 'Desarraigo', '00:07:38', 'Desarraigo.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('50', 'Autorretrato', '00:07:23', 'Autorretrato.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('51', 'Golfa', '00:05:59', 'Golfa.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('52', 'Erase una Vez', '00:04:35', 'EraseunaVez.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('53', 'Salir', '00:05:19', 'Salir.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('54', 'El Dia de la Bestia', '00:04:46', 'ElDiadelaBestia.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('55', 'So Payaso', '00:04:43', 'SoPayaso.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('56', 'Prometeo', '00:03:29', 'Prometeo.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('57', 'Buscando una Luna', '00:04:13', 'BuscandoUnaLuna.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('58', 'Si Quieren Frontear', '00:03:47', 'SiQuierenFrontear.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('59', 'Goteo ', '00:03:04', 'Goteo.wav ', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('60 ', 'Givenchy ', '00:03:45', 'givenchy.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('61', 'Celosa', '00:02:51', 'Celosa.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('62', 'She Dont Give a Fo', '00:03:50', 'SheDontGiveaFo.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('63', 'Rockstar', '00:01:53', 'Rockstar.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('64', 'No vendo trap', '00:04:25', 'Novendotrap.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('65', 'Hello Cotto', '00:03:37', 'HelloCotto.wav', 'cancion');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('66', 'Superar la muerte gracias a la neurociencia, Neuralink', '02:42:54', 'thewildproject1.wav', 'podcast');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('67', '453 dias encerrado en la Prisión más PELIGROSA de IRAN', '03:16:04', 'thewildproject2.wav', 'podcast');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('68', 'EL GRAN DEBATE SOBRE EL CAMBIO CLIMATICO', '03:09.39', 'thewildproject3.wav', 'podcast');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('69', 'Las Tribus No Contactadas', '00:13:33', 'joerogan1.wav', 'podcast');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('70', 'La Inteligencia Artificial', '00:25:27', 'joerogan2.wav', 'podcast');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('71', 'El OVNI Fue Un Descubrimiento Arqueológico ', '00:12:09', 'joerogan3.wav ', 'podcast');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('72', 'Mejora tu Sueño y Mantente Despierto Durante el Día', '01:22:04', 'huberman1.wav', 'podcast');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('73', 'Fallos, Movimiento y Equilibrio Para Aprender Mas Rapido', '01:28:05', 'huberman2.wav', 'podcast');
INSERT INTO `reto4grupo5_m`.`audio` (`IDAudio`, `Nombre`, `Duracion`, `Audio`, `Tipo`) VALUES ('74', 'Cómo Concentrarse Para Cambiar TU Cerebro', '01:29:42', 'huberman3.wav', 'podcast');




INSERT INTO `reto4grupo5_m`.`podcast` (`IDAudio`, `Colaboradores`, `IdPodcaster`) VALUES ('66', 'Jordi Wild, Jesus Martin', '1');
INSERT INTO `reto4grupo5_m`.`podcast` (`IDAudio`, `Colaboradores`, `IdPodcaster`) VALUES ('67', 'Jordi Wild, Santi Sanchez', '1');
INSERT INTO `reto4grupo5_m`.`podcast` (`IDAudio`, `Colaboradores`, `IdPodcaster`) VALUES ('68', 'Jordi Wild, Francisco Cacho, José Ramón', '1');
INSERT INTO `reto4grupo5_m`.`podcast` (`IDAudio`, `Colaboradores`, `IdPodcaster`) VALUES ('69', 'Joe Rogan, Beneil Darriush, Jamie', '2');
INSERT INTO `reto4grupo5_m`.`podcast` (`IDAudio`, `Colaboradores`, `IdPodcaster`) VALUES ('70', 'Joe Rogan, Elon MUSK', '2');
INSERT INTO `reto4grupo5_m`.`podcast` (`IDAudio`, `Colaboradores`, `IdPodcaster`) VALUES ('71', 'Joe Rogan, Bob Lazar', '2');
INSERT INTO `reto4grupo5_m`.`podcast` (`IDAudio`, `Colaboradores`, `IdPodcaster`) VALUES ('72', 'Andrew Huberman', '3');
INSERT INTO `reto4grupo5_m`.`podcast` (`IDAudio`, `Colaboradores`, `IdPodcaster`) VALUES ('73', 'Andrew Huberman', '3');
INSERT INTO `reto4grupo5_m`.`podcast` (`IDAudio`, `Colaboradores`, `IdPodcaster`) VALUES ('74', 'Andrew Huberman', '3');


INSERT INTO `reto4grupo5_m`.`album` (`IDAlbum`, `Titulo`, `Año`, `Genero`, `Imagen`, `IDMusico`) 
VALUES ('1', 'Zeinen ederra izango den', 2019, 'Euskera', 'zeinenederraizangoden.jpg', '1');
INSERT INTO `reto4grupo5_m`.`album` (`IDAlbum`, `Titulo`, `Año`, `Genero`, `Imagen`, `IDMusico`) 
VALUES ('2', 'AAZTIYEN', 2023, 'Euskera', 'aaztiyen.jpg', '1');
INSERT INTO `reto4grupo5_m`.`album` (`IDAlbum`, `Titulo`, `Año`, `Genero`, `Imagen`, `IDMusico`) 
VALUES ('3', 'A donde vamos', 2021, 'Pop', 'adondevamos.jpg', '2');
INSERT INTO `reto4grupo5_m`.`album` (`IDAlbum`, `Titulo`, `Año`, `Genero`, `Imagen`, `IDMusico`) 
VALUES ('4', 'Balas Perdidas', 2018, 'Pop', 'balasperdidas.jpg', '2');
INSERT INTO `reto4grupo5_m`.`album` (`IDAlbum`, `Titulo`, `Año`, `Genero`, `Imagen`, `IDMusico`) 
VALUES ('5', 'Sobre El Amor Y Sus Efectos Secundarios', 2017, 'Hip-hop', 'sobreelamorysusefectossecundarios.jpg', '2');
INSERT INTO `reto4grupo5_m`.`album` (`IDAlbum`, `Titulo`, `Año`, `Genero`, `Imagen`, `IDMusico`) 
VALUES ('6', 'Ride The Lightning', 1984, 'Rock', 'ridethelightning.jpg', '3');
INSERT INTO `reto4grupo5_m`.`album` (`IDAlbum`, `Titulo`, `Año`, `Genero`, `Imagen`, `IDMusico`) 
VALUES ('7', 'Metallical', 1991, 'Rock', 'metallical.jpg', '3');
INSERT INTO `reto4grupo5_m`.`album` (`IDAlbum`, `Titulo`, `Año`, `Genero`, `Imagen`, `IDMusico`) 
VALUES ('8', 'Master of Puppets', 1986, 'Rock', 'masterofpuppets.jpg', '3');
INSERT INTO `reto4grupo5_m`.`album` (`IDAlbum`, `Titulo`, `Año`, `Genero`, `Imagen`, `IDMusico`) 
VALUES ('9', 'Kill Em All', 1983, 'Rock', 'killemall.jpg', '3');
INSERT INTO `reto4grupo5_m`.`album` (`IDAlbum`, `Titulo`, `Año`, `Genero`, `Imagen`, `IDMusico`) 
VALUES ('10', 'Por la boca vive el pez', 2006, 'Rock', 'porlabocaviveelpez.jpg', '4');
INSERT INTO `reto4grupo5_m`.`album` (`IDAlbum`, `Titulo`, `Año`, `Genero`, `Imagen`, `IDMusico`) 
VALUES ('11', 'Lo mas lejos a tu lado', 2003, 'Rock', 'lomaslejosatulado.jpg', '4');
INSERT INTO `reto4grupo5_m`.`album` (`IDAlbum`, `Titulo`, `Año`, `Genero`, `Imagen`, `IDMusico`) 
VALUES ('12', 'Antes de que cuente 10', 2019, 'Rock', 'antesdequecuente10.jpg', '4');
INSERT INTO `reto4grupo5_m`.`album` (`IDAlbum`, `Titulo`, `Año`, `Genero`, `Imagen`, `IDMusico`) 
VALUES ('13', 'Yo, minoria absoluta', 2002, 'Hard rock', 'yominoriaabsoluta.jpg', '5');
INSERT INTO `reto4grupo5_m`.`album` (`IDAlbum`, `Titulo`, `Año`, `Genero`, `Imagen`, `IDMusico`) 
VALUES ('14', 'Material Defectuoso', 2011, 'Rock nacional', 'materialdefectuoso.jpg', '5');
INSERT INTO `reto4grupo5_m`.`album` (`IDAlbum`, `Titulo`, `Año`, `Genero`, `Imagen`, `IDMusico`) 
VALUES ('15', 'Canciones Prohibidas', 1998, 'Rock', 'cancionesprohibidas.jpg', '5');
INSERT INTO `reto4grupo5_m`.`album` (`IDAlbum`, `Titulo`, `Año`, `Genero`, `Imagen`, `IDMusico`) 
VALUES ('16', 'Agila', 1996, 'Hard rock', 'agila.jpg', '5');
INSERT INTO `reto4grupo5_m`.`album` (`IDAlbum`, `Titulo`, `Año`, `Genero`, `Imagen`, `IDMusico`) 
VALUES ('17', 'Temporada de reggaetón' , 2021, 'Reggaetón', 'temporadadereggaeton.jpg', '6');
INSERT INTO `reto4grupo5_m`.`album` (`IDAlbum`, `Titulo`, `Año`, `Genero`, `Imagen`, `IDMusico`) 
VALUES ('18', 'Mejores Exitos', 2023, 'Reggaetón', 'mejoresexitos.jpg', '6');


INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('1', '1');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('2', '1');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('3', '1');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('4', '1');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('5', '2');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('6', '2');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('7', '2');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('8', '2');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('9', '3');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('10', '3');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('11', '3');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('12', '4');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('13', '4');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('14', '4');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('15', '5');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('16', '5');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('17', '5');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('18', '6');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('19', '6');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('20', '6');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('21', '7');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('22', '7');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('23', '7');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('24', '8');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('25', '8');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('26', '8');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('27', '9');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('28', '9');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('29', '9');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('30', '10');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('31', '10');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('32', '10');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('33', '10');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('34', '11');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('35', '11');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('36', '11');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('37', '11');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('38', '12');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('39', '12');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('40', '12');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('41', '12');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('42', '13');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('43', '13');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('44', '13');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('45', '13');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('46', '14');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('47', '14');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('48', '14');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('49', '14');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('50', '15');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('51', '15');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('52', '15');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('53', '15');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('54', '16');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('55', '16');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('56', '16');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('57', '16');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('58', '17');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('59', '17');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('60', '17');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('61', '17');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('62', '18');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('63', '18');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('64', '18');
INSERT INTO `reto4grupo5_m`.`cancion` (`IDAudio`, `IDAlbum`) VALUES ('65', '18');


INSERT INTO `reto4grupo5_m`.`cliente` (`IDCliente`, `Nombre`, `Apellido`, `Usuario`, `Contrasena`, `FechaNacimiento`, `FechaRegistro`, `TipoCliente`) VALUES ('1', 'Mikel', 'Vara', 'mikelvara', 'mikelvara123', '2005-09-17', '2024-04-23', 'premium');


INSERT INTO `reto4grupo5_m`.`premium` (`IDCliente`, `FechaCaducidad`) VALUES ('1', '2025-04-23');

INSERT INTO `reto4grupo5_m`.`playlist_canciones` (`IDList`, `IDAudio`, `fechaPlaylist_Cancion`) VALUES ('1', '4', '2024-04-24');
INSERT INTO `reto4grupo5_m`.`playlist_canciones` (`IDList`, `IDAudio`, `fechaPlaylist_Cancion`) VALUES ('1', '32', '2024-04-24');
INSERT INTO `reto4grupo5_m`.`playlist_canciones` (`IDList`, `IDAudio`, `fechaPlaylist_Cancion`) VALUES ('1', '45', '2024-04-24');
INSERT INTO `reto4grupo5_m`.`playlist_canciones` (`IDList`, `IDAudio`, `fechaPlaylist_Cancion`) VALUES ('1', '55', '2024-04-24');


INSERT INTO `reto4grupo5_m`.`gustos` (`IDCliente`, `IDAudio`) VALUES ('1', '12');
INSERT INTO `reto4grupo5_m`.`gustos` (`IDCliente`, `IDAudio`) VALUES ('1', '46');
INSERT INTO `reto4grupo5_m`.`gustos` (`IDCliente`, `IDAudio`) VALUES ('1', '53');

