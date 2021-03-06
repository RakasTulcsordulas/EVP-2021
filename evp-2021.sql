-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Gép: 127.0.0.1
-- Létrehozás ideje: 2022. Jan 11. 13:14
-- Kiszolgáló verziója: 10.4.22-MariaDB
-- PHP verzió: 8.0.13

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
-- Tábla szerkezet ehhez a táblához `auditorium`
--

CREATE TABLE `auditorium` (
  `id` int(11) NOT NULL,
  `name` varchar(32) NOT NULL,
  `seats_no` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='seats_no is redundancy (it could be computed by counting Seat.id_seat related to specific room)';

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `employee`
--

CREATE TABLE `employee` (
  `id` int(11) NOT NULL,
  `username` varchar(32) NOT NULL,
  `password` varchar(100) NOT NULL,
  `salt` varchar(10) NOT NULL COMMENT 'Security ''salt'' for the password. Randomized UTF-8 String.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Employee list (users of system)';

--
-- A tábla adatainak kiíratása `employee`
--

INSERT INTO `employee` (`id`, `username`, `password`, `salt`) VALUES
(1, 'Attila', '*E61D14E12ADD0C1A23ABD98DE7ABD38B664AB653', 'asdfghjklé'),
(2, 'Zsolt', '*64E8693C13EBBD0389DC15CD5B684B78F5048CE9', 'qwertzuiop'),
(3, 'Ádám', '*6FB0989C28E7333DAA3578C0D122829632FC2FFD', 'íyxcvbnmkl'),
(4, 'Kornél', '*E41AB1AC577E10B9A5B122938A4F6EF3EA4ADFD0', 'qaíwsyedxr'),
(5, 'Gergő', '*286426CEB8FA4B317F6810DD70A9E2DC7E184094', 'úűőápéolik');

--
-- Eseményindítók `employee`
--
DELIMITER $$
CREATE TRIGGER `passwordHash` BEFORE INSERT ON `employee` FOR EACH ROW SET NEW.password = PASSWORD(NEW.password)
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `movie`
--

CREATE TABLE `movie` (
  `id` int(11) NOT NULL,
  `title` varchar(256) NOT NULL,
  `director` varchar(256) DEFAULT NULL,
  `cast` varchar(1024) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `rating` int(11) NOT NULL,
  `duration_min` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `reservation`
--

CREATE TABLE `reservation` (
  `id` int(11) NOT NULL,
  `screening_id` int(11) NOT NULL,
  `employee_reserved_id` int(11) DEFAULT NULL,
  `reservation_token` varchar(1024) NOT NULL,
  `reservation_activated` tinyint(4) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `screening`
--

CREATE TABLE `screening` (
  `id` int(11) NOT NULL,
  `movie_id` int(11) NOT NULL,
  `auditorium_id` int(11) NOT NULL,
  `screening_start` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `seat`
--

CREATE TABLE `seat` (
  `id` int(11) NOT NULL,
  `row` int(11) NOT NULL,
  `number` int(11) NOT NULL,
  `auditorium_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `seat_reserved`
--

CREATE TABLE `seat_reserved` (
  `id` int(11) NOT NULL,
  `seat_id` int(11) NOT NULL,
  `reservation_id` int(11) NOT NULL,
  `screening_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexek a kiírt táblákhoz
--

--
-- A tábla indexei `auditorium`
--
ALTER TABLE `auditorium`
  ADD PRIMARY KEY (`id`);

--
-- A tábla indexei `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`id`);

--
-- A tábla indexei `movie`
--
ALTER TABLE `movie`
  ADD PRIMARY KEY (`id`);

--
-- A tábla indexei `reservation`
--
ALTER TABLE `reservation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `Reservation_Projection` (`screening_id`);

--
-- A tábla indexei `screening`
--
ALTER TABLE `screening`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `Projection_ak_1` (`auditorium_id`,`screening_start`),
  ADD KEY `Projection_Movie` (`movie_id`);

--
-- A tábla indexei `seat`
--
ALTER TABLE `seat`
  ADD PRIMARY KEY (`id`),
  ADD KEY `Seat_Room` (`auditorium_id`);

--
-- A tábla indexei `seat_reserved`
--
ALTER TABLE `seat_reserved`
  ADD PRIMARY KEY (`id`),
  ADD KEY `Seat_reserved_Reservation_reservation` (`reservation_id`),
  ADD KEY `Seat_reserved_Seat` (`seat_id`),
  ADD KEY `Seat_reserved_screening` (`screening_id`);

--
-- A kiírt táblák AUTO_INCREMENT értéke
--

--
-- AUTO_INCREMENT a táblához `auditorium`
--
ALTER TABLE `auditorium`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT a táblához `employee`
--
ALTER TABLE `employee`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT a táblához `movie`
--
ALTER TABLE `movie`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT a táblához `reservation`
--
ALTER TABLE `reservation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT a táblához `screening`
--
ALTER TABLE `screening`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT a táblához `seat`
--
ALTER TABLE `seat`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=819;

--
-- AUTO_INCREMENT a táblához `seat_reserved`
--
ALTER TABLE `seat_reserved`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- Megkötések a kiírt táblákhoz
--

--
-- Megkötések a táblához `reservation`
--
ALTER TABLE `reservation`
  ADD CONSTRAINT `Reservation_Projection` FOREIGN KEY (`screening_id`) REFERENCES `screening` (`id`),
  ADD CONSTRAINT `Reservation_reserving_employee` FOREIGN KEY (`employee_reserved_id`) REFERENCES `employee` (`id`);

--
-- Megkötések a táblához `screening`
--
ALTER TABLE `screening`
  ADD CONSTRAINT `Projection_Movie` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`id`),
  ADD CONSTRAINT `Projection_Room` FOREIGN KEY (`auditorium_id`) REFERENCES `auditorium` (`id`);

--
-- Megkötések a táblához `seat`
--
ALTER TABLE `seat`
  ADD CONSTRAINT `Seat_Room` FOREIGN KEY (`auditorium_id`) REFERENCES `auditorium` (`id`);

--
-- Megkötések a táblához `seat_reserved`
--
ALTER TABLE `seat_reserved`
  ADD CONSTRAINT `Seat_reserved_Reservation_reservation` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`id`),
  ADD CONSTRAINT `Seat_reserved_Seat` FOREIGN KEY (`seat_id`) REFERENCES `seat` (`id`),
  ADD CONSTRAINT `Seat_reserved_screening` FOREIGN KEY (`screening_id`) REFERENCES `screening` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
