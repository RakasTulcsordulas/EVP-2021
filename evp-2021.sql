-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Gép: 127.0.0.1:3307
-- Létrehozás ideje: 2021. Okt 31. 14:26
-- Kiszolgáló verziója: 10.4.21-MariaDB
-- PHP verzió: 8.0.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Adatbázis: `evp-2021`
--

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `booking`
--

CREATE TABLE `booking` (
  `booking_id` int(11) NOT NULL COMMENT 'These are the identifier(ID) of the customers.',
  `booking_time` varchar(5) NOT NULL COMMENT 'These are the identifier of the booking times.',
  `booking_expired` varchar(10) NOT NULL COMMENT 'These are the identifier of the expired booking times.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='It stores the booking datas.';

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `movies`
--

CREATE TABLE `movies` (
  `movie_id` int(11) NOT NULL COMMENT 'Unique ID auto increments for each movie.',
  `title` varchar(128) NOT NULL COMMENT 'Release title of the movie. Can repeat.',
  `release_date` varchar(10) NOT NULL COMMENT 'Release date of the movie, only year.',
  `length` varchar(5) NOT NULL COMMENT 'Movie''s length in minutes, rounded up. Not including ad time.',
  `type` varchar(16) NOT NULL COMMENT 'Movie''s media type (2D / 3D etc.). ',
  `aspect` varchar(16) NOT NULL COMMENT 'Aspect ratio of the movie (16:9 etc). ',
  `language` varchar(128) NOT NULL COMMENT 'List of available languages (ENG/HU etc).',
  `directors` varchar(100) NOT NULL COMMENT 'Directors of the movies.',
  `category` varchar(50) NOT NULL COMMENT 'The category of the movies. Like Horror, Action...'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='It stores the movies datas.';

--
-- A tábla adatainak kiíratása `movies`
--

INSERT INTO `movies` (`movie_id`, `title`, `release_date`, `length`, `type`, `aspect`, `language`, `directors`, `category`) VALUES
(1, 'Venom 2. - Vérontó', '2021', '97', '2D', '2,39:1', 'Magyar szinkron', 'Andy Serkis', 'action-horror, comedy'),
(2, 'Dűne', '2021', '155', '2D', '1,85:1', 'Angol szinkron, Magyar felirat', 'illeneuve, Mary Parent, Cale Boyter, Joe Caracci', 'sci-fi drama, advanture'),
(3, 'Elk*rtuk', '2021', '127', '2D', '1,85:1', 'Magyar szinkron', 'Kálomista Gábor', 'crime-drama'),
(4, 'Gyilkos Halloween', '2021', '106', '3D', '2,39:1', 'Magyar szinkron', 'David Gordon Green', 'horror / thriller'),
(5, 'Post Mortem', '2020', '115', '2D', '1,85:1', 'Magyar szinkron', 'Bergendy Péter', 'thriller / mystic');

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `rooms`
--

CREATE TABLE `rooms` (
  `room_id` int(2) NOT NULL COMMENT 'The number of ther screening rooms.',
  `chair_number` int(3) NOT NULL COMMENT 'The numbers of the chairs in a screening room.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='It stores the screening rooms datas.';

--
-- A tábla adatainak kiíratása `rooms`
--

INSERT INTO `rooms` (`room_id`, `chair_number`) VALUES
(1, 10),
(2, 8),
(3, 25),
(4, 49),
(5, 17);

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `screens`
--

CREATE TABLE `screens` (
  `day` int(2) NOT NULL COMMENT 'Days of screenings in a month.\r\n1<=day<=31',
  `time` varchar(5) NOT NULL COMMENT 'The time of the screening days.\r\n8:00-24:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='It stores the screening time datas.';

--
-- A tábla adatainak kiíratása `screens`
--

INSERT INTO `screens` (`day`, `time`) VALUES
(1, '14:00'),
(2, '14:30'),
(10, '19:30'),
(15, '20:30'),
(22, '21:30');

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL COMMENT 'These are the identifier(id) of all users. ',
  `username` varchar(30) NOT NULL,
  `password` varchar(224) NOT NULL,
  `salt` varchar(10) NOT NULL,
  `emailAddress` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='It stores the users datas.';

--
-- A tábla adatainak kiíratása `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `salt`, `emailAddress`) VALUES
(17, 'attila', '*7975363AAA5B35675359236D56E5F330AAEF53FF', '��Ӡ����s�', 'attila@email.com'),
(18, 'Nándor', '*EAB89CB19B9C03864DCD984311D9CD2CFEF82D01', '�)����nI', 'nandor@email.hu'),
(19, 'asd', '*9B056AD9B98CEB655AE216C79D7104ADF488E295', 'z/޽�ˊs�', 'asd@asd.cga');

--
-- Eseményindítók `users`
--
DELIMITER $$
CREATE TRIGGER `passwordHash` BEFORE INSERT ON `users` FOR EACH ROW SET NEW.password = PASSWORD(NEW.password)
$$
DELIMITER ;

--
-- Indexek a kiírt táblákhoz
--

--
-- A tábla indexei `booking`
--
ALTER TABLE `booking`
  ADD PRIMARY KEY (`booking_id`);

--
-- A tábla indexei `movies`
--
ALTER TABLE `movies`
  ADD PRIMARY KEY (`movie_id`);

--
-- A tábla indexei `rooms`
--
ALTER TABLE `rooms`
  ADD PRIMARY KEY (`room_id`);

--
-- A tábla indexei `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `userName` (`username`);

--
-- A kiírt táblák AUTO_INCREMENT értéke
--

--
-- AUTO_INCREMENT a táblához `booking`
--
ALTER TABLE `booking`
  MODIFY `booking_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'These are the identifier(ID) of the customers.';

--
-- AUTO_INCREMENT a táblához `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'These are the identifier(id) of all users. ', AUTO_INCREMENT=20;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
