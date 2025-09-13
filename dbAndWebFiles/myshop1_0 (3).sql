-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 04, 2025 at 12:12 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `myshop1.0`
--

-- --------------------------------------------------------

--
-- Table structure for table `bill_tbl`
--

CREATE TABLE `bill_tbl` (
  `id` int(11) NOT NULL,
  `shopId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `cusSupId` int(11) NOT NULL,
  `haveToPay` double NOT NULL,
  `sellBuy` varchar(30) NOT NULL,
  `datetimeOfEntry` datetime NOT NULL,
  `datetime` datetime NOT NULL,
  `updated` int(11) DEFAULT NULL,
  `updatedFrom` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bill_tbl`
--

INSERT INTO `bill_tbl` (`id`, `shopId`, `userId`, `cusSupId`, `haveToPay`, `sellBuy`, `datetimeOfEntry`, `datetime`, `updated`, `updatedFrom`) VALUES
(23, 1, 1, 1, 120, '0b0u0y', '2025-09-01 16:49:00', '2025-09-01 16:49:29', NULL, NULL),
(24, 1, 1, 1, 120, '0b0u0y', '2025-09-01 16:49:00', '2025-09-01 16:49:47', NULL, NULL),
(25, 1, 1, 1, 120, '0b0u0y', '2025-09-01 16:49:00', '2025-09-01 16:50:10', NULL, NULL),
(26, 1, 1, 1, 120, '0b0u0y', '2025-09-01 16:49:00', '2025-09-01 16:50:11', NULL, NULL),
(27, 1, 1, 1, 120, '0b0u0y', '2025-09-01 17:50:38', '2025-09-01 16:50:40', NULL, NULL),
(28, 1, 1, 1, 120, '0b0u0y', '2025-09-01 16:50:25', '2025-09-01 16:51:27', NULL, NULL),
(29, 1, 1, 4, 44.1, '0b0u0y', '2025-09-01 23:23:15', '2025-09-01 23:23:54', NULL, NULL),
(30, 1, 1, 0, 189, '0b0u0y', '2025-09-02 17:54:27', '2025-09-02 17:55:12', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `cus_sup_tbl`
--

CREATE TABLE `cus_sup_tbl` (
  `id` int(11) NOT NULL,
  `shopId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `name` varchar(60) NOT NULL,
  `datetime` datetime NOT NULL,
  `updated` int(11) DEFAULT NULL,
  `updatedFrom` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `each_item_of_bill_tbl`
--

CREATE TABLE `each_item_of_bill_tbl` (
  `id` int(11) NOT NULL,
  `billId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `prodId` int(11) NOT NULL,
  `quan` double NOT NULL,
  `price` double NOT NULL,
  `datetime` datetime NOT NULL,
  `updated` int(11) DEFAULT NULL,
  `updatedFrom` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `each_item_of_bill_tbl`
--

INSERT INTO `each_item_of_bill_tbl` (`id`, `billId`, `userId`, `prodId`, `quan`, `price`, `datetime`, `updated`, `updatedFrom`) VALUES
(27, 23, 1, 2, 9, 9, '2025-09-01 16:49:00', NULL, NULL),
(28, 23, 1, 3, 8, 6, '2025-09-01 16:49:00', NULL, NULL),
(29, 24, 1, 2, 9, 9, '2025-09-01 16:49:00', NULL, NULL),
(30, 24, 1, 3, 8, 6, '2025-09-01 16:49:00', NULL, NULL),
(31, 25, 1, 2, 9, 9, '2025-09-01 16:49:00', NULL, NULL),
(32, 25, 1, 3, 8, 6, '2025-09-01 16:49:00', NULL, NULL),
(33, 26, 1, 2, 9, 9, '2025-09-01 16:49:00', NULL, NULL),
(34, 26, 1, 3, 8, 6, '2025-09-01 16:49:00', NULL, NULL),
(35, 27, 1, 2, 9, 9, '2025-09-01 17:50:38', NULL, NULL),
(36, 27, 1, 3, 8, 6, '2025-09-01 17:50:38', NULL, NULL),
(37, 28, 1, 2, 9, 9, '2025-09-01 16:50:25', NULL, NULL),
(38, 28, 1, 3, 8, 6, '2025-09-01 16:50:25', NULL, NULL),
(39, 29, 1, 3, 6, 6, '2025-09-01 23:23:15', NULL, NULL),
(40, 29, 1, 4, 9, 0.9, '2025-09-01 23:23:15', NULL, NULL),
(41, 30, 1, 4, 6, 6, '2025-09-02 17:54:27', NULL, NULL),
(42, 30, 1, 5, 6, 9, '2025-09-02 17:54:27', NULL, NULL),
(43, 30, 1, 5, 5, 9, '2025-09-02 17:54:27', NULL, NULL),
(44, 30, 1, 3, 6, 9, '2025-09-02 17:54:27', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `payment_tbl`
--

CREATE TABLE `payment_tbl` (
  `id` int(11) NOT NULL,
  `shopId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `cusSupId` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  `datetime` datetime NOT NULL,
  `datetimeOfEntry` datetime NOT NULL,
  `updated` int(11) DEFAULT NULL,
  `updatedFrom` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payment_tbl`
--

INSERT INTO `payment_tbl` (`id`, `shopId`, `userId`, `cusSupId`, `amount`, `datetime`, `datetimeOfEntry`, `updated`, `updatedFrom`) VALUES
(19, 1, 1, 1, 120, '2025-09-01 16:49:29', '2025-09-01 16:49:00', NULL, NULL),
(20, 1, 1, 1, 120, '2025-09-01 16:49:47', '2025-09-01 16:49:00', NULL, NULL),
(21, 1, 1, 1, 120, '2025-09-01 16:50:10', '2025-09-01 16:49:00', NULL, NULL),
(22, 1, 1, 1, 120, '2025-09-01 16:50:12', '2025-09-01 16:49:00', NULL, NULL),
(23, 1, 1, 1, 120, '2025-09-01 16:50:41', '2025-09-01 17:50:38', NULL, NULL),
(24, 1, 1, 1, 120, '2025-09-01 16:51:27', '2025-09-01 16:50:25', NULL, NULL),
(25, 1, 1, 4, 44, '2025-09-01 23:23:54', '2025-09-01 23:23:15', NULL, NULL),
(26, 1, 1, 0, 189, '2025-09-02 17:55:12', '2025-09-02 17:54:27', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `prod_tbl`
--

CREATE TABLE `prod_tbl` (
  `id` int(11) NOT NULL,
  `shopId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `name` varchar(60) NOT NULL,
  `datetime` datetime NOT NULL,
  `updated` int(11) DEFAULT NULL,
  `updatedFrom` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `prod_tbl`
--

INSERT INTO `prod_tbl` (`id`, `shopId`, `userId`, `name`, `datetime`, `updated`, `updatedFrom`) VALUES
(2, 1, 1, '0p0r0o0d01', '2025-08-31 23:24:30', NULL, NULL),
(3, 1, 1, '0p0r0o0d02', '2025-08-31 23:58:54', NULL, NULL),
(4, 1, 1, '0p0r0o0d03', '2025-08-31 23:59:10', NULL, NULL),
(5, 1, 1, '0p0r0o0d04', '2025-09-01 00:04:55', NULL, NULL),
(6, 1, 1, '0p0r0o0d05', '2025-09-01 00:05:09', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `shop_tbl`
--

CREATE TABLE `shop_tbl` (
  `id` int(11) NOT NULL,
  `name` varchar(60) NOT NULL,
  `datetime` datetime NOT NULL,
  `updated` int(11) DEFAULT NULL,
  `updatedFrom` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `shop_tbl`
--

INSERT INTO `shop_tbl` (`id`, `name`, `datetime`, `updated`, `updatedFrom`) VALUES
(1, '0s0h0o0p01', '0000-00-00 00:00:00', NULL, NULL),
(2, '0s0h0o0p02', '0000-00-00 00:00:00', NULL, NULL),
(3, '0h', '2025-08-31 23:01:29', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `user_permission_tbl`
--

CREATE TABLE `user_permission_tbl` (
  `id` int(11) NOT NULL,
  `shopId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `attribute` varchar(30) NOT NULL,
  `datetime` datetime NOT NULL,
  `updated` int(11) DEFAULT NULL,
  `updatedFrom` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_permission_tbl`
--

INSERT INTO `user_permission_tbl` (`id`, `shopId`, `userId`, `attribute`, `datetime`, `updated`, `updatedFrom`) VALUES
(1, 1, 1, 'creater', '2025-08-30 23:12:34', NULL, NULL),
(2, 2, 1, 'creater', '2025-08-31 22:58:11', NULL, NULL),
(3, 3, 1, 'creater', '2025-08-31 23:01:29', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `user_tbl`
--

CREATE TABLE `user_tbl` (
  `id` int(11) NOT NULL,
  `name` varchar(60) NOT NULL,
  `phno` varchar(30) NOT NULL,
  `email` varchar(60) NOT NULL,
  `pass` varchar(60) NOT NULL,
  `updated` int(11) DEFAULT NULL,
  `updatedFrom` int(11) DEFAULT NULL,
  `datetime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_tbl`
--

INSERT INTO `user_tbl` (`id`, `name`, `phno`, `email`, `pass`, `updated`, `updatedFrom`, `datetime`) VALUES
(1, '0s0a0y0a0n0 0b0a0n0i0k', '09090302030505080201', '0s0a0y0a0n0b0a0n0i0k010909070@0g0m0a0i0l0.0c0o0m', '010203', NULL, NULL, '0000-00-00 00:00:00');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bill_tbl`
--
ALTER TABLE `bill_tbl`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `cus_sup_tbl`
--
ALTER TABLE `cus_sup_tbl`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `each_item_of_bill_tbl`
--
ALTER TABLE `each_item_of_bill_tbl`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `payment_tbl`
--
ALTER TABLE `payment_tbl`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `prod_tbl`
--
ALTER TABLE `prod_tbl`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `shop_tbl`
--
ALTER TABLE `shop_tbl`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_permission_tbl`
--
ALTER TABLE `user_permission_tbl`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_tbl`
--
ALTER TABLE `user_tbl`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bill_tbl`
--
ALTER TABLE `bill_tbl`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `cus_sup_tbl`
--
ALTER TABLE `cus_sup_tbl`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT for table `each_item_of_bill_tbl`
--
ALTER TABLE `each_item_of_bill_tbl`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=45;

--
-- AUTO_INCREMENT for table `payment_tbl`
--
ALTER TABLE `payment_tbl`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `prod_tbl`
--
ALTER TABLE `prod_tbl`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `shop_tbl`
--
ALTER TABLE `shop_tbl`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `user_permission_tbl`
--
ALTER TABLE `user_permission_tbl`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `user_tbl`
--
ALTER TABLE `user_tbl`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
