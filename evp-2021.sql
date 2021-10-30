-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Gép: 127.0.0.1
-- Létrehozás ideje: 2021. Okt 30. 19:36
-- Kiszolgáló verziója: 10.4.18-MariaDB
-- PHP verzió: 8.0.3

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
-- Tábla szerkezet ehhez a táblához `movies`
--

CREATE TABLE `movies` (
  `id` int(11) NOT NULL COMMENT 'Unique ID auto increments for each movie.',
  `title` varchar(128) NOT NULL COMMENT 'Release title of the movie. Can repeat.',
  `release_date` int(11) NOT NULL COMMENT 'Release date of the movie, only year.',
  `length` int(11) NOT NULL COMMENT 'Movie''s length in minutes, rounded up. Not including ad time.',
  `type` varchar(16) NOT NULL COMMENT 'Movie''s media type (2D / 3D etc.). ',
  `aspect` varchar(16) NOT NULL COMMENT 'Aspect ratio of the movie (16:9 etc). ',
  `language` varchar(128) NOT NULL COMMENT 'List of available languages (ENG/HU etc).'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(30) NOT NULL,
  `password` varchar(224) NOT NULL,
  `salt` varchar(10) NOT NULL,
  `emailAddress` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- A tábla adatainak kiíratása `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `salt`, `emailAddress`) VALUES
(17, 'attila', '*7975363AAA5B35675359236D56E5F330AAEF53FF', '��Ӡ����s�', 'attila@email.com'),
(18, 'Nándor', '*EAB89CB19B9C03864DCD984311D9CD2CFEF82D01', '�)����nI', 'nandor@email.hu'),
(19, 'Feri', '*C6FEC89B259FDB6179FB51E46FDD9FAC3F6293B5', '(;����ø@', 'aki@aseggedbeveri.com');

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
-- A tábla indexei `movies`
--
ALTER TABLE `movies`
  ADD PRIMARY KEY (`id`);

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
-- AUTO_INCREMENT a táblához `movies`
--
ALTER TABLE `movies`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique ID auto increments for each movie.';

--
-- AUTO_INCREMENT a táblához `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
